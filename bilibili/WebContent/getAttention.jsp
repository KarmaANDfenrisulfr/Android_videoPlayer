<%@page import="android.bilibili_Bean"%>
<%@page import="android.User"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%! 
 private String add_String(String index,String temp){
	    String str="\""+index+"\":"+"\""+temp+"\""+",";
		return str;
 }
 private String add_String2(String index,String temp){
	    String str="\""+index+"\":"+"\""+temp+"\"";
		return str;
}
 %>
<%
bilibili_Bean bean=new bilibili_Bean();
String account=request.getParameter("account");
List<User> data=bean.getAttention(account);
out.println("[");
Iterator<User> dynamic=data.iterator();
while(dynamic.hasNext()){
	User Info=dynamic.next();
	out.println("{");
	out.println(add_String("account",Info.getAccount()));
	out.println(add_String("userPhoto",Info.getUserPhoto()));
	out.println(add_String("nickName",Info.getNickName()));
	out.println(add_String2("signature",Info.getSignature()));
	out.println("}");
	if(dynamic.hasNext())
	{
		out.println(",");
	}
}
out.println("]");
%>