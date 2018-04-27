package xianzhan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import xianzhan.pojo.Resource;

/**
 * @auther xianzhan
 * @sinese 2018-04-27
 */
@Controller
@RequestMapping("/ftl")
public class FreeMarkerController {

    @Autowired
    private Resource resource;

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("resource", resource);
        return "/freemarker/index";
    }
}
