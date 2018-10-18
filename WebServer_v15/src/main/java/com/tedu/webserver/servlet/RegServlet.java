package com.tedu.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/**
 * 注册业务
 * @author ta
 *
 */
public class RegServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response) {
		/*
		 * 注册流程
		 * 1:通过request获取用户注册页面中表单提交的
		 *   注册信息
		 * 2:将这些信息写入到文件user.dat中保存
		 * 3:若保存成功则响应给客户端注册成功的页面
		 * 4:若在注册过程中出现了异常，响应错误页面  
		 */
		System.out.println("RegServlet:开始注册");
		//1
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		int age = Integer.parseInt(
			request.getParameter("age")
		);
	
		/*
		 * 2
		 * 使用RandomAccessFile对user.dat文件进行写
		 * 操作。
		 * 每个用户信息包含4项:
		 * 用户名，密码，昵称，年龄
		 * 除了年龄为int值之外，剩下的全部为String。
		 * 规划每条记录在user.dat文件中占用100字节
		 * 其中:用户名，密码，昵称各占用32字节，年龄是
		 * int值固定为4字节。
		 * 
		 */
		try (
			RandomAccessFile raf
				= new RandomAccessFile("user.dat","rw");
		){
			//将指针移动到文件末尾，以便追加写操作
			raf.seek(raf.length());
					
			//写用户名
			byte[] data = username.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);//写32字节	
			//写密码
			data = password.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);	
			//写昵称
			data = nickname.getBytes("UTF-8");
			data = Arrays.copyOf(data, 32);
			raf.write(data);	
			//写年龄
			raf.writeInt(age);
			
			//3 注册成功，跳转注册成功的页面
			forward("/myweb/reg_success.html", request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		System.out.println("RegServlet:注册完毕");
	}
}










