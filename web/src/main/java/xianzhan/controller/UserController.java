package xianzhan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xianzhan.pojo.vo.UserVO;
import xianzhan.pojo.vo.ResultVO;

import java.util.Date;

/**
 * @author xianzhan
 * @since 2018-04-18
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/get")
    public ResultVO getUser() {
        UserVO user = new UserVO();
        user.setId(1);
        user.setAge(20);
        user.setName("lee");
        user.setPassword("2");
        user.setBirthday(new Date());

        // user.desc 不会显示
        return ResultVO.ok(user);
    }
}
