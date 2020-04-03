package com.jpwalk.util;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author jiangpeng
 * @date 2019/12/514:33
 */
public class RpcClientTaskPool {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

    public static void addTask(RpcResponseBody responseBody) {
      //  threadPoolTaskExecutor.submit((Runnable) () -> responseBody);

    }
}
