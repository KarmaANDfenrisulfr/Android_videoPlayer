<%@page import="android.bilibili_Bean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String account = request.getParameter("account");
String time = request.getParameter("time");
//删除
bilibili_Bean bean = new bilibili_Bean();
boolean result = bean.deleteDynamic(account,time);
if(result) {
out.println("删除成功");
}
else {
out.println("删除失败");
}
%>