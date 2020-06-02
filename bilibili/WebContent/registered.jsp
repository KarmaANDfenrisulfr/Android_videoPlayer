<%@page import="android.bilibili_Bean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String account = request.getParameter("account");
String password = request.getParameter("password");
String userPhoto=request.getParameter("userPhoto");
//新增
bilibili_Bean bean = new bilibili_Bean();
boolean result = bean.registered(account,password,userPhoto);
if(result) {
out.println("注册成功");
}
else {
out.println("注册失败");
}
%>