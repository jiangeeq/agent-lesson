package com.jpwalk.util;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static java.lang.System.out;

/**
 * 反射工具类
 * @author jiangpeng
 * @date 2019/12/1417:27
 */
public class RelfetUtils {
    private static final String FMT = "%24s: %s%n";

    public static void printClassElement(Class clazz) {
        try {
            Class<?> c = Class.forName(clazz.getName());
            Method[] allMethods = c.getDeclaredMethods();
            for (Method m : allMethods) {

                out.format("%s%n", m.toGenericString());

                out.format(FMT, "ReturnType", m.getReturnType());
                if(m.getReturnType().equals(Object.class)){
                    out.format(FMT, "isTrue", true);
                }
                out.format(FMT, "GenericReturnType",
                        m.getGenericReturnType());

                Class<?>[] pType = m.getParameterTypes();
                Type[] gpType = m.getGenericParameterTypes();
                for (int i = 0; i < pType.length; i++) {
                    out.format(FMT, "ParameterType", pType[i]);
                    out.format(FMT, "GenericParameterType", gpType[i]);
                }

                Class<?>[] xType = m.getExceptionTypes();
                Type[] gxType = m.getGenericExceptionTypes();
                for (int i = 0; i < xType.length; i++) {
                    out.format(FMT, "ExceptionType", xType[i]);
                    out.format(FMT, "GenericExceptionType", gxType[i]);
                }
            }
            // production code should handle these exceptions more gracefully
        } catch (ClassNotFoundException x) {
            x.printStackTrace();
        }
    }
}
