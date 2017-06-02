package com.example.secret.booklist60;

import cn.bmob.newim.bean.BmobIMExtraMessage;

/**
 * Created by Secret on 2016/12/4.
 * 关注，发送消息给对方说有一个新粉丝
 */

public class AddFollowerMessage extends BmobIMExtraMessage{
    public AddFollowerMessage(){}
      @Override
      public String getMsgType() {
          //自定义一个`add`的消息类型
          return "add";
      }

    @Override
    public boolean isTransient() {
        //设置为true,表明为暂态消息，那么这条消息并不会保存到本地db中，SDK只负责发送出去
        //设置为false,则会保存到指定会话的数据库中
        return true;
    }
}
