<%@page import="android.bilibili_Bean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String account = request.getParameter("account");
String userPhoto=request.getParameter("userPhoto");
String author = request.getParameter("author");
String theme = request.getParameter("theme");
String image = request.getParameter("image");
String time = request.getParameter("time");
author=java.net.URLDecoder.decode(author, "UTF-8");
theme= java.net.URLDecoder.decode(theme, "UTF-8");
//更新
bilibili_Bean bean = new bilibili_Bean();
boolean result = bean.pushDynamic(account,userPhoto,author,theme,image,time);
if(result) {
out.println("发布成功");
}
else {
out.println("发布失败");
} 
%>