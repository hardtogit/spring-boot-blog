package com.company.project.web;
import com.company.project.model.User;
import com.company.project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller
public  class ViewController {
    @Resource
    private UserService userService;
    @RequestMapping("/hello")
    public ModelAndView hello() {
        return new ModelAndView();
    }
    @RequestMapping("/index")
    public String index(Model model) {
        List<User> users =userService.findAll();
        model.addAttribute("users", users);
        return "index";
    }
}
