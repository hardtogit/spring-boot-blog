package com.company.project.web;
import com.company.project.model.Article;
import com.company.project.model.User;
import com.company.project.service.UserService;
import com.company.project.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller
public  class ViewController {
    @Resource
    private UserService userService;
    @Resource
    private ArticleService articleService;
    @RequestMapping("/hello")
    public ModelAndView hello() {
        return new ModelAndView();
    }
    @RequestMapping("/index")
    public String index(Model model) {
        List<User> users =userService.findAll();
        List<Article> articles =articleService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("articles", articles);
        return "index";
    }
    @RequestMapping("/detail")
    public String detail(Model model,@RequestParam Integer id) {
        Article article =articleService.findById(id);
        article.setScan(article.getScan()+1);
        articleService.update(article);
        model.addAttribute("article", article);
        return "detail";
    }
    @RequestMapping("/editArticle")
    public String editArticle(Model model) {
//        Article article =articleService.findById(id);
//        model.addAttribute("article", article);
        return "editArticle";
    }
}
