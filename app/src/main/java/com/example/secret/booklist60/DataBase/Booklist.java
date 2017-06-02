package com.example.secret.booklist60.DataBase;

import cn.bmob.v3.BmobObject;

/**
 * Created by Secret on 2017/5/31.
 */

public class Booklist extends BmobObject {

    private String booklistName;
    private Integer bookCount;
    private MyUser myUser;
    private Book book;

    public String getBooklistName() {
        return booklistName;
    }

    public void setBooklistName(String booklistName) {
        this.booklistName = booklistName;
    }

    public Integer getBookCount() {
        return bookCount;
    }

    public void setBookCount(Integer bookCount) {
        this.bookCount = bookCount;
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
