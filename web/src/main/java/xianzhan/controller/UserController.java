package xianzhan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xianzhan.pojo.vo.User;
import xianzhan.pojo.vo.VResult;

import java.util.Date;

/**
 * @auther xianzhan
 * @sinese 2018-04-18
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/get")
    public VResult getUser() {
        User user = new User();
        user.setId(1);
        user.setAge(10);
        user.setName("lee");
        user.setPassword("111");
        user.setBirthday(new Date());

        // user.desc 不会显示
        return VResult.ok(user);
    }
}
