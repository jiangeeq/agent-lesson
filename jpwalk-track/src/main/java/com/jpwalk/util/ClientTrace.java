package com.jpwalk.util;

import com.alibaba.ttl.TtlCallable;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.joda.time.DateTime;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiangpeng
 * @date 2019/12/511:14
 */
public class ClientTrace {
   private static RaptorLogger LOGGER = new RaptorLogger(ClientTrace.class);
    private static final String HEARTBEAT_METHOD = "";
    RpcPushDefine rpc = new RpcPushDefine();
    AtomicInteger heartbeatCount = new AtomicInteger();
    private String serverNode = "";
    private static Map<String , RpcHandlerObject> RPC_MAPPING = new HashMap<>();
    private static ThreadPoolTaskExecutor POOLTASKEXECUTOR = new ThreadPoolTaskExecutor();

    static ObjectPool pool = new GenericObjectPool(new PoolableObjectFactory());


    public void pushMessage( ChannelHandlerContext ctx ,RpcRequestBody requestBody) {
        // RpcPushDefine rpc = this;
        ChannelFuture future = ctx.writeAndFlush(requestBody);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                //mark:回调此处会发生一次线程上下文切换,需要重置线程号.
                ThreadContext.TRACEID.set(requestBody.getTraceId());
                if (future.isSuccess()) {
                    LOGGER.info("RPC客户端数据出站SUCCESS, " + requestBody);
                    try {
                        pool.returnObject(rpc);
                    } catch (Exception e) {
                        String message = e.getMessage();
                        LOGGER.error("资源释放异常,tcpId: " + rpc.getTcpId() + ", serverNode: " + serverNode + ", message: "
                                + message);
                        pool.invalidateObject(rpc);
                    }
                } else {
//                    outboundException(requestBody.getMessageId(), requestBody.getTraceId(), "Rpc出站Fail.", RpcResult
//                    .FAIL_NETWORK_TRANSPORT);
                    String message = future.cause().getMessage();
                    LOGGER.warn("RPC客户端数据出站FAIL: " + requestBody + ", tcpId: " + rpc.getTcpId() + ", serverNode: " + serverNode + ", message: " + message);
                    pool.invalidateObject(rpc);
                }
            }
        });
    }

    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseBody responseBody) throws Exception {
        if (responseBody.getRpcMethod().equals(HEARTBEAT_METHOD)) {
            heartbeatCount.set(0);
            LOGGER.warn("[重要!!!]tcp 心跳包收到响应,tcpId: " + rpc.getTcpId() + ", serverNode: " + serverNode);
            return;
        }

        ThreadContext.TRACEID.set(responseBody.getTraceId());
        responseBody.setReturnTime(new DateTime());
        RpcClientTaskPool.addTask(responseBody);
    }

    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestBody msg) throws Exception {
        ThreadContext.TRACEID.set(msg.getTraceId());
        RpcServerTaskPool.addTask(msg, new AbstractCallBack() {
            @Override
            public void invoke(RpcResponseBody responseBody) {
                ChannelFuture future = ctx.writeAndFlush(responseBody);
                future.addListener((ChannelFutureListener) future1 -> {
                    ThreadContext.TRACEID.set(msg.getTraceId());
                    if (future1.isSuccess()) {
                        LOGGER.info("RPC服务端数据出站SUCCESS, " + responseBody);
                    } else {
                        String message = future1.cause().getMessage();
                        LOGGER.warn("RPC服务端数据出站FAIL: " + responseBody + ", message: " + message);
                    }
                });
            }
        });
    }

    /**
     * @param Object obj 请求参数, AbstractCallBack call 业务回调对象.
     * @return void
     * @author gewx 添加任务入业务线程池
     **/
    public static void addTask(RpcRequestBody requestBody, AbstractCallBack call) {
        String rpcMethod = requestBody.getRpcMethod();
        String traceId = requestBody.getTraceId();
        ListenableFuture<RpcResponseBody> future = POOLTASKEXECUTOR
                .submitListenable(TtlCallable.get(() -> {
                    LOGGER.info("RPC服务端收到请求信息: " + requestBody);
                    RpcHandlerObject handler = RPC_MAPPING.get(rpcMethod);
                    if (handler == null) {
                        throw new RuntimeException("RPC参数缺失,RpcMethod is null !" + RpcResult.ERROR);
                    }

                    Object result = null;
                    Object[] objArray = requestBody.getBody();
                    if (ArrayUtils.isNotEmpty(objArray)) {
                        //MethodUtils.invokeExactMethod(object, methodName); 根据类型完全匹配.
                        result = MethodUtils.invokeMethod(handler.getObject(), handler.getRpcKey(), objArray);
                    } else {
                        result = MethodUtils.invokeMethod(handler.getObject(), handler.getRpcKey());
                    }

                    RpcResponseBody body = new RpcResponseBody();
                    body.setRpcCode(RpcResult.SUCCESS);
                    body.setMessageId(requestBody.getMessageId());
                    body.setTraceId(traceId);
                    body.setRpcMethod(rpcMethod);
                    body.setBody(result);
                    body.setMessage("RPC调用成功!");
                    return body;
                }));

        future.addCallback(new ListenableFutureCallback<RpcResponseBody>() {

            @Override
            public void onSuccess(RpcResponseBody body) {
                call.invoke(body);
            }

            @Override
            public void onFailure(Throwable throwable) {
                String message = "RPC 服务调用失败,message:[" + throwable.getMessage() + "]";
                LOGGER.warn(rpcMethod, message);

                /**
                 * 定义回调异常,默认响应体
                 * **/
                RpcResponseBody body = new RpcResponseBody();
                body.setRpcCode(RpcResult.FAIL);
                body.setMessageId(requestBody.getMessageId());
                body.setTraceId(traceId);
                body.setRpcMethod(rpcMethod);
                body.setMessage("RPC 服务调用失败,message:[" + ExceptionUtils.getRootCauseMessage(throwable) + "]");

                call.invoke(body);
            }
        });
    }
}
