package com.github.xianzhan.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 描述：反射工具类
 *
 * @author Lee
 * @since 2017/7/6
 */
public class ReflectionUtils {

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

        }

        return null;
    }

    public static String[] getMethodNames(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        String[] names = new String[methods.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = methods[i].getName();
        }
        return names;
    }
}
