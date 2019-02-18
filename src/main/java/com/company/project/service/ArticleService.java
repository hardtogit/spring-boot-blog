package com.company.project.service;
import com.company.project.model.Article;
import com.company.project.core.Service;
import com.github.pagehelper.PageInfo;

/**
 * Created by CodeGenerator on 2019/01/23.
 */
public interface ArticleService extends Service<Article> {
    PageInfo<Article> findAllByPage(Integer pageNum,Integer pageSize,Article article);//获取所
}
