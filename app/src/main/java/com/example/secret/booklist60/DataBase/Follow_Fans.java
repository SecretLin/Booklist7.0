package com.example.secret.booklist60.DataBase;

import cn.bmob.v3.BmobObject;

/**
 * Created by Secret on 2016/11/30.
 * 关注和粉丝
 */

public class Follow_Fans extends BmobObject {
    private MyUser Follower;
    private MyUser Fans;

    public MyUser getFollower() {
        return Follower;
    }

    public void setFollower(MyUser follower) {
        Follower = follower;
    }

    public MyUser getFans() {
        return Fans;
    }

    public void setFans(MyUser fans) {
        Fans = fans;
    }
}
