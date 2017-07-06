package com.github.xianzhan.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 描述：反射工具类
 *
 * @author Lee
 * @since 2017/7/6
 */
public class ReflectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

    /**
     * 将 Map 数据映射到 clazz 实体类
     *
     * @param clazz Entity.class
     * @param map   数据, key 对应 field
     * @param <T>
     * @return
     */
    public static <T> T assignMapToEntityField(Class<T> clazz, Map<String, Object> map) {
        if (map == null || map.size() < 1) {
            LOGGER.error("映射为空, 无法赋值!");
            return null;
        }

        try {
            Object obj = clazz.newInstance();
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
            LOGGER.error("[ReflectionUtils][assignToEntity]转换发生异常:", e);
        }

        return null;
    }
}
