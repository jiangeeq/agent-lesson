package pers.agent.main.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

/**
 * @author jiangpeng
 * @date 2020/7/1517:38
 */
public class ReloadClass {
    public static void main(String[] args) {
        ByteBuddyAgent.install();
        Foo foo = new Foo();
        new ByteBuddy()
                .redefine(Bar.class)
                .name(Foo.class.getName())
                .defineField("key", String.class).value("zhaocaiwa")
                .make()
                .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        System.out.println(foo.m());
    }

    static class Foo {
        String m() { return "foo"; }
    }

    static class Bar {
        String m() { return "bar"; }
    }
}
