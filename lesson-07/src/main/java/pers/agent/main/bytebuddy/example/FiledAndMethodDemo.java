package pers.agent.main.bytebuddy.example;

import pers.agent.main.bytebuddy.example.domain.Foot;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

/**
 * 属性和方法
 * @author jiangpeng
 * @date 2019/12/619:16
 */
public class FiledAndMethodDemo {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Foot foot = new ByteBuddy()
                .subclass(Foot.class)
                 .method(isDeclaredBy(Foot.class)).intercept(FixedValue.value("One!"))
                .method(named("foot")).intercept(FixedValue.value("Two!"))
                .method(named("foot").and(takesArguments(1))).intercept(FixedValue.value("Three!"))
                .make()
                .load(Foot.class.getClassLoader())
                .getLoaded()
                .newInstance();
        System.out.println(foot.bar());
        System.out.println(foot.foot());
        System.out.println(foot.foot(null));
    }
}
