package pers.agent.main.bytebuddy.example;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

/**
 * 重新加载类
 * @author jiangpeng
 * @date 2019/12/617:44
 */
public class RedefineClassDemo {
    public static void main(String[] args) {
        ByteBuddyAgent.install();
        Foo foo = new Foo();
        new ByteBuddy()
                .redefine(Bar.class)
                .name(Foo.class.getName())
                .make()
                .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        System.out.println(foo.m());
    }
}
class Foo {
    String m() { return "foo"; }
}

class Bar {
    String m() { return "bar"; }
}
