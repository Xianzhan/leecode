package xianzhan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Random;

/**
 * @author xianzhan
 * @since 2018-04-29
 */
@Controller
@RequestMapping("/th")
public class ThymeleafController {

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("name", "xianzhan");
        return "/thymeleaf/index";
    }

    /**
     * 异常测试页面
     *
     * @return 错误页面
     */
    @GetMapping("/error")
    public String error() {

        int a = new Random().nextInt() / 0;
        System.out.println(a);
        return "/thymeleaf/error/error";
    }
}
