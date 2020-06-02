package android;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class bilibili_Bean {
	
	public boolean registered(String account,String password,String userPhoto) {

	Connection conn = DBconn.GetConnection();// 获得连接
	
	try{
		String sql = " insert into up "
				+ " (account,password,userPhoto) " 
				+ " values(?, ?, ?)";
		PreparedStatement st = conn.prepareStatement(  sql );
		
		st.setString(1, account);
		st.setString(2, password);
		st.setString(3, userPhoto);
					
		int count = st.executeUpdate(  );// 执行语句
		System.out.printf( "新增%d条注册记录\n", count );
	}
	catch( SQLException e ){
		System.out.printf( "新增注册失败:" + e.getMessage()+"\n" );
		return false;
	}
	finally{
		if( conn != null ){
			try{
				conn.close();
			}
			catch( SQLException e ){
				System.out.printf( "关闭连接失败\n" + e.getMessage()  );
			}// try
		}// if
		
	}// finally
	return true;
}
	
	public boolean login(String account,String password) {

		Connection conn = DBconn.GetConnection();// 获得连接
		try{
			PreparedStatement st = conn.prepareStatement( 
 					"select  password  from  up where account=\""+account+"\"");		
			ResultSet rs = st.executeQuery(  );// 执行查询语句
			String psd=null;
			while( rs.next() ){
 			   psd = rs.getString( "password" );
 			}
			if(psd.equals(password)) {
				return true;
			}
		}
		catch( SQLException e ){
			System.out.printf( "数据库查询失败\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
			
		}// finally
		return false;
	}
	
	public boolean changePassword(String account,String newPassword) {

		Connection conn = DBconn.GetConnection();// 获得连接
		try{
			PreparedStatement st = conn.prepareStatement( 
 					"update  up  set  password =\""
 					+newPassword+"\""
 					+"where account=\""+account+"\"");		
			int count = st.executeUpdate(  );// 执行语句
			System.out.printf( "更新%d条记录", count );
		}
		catch( SQLException e ){
			System.out.printf( "数据库查询失败\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
			
		}// finally
		return true;
	}
	
	public String getAttentionNumber(String account) {
		String count="0";
		Connection conn = DBconn.GetConnection();// 获得连接
		try{
	 		PreparedStatement st = conn.prepareStatement( 
	 					"select count(*) as total "
	 		 			+" from attention "
	 		 			+" where account=\""+account+"\" ");
	 		 					
			ResultSet rs = st.executeQuery(  );// 执行查询语句
			if(rs.next()){
				   count = rs.getString("total");
				}
			System.out.printf( "共有%s个关注\n", count);
		}
		catch( SQLException e ){
			System.out.printf( "查询失败:" + e.getMessage()+"\n");
			return new String("false");
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
			
		}// finally
		return count;
	}

	public String getFansNumber(String account) {
		String count="0";
		Connection conn = DBconn.GetConnection();// 获得连接
		
		try{
	 		PreparedStatement st = conn.prepareStatement( 
	 					"select count(*) as total "
	 		 			+" from fans "
	 		 			+" where account=\""+account+"\" ");
	 		 					
			ResultSet rs = st.executeQuery(  );// 执行查询语句
			if(rs.next()){
				   count = rs.getString("total");
				}
			System.out.printf( "共有%s个粉丝\n", count);
		}
		catch( SQLException e ){
			System.out.printf( "查询失败:" + e.getMessage()+"\n");
			return new String("false");
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
			
		}// finally
		return count;
	}

	public String getDynamicNumber(String account) {
		String count="0";
		Connection conn = DBconn.GetConnection();// 获得连接
		try{
			PreparedStatement st = conn.prepareStatement( 
 					"select count(*) as total "
 		 			+" from dynamic where account=\""
 					+account+"\" ");
			ResultSet rs = st.executeQuery(  );// 执行查询语句
			if(rs.next()){
				   count = rs.getString("total");
				}
			System.out.printf( "共有%s个动态\n", count);
		}
		catch( SQLException e ){
			System.out.printf( "查询失败:" + e.getMessage()+"\n");
			return new String("false");
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
			
		}// finally
		return count;
	}

	public User getUser(String account) {
		User info=new User();
		Connection conn = DBconn.GetConnection();// 获得连接
		try{
	 		PreparedStatement st = conn.prepareStatement( 
	 					"select userPhoto,nickName,sex,birthday,signature "
	 		 			+" from up "
	 		 			+" where account=\""+account+"\" ");
	 		 					
			ResultSet rs = st.executeQuery(  );// 执行查询语句
			if(rs.next()) {
				info.setUserPhoto(rs.getString("userPhoto"));
				info.setNickName(rs.getString("nickName"));
				info.setSex(rs.getString("sex"));
				info.setBirthday(rs.getString("birthday"));
				info.setSignature(rs.getString("signature"));
			}
			System.out.printf( "查询User成功\n");
		}
		catch( SQLException e ){
			System.out.printf( "查询User失败:" + e.getMessage()+"\n");
			return new User ("error");
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
			
		}// finally
		return info;
	}

	public List<User> getAttention(String account) {
		ArrayList<User> data=new ArrayList<User>();
		String id="";
		Connection conn = DBconn.GetConnection();// 获得连接
		try {
			PreparedStatement pt = conn.prepareStatement( 
 					"select attention "
 		 			+" from attention "
 		 			+" where account=\""+account+"\" ");
 		 					
			ResultSet ps = pt.executeQuery(  );// 执行查询语句
			while(ps.next()) {
				id=id+ps.getString("attention")+",";
			}
		}catch( SQLException e ){
			System.out.printf( "查询关注人账号失败:" + e.getMessage()+"\n");
		}
		try{
			if(id.length()>0){
				id=id.substring(0,id.length() -1);
		 		PreparedStatement st = conn.prepareStatement( 
		 					"select account,userPhoto,nickName,signature "
		 		 			+" from up "
		 		 			+" where account in ("+id+") and account !=\""+account+"\" ");			
				ResultSet rs = st.executeQuery(  );// 执行查询语句
				while(rs.next()) {
					User info=new User();
					info.setAccount(rs.getString("account"));
					info.setUserPhoto(rs.getString("userPhoto"));
					info.setNickName(rs.getString("nickName"));
					info.setSignature(rs.getString("signature"));
					data.add(info);
				}
				System.out.printf( "查询关注人信息成功\n");
			}
		}
		catch( SQLException e ){
			System.out.printf( "查询关注人信息失败:" + e.getMessage()+"\n");
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
			
		}// finally
		return data;
	}

	public List<User> getFans(String account) {
		ArrayList<User> data=new ArrayList<User>();
		String id="";
		Connection conn = DBconn.GetConnection();// 获得连接
		try {
			PreparedStatement pt = conn.prepareStatement( 
 					"select fan "
 		 			+" from fans "
 		 			+" where account=\""+account+"\" ");
 		 					
			ResultSet ps = pt.executeQuery(  );// 执行查询语句
			while(ps.next()) {
				id=id+ps.getString("fan")+",";
			}
		}catch( SQLException e ){
			System.out.printf( "查询关注人账号失败:" + e.getMessage()+"\n");
		}
		try{
			if(id.length()>0){
				id=id.substring(0,id.length() -1);
		 		PreparedStatement st = conn.prepareStatement( 
		 					"select account,userPhoto,nickName,signature "
		 		 			+" from up "
		 		 			+" where account in ("+id+") and account !=\""+account+"\" ");
		 		 					
				ResultSet rs = st.executeQuery(  );// 执行查询语句
				while(rs.next()) {
					User info=new User();
					info.setAccount(rs.getString("account"));
					info.setUserPhoto(rs.getString("userPhoto"));
					info.setNickName(rs.getString("nickName"));
					info.setSignature(rs.getString("signature"));
					data.add(info);
				}
				System.out.printf( "查询粉丝信息成功\n");
			}
		}
		catch( SQLException e ){
			System.out.printf( "查询粉丝信息失败:" + e.getMessage()+"\n");
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
			
		}// finally
		return data;
	}
	
	public boolean setUser(String account,String userPhoto,String nickName,String sex,String birthday,String signature) {
		Connection conn = DBconn.GetConnection();// 获得连接
		try{
	 		PreparedStatement st = conn.prepareStatement( 
	 				"update  up  set  userPhoto=?,nickName=?,sex=?,birthday=?,signature=? "
	 	 					+" where account=\""+account+"\" ");
	 		
	 		st.setString(1, userPhoto);					
			st.setString(2, nickName);
			st.setString(3, sex);
			st.setString(4, birthday);
			st.setString(5, signature);	
			int count = st.executeUpdate(  );// 执行语句
			System.out.printf( "更新%d条up信息记录\n", count );
			
			PreparedStatement pt = conn.prepareStatement( 
	 				"update  dynamic  set  author=? ,userPhoto=? "
	 	 					+" where account=\""+account+"\" ");
	 		 					
			pt.setString(1, nickName);
			pt.setString(2, userPhoto);
			int counts = pt.executeUpdate(  );// 执行语句
			System.out.printf( "更新%d条dynamic昵称头像记录\n", counts );
			
		}
		catch( SQLException e ){
			System.out.printf( "更新失败:" + e.getMessage()+"\n");
			return false;
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
			
		}// finally
		return true;
	}

	public List<Dynamic> getPersonDynamic(String account) {
		ArrayList<Dynamic> data=new ArrayList<Dynamic>();
		Connection conn = DBconn.GetConnection();// 获得连接
		try{
	 		PreparedStatement st = conn.prepareStatement( 
	 					"select author,theme,picture,time "
	 		 			+" from dynamic "
	 		 			+" where account=\""+account+"\" ");
	 		 					
			ResultSet rs = st.executeQuery(  );// 执行查询语句
			while(rs.next()) {
				Dynamic info=new Dynamic();
				info.setDynamicAuthor(rs.getString("author"));
				info.setDynamicTheme(rs.getString("theme"));
				info.setDynamicImage(rs.getString("picture"));
				info.setDynamicTime(rs.getString("time"));
				data.add(info);
			}
			System.out.printf( "查询Dynamic成功\n");
		}
		catch( SQLException e ){
			System.out.printf( "查询失败:" + e.getMessage()+"\n");
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
			
		}// finally
		return data;
	}

	public List<Dynamic> getAttentionDynamic(String account) {
		ArrayList<Dynamic> data=new ArrayList<Dynamic>();
		ArrayList<String> user=new ArrayList<String>();
		String id="";
		Connection conn = DBconn.GetConnection();// 获得连接
		try{
	 		PreparedStatement st = conn.prepareStatement( 
	 					"select attention "
	 		 			+" from attention "
	 		 			+" where account=\""+account+"\" ");	
			ResultSet rs = st.executeQuery(  );// 执行查询语句
			while(rs.next()) {
				user.add(rs.getString("attention"));
			}
			System.out.printf( "查询动态用户成功\n");
		}
		catch( SQLException e ){
			System.out.printf( "查询动态用户失败:" + e.getMessage()+"\n");
		}
		Iterator<String> iter=user.iterator();
			while(iter.hasNext()) {
				id=id+iter.next()+",";
			}
			if(id.length()>0){
				id=id.substring(0,id.length() -1);
				try{
					PreparedStatement pt = conn.prepareStatement( 
			 				"select userPhoto,author,theme,picture,time "
			 		 		+" from dynamic "
			 		 		+" where account in ("+id+") "
			 		 		+" order by time desc");
		 					
						ResultSet ps = pt.executeQuery(  );// 执行查询语句
					while(ps.next()) {
						Dynamic info=new Dynamic();
						info.setUserPhoto(ps.getString("userPhoto"));
						info.setDynamicAuthor(ps.getString("author"));
						info.setDynamicTheme(ps.getString("theme"));
						info.setDynamicImage(ps.getString("picture"));
						info.setDynamicTime(ps.getString("time"));
						data.add(info);
					}
					System.out.printf( "查询Dynamic成功\n");
				}
				catch( SQLException e ){
					System.out.printf( "查询失败:" + e.getMessage()+"\n");
				}
				finally{
					if( conn != null ){
						try{
							conn.close();
						}
						catch( SQLException e ){
							System.out.printf( "关闭连接失败\n" + e.getMessage()  );
						}// try
					}// if
				}// finally
			}
			else {
				if( conn != null ){
					try{
						conn.close();
					}
					catch( SQLException e ){
						System.out.printf( "关闭连接失败\n" + e.getMessage()  );
					}// try
				}// if
			}
		
		return data;
	}
	
	public boolean pushDynamic(String account,String userPhoto,String nickName,String theme,String image,String time) {
		Connection conn = DBconn.GetConnection();// 获得连接
		try{
			String sql = " insert into dynamic "
					+ " (account,userPhoto,author,theme,picture,time) " 
					+ " values(?, ?, ?, ?, ?, ?)";
			PreparedStatement st = conn.prepareStatement( sql );
			
			st.setString(1, account);
			st.setString(2, userPhoto);
			st.setString(3, nickName);
			st.setString(4, theme);	
			st.setString(5, image);
			st.setString(6, time);
			int count = st.executeUpdate(  );// 执行语句
			System.out.printf( "新增%d条dynamic记录\n", count );
		}
		catch( SQLException e ){
			System.out.printf( "插入dynamic信息失败:" + e.getMessage()+"\n");
			return false;
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
		}// finally
		return true;
	}

	public boolean deleteAttention(String account,String attention) {
		Connection conn = DBconn.GetConnection();// 获得连接
		try{
			PreparedStatement st = conn.prepareStatement( 
 					"delete from attention where account=? and attention=? ");
			
			st.setString(1, account);
			st.setString(2, attention);
			int count = st.executeUpdate(  );// 执行语句
			System.out.printf( "删除%d条关注记录\n", count );
			
			PreparedStatement pt = conn.prepareStatement( 
 					"delete from fans where account=? and fan=? ");
			
			pt.setString(1, attention);
			pt.setString(2, account);
			int counts = pt.executeUpdate(  );// 执行语句
			System.out.printf( "删除%d条粉丝记录\n", counts );
		}
		catch( SQLException e ){
			System.out.printf( "数据库删除失败\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
		}// finally
		return true;
	}

	public boolean addAttention(String account,String attention) {
		Connection conn = DBconn.GetConnection();// 获得连接
		try{
			PreparedStatement st = conn.prepareStatement( 
					" insert into attention "
							+ " (account,attention) " 
							+ " values(?, ?)" );		
			st.setString(1, account);
			st.setString(2, attention);
			int count = st.executeUpdate(  );// 执行语句
			System.out.printf( "插入%d条关注记录\n", count );
			
			PreparedStatement pt = conn.prepareStatement( 
					" insert into fans "
							+ " (account,fan) " 
							+ " values(?, ?)" );		
			pt.setString(1, attention);
			pt.setString(2, account);
			int counts = pt.executeUpdate(  );// 执行语句
			System.out.printf( "插入%d条粉丝记录\n", counts );
		}
		catch( SQLException e ){
			System.out.printf( "数据库插入失败\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
			
		}// finally
		return true;
	}

	public List<User> getNewUp(String account,String keyword) {
		ArrayList<User>data=new ArrayList<User>();
		String id="";
		Connection conn = DBconn.GetConnection();// 获得连接
		try{
			PreparedStatement pt = conn.prepareStatement( 
 					"select attention "
 		 			+" from attention "
 		 			+" where account=\""+account+"\" ");
			ResultSet ps = pt.executeQuery(  );// 执行查询语句
			while(ps.next()) {
				id=id+ps.getString("attention")+",";
			}
			System.out.printf( "查询attention成功\n");
		}catch(SQLException e ){
			System.out.printf( "查询attention失败:" + e.getMessage()+"\n");
		}
		id=id+account;
		try {
		 		PreparedStatement st = conn.prepareStatement( 
		 					"select account,userPhoto,nickName,signature "
		 		 			+" from up "
		 		 			+" where  account not in ("+id+") and nickName like \"%"+keyword+"%\"");
		 		 					
				ResultSet rs = st.executeQuery(  );// 执行查询语句
				while(rs.next()) {
					User info=new User();
					info.setAccount(rs.getString("account"));
					info.setUserPhoto(rs.getString("userPhoto"));
					info.setNickName(rs.getString("nickName"));
					info.setSignature(rs.getString("signature"));
					data.add(info);
				}
				System.out.printf( "查询NewUp成功\n");
		}
		catch( SQLException e ){
			System.out.printf( "查询NewUp失败:" + e.getMessage()+"\n");
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
			
		}// finally
		return data;
	}
	
	public boolean deleteDynamic(String account,String time) {
		Connection conn = DBconn.GetConnection();// 获得连接
		try{
			PreparedStatement st = conn.prepareStatement( 
 					"delete from dynamic where account=? and time=? ");
			
			st.setString(1, account);
			st.setString(2, time);
			int count = st.executeUpdate(  );// 执行语句
			System.out.printf( "删除%d条动态记录\n", count );
		}
		catch( SQLException e ){
			System.out.printf( "数据库删除动态失败\n" + e.getMessage()  );
			return false;
		}
		finally{
			if( conn != null ){
				try{
					conn.close();
				}
				catch( SQLException e ){
					System.out.printf( "关闭连接失败\n" + e.getMessage()  );
				}// try
			}// if
		}// finally
		return true;
	}

}