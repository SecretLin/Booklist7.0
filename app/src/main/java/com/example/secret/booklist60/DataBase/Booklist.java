package com.example.secret.booklist60.DataBase;

import cn.bmob.v3.BmobObject;

/**
 * Created by Secret on 2017/5/31.
 */

public class Booklist extends BmobObject {

    private String booklistName;

    private MyUser myUser;


    public String getBooklistName() {
        return booklistName;
    }

    public void setBooklistName(String booklistName) {
        this.booklistName = booklistName;
    }



    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }


}
