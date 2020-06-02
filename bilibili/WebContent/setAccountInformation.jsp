<%@page import="android.bilibili_Bean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String account = request.getParameter("account");
String userPhoto=request.getParameter("userPhoto");
String nickName = request.getParameter("nickName");
String sex = request.getParameter("sex");
String birthday = request.getParameter("birthday");
String signature = request.getParameter("signature");
nickName=java.net.URLDecoder.decode(nickName, "UTF-8");
sex= java.net.URLDecoder.decode(sex, "UTF-8");
signature= java.net.URLDecoder.decode(signature, "UTF-8");
//更新
bilibili_Bean bean = new bilibili_Bean();
boolean result = bean.setUser(account,userPhoto,nickName,sex,birthday,signature);
if(result) {
out.println("编辑成功");
}
else {
out.println("编辑失败");
}
%>