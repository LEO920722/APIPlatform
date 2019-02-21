package com.yisquare.servlet;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yisquare.tools.DBUtil;
import com.yisquare.tools.LogCreate;

public class QueryServiceDetails extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger
			.getLogger(QueryServiceDetails.class);
	
	public QueryServiceDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		try {
			Hashtable<String, String> ht = new Hashtable<String, String>();
			String id = request.getParameter("ID");
			String businessId = request.getParameter("BUSINESS_ID");
			String ruleId = request.getParameter("RULE_ID");
			String serviceName = request.getParameter("SERVICENAME");
			String batchId = request.getParameter("BATCH_ID");
			String serviceStatus = request.getParameter("SERVICE_STATUS");
			String createTime1 = request.getParameter("CREATE_TIME1");
			String createTime2 = request.getParameter("CREATE_TIME2");
			
			if (id != "" && id != null && id.length() != 0) {
				ht.put("ID", id);
			}
			if (businessId != "" && businessId != null
					&& businessId.length() != 0) {
				ht.put("BUSINESS_ID", businessId);
			}
			if (ruleId != "" && ruleId != null && ruleId.length() != 0) {
				ht.put("RULE_ID", ruleId);
			}
			if (serviceName != "" && serviceName != null
					&& serviceName.length() != 0) {
				ht.put("SERVICENAME", serviceName);
			}
			if (serviceStatus != "" && serviceStatus != null
					&& serviceStatus.length() != 0) {
				if (serviceStatus.equals("Error")) {
					ht.put("SERVICE_STATUS", "E");
				} else if (serviceStatus.equals("Success")) {
					ht.put("SERVICE_STATUS", "S");
				}
			}
			if (batchId != "" && batchId != null && batchId.length() != 0) {
				ht.put("BATCH_ID", batchId);
				String sql = DBUtil.getQuerySql(ht, "EMAIL_BATCH", createTime1,
						createTime2);// Select from EMAIL_BATCH to get Service ID
				String[] list = com.yisquare.tools.Util.getServiceIDList(DBUtil
						.select(sql));// Get servcie and split into list
				ht.clear();
				String rs = "";// Initial result String
				for (int i = 0; i < list.length; i++) {
					ht.put("ID", list[i]);// Put Service ID into Hashtable one by one.
					String tmp = DBUtil.select(DBUtil.getQuerySql(ht,
							"SERVICE_MONITOR", createTime1, createTime2));// Get result String one by one
					if (i == 0) {
						tmp = tmp.replace("]", " ");
						tmp = tmp.concat(",");// element,concat with ','
					} else if (i == list.length - 1) {
						tmp = tmp.replace("[", " ");
					} else {
						tmp = tmp.replace("[", " ");
						tmp = tmp.replace("]", " ");
						tmp = tmp.concat(",");// element,concat with ','
					}
					rs += tmp;
				}
				response.getWriter().print("{\"rows\":" + rs + "}");
			} else {
				response.getWriter().print(
						"{\"rows\":"
								+ DBUtil.select(DBUtil.getQuerySql(ht,
										"SERVICE_MONITOR", createTime1,
										createTime2)) + "}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception:" + LogCreate.getException(e));
		}
	}

}
