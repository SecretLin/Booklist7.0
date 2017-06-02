package com.example.secret.booklist60.DataBase;

import cn.bmob.v3.BmobObject;

/**
 * Created by Secret on 2016/12/5.
 * 书的基本资料
 */

public class Book extends BmobObject {
    private String type;
    private String desc;
    private String doubanurl;
    private String cover;
    private String isbn;
    private String jdurl;
    private String libID;
    private String name;
    private double score;
    private String author;
    private Integer count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDoubanurl() {
        return doubanurl;
    }

    public void setDoubanurl(String doubanurl) {
        this.doubanurl = doubanurl;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getJdurl() {
        return jdurl;
    }

    public void setJdurl(String jdurl) {
        this.jdurl = jdurl;
    }

    public String getLibID() {
        return libID;
    }

    public void setLibID(String libID) {
        this.libID = libID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
