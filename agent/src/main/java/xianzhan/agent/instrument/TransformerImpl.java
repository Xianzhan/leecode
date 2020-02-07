package xianzhan.agent.instrument;

import xianzhan.agent.compiler.DynamicLoader;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Map;

public class TransformerImpl implements ClassFileTransformer {

    private static final String ENHANCE_CLASS = "xianzhan/agent/enhance/EnhanceClass";

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer)
            throws IllegalClassFormatException {
//        System.out.println(className);

        if (ENHANCE_CLASS.equals(className)) {
            System.out.println("------ 进入加强代码块...");
            String source = """
                    package xianzhan.agent.enhance;
                    public class EnhanceClass {
                        public void test() {
                            System.out.println("EnhanceClass#test // agent");
                        }
                    }""";

            Map<String, byte[]> compile = DynamicLoader.compile(ENHANCE_CLASS, source);
            System.out.println("------ 编译成功");
            return compile.get(ENHANCE_CLASS.replace('/', '.'));
        }
        return classfileBuffer;
    }
}
