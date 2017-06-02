package com.example.secret.booklist60.event;

/**
 * Created by Secret on 2016/11/20.
 * 刷新事件
 */

public class RefreshEvent {
    private String mMsg;
    public RefreshEvent(String msg) {

        mMsg = msg;
    }
    public String getMsg(){
        return mMsg;
    }
}
