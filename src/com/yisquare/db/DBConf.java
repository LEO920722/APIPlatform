package com.yisquare.db;

import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.SQLException;
import org.apache.log4j.Logger;

import com.yisquare.tools.LogCreate;
import com.yisquare.tools.PropertiesUtil;

public class DBConf {  
	private static Logger logger = Logger.getLogger(DBConf.class);
    private static final String name = "oracle.jdbc.driver.OracleDriver";  
//	private static String url = "jdbc:oracle:thin:@10.143.181.60:1521:cnywmdev";  //�������ʱ���뽫ip��Ϊ��Ӧ��ip
//    private static String user = "CNINTWMDVCA";  
//    private static String password = "wmdvca1607";  
    private  Connection conn=null;
   
    public  Connection getConn() {
	    try {
	        Class.forName(name); 
	        String ipString = PropertiesUtil.getDBInfo();//��ȡ��ص�db��Ϣ
	        String ipArray [] = ipString.split("-");
	        conn = DriverManager.getConnection("jdbc:oracle:thin:@"+ipArray[0]+":"+ipArray[1]+":"+ipArray[2], ipArray[3], ipArray[4]);  
//	        conn = DriverManager.getConnection(url,user,password);
	    } catch (Exception e) {  
	    	logger.error("Exception:"+LogCreate.getException(e));
	        e.printStackTrace();  
	    }
	    return conn;
    }  
    

  public void getConnection(String ip,String port, String sid, String username, String  password) throws SQLException, ClassNotFoundException{
	  Class.forName(name); 
	  String string = "jdbc:oracle:thin:@"+ip+":"+port+":"+sid;
	  System.out.println(string);
      DriverManager.getConnection(string, username, password);
      
  }
}  