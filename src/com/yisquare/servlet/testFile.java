package com.yisquare.servlet;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class testFile extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        fileControl(req, resp);
    }

    /**
     * �ϴ��ļ��Ĵ���
     * @throws UnsupportedEncodingException 
     */
    private void fileControl(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnsupportedEncodingException {
    	request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
        // �ڽ�������֮ǰ���ж����������Ƿ�Ϊ�ļ��ϴ�����
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        System.out.println("�û�������ļ����ǣ�"+isMultipart);
        // �ļ��ϴ�������
        FileItemFactory factory = new DiskFileItemFactory();

        // �����ļ��ϴ�������
        ServletFileUpload upload = new ServletFileUpload(factory);

        // ��ʼ����������Ϣ
        List items = null;
        try {
            items = upload.parseRequest(request);
        }
        catch (FileUploadException e) {
            e.printStackTrace();
        }

        // ������������Ϣ�����ж�
        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            // ��ϢΪ��ͨ�ĸ�ʽ
            if (item.isFormField()) {
                String fieldName = item.getFieldName();
                String value = item.getString();
                String remk = new String(item.getString().getBytes("iso-8859-1"),"UTF-8");
                System.out.println(fieldName+""+remk);
            }
            // ��ϢΪ�ļ���ʽ
            else {
                String fileName = item.getName();
                int index = fileName.lastIndexOf("\\");
                fileName = fileName.substring(index + 1);
                request.setAttribute("realFileName", fileName);

                // ���ļ�д��
//                String path = req.getContextPath();
//                String directory = "uploadFile";
//                String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/" + directory;
                String basePath = request.getSession().getServletContext().getRealPath("/uploadFile");
                File file = new File(basePath, fileName);
                file.setWritable(true,false);
                try {
                    item.write(file);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
        	response.getWriter().print("�ϴ��ɹ�");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}