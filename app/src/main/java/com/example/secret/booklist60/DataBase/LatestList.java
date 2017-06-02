package com.example.secret.booklist60.DataBase;

/**
 * Created by Secret on 2017/3/14.
 */

public class LatestList{
    private MyUser myUser;
    private Book book;
    private String cover;
    private String bookname;
    private String author;
    private boolean isLike;
    private Comment comment;

    private String content;
    private Integer count;


    private MyUser User2;
    private String content2;
    private Ground ground;
    private String groundType;
    private int who;

    private String time;
    private String type;


    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }


    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public MyUser getUser2() {
        return User2;
    }

    public void setUser2(MyUser user2) {
        User2 = user2;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroundType() {
        return groundType;
    }

    public void setGroundType(String groundType) {
        this.groundType = groundType;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
