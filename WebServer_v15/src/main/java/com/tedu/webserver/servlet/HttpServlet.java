package com.tedu.webserver.servlet;

import java.io.File;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/**
 * Servlet�ĳ��࣬�������й��ù����Լ�Լ��������
 * ��Ϊ
 * @author ta
 *
 */
public abstract class HttpServlet {
	public abstract void service(HttpRequest request,HttpResponse response);
	
	/**
	 * ��ת��ָ��·��
	 * �÷�����tomcat��Ӧ��ʱ����request��ȡת������
	 * ����ķ���������ʵ���ڲ�ͬServlet��jsp֮���ڲ�
	 * ��ת��
	 * @param url
	 * @param request �������ò���
	 * @param response
	 */
	public void forward(String url,HttpRequest request,HttpResponse response) {
		File file = new File("webapps"+url);
		response.setEntity(file);
	}

}












