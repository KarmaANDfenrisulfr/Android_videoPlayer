package com.example.video_test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    private static final String base_url="http://192.168.43.61:8080/bilibili/";
    public static final MediaType FORM_CONTENT_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");//解决OKHttp传输中文乱码问题

    public static OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            . writeTimeout(120, TimeUnit.SECONDS)
            .build();

    public static void registered(String url,String account,String password,String userPhoto,okhttp3.Callback callback) {
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account)
                .add("password", password)
                .add("userPhoto", userPhoto)
                .build();
        Request request = new Request.Builder()
                .url(base_url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void login(String url,String account,String password,okhttp3.Callback callback) {
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(base_url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void changePassword(String url,String account,String newPassword,okhttp3.Callback callback) {
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account)
                .add("newPassword", newPassword)
                .build();
        Request request = new Request.Builder()
                .url(base_url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getMyActivityNeedNumber(String url,String account,okhttp3.Callback callback) {
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account)
                .build();
        Request request = new Request.Builder()
                .url(base_url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void setAccountInformation(String url,String account,String userPhoto,String nickName,String sex,String birthday,String signature,okhttp3.Callback callback){
        try {
            nickName= URLEncoder.encode(nickName,"utf-8");
            sex= URLEncoder.encode(sex,"utf-8");
            signature= URLEncoder.encode(signature,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account)
                .add("userPhoto",userPhoto)
                .add("nickName",nickName)
                .add("sex",sex)
                .add("birthday",birthday)
                .add("signature",signature)
                .build();
        Request request = new Request.Builder()
                .url(base_url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getVideo(String url,okhttp3.Callback callback) {
        RequestBody requestBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getPersonDynamic(String url,String account,okhttp3.Callback callback) {
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account)
                .build();
        Request request = new Request.Builder()
                .url(base_url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getAttentionDynamic(String url,String account,okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account)
                .build();
        Request request = new Request.Builder()
                .url(base_url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void pushDynamic(String url,String account,String userPhoto,String author,String theme,String image,String time,okhttp3.Callback callback){
        try {
            author= URLEncoder.encode(author,"utf-8");
            theme= URLEncoder.encode(theme,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account)
                .add("userPhoto",userPhoto)
                .add("author",author)
                .add("theme",theme)
                .add("image",image)
                .add("time",time)
                .build();
        Request request = new Request.Builder()
                .url(base_url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getAttention(String url,String account,okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account)
                .build();
        Request request = new Request.Builder()
                .url(base_url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void addAttention(String url,String account,String attention,okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account)
                .add("attention", attention)
                .build();
        Request request = new Request.Builder()
                .url(base_url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void deleteAttention(String url,String account,String attention,okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account)
                .add("attention", attention)
                .build();
        Request request = new Request.Builder()
                .url(base_url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getFans(String url,String account,okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account)
                .build();
        Request request = new Request.Builder()
                .url(base_url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void getNewUp(String url,String account,String keyword,okhttp3.Callback callback){
        try {
            keyword= URLEncoder.encode(keyword,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account)
                .add("keyword", keyword)
                .build();
        Request request = new Request.Builder()
                .url(base_url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void deleteDynamic(String url,String account,String time,okhttp3.Callback callback){
        RequestBody requestBody = new FormBody.Builder()
                .add("account", account)
                .add("time", time)
                .build();
        Request request = new Request.Builder()
                .url(base_url+url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
