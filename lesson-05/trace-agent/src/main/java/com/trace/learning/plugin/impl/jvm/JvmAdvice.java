package com.trace.learning.plugin.impl.jvm;

import net.bytebuddy.asm.Advice;

/**
 *
 * @author jiangpeng
 * @date 2019/12/9 0009
 */
public class JvmAdvice {
    @Advice.OnMethodExit()
    public static void exit() {
        JvmStack.printMemoryInfo();
//        JvmStack.printGCInfo();
    }
}
