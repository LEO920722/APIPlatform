package com.yisquare.servlet;


import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

 
public class Log4jListener implements ServletContextListener {
	public static final String log4jdirkey = "log4jdir";
	public static String path = "";
	//public static String path2 = "";
	public void contextDestroyed(ServletContextEvent servletcontextevent) {
		System.getProperties().remove(log4jdirkey);
	}

	public void contextInitialized(ServletContextEvent servletcontextevent) {
		String log4jdir = servletcontextevent.getServletContext().getRealPath("/");
		path = log4jdir+"/config.txt";
		//path2 = log4jdir+"/key.txt";
		File file1 = new File(path);
		//File file2 = new File(path2);
        // �����ļ��� 
        if (!file1.exists()) {
        	try {
				file1.createNewFile();
				//file2.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		System.setProperty(log4jdirkey, log4jdir);
	} 
}
