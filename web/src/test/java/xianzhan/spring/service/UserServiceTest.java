package xianzhan.spring.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import xianzhan.pojo.User;
import xianzhan.service.IUserService;

import java.util.Date;

/**
 * @author xianzhan
 * @since 2018-05-08
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest // (classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {
    @Autowired
    private IUserService userService;

    @Test
    public void testSave() {
        User user = new User();
        user.setAccount("xianzhan");
        user.setNickname("hello");
        user.setPassword("123456");
        user.setCreateTime(new Date());
        int save = userService.save(user);
        System.out.println(save);
    }

    @Test
    public void testGetById() {
        System.out.println(userService.getById(1));
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(1);
        user.setNickname("super man");
        System.out.println(userService.update(user));
    }
}
