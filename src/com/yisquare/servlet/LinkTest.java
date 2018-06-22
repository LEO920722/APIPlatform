package com.yisquare.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yisquare.db.DBConf;
import com.yisquare.tools.PropertiesUtil;

/**
 * Servlet implementation class LinkTest
 * ���ã�
 * ������������ѯdb������Ϣ��0��ʾ����ʧ�ܣ�1��ʾ���ӳɹ�
 * �������� ��ʾ�״�����������������Ϣ
 */
public class LinkTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(LinkTest.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LinkTest() {
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
	 * @throws IOException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub 
		PrintWriter writer= response.getWriter();
		String ipStr=request.getParameter("IP");
		//System.out.println("���������ipStr�ǣ�"+ipStr);
		if (ipStr!=""&&ipStr!=null&&ipStr.length()!=0) {//���û���������� �����ʸ�ģ��
			//���Ȳ��������Ƿ�����
			try {
				String [] array = ipStr.split("-");
				new DBConf().getConnection(array[0], array[1], array[2], array[3], array[4],array[5]);
				PropertiesUtil.setDBInfo(ipStr);//ע������ip��������Ϣƴ��������
				writer.print("2");//���ӳɹ�
			} catch (Exception e) {//�������ݿ�ʧ�ܣ�ǰ�˱�ʾΪ
				// TODO: handle exception
				e.printStackTrace();
				logger.warn("connect to the db faild");
				writer.print("1");//�����򶶶�
			}
		}
		else{//��һ�ν��� û�������κβ���
			String string = null;
			try {
				string = PropertiesUtil.getDBInfo();
				System.out.println("_"+string+"_");
				if(string == null||string.length()==0){//û�в鵽������Ϣ
					writer.print("0");//������
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.warn("write the dbinfo faild");
			}
		}
		
	}

}
