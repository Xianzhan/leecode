package xianzhan.misc.jdk5.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author xianzhan
 * @since 2020-08-12
 */
public class TimingHandler implements InvocationHandler {

    private Human human;

    public Human getHuman() {
        return human;
    }

    public void setHuman(Human human) {
        this.human = human;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        var start = System.currentTimeMillis();
        Object ret = method.invoke(human, args);
        var end = System.currentTimeMillis();
        var consuming = end - start;
        System.out.println("耗时: " + consuming);
        return ret;
    }
}
