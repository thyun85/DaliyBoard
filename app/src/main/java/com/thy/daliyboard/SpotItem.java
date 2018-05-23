package com.thy.daliyboard;

public class SpotItem {

    int no;
    String nickName;
    String msg;
    String imgPath;
    String upDate;
    boolean isFavorite = false;
    boolean isLike = false;

    public SpotItem(int no, String nickName, String msg, String imgPath, String upDate) {
        this.no = no;
        this.nickName = nickName;
        this.msg = msg;
        this.imgPath = imgPath;
        this.upDate = upDate;
    }

    public SpotItem(int no, String nickName, String msg, String imgPath, String upDate, boolean isFavorite) {
        this.no = no;
        this.nickName = nickName;
        this.msg = msg;
        this.imgPath = imgPath;
        this.upDate = upDate;
        this.isFavorite = isFavorite;
    }

    public SpotItem(int no, String nickName, String msg, String imgPath, String upDate, boolean isFavorite, boolean isLike) {
        this.no = no;
        this.nickName = nickName;
        this.msg = msg;
        this.imgPath = imgPath;
        this.upDate = upDate;
        this.isFavorite = isFavorite;
        this.isLike = isLike;
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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
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
}
