package com.example.secret.booklist60.DataBase;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Secret on 2016/9/13.
 * 用户
 */
public class MyUser extends BmobUser {
    private BmobFile Head;
    private boolean isReceiveStrangerMsg;
    private String urlHead;
    public BmobFile getHead() {
        return Head;
    }

    public void setHead(BmobFile head) {
        Head = head;
    }

    public boolean isReceiveStrangerMsg() {
        return isReceiveStrangerMsg;
    }

    public void setReceiveStrangerMsg(boolean receiveStrangerMsg) {
        isReceiveStrangerMsg = receiveStrangerMsg;
    }

    public String getUrlHead() {
        return urlHead;
    }

    public void setUrlHead(String urlHead) {
        this.urlHead = urlHead;
    }
}
