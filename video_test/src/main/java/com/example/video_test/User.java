package com.example.video_test;

public class User {
    private String account;
    private String userPhoto;
    private String nickName;
    private String sex;
    private String birthday;
    private String signature;

    public User(){

    }

    public User(String account, String userPhoto, String nickName, String sex, String birthday, String signature) {
        this.account = account;
        this.userPhoto = userPhoto;
        this.nickName = nickName;
        this.sex = sex;
        this.birthday = birthday;
        this.signature = signature;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
