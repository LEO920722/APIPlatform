package com.yisquare.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;

import org.apache.log4j.Logger;

import com.yisquare.servlet.DeleteApi;
import com.yisquare.servlet.Log4jListener;

public class PropertiesUtil {
	private static Logger logger = Logger.getLogger(DeleteApi.class);
	static String path = Log4jListener.path;
	public static String getDBInfo() throws IOException{
		 logger.warn("��ȡ��·���ǣ�"+path);
		 FileInputStream fileInputStream = new FileInputStream(path);
         InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
         BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
         StringBuffer sb = new StringBuffer();
         String text = null;
         while((text = bufferedReader.readLine()) != null){
             sb.append(text);
         }
         bufferedReader.close();
         return sb.toString();
	}
	public static void setDBInfo(String ip) throws IOException{
		/* д��Txt�ļ� */
		// Remove print console -- 20180612
		//System.out.println("���õ������ǣ�"+ip);
		File writename = new File(path); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�
		BufferedWriter out = new BufferedWriter(new FileWriter(writename));
		
		out.write(ip); // \r\n��Ϊ����
		out.flush(); // �ѻ���������ѹ���ļ�
		out.close(); // ���ǵùر��ļ�
	}
}
