package pers.agent.main.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;

/**
 * @author jiangpeng
 * @date 2020/7/1518:07
 */
public class AddFieldClass {
    public static void main(String[] args) {
        ReloadClass.Foo foo = new ReloadClass.Foo();
        final DynamicType.Loaded<Object> load = new ByteBuddy().
                subclass(Object.class, ConstructorStrategy.Default.NO_CONSTRUCTORS)
                .name("pers.agent.main.bytebuddy.AddFieldClass.Foo")
                .defineField("key", String.class).value("zhaocaiwa")
                .make()
                .load(ReloadClass.Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        System.out.println(load);
        System.out.println(foo.m());
    }

    static class Foo {
        String m() {
            return "foo";
        }
    }

    static class Bar {
        String m() {
            return "bar";
        }
    }
}
