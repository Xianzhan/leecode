package com.github.xianzhan.core.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class MapUtils {

    private static void cleanNullValue(Map<?, ?> map) {
        Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
        iterator.forEachRemaining(entry -> {
            if (entry.getValue() == null)
                iterator.remove();
        });
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", "");
        map.put("c", null);

        System.out.println(map);
        cleanNullValue(map);
        System.out.println(map);
    }
}
