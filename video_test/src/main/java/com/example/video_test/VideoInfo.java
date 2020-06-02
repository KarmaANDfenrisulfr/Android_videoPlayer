package com.example.video_test;

public class VideoInfo {
    String head_url;
    String first_pic;
    String share_url;
    String video_time;
    String upload_time;
    String name;
    String description;

    public VideoInfo(){

    }

    public VideoInfo(String head_url, String first_pic, String share_url, String video_time, String upload_time, String name, String description) {
        this.head_url = head_url;
        this.first_pic = first_pic;
        this.share_url = share_url;
        this.video_time = video_time;
        this.upload_time = upload_time;
        this.name = name;
        this.description = description;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getFirst_pic() {
        return first_pic;
    }

    public void setFirst_pic(String first_pic) {
        this.first_pic = first_pic;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getVideo_time() {
        return video_time;
    }

    public void setVideo_time(String video_time) {
        this.video_time = video_time;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
