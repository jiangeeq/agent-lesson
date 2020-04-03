package pers.agent.main.bytebuddy.example;

import pers.agent.main.bytebuddy.example.domain.Source;
import pers.agent.main.bytebuddy.example.domain.Target;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;

import static net.bytebuddy.matcher.ElementMatchers.named;

/**
 * 委托方法调用
 * @author jiangpeng
 * @date 2019/12/619:50
 */
public class DelegateMethodDemo {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        String helloWorld = new ByteBuddy()
                .subclass(Source.class)
                .method(named("hello")).intercept(MethodDelegation.to(Target.class))
                .make()
                .load(ClassLoader.getSystemClassLoader())
                .getLoaded()
                .newInstance()
                .hello("World");
        System.out.println(helloWorld);
    }
}
