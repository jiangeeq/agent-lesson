package com.trace.learning.plugin.impl;

/**
 * @author jiangpeng
 * @date 2019/12/9 0009
 */
public interface IPlugin {
    /**
     * 名称
     */
    String name();

    /**
     * 监控点
     *
     * @return InterceptPoint
     */
    InterceptPoint[] buildInterceptPoint();

    /**
     * 拦截器类
     *
     * @return Class
     */
    Class adviceClass();
}
