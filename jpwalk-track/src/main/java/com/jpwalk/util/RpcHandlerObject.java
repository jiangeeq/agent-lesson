package com.jpwalk.util;

/**
 * @author jiangpeng
 * @date 2019/12/511:47
 */
public class RpcHandlerObject {
    private Object object;
    private String rpcKey;

    public String getRpcKey() {
        return rpcKey;
    }

    public void setRpcKey(String rpcKey) {
        this.rpcKey = rpcKey;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
