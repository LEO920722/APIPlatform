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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.yisquare.tools.DBUtil;
import com.yisquare.tools.LogCreate;
import com.yisquare.tools.Util;

public class CreateAPI extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(CreateAPI.class);
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        fileControl(req, resp);
    }

    /**
     * �ϴ��ļ��Ĵ���
     * @throws UnsupportedEncodingException 
     */
    private void fileControl(HttpServletRequest request, HttpServletResponse response) throws ServletException, UnsupportedEncodingException {
    	Hashtable<String, String> ht=new Hashtable<String, String>();//��������api����
		Hashtable<String, String> htFile=new Hashtable<String, String>();//��������files����
		String timestamp = Calendar.getInstance().getTimeInMillis()+""+new Random().nextInt(1000);
		String timestamp1 = Calendar.getInstance().getTimeInMillis()+""+new Random().nextInt(100);
		String nowStr = Util.getNowFormat();
		String creator = "";
        // �ڽ�������֮ǰ���ж����������Ƿ�Ϊ�ļ��ϴ�����
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//        System.out.println("�û�������ļ����ǣ�"+isMultipart);
        FileItemFactory factory = new DiskFileItemFactory();// �ļ��ϴ�������
        ServletFileUpload upload = new ServletFileUpload(factory);// �����ļ��ϴ�������
        // ��ʼ����������Ϣ
        List items = null;
        try {
            items = upload.parseRequest(request);
        }
        catch (FileUploadException e) {
            e.printStackTrace();
        }
        Iterator iter = items.iterator();// ������������Ϣ�����ж�
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            // �ַ�����ʽ
            if (item.isFormField()) {
                String fieldName = item.getFieldName();
                String remk = new String(item.getString().getBytes("iso-8859-1"),"UTF-8");
                System.out.println(fieldName+""+remk);
                if(!"textfield".equals(fieldName)){
                	ht.put(fieldName, remk);
                }
                if("CREATOR".equals(fieldName)){
                	creator = remk;
                }
            }
            // �ļ���ʽ
            else {
                String fileName = item.getName();
                int index = fileName.lastIndexOf("\\");
                fileName = fileName.substring(index + 1);
                //ע���ڴ洢�ļ���ʱ��Ϊ�˱������Ĳ��ܷ��ʻ������룬�ļ��ᱻ����������������ʽΪʱ���+�ļ���׺
                String targetFileName = timestamp1+"."+fileName.substring(fileName.lastIndexOf(".")+1);
                request.setAttribute("realFileName", fileName);
                // ���ļ�д��
                String basePath = this.getServletContext().getRealPath("/uploadFile");
//                String basePathLinux = "/opt/webMethods/webMethods912/profiles/MWS_default/configuration/org.eclipse.osgi/uploadFile";
//                String basePath = Util.isOSLinux()?basePathLinux:basePathWindows;
                File file1 = new File(basePath);
                // �����ļ���
                if (!file1.exists()) {
                	file1.mkdirs();
                }
                File file = new File(basePath, targetFileName);
                try {//���ļ���Ϣд�����ݿ�
                	htFile.put("CREATOR", creator);
                	htFile.put("NAME", fileName);
                	htFile.put("CREATE_TIME", nowStr);
                	htFile.put("URL", "/APIPlatform/DownLoadServlet?fileName="+targetFileName);
                	htFile.put("API_ID", timestamp);
                	 htFile.put("NOTE", targetFileName);
                	if(fileName.length()!=0){
                		item.write(file);
                		DBUtil.insert(htFile,"APIPlatform_FILE");
                		LogCreate.insertLog("Add new file <font color=\"#006F97\">"+fileName+"</font> successfully!", creator, Util.getNowFormat(), "SUCCESS", "CREATE FILE");
                	}
                }
                catch (Exception e) {
                	logger.warn("upload file error:",e);
                }
            }
        }
        try {//��api��Ϣд�����ݿ�
        	ht.put("CREATE_TIME", "to_date('"+nowStr+"','yyyy-mm-dd hh24:mi:ss')");
        	ht.put("LAST_CHANGETIME", nowStr);
        	ht.put("FILE_ID", timestamp);
        	DBUtil.insert(ht,"APIPlatform_API");
        	String result = DBUtil.select("SELECT ID FROM APIPlatform_API WHERE FILE_ID = "+timestamp);//ͨ��timestamp��ȡAPI ID
        	int apiId = Util.getIdNum(result);
        	LogCreate.insertLog("Create new API successfully! API Id is "+apiId, creator, nowStr, "SUCCESS", "CREATE API");
        	logger.warn("the insert api result is"+result);
        	response.sendRedirect("pages/APIDetail.html?API_ID="+apiId+"&FILE_ID="+timestamp);//��ת���ո�����ҳ�棬���Ѹոյ�api_Id��file_id��timestamp����Ϊ����
        }
        catch (Exception e) {
        	try {
				response.sendRedirect("pages/APIApply.html?state=0");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//��ת���ո�����ҳ�棬���Ѹոյ�api_Id��file_id��timestamp����Ϊ����
            
        	LogCreate.insertLog(e, creator, nowStr, "WARNING", "CREATE API");
        	logger.warn("reigst new API faild",e);
        }
    }
}