package com.example.secret.booklist60.DataBase;

import cn.bmob.v3.BmobObject;

/**
 * Created by Secret on 2016/10/21.
 * 收藏
 */

public class Shoucang extends BmobObject {
    private MyUser myUser;
    private Book book;

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