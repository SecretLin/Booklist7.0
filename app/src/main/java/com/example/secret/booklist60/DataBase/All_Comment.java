package com.example.secret.booklist60.DataBase;

import cn.bmob.v3.BmobObject;

/**
 * Created by Secret on 2017/3/13.
 */

public class All_Comment extends BmobObject {

    private MyUser User1;
    private MyUser User2;
    private String content1;
    private String content2;
    private Comment comment;
    private Ground ground;
    private int who;


    public MyUser getUser1() {
        return User1;
    }

    public void setUser1(MyUser user1) {
        User1 = user1;
    }

    public MyUser getUser2() {
        return User2;
    }

    public void setUser2(MyUser user2) {
        User2 = user2;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Ground getGround() {
        return ground;
    }

    public void setGround(Ground ground) {
        this.ground = ground;
    }

    public int getWho() {
        return who;
    }

    public void setWho(int who) {
        this.who = who;
    }
}
