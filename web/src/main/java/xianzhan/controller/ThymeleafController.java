package xianzhan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @auther xianzhan
 * @sinese 2018-04-29
 */
@Controller
@RequestMapping("/th")
public class ThymeleafController {

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("name", "xianzhan");
        return "/thymeleaf/index";
    }
}
