package com.example.secret.booklist60.DataBase;

import cn.bmob.v3.BmobObject;

/**
 * Created by Secret on 2016/12/13.
 * 此数据库包括在发现页的提问、分享的评论和读书心得
 */

public class Ground extends BmobObject {
    private MyUser user;
    private Book book;
    private String content;
    private String type;
    private Integer count;


    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
