package xianzhan.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author xianzhan
 * @since 2018-05-12
 */
@Component
public class RedisCache {

    @Autowired
    private StringRedisTemplate stringRedis;

    public void setString(String key, String value) {
        stringRedis.opsForValue().set(key, value);
    }

    public String getString(String key) {
        return stringRedis.opsForValue().get(key);
    }
}
