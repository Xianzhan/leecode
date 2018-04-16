package xianzhan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @auther xianzhan
 * @sinese 2018-04-16
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    @ResponseBody
    @RequestMapping("")
    public String hello() {
        return "Hello World";
    }
}
