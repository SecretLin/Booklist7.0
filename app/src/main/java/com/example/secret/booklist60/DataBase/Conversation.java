package com.example.secret.booklist60.DataBase;

/**
 * Created by Secret on 2016/11/12.
 * 私信
 */

public class Conversation {
    private String avatar;
    private String title;
    private String content;
    private Long time;
    private String userId;
    private long unReadCount;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public long getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(long unReadCount) {
        this.unReadCount = unReadCount;
    }
}
