package xianzhan.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 描述：反射工具类
 *
 * @author Lee
 * @since 2017/7/6
 */
public final class ReflectionUtils {

    /**
     * 将 Map 数据映射到 JavaBean
     *
     * @param clazz Entity.class
     * @param map   数据, key 对应 field
     * @param <T>
     * @return
     */
    public static <T> T assignMapToEntityField(Class<T> clazz, Map<String, Object> map) {
        if (map == null || map.size() < 1) {
            return null;
        }

        try {
            // Class.newInstance() is @Deprecated(since="9")
            Constructor<T> constructor = clazz.getConstructor();
            Object obj = constructor.newInstance();
            Class<?> fieldType;
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                if (methodName.startsWith("set")) {
                    for (String key : map.keySet()) {
                        if (methodName.substring(3).equalsIgnoreCase(key)) {
                            fieldType = method.getParameterTypes()[0];
                            if (fieldType.equals(Integer.TYPE)) {
                                fieldType = Integer.class;
                            } else if (fieldType.equals(Float.TYPE)) {
                                fieldType = Float.class;
                            } else if (fieldType.equals(Double.TYPE)) {
                                fieldType = Double.class;
                            } else if (fieldType.equals(Boolean.TYPE)) {
                                fieldType = Boolean.class;
                            } else if (fieldType.equals(Short.TYPE)) {
                                fieldType = Short.class;
                            } else if (fieldType.equals(Long.TYPE)) {
                                fieldType = Long.class;
                            } else if (fieldType.equals(Character.TYPE)) {
                                fieldType = Character.class;
                            } else if (fieldType.equals(Byte.TYPE)) {
                                fieldType = Byte.class;
                            }
                            method.invoke(obj, fieldType.cast(map.get(key)));
                            break;
                        }
                    }
                }
            }
            return clazz.cast(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 创建实例
     */
    public static Object newInstance(Class<?> clazz) {
        Object instance;
        try {
            instance = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return instance;
    }

    /**
     * 调用方法
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        Object result;
        try {
            method.setAccessible(true);
            result = method.invoke(obj, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置成员变量的值
     */
    public static void setField(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
