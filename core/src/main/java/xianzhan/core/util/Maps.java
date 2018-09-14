package xianzhan.core.util;

import java.util.Iterator;
import java.util.Map;

public final class Maps {

    public static void cleanNullValue(Map<?, ?> map) {
        Iterator<? extends Map.Entry<?, ?>> iterator = map.entrySet().iterator();
        iterator.forEachRemaining(entry -> {
            if (entry.getValue() == null) {
                iterator.remove();
            }
        });
    }
}
