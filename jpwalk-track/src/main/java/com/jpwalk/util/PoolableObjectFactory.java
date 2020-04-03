package com.jpwalk.util;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiangpeng
 * @date 2019/12/515:14
 */
public class PoolableObjectFactory<T> implements PooledObjectFactory<Object> {
    private static AtomicInteger counter = new AtomicInteger(0);

    @Override
    public PooledObject<Object> makeObject() throws Exception {
        Object obj = String.valueOf(counter.getAndIncrement());
        System.out.println("PooledObject " + obj);
        PooledObject<Object> pooledObject = new DefaultPooledObject<>(obj);
        return pooledObject;
    }

    @Override
    public void destroyObject(PooledObject<Object> p) throws Exception {
        System.out.println("destroyObject " + p);
    }

    @Override
    public boolean validateObject(PooledObject<Object> p) {
        return false;
    }

    @Override
    public void activateObject(PooledObject<Object> p) throws Exception {
        System.out.println("activateObject " + p);
    }

    @Override
    public void passivateObject(PooledObject<Object> p) throws Exception {
        System.out.println("passivateObject " + p);
    }
}
