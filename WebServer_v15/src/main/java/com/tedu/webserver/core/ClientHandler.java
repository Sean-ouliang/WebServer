package com.tedu.webserver.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import com.tedu.webserver.http.EmptyRequestException;
import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;
import com.tedu.webserver.servlet.HttpServlet;

/**
 * �ͻ��˴���������������ָ���ͻ��˵�����������Ӧ
 * @author ta
 *
 */
public class ClientHandler implements Runnable{
	private Socket socket;
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			System.out.println("��ʼ����ͻ�������");
			/*
			 * ����ͻ�������Ĵ�������
			 * 1:��������
			 * 2:��������
			 * 3:��Ӧ�ͻ���
			 */
			//1
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			
			//2
			//2.1����·��
			String url = request.getRequestURI();
			//2.2�жϸ������Ƿ�������һ��ҵ��
			String servletName = ServerContext.getServletName(url);
			if(servletName!=null){
				Class cls = Class.forName(servletName);
				System.out.println("���ڼ���:"+servletName);
				HttpServlet servlet = (HttpServlet)cls.newInstance();
				servlet.service(request, response);
			}else{
				//2.3��Ӧ��webappsĿ¼���ҵ�����Դ
				File file = new File("webapps"+url);
				if(file.exists()) {
					response.setEntity(file);		
					System.out.println("����Դ���ҵ�!");
				}else {
					file = new File("webapps/root/404.html");
					response.setStatusCode(404);
					response.setEntity(file);	
					System.out.println("����Դδ�ҵ�!");
				}
			}
			
			//3��Ӧ�ͻ���
			response.flush();
			System.out.println("����ͻ����������");
		} catch(EmptyRequestException e) {
			System.out.println("������!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//����Ӧ�ͻ��˺���֮�Ͽ�����
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	
	
	
	
	

}



