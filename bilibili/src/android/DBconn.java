package android;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 数据库连接
 * @author zjfcdf
 *
 */
public class DBconn {

	/**
	 * 获取数据库连接
	 * @return: Connection, 数据库连接
	 */
    public static Connection GetConnection(){
    	
    	Connection con = null;
    	 String url = "jdbc:mysql://localhost:3306/android?useSSL=false&serverTimezone=Asia/Shanghai";
         String username = "root";  
         String password = "123456";
    	try{
    		Class.forName( "com.mysql.cj.jdbc.Driver" );// 加载MySql数据驱动
    	    con = DriverManager.getConnection( url,username, password);// 创建数据库连接
    	}
    	catch( Exception e ){
    		System.out.printf( "数据库连接失败\n" );
    	}
    	
    	return con;

    }

	
}
