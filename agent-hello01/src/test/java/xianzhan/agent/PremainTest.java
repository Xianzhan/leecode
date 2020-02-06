package xianzhan.agent;

public class PremainTest {

    /**
     * java -javaagent:${project}/target/agent-hello.jar=agentArgs PremainTest
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        System.out.println("Premain Test");
    }
}
