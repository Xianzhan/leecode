package xianzhan.misc.jdk5.proxy;

import java.lang.reflect.Proxy;

/**
 * @author xianzhan
 * @since 2020-08-12
 */
public class Main {
    public static void main(String[] args) {
        Human man = new Man();
        TimingHandler timing = new TimingHandler();
        timing.setHuman(man);

        Human manProxy = (Human) Proxy.newProxyInstance(Human.class.getClassLoader(), new Class[]{Human.class}, timing);
        manProxy.run();
    }
}
