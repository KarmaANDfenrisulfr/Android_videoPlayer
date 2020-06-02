<%@page import="android.bilibili_Bean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String account = request.getParameter("account");
String attention = request.getParameter("attention");
//删除
bilibili_Bean bean = new bilibili_Bean();
boolean result = bean.deleteAttention(account,attention);
if(result) {
out.println("取消关注成功");
}
else {
out.println("取消关注失败");
}
%>