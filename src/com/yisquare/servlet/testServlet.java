package com.yisquare.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.tomcat.util.http.fileupload.FileItem;

import com.yisquare.tools.DBUtil;

/**
 * Servlet implementation class test
 */
public class testServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public testServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		//�жϱ��Ƿ���������ύ����
		  boolean isMultipart = ServletFileUpload.isMultipartContent(request);  
		        if(!isMultipart){  
		            throw new RuntimeException("�������ı���enctype���ԣ�ȷ����multipart/*");  
		        } 
		        DiskFileItemFactory dfif = new DiskFileItemFactory();  
		        ServletFileUpload parser = new ServletFileUpload(dfif);  

		        parser.setFileSizeMax(3*1024*1024);//���õ����ļ��ϴ��Ĵ�С  
		        parser.setSizeMax(6*1024*1024);//���ļ��ϴ�ʱ�ܴ�С����  
		        //�ļ���Ŀ¼
		        String uploadPath = this.getServletContext().getRealPath("")  + File.separator;
                System.out.println(uploadPath);
		        //���������,�Ա���֮��ȡ��
		       Map<String,String> map = new HashMap<String, String>();

		        try {
		            List<?> items = parser.parseRequest(request);
		            Iterator iter = items.iterator();
		            if (items != null && items.size() > 0) {
		                // ����������
		                while (iter.hasNext()) {  
		                    FileItem item = (FileItem) iter.next();  
		                    if (item.isFormField()) {  
		                        //�������ͨ���ֶ�  
		                        String name = item.getFieldName();  //��ñ�name�ֶ�����
		                        String value = item.getString();  //��ñ�name�ֶ����ƶ�Ӧ��ֵ
		                        map.put(name, value);
//		                        log.info("name={},value={}",name,value);
		                    } else if (!item.isFormField()&&item.getName()!=null) {
		                        String fileName = new File(item.getName()).getName();
		                        if(fileName == null || "".equals(fileName)){
		                            continue;
		                        }
		                        String filePath = uploadPath  + fileName;
		                        map.put("filePath",filePath);
		                        File storeFile = new File(filePath);
		                        // �ڿ���̨����ļ����ϴ�·��
		                        System.out.println(filePath);
		                        // ����ļ��������򱣴浽ָ���ļ��з���ֱ�ӷ���
		                        if(!storeFile.exists())
		                            item.write(storeFile);
		                        request.setAttribute("message",
		                            "�ļ��ϴ��ɹ�!");
		                        continue;
		                    }
		                }
		            }
		        } catch (Exception e) {
		             request.setAttribute("message","������Ϣ: " + e.getMessage());
		        }
	}

}
