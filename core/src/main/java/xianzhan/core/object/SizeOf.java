package xianzhan.core.object;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;

/**
 * HotSpot 虚拟机中，对象在内存中存储的布局可以分为三块区域：
 * 对象头（Header）、实例数据（Instance Data）和对齐填充（Padding）。
 * <p>
 * JVM 对象头一般占用两个机器码，
 * 在 32-bit JVM 上占用 64bit，
 * 在 64-bit JVM 上占用 128bit 即 8+8=16 bytes（开启指针压缩后占用 4+8=12 bytes）
 * 更具体的对象头介绍请参考：http://blog.csdn.net/wenniuwuren/article/details/50939410
 *
 * 测试类：
 * see xianzhan.core.object.SizeOfTest
 */
public class SizeOf {

    static Instrumentation instrumentation;

    /**
     * @param args -javaagent 传入
     * @param inst JVM 传入
     */
    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    /**
     * 返回没有子类对象的大小
     *
     * @param o 没有子类的对象
     * @return Size
     */
    public static long sizeOf(Object o) {
        if (instrumentation == null) {
            String message = "\nCan not access instrumentation environment.\n" +
                    "Please check if jar file containing SizeOf.class is\n" +
                    "specified in the java's \"-javaagent\" command line argument.";
            throw new IllegalStateException(message);
        }
        return instrumentation.getObjectSize(o);
    }

    /**
     * 计算复合对象
     *
     * @param obj Object to calculate size of
     * @return Object size
     */
    public static long fullSizeOf(Object obj) {
        Map<Object, Object> visited = new IdentityHashMap<>();
        Stack<Object> stack = new Stack<>();

        long result = internalSizeOf(obj, stack, visited);
        while (!stack.isEmpty()) {
            result += internalSizeOf(stack.pop(), stack, visited);
        }
        visited.clear();
        return result;
    }

    private static long internalSizeOf(Object obj, Stack<Object> stack, Map<Object, Object> visited) {
        if (skipObject(obj, visited)) {
            return 0;
        }
        visited.put(obj, null);

        long result = 0;
        // get size of object + primitive variables + member pointers
        result += sizeOf(obj);

        // 处理所有数组内容
        Class<?> clazz = obj.getClass();
        if (clazz.isArray()) {
            // [I, [F 基本类型名字长度是 2
            if (clazz.getName().length() != 2) { // skip primitive type array
                int length = Array.getLength(obj);
                for (int i = 0; i < length; i++) {
                    stack.add(Array.get(obj, i));
                }
            }
        }

        // 处理对象的所有字段
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                // 不重复计算静态类型字段
                if (!Modifier.isStatic(fields[i].getModifiers())) {
                    // 不重复计算原始类型字段
                    if (fields[i].getType().isPrimitive()) {
                        continue;
                    } else {
                        // 使 private 属性可访问
                        fields[i].setAccessible(true);
                        try {
                            // objects to be estimated are put to stack
                            Object objectToAdd = fields[i].get(obj);
                            if (objectToAdd != null) {
                                stack.add(objectToAdd);
                            }
                        } catch (IllegalAccessException e) {
                            assert false;
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        return result;
    }

    /**
     * 这个算法使每个对象仅计算一次， 避免循环引用， 即死循环计算
     *
     * @param obj     计算对象
     * @param visited
     * @return boolean
     */
    private static boolean skipObject(Object obj, Map<Object, Object> visited) {
        if (obj instanceof String) {
            // String 池里已有的不再计算
            if (obj == ((String) obj).intern()) {
                return true;
            }
        }
        return (obj == null) || visited.containsKey(obj);
    }
}
