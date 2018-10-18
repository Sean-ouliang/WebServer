package com.tedu.webserver.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import com.tedu.webserver.http.EmptyRequestException;
import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;
import com.tedu.webserver.servlet.HttpServlet;

/**
 * 客户端处理器，用来处理指定客户端的请求并予以响应
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
			System.out.println("开始处理客户端请求");
			/*
			 * 处理客户端请求的大致流程
			 * 1:解析请求
			 * 2:处理请求
			 * 3:响应客户端
			 */
			//1
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			
			//2
			//2.1请求路径
			String url = request.getRequestURI();
			//2.2判断该请求是否是请求一个业务
			String servletName = ServerContext.getServletName(url);
			if(servletName!=null){
				Class cls = Class.forName(servletName);
				System.out.println("正在加载:"+servletName);
				HttpServlet servlet = (HttpServlet)cls.newInstance();
				servlet.service(request, response);
			}else{
				//2.3对应从webapps目录中找到该资源
				File file = new File("webapps"+url);
				if(file.exists()) {
					response.setEntity(file);		
					System.out.println("该资源已找到!");
				}else {
					file = new File("webapps/root/404.html");
					response.setStatusCode(404);
					response.setEntity(file);	
					System.out.println("该资源未找到!");
				}
			}
			
			//3响应客户端
			response.flush();
			System.out.println("处理客户端请求完毕");
		} catch(EmptyRequestException e) {
			System.out.println("空请求!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//当响应客户端后与之断开连接
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	
	
	
	
	
	

}



