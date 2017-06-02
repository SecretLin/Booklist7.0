package com.example.secret.booklist60.DataBase;

import cn.bmob.v3.BmobObject;

/**
 * Created by Secret on 2017/6/2.
 */

public class Relation_booklist extends BmobObject {
    private MyUser myUser;
    private Booklist booklist;
    private Book book;
    private String s ;

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public Booklist getBooklist() {
        return booklist;
    }

    public void setBooklist(Booklist booklist) {
        this.booklist = booklist;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
