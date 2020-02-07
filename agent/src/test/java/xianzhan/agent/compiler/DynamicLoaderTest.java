package xianzhan.agent.compiler;

import java.util.Arrays;
import java.util.Map;

public class DynamicLoaderTest {

    private static final String ENHANCE_CLASS = "xianzhan/agent/enhance/EnhanceClass";

    public static void main(String[] args) {
        String source = """
                    package xianzhan.agent.enhance;
                    class EnhanceClass {
                        public void test() {
                            System.out.println("EnhanceClass#test // agent");
                        }
                    }""";

        Map<String, byte[]> compile = DynamicLoader.compile(ENHANCE_CLASS, source);
        System.out.println("------ 编译成功");
        byte[] bytes = compile.get(ENHANCE_CLASS.replace('/', '.'));
        if (bytes != null) {
            System.out.println(Arrays.toString(bytes));
        } else {
            System.err.println("bytes is null");
        }
    }
}
