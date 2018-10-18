package com.tedu.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/**
 * ע��ҵ��
 * @author ta
 *
 */
public class RegServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response) {
		/*
		 * ע������
		 * 1:ͨ��request��ȡ�û�ע��ҳ���б��ύ��
		 *   ע����Ϣ
		 * 2:����Щ��Ϣд�뵽�ļ�user.dat�б���
		 * 3:������ɹ�����Ӧ���ͻ���ע��ɹ���ҳ��
		 * 4:����ע������г������쳣����Ӧ����ҳ��  
		 */
		System.out.println("RegServlet:��ʼע��");
		//1
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		int age = Integer.parseInt(
			request.getParameter("age")
		);
	
		/*
		 * 2
		 * ʹ��RandomAccessFile��user.dat�ļ�����д
		 * ������
		 * ÿ���û���Ϣ����4��:
		 * �û��������룬�ǳƣ�����
		 * ��������Ϊintֵ֮�⣬ʣ�µ�ȫ��ΪString��
		 * �滮ÿ����¼��user.dat�ļ���ռ��100�ֽ�
		 * ����:�û��������룬�ǳƸ�ռ��32�ֽڣ�������
		 * intֵ�̶�Ϊ4�ֽڡ�
		 * 
		 */
		try (
			RandomAccessFile raf
				= new RandomAccessFile("user.dat","rw");
		){
			//��ָ���ƶ����ļ�ĩβ���Ա�׷��д����
			raf.seek(raf.length());
					
			//д�û���
			byte[] data = username.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);//д32�ֽ�	
			//д����
			data = password.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);	
			//д�ǳ�
			data = nickname.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);	
			//д����
			raf.writeInt(age);
			
			//3 ע��ɹ�����תע��ɹ���ҳ��
			forward("/myweb/reg_success.html", request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		System.out.println("RegServlet:ע�����");
	}
}










