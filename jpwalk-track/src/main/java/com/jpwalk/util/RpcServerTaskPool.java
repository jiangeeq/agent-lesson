package com.jpwalk.util;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;

/**
 * @author jiangpeng
 * @date 2019/12/514:41
 */
public class RpcServerTaskPool {
    private static ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    public static void addTask(RpcRequestBody msg, AbstractCallBack callBack) {
        threadPoolTaskExecutor.submit(new Callable<Object>() {

            @Override
            public Object call() throws Exception {
                callBack.invoke(null);
                return null;
            };
        });
    }
}
