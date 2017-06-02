package com.example.secret.booklist60.utils;

import cn.bmob.newim.bean.BmobIMExtraMessage;

/**
 * Created by Secret on 2017/3/13.
 */

public class CommentMessage extends BmobIMExtraMessage {
    public CommentMessage(){}
    @Override
    public String getMsgType() {
        //自定义一个`add`的消息类型
        return "new_Comment";
    }

    @Override
    public boolean isTransient() {
        //设置为true,表明为暂态消息，那么这条消息并不会保存到本地db中，SDK只负责发送出去
        //设置为false,则会保存到指定会话的数据库中
        return true;
    }
}
