package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

public class Article {
    /**
     * 文章名称
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private String content;

    @Column(name = "add_user")
    private String addUser;

    @Column(name = "add_time")
    private Date addTime;

    private Integer scan;

    private String cover;

    private Byte status;

    private String tags;

    /**
     * 获取文章名称
     *
     * @return id - 文章名称
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置文章名称
     *
     * @param id 文章名称
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return add_user
     */
    public String getAddUser() {
        return addUser;
    }

    /**
     * @param addUser
     */
    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    /**
     * @return add_time
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * @param addTime
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * @return scan
     */
    public Integer getScan() {
        return scan;
    }

    /**
     * @param scan
     */
    public void setScan(Integer scan) {
        this.scan = scan;
    }

    /**
     * @return cover
     */
    public String getCover() {
        return cover;
    }

    /**
     * @param cover
     */
    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * @return status
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * @return tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }
}