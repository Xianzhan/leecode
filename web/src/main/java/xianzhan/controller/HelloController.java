package xianzhan.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xianzhan.pojo.Resource;
import xianzhan.pojo.vo.VResult;

/**
 * @author xianzhan
 * @since 2018-04-16
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private Resource resource;

    @ResponseBody
    @RequestMapping("")
    public String hello() {
        return "Hello World";
    }

    @ResponseBody
    @GetMapping("/resource")
    public VResult getResource() {

        Resource bean = new Resource();
        BeanUtils.copyProperties(resource, bean);

        return VResult.ok(bean);
    }
}
