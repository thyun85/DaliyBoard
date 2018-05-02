package com.thy.daliyboard;

public class DustInfo {

    String msrdate="", msrstename="", grade="", pm10="", pm25="", alarm="";
        //측정날짜, 측정소명, 환경지수 등급, 미세먼지 지수, 초미세먼지 지수

    public DustInfo() {

    }

    public DustInfo(String msrdate, String msrstename, String grade, String pm10, String pm25, String alarm) {
        this.msrdate += msrdate;
        this.msrstename += msrstename;
        this.grade += grade;
        this.pm10 += pm10;
        this.pm25 += pm25;
        this.alarm += alarm;
    }

    public String getMsrdate() {
        return msrdate;
    }

    public void setMsrdate(String msrdate) {
        this.msrdate = msrdate;
    }

    public String getMsrstename() {
        return msrstename;
    }

    public void setMsrstename(String msrstename) {
        this.msrstename = msrstename;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPm10() {
        return pm10;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }
}
