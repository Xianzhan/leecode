package xianzhan.misc.jdk7;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * 描述：动态语言 API
 *
 * @author Lee
 * @since 2019-04-02
 */
public class ConstantDynamicDemo {

    private static void test() {
        System.out.println("test");
    }

    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle mh = lookup.findStatic(
                ConstantDynamicDemo.class,
                "test",
                MethodType.methodType(void.class)
        );
        mh.invokeExact();
    }
}
