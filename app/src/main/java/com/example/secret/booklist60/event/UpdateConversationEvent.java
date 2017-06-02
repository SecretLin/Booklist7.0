package com.example.secret.booklist60.event;

/**
 * Created by Secret on 2016/11/14.
 * 更新对话事件
 */

public class UpdateConversationEvent {
    private String mMsg;
    public UpdateConversationEvent(String msg) {

        mMsg = msg;
    }
    public String getMsg(){
        return mMsg;
    }
}
