package xianzhan.misc.jni;

/**
 * @author xianzhan
 * @since 2019-07-11
 */
public class JavaNativeInterface {

    static {
        System.loadLibrary("JavaNativeInterface");
    }

    public static native void hello(String name);

    public static void main(String[] args) {
        hello("hello world");
    }
}
