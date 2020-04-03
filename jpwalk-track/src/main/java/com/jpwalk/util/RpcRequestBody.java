package com.jpwalk.util;

/**
 * @author jiangpeng
 * @date 2019/12/511:36
 */
public class RpcRequestBody {
    private String rpcMethod;
    private String traceId;
    private Object[] body;
    private String messageId;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Object[] getBody() {
        return body;
    }

    public void setBody(Object[] body) {
        this.body = body;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRpcMethod() {
        return rpcMethod;
    }

    public void setRpcMethod(String rpcMethod) {
        this.rpcMethod = rpcMethod;
    }
}
