package com.yisquare.servlet;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.net.aso.f;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.yisquare.tools.DBUtil;
import com.yisquare.tools.LogCreate;
import com.yisquare.tools.Util;
public class AddNewFiles extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AddNewFiles.class);
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
		String timestamp1 = Calendar.getInstance().getTimeInMillis()+""+new Random().nextInt(100);
		String skipApiID = "";//���û������ϴ����ļ���ʱ�� ��Ҫ��ת��ҳ�棬���iD��api��id
		String uploadApiId = "";//���id������Ψһ��ʶapi��id�����������ļ��õģ�ֵ��ע��apiʱ���ʱ���
		Hashtable<String, String> htFile=new Hashtable<String, String>();//��������files����
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
                System.out.println(remk);
                if("SKIP_API_ID".equals(fieldName)){
                	skipApiID = remk;
                }else{
                	if("API_ID".equals(fieldName)){
                		htFile.put("API_ID", remk);
                		uploadApiId = remk;
                	}else{
                		
                		htFile.put(fieldName, remk);
                	}
                }
            }
            // ��ϢΪ�ļ���ʽ
            else {
                String fileName = item.getName();
                int index = fileName.lastIndexOf("\\");
                fileName = fileName.substring(index + 1);
              //ע���ڴ洢�ļ���ʱ��Ϊ�˱������Ĳ��ܷ��ʻ������룬�ļ��ᱻ����������������ʽΪʱ���+�ļ���׺
                String targetFileName = timestamp1+"."+fileName.substring(fileName.lastIndexOf(".")+1);
                request.setAttribute("realFileName", fileName);
                // ���ļ�д��
//                String path = req.getContextPath();
//                String directory = "uploadFile";
//                String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/" + directory;
                String basePath = this.getServletContext().getRealPath("/uploadFile");
//                String basePathLinux = "/opt/webMethods/webMethods912/profiles/MWS_default/configuration/org.eclipse.osgi/uploadFile";
//                String basePath = Util.isOSLinux()?basePathLinux:basePathWindows;
                File file1 = new File(basePath);
                // �����ļ���
                if (!file1.exists()) {
                	file1.mkdirs();
                }
                File file = new File(basePath, targetFileName);
                try {
                	if(fileName.length()!=0){
                		item.write(file);
                		htFile.put("NAME", fileName);
                		htFile.put("URL", "/APIPlatform/DownLoadServlet?fileName="+targetFileName);
                		htFile.put("CREATE_TIME", Util.getNowFormat());
                		htFile.put("NOTE", targetFileName);
                		DBUtil.insert(htFile, "APIPlatform_FILE");
                		System.out.println(skipApiID+"=="+uploadApiId);
                		LogCreate.insertLog("Add new file <font color=\"#006F97\">"+fileName+"</font> successfully! API Id is "+skipApiID, "", Util.getNowFormat(), "SUCCESS", "CREATE FILE");
                		response.sendRedirect("pages/APIDetail.html?API_ID="+skipApiID+"&FILE_ID="+uploadApiId);//��ת���ո�����ҳ�棬���Ѹոյ�api_Id��file_id��timestamp����Ϊ����
                	}
                	else{
                		response.sendRedirect("pages/APIDetail.html?API_ID="+skipApiID+"&FILE_ID="+uploadApiId);//��ת���ո�����ҳ�棬���Ѹոյ�api_Id��file_id��timestamp����Ϊ����
                	}
                }
                catch (Exception e) {
                    e.printStackTrace();
                    logger.warn("add new File faild", e);
                    LogCreate.insertLog(e, "", Util.getNowFormat(), "WARNING", "CREATE FILE");
                    try {
						response.sendRedirect("pages/APIDetail.html?API_ID="+skipApiID+"&FILE_ID="+uploadApiId);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}//��ת���ո�����ҳ�棬���Ѹոյ�api_Id��file_id��timestamp����Ϊ����
                }
            }
        }
    }
}