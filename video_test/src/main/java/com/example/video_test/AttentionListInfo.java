package com.example.video_test;

import android.graphics.Bitmap;

public class AttentionListInfo {
    private Bitmap userPhoto;
    private String nickName;
    private String account;
    private String signature;
    private Bitmap attention;
    private int tag;

    public AttentionListInfo(){

    }

    public AttentionListInfo(String account,Bitmap userPhoto, String nickName, String signature, Bitmap attention,int tag) {
        this.account=account;
        this.userPhoto = userPhoto;
        this.nickName = nickName;
        this.signature = signature;
        this.attention = attention;
        this.tag=tag;
    }

    public AttentionListInfo(String account,Bitmap userPhoto, String nickName, String signature) {
        this.account=account;
        this.userPhoto = userPhoto;
        this.nickName = nickName;
        this.signature = signature;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Bitmap getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Bitmap userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Bitmap getAttention() {
        return attention;
    }

    public void setAttention(Bitmap attention) {
        this.attention = attention;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
