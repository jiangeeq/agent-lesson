package com.jpwalk.util;

import org.joda.time.DateTime;

/**
 * @author jiangpeng
 * @date 2019/12/511:33
 */
public class RpcResponseBody {
    private String rpcCode;
    private String messageId;
    private String traceId;
    private String rpcMethod;
    private String message;
    private DateTime returnTime;
    private Object body;



    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public DateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(DateTime returnTime) {
        this.returnTime = returnTime;
    }

    public String getRpcCode() {
        return rpcCode;
    }

    public void setRpcCode(String rpcCode) {
        this.rpcCode = rpcCode;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getRpcMethod() {
        return rpcMethod;
    }

    public void setRpcMethod(String rpcMethod) {
        this.rpcMethod = rpcMethod;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
