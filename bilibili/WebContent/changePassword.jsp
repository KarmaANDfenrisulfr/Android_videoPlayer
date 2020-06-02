<%@page import="android.bilibili_Bean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String account = request.getParameter("account");
String newPassword = request.getParameter("newPassword");
//新增
bilibili_Bean bean = new bilibili_Bean();
boolean result = bean.changePassword(account,newPassword);
if(result) {
out.println("修改成功");
}
else {
out.println("修改失败");
}
%>