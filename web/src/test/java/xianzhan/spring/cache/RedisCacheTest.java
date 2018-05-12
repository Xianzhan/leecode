package xianzhan.spring.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xianzhan.cache.RedisCache;

/**
 * @author xianzhan
 * @since 2018-05-12
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest
public class RedisCacheTest {

    @Autowired
    private RedisCache cache;

    @Test
    public void testSetString() {
        cache.setString("one", "一");
    }

    @Test
    public void testGetString() {
        System.out.println(cache.getString("one"));
    }
}
