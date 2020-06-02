<%@page import="android.bilibili_Bean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String account = request.getParameter("account");
String password = request.getParameter("password");
//登录
bilibili_Bean bean = new bilibili_Bean();
boolean result = bean.login(account,password);
if(result) {
out.println("登录成功");
}
else {
out.println("登录失败");
}
%>