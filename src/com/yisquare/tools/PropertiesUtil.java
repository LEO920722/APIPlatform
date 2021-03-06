package com.yisquare.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;

import org.apache.log4j.Logger;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;

//import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil.ToStringAdapter;
import com.yisquare.servlet.DeleteApi;
import com.yisquare.servlet.Log4jListener;
import com.yisquare.tools.*;

import oracle.net.aso.b;


public class PropertiesUtil {
	private static Logger logger = Logger.getLogger(DeleteApi.class);
	static String path = Log4jListener.path;

	public static String getDBInfo() throws Exception {
//		logger.warn("读取的路径是：" + path);
//		// 从文件中读取数据，从输入流对象读取文件。
//		FileInputStream fileInputStream = new FileInputStream(path);
//		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//		StringBuffer sb = new StringBuffer();
//		String text = null;
//		while ((text = bufferedReader.readLine()) != null) {
//			sb.append(text);
//		}
//		bufferedReader.close();
//		return sb.toString();
		String resultInfo = DesUtil.getInfo();
		return resultInfo;
	}
	
	public static void setDBInfo(String ip) throws Exception {
		/* 写入Txt文件 */
		// Remove print console -- 20180612
//		File writename = new File(path); // 相对路径，如果没有则要建立一个新的output。txt文件
//		BufferedWriter out = new BufferedWriter(new FileWriter(writename));
//		out.write(ip); // \r\n即为换行,Base64加密
//		out.flush(); // 把缓存区内容压入文件
//		out.close(); // 最后记得关闭文件
		DesUtil.saveDBinfo(ip);
	}
}
