package com.example.video_test;

import android.graphics.Bitmap;

public class DynamicListInfo {
    Bitmap dynamicImage;
    Bitmap userPhoto;
    String dynamicAuthor;
    String dynamicTime;
    String dynamicTheme;

    public DynamicListInfo(){

    }

    public DynamicListInfo(Bitmap dynamicImage, Bitmap userPhoto, String dynamicAuthor, String dynamicTime, String dynamicTheme) {
        this.dynamicImage = dynamicImage;
        this.userPhoto = userPhoto;
        this.dynamicAuthor = dynamicAuthor;
        this.dynamicTime = dynamicTime;
        this.dynamicTheme = dynamicTheme;
    }

    public DynamicListInfo(Bitmap userPhoto, String dynamicAuthor, String dynamicTime, String dynamicTheme) {
        this.userPhoto = userPhoto;
        this.dynamicAuthor = dynamicAuthor;
        this.dynamicTime = dynamicTime;
        this.dynamicTheme = dynamicTheme;
    }

    @Override
    public String toString() {
        return "DynamicListInfo{" +
                "dynamicImage=" + dynamicImage +
                ", userPhoto=" + userPhoto +
                ", dynamicAuthor='" + dynamicAuthor + '\'' +
                ", dynamicTime='" + dynamicTime + '\'' +
                ", dynamicTheme='" + dynamicTheme + '\'' +
                '}';
    }

    public Bitmap getDynamicImage() {
        return dynamicImage;
    }

    public void setDynamicImage(Bitmap dynamicImage) {
        this.dynamicImage = dynamicImage;
    }

    public Bitmap getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Bitmap userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getDynamicAuthor() {
        return dynamicAuthor;
    }

    public void setDynamicAuthor(String dynamicAuthor) {
        this.dynamicAuthor = dynamicAuthor;
    }

    public String getDynamicTime() {
        return dynamicTime;
    }

    public void setDynamicTime(String dynamicTime) {
        this.dynamicTime = dynamicTime;
    }

    public String getDynamicTheme() {
        return dynamicTheme;
    }

    public void setDynamicTheme(String dynamicTheme) {
        this.dynamicTheme = dynamicTheme;
    }
}
