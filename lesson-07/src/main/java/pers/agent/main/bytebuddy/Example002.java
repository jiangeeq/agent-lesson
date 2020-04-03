package pers.agent.main.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class Example002 {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        Class dynamicType = new ByteBuddy()
                // 创建一个类
                .subclass(Dog.class)
                .method(ElementMatchers.named("hello"))
                .intercept(MethodDelegation.to(MyServiceInterceptor.class))
                .make()
                // WRAPPER 策略创建一个新的包装 ClassLoader
                .load(Example002.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                // getLoaded 方法返回一个 Java Class 的实例，它就表示现在加载的动态类。
                .getLoaded();

        Object s = dynamicType.newInstance();
        System.out.println(((Dog)s).hello());
    }

}
