<%@page import="android.bilibili_Bean"%>
<%@page import="android.Dynamic"%>
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
List<Dynamic> data=bean.getAttentionDynamic(account);
out.println("[");
Iterator<Dynamic> dynamic=data.iterator();
while(dynamic.hasNext()){
	Dynamic Info=dynamic.next();
	out.println("{");
	out.println(add_String("userPhoto",Info.getUserPhoto()));
	out.println(add_String("author",Info.getDynamicAuthor()));
	out.println(add_String("theme",Info.getDynamicTheme()));
	out.println(add_String("image",Info.getDynamicImage()));
	out.println(add_String2("uploadtime",Info.getDynamicTime()));
	out.println("}");
	if(dynamic.hasNext())
	{
		out.println(",");
	}
}
out.println("]");
%>