package pers.agent.main.bytebuddy.example;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.pool.TypePool;
import pers.agent.main.bytebuddy.foo.Bar;

/**
 * 操作没有加载的类
 * @author jiangpeng
 * @date 2019/12/618:31
 */
public class NoClassLoadDemo {
    public static void main(String[] args) throws NoSuchFieldException {
        TypePool typePool = TypePool.Default.of(NoClassLoadDemo.class.getClassLoader());
        new ByteBuddy()
                // do not use 'Bar.class'
                .redefine(typePool.describe("pers.agent.main.bytebuddy.foo.Bar").resolve(),
                        ClassFileLocator.ForClassLoader.of(NoClassLoadDemo.class.getClassLoader()))
                // defining fields
                .defineField("qux", String.class)
                .make()
                .load(ClassLoader.getSystemClassLoader());
        System.out.println(Bar.class.getDeclaredField("qux"));
    }
}
