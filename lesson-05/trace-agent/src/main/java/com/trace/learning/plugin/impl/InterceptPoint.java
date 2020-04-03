package com.trace.learning.plugin.impl;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * @author jiangpeng
 * @date 2019/12/9 0009
 */
public interface InterceptPoint {
    /**
     * 类匹配规则
     *
     * @return ElementMatcher<TypeDescription>
     */
    ElementMatcher<TypeDescription> buildTypesMatcher();

    /**
     * 方法匹配规则
     *
     * @return ElementMatcher<MethodDescription>
     */
    ElementMatcher<MethodDescription> buildMethodsMatcher();
}
