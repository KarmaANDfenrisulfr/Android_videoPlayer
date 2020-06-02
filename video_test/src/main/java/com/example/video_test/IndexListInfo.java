package com.example.video_test;

import android.graphics.Bitmap;

public class IndexListInfo {
    private Bitmap videoStartImage;
    private Bitmap userPhoto;
    private String videoTheme;
    private String videoAuthorAndTime;
    private String videoRunTime;
    private String video_url;
    private String upload_time;

    public IndexListInfo(){

    }

    public IndexListInfo(Bitmap videoStartImage, Bitmap userPhoto, String videoTheme, String videoAuthorAndTime, String videoRunTime,String video_url) {
        this.videoStartImage = videoStartImage;
        this.userPhoto = userPhoto;
        this.videoTheme = videoTheme;
        this.videoAuthorAndTime = videoAuthorAndTime;
        this.videoRunTime = videoRunTime;
        this.video_url=video_url;
    }

    public IndexListInfo(Bitmap videoStartImage, Bitmap userPhoto, String videoTheme, String videoAuthorAndTime,String upload_time, String videoRunTime,String video_url) {
        this.videoStartImage = videoStartImage;
        this.userPhoto = userPhoto;
        this.videoTheme = videoTheme;
        this.videoAuthorAndTime = videoAuthorAndTime;
        this.upload_time=upload_time;
        this.videoRunTime = videoRunTime;
        this.video_url=video_url;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public Bitmap getVideoStartImage() {
        return videoStartImage;
    }

    public void setVideoStartImage(Bitmap videoStartImage) {
        this.videoStartImage = videoStartImage;
    }

    public Bitmap getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(Bitmap userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getVideoTheme() {
        return videoTheme;
    }

    public void setVideoTheme(String videoTheme) {
        this.videoTheme = videoTheme;
    }

    public String getVideoAuthorAndTime() {
        return videoAuthorAndTime;
    }

    public void setVideoAuthorAndTime(String videoAuthorAndTime) {
        this.videoAuthorAndTime = videoAuthorAndTime;
    }

    public String getVideoRunTime() {
        return videoRunTime;
    }

    public void setVideoRunTime(String videoRunTime) {
        this.videoRunTime = videoRunTime;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
