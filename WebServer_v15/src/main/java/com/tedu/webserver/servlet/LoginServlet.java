package com.tedu.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

public class LoginServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response) {
		//1��ȡ��¼��Ϣ
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		//2
		try (
			RandomAccessFile raf
				= new RandomAccessFile("user.dat","r");
		){
			//�Ƿ�ƥ�䵽�û�
			boolean have = false;
			for(int i=0;i<raf.length()/100;i++) {
				//�ƶ�ָ�뵽��ǰ��¼�Ŀ�ʼλ��
				raf.seek(i*100);
				
				//��ȡ�û���
				byte[] data = new byte[32];
				raf.read(data);//һ�ζ�32�ֽ�
				String user = new String(data,"UTF-8").trim();				
				//�ȶ��Ƿ�Ϊ���û�
				if(user.equals(username)) {
					//�ҵ����û�,������
					raf.read(data);
					String pwd = new String(data,"UTF-8").trim();
					if(pwd.equals(password)) {
						//����һ�£���¼�ɹ�
						forward("/myweb/login_success.html", request, response);
						
					}else {
						//���벻�ԣ���¼
						forward("/myweb/login_fail.html", request, response);
					}
					have = true;
					//ֹͣѭ�������������¶�ȡ����
					break;
				}
			}
			//
			if(!have) {
				//�û���������Ч
				forward("/myweb/login_fail.html", request, response);
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}






