package com.company.project.service.impl;

import com.company.project.dao.ArticleMapper;
import com.company.project.model.Article;
import com.company.project.service.ArticleService;
import com.company.project.core.AbstractService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by CodeGenerator on 2019/01/23.
 */
@Service
@Transactional
public class ArticleServiceImpl extends AbstractService<Article> implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;
    public PageInfo<Article> findAllByPage(Integer pageNum,Integer pageSize,Article article) {

        PageHelper.startPage(pageNum,pageSize);
        List<Article> docs= mapper.selectAll();
        PageInfo<Article> pageInfo = new PageInfo<>(docs);
        return pageInfo;
    }

}
