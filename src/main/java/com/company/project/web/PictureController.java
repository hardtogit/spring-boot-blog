package com.company.project.web;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Article;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/picture")
public class PictureController {
    @PostMapping("/upLoad")
    public Result upLoad(Article article) {
        return ResultGenerator.genSuccessResult();
    }



}
