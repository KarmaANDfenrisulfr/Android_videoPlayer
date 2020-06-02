package android;

public class Dynamic {
    String dynamicImage;
    String userPhoto;
    String dynamicAuthor;
    String dynamicTime;
    String dynamicTheme;
    public Dynamic(){

    }

    public Dynamic(String dynamicImage, String userPhoto, String dynamicAuthor, String dynamicTime, String dynamicTheme) {
        this.dynamicImage = dynamicImage;
        this.userPhoto = userPhoto;
        this.dynamicAuthor = dynamicAuthor;
        this.dynamicTime = dynamicTime;
        this.dynamicTheme = dynamicTheme;
    }


    public String getDynamicImage() {
        return dynamicImage;
    }

    public void setDynamicImage(String dynamicImage) {
        this.dynamicImage = dynamicImage;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
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
