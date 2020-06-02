<%@page import="android.bilibili_Bean"%>
<%@page import="android.User"%>
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
 private String add_Int(String index,int temp){
	    String demo=String.valueOf(temp);
	    String str="\""+index+"\":"+"\""+demo+"\"";
		return str;
}
 %>
<%
bilibili_Bean bean=new bilibili_Bean();
String account=request.getParameter("account");
String dynamic=bean.getDynamicNumber(account);
String attention=bean.getAttentionNumber(account);
String fans=bean.getFansNumber(account);
User info=bean.getUser(account);
String sex=info.getSex();
String flag="true";
if(dynamic.equals("false")||attention.equals("false")||fans.equals("false")){
	if(sex!=null&&sex.equals("error"))
		flag="false";
}
out.println("[");
out.println("{");
out.println(add_String("flag",flag));
out.println(add_String("dynamic",dynamic));
out.println(add_String("attention",attention));
out.println(add_String("fans",fans));
out.println(add_String("userPhoto",info.getUserPhoto()));
out.println(add_String("nickName",info.getNickName()));
out.println(add_String("sex",info.getSex()));
out.println(add_String("birthday",info.getBirthday()));
out.println(add_String2("signature",info.getSignature()));
out.println("}");
out.println("]");
%>