package xianzhan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xianzhan
 * @since 2018-04-27
 */
@Controller
@RequestMapping("/ftl")
public class FreeMarkerController {

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("name", "FreeMarker");
        return "/freemarker/index";
    }
}
