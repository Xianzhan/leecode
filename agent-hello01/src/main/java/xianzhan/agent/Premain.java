package xianzhan.agent;

import java.lang.instrument.Instrumentation;

/**
 * Java 代理
 *
 * @author xianzhan
 * @since 2020-02-06
 */
public class Premain {

    /**
     * public static void premain(String agentArgs);
     * 若存在上面方法, 依然会优先执行此方法
     *
     * @param agentArgs 代理参数
     * @param inst      用于修改字节码
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println(String.format("Premain agentArgs: [%s]", agentArgs));
    }
}
