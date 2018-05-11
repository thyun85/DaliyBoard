package com.thy.daliyboard;

public class ReviewLbItem {

    int no;
    String nickName;
    String msg;
    String imgPath;
    String upDate;

    public ReviewLbItem(int no, String nickName, String msg, String imgPath, String upDate) {
        this.no = no;
        this.nickName = nickName;
        this.msg = msg;
        this.imgPath = imgPath;
        this.upDate = upDate;
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
}
