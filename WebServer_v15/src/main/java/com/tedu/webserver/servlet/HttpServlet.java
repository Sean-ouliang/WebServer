package com.tedu.webserver.servlet;

import java.io.File;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/**
 * Servlet的超类，定义所有共用功能以及约定派生类
 * 行为
 * @author ta
 *
 */
public abstract class HttpServlet {
	public abstract void service(HttpRequest request,HttpResponse response);
	
	/**
	 * 跳转到指定路径
	 * 该方法在tomcat中应用时是用request获取转发器中
	 * 定义的方法，可以实现在不同Servlet或jsp之间内部
	 * 跳转。
	 * @param url
	 * @param request 在这里用不上
	 * @param response
	 */
	public void forward(String url,HttpRequest request,HttpResponse response) {
		File file = new File("webapps"+url);
		response.setEntity(file);
	}

}












