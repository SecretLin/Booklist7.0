package com.example.secret.booklist60.DataBase;

import cn.bmob.v3.BmobObject;

/**
 * Created by Secret on 2016/12/12.
 */

public class FeedBack extends BmobObject {
    private MyUser user;
    private String content;
    private int type;//1为系统发的消息类型，2为用户发的消息类型

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
