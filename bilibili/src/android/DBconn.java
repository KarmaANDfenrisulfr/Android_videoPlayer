package android;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * ���ݿ�����
 * @author zjfcdf
 *
 */
public class DBconn {

	/**
	 * ��ȡ���ݿ�����
	 * @return: Connection, ���ݿ�����
	 */
    public static Connection GetConnection(){
    	
    	Connection con = null;
    	 String url = "jdbc:mysql://localhost:3306/android?useSSL=false&serverTimezone=Asia/Shanghai";
         String username = "root";  
         String password = "123456";
    	try{
    		Class.forName( "com.mysql.cj.jdbc.Driver" );// ����MySql��������
    	    con = DriverManager.getConnection( url,username, password);// �������ݿ�����
    	}
    	catch( Exception e ){
    		System.out.printf( "���ݿ�����ʧ��\n" );
    	}
    	
    	return con;

    }

	
}
