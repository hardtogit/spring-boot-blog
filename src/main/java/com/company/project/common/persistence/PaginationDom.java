package com.company.project.common.persistence;

import com.github.pagehelper.PageInfo;

public class PaginationDom {
    public static String getDom(PageInfo pageInfo){

        String html="<nav aria-label='Page navigation'><ul class='pagination pull-right'>";
        if(pageInfo.getPages()==1){
            return null;
        }else{
            if(pageInfo.getPageNum()==1){
                html = getString(pageInfo, html);
                html+=String.format("  <li> <a href='/index?pageNum=%s&pageSize=%s' aria-label='next'>" +
                                "<span aria-hidden='true'>下一页</span>" +
                                "</a></li></ul></nav>",pageInfo.getPageNum()+1,pageInfo.getPageSize());
               return html;
            }else{
                html+=String.format("  <li> <a href='/index?pageNum=%s&pageSize=%s' aria-label='next'>" +
                        "<span aria-hidden='true'>上一页</span>" +
                        "</a></li>",pageInfo.getPageNum()-1,pageInfo.getPageSize()) ;
                html = getString(pageInfo, html);
                if(pageInfo.getPageNum()!=pageInfo.getPages()){
                    html+=String.format("  <li> <a href='/index?pageNum=%s&pageSize=%s' aria-label='next'>" +
                            "<span aria-hidden='true'>下一页</span>" +
                            "</a></li></ul></nav>",pageInfo.getPageNum()+1,pageInfo.getPageSize());
                }
                html+="</ul></nav>";
            }
        }
        return html;
    }

    private static String getString(PageInfo pageInfo, String html) {
        String active;
        for(int i = 0; i<pageInfo.getPages(); i++){
            if((1+i)==pageInfo.getPageNum()){
                active="active";
            }else {
                active="normal";
            }
            if((pageInfo.getPageNum()-(i+1)>5&&pageInfo.getPages()-10>i)||(((i+1)-pageInfo.getPageNum())>4&&(i+1)>10)){
                continue;
            }
            html+=String.format("<li class='%s'><a href='/index?pageNum=%s&pageSize=%s'>%s</a></li>",active,1+i,pageInfo.getPageSize(),1+i);
        }
        return html;
    }
}
