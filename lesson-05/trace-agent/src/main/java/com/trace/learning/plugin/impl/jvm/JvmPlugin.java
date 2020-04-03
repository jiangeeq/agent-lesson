package com.trace.learning.plugin.impl.jvm;

import com.trace.learning.common.ElementClassConst;
import com.trace.learning.plugin.impl.IPlugin;
import com.trace.learning.plugin.impl.InterceptPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 *
 * @author jiangpeng
 * @date 2019/12/9 0009
 */
public class JvmPlugin implements IPlugin {
    @Override
    public String name() {
        return "jvm";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{
                new InterceptPoint() {
                    @Override
                    public ElementMatcher<TypeDescription> buildTypesMatcher() {
                        // return ElementMatchers.nameStartsWith("cn.jpsite.learning");
                        return ElementMatchers.nameMatches(ElementClassConst.CLASS_TYPE);
                    }

                    @Override
                    public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                        return ElementMatchers.isMethod()
                                .and(ElementMatchers.any())
                                .and(ElementMatchers.not(ElementMatchers.nameStartsWith(ElementClassConst.NOT_TRACE_METHOD)));
                    }
                }
        };
    }

    @Override
    public Class adviceClass() {
        return JvmAdvice.class;
    }
}
