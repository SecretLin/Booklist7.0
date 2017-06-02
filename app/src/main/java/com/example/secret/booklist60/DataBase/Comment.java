package com.example.secret.booklist60.DataBase;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/10/14.
 * 父评论，即不是回复的评论
 */
public class Comment extends BmobObject {
    private String Commentcontent;
    private MyUser myUser;
    private Book book;
    private Integer count;


    public void setCommentcontent(String commentcontent){
        this.Commentcontent = commentcontent;
    }
    public String getCommentcontent(){
        return Commentcontent;
    }
    public void setMyUser(MyUser myUser){
        this.myUser = myUser;
    }
    public MyUser getMyUser(){
        return myUser;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }


    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}