package com.company.project.web;
import com.alibaba.fastjson.JSONObject;
import com.company.project.common.persistence.PaginationDom;
import com.company.project.model.Article;
import com.company.project.model.User;
import com.company.project.service.UserService;
import com.company.project.service.ArticleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public String index(Model model,@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                        Article article) {
        PageInfo<Article> pageInfo =articleService.findAllByPage(pageNum,pageSize,article);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pagination",PaginationDom.getDom(pageInfo));
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
