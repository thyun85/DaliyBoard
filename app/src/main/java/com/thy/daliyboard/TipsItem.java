package com.thy.daliyboard;

public class TipsItem {

    int no;
    String nickName;
    String msg;
    String aviPath;
    String upDate;
    int likeCnt;
    boolean isLike = false;
    boolean isFavorite = false;

//    public TipsItem(int no, String nickName, String msg, String aviPath, String upDate) {
//        this.no = no;
//        this.nickName = nickName;
//        this.msg = msg;
//        this.aviPath = aviPath;
//        this.upDate = upDate;
//    }
//
//    public TipsItem(int no, String nickName, String msg, String aviPath, String upDate, boolean isFavorite) {
//        this.no = no;
//        this.nickName = nickName;
//        this.msg = msg;
//        this.aviPath = aviPath;
//        this.upDate = upDate;
//        this.isFavorite = isFavorite;
//    }

    public TipsItem(int no, String nickName, String msg, String aviPath, String upDate, boolean isLike, boolean isFavorite, int likeCnt) {
        this.no = no;
        this.nickName = nickName;
        this.msg = msg;
        this.aviPath = aviPath;
        this.upDate = upDate;
        this.isLike = isLike;
        this.isFavorite = isFavorite;
        this.likeCnt = likeCnt;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAviPath() {
        return aviPath;
    }

    public void setAviPath(String aviPath) {
        this.aviPath = aviPath;
    }

    public String getUpDate() {
        return upDate;
    }

    public void setUpDate(String upDate) {
        this.upDate = upDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }
}
