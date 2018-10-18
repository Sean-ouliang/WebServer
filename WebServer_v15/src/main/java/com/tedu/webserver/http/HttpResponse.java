package com.tedu.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 响应对象
 * 其每一个实例用于表示给客户端回应的一个标准的HTTP响应
 * @author ta
 *
 */
public class HttpResponse {
	/*
	 * 状态行相关信息定义
	 */
	//状态代码   默认:200
	private int statusCode = 200;
	
	/*
	 * 响应头相关信息定义
	 */
	private Map<String,String> headers = new HashMap<String,String>();
	
	
	/*
	 * 响应正文相关信息定义
	 */
	//响应实体文件
	private File entity;
	
	private Socket socket;
	private OutputStream out;
	
	public HttpResponse(Socket socket) {
		try {
			this.socket = socket;
			this.out = socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将当前响应对象内容响应给客户端
	 */
	public void flush() {
		//1发送状态行
		sendStatusLine();
		//2发送响应头
		sendHeaders();
		//3发送响应正文
		sendContent();
	}
	/**
	 * 发送状态行
	 */
	private void sendStatusLine() {
		System.out.println("发送状态行...");
		try {
			String line = "HTTP/1.1"+" "+statusCode+" "+HttpContext.getStatusReason(statusCode);
			println(line);
			System.out.println("发送状态行:"+line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("发送状态行完毕");
	}
	/**
	 * 发送响应头
	 */
	private void sendHeaders() {
		System.out.println("发送响应头...");
		try {
			/*
			 * 遍历headers,发送所有响应头
			 */
			Set<Entry<String,String>> entrySet = headers.entrySet();
			for(Entry<String,String> header : entrySet) {
				String line = header.getKey()+": "+header.getValue();
				println(line);
				System.out.println("发送响应头:"+line);
			}
			//单独发送CRLF表示响应头部分发送完毕
			println("");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("发送响应头完毕");
	}
	/**
	 * 发送响应正文
	 */
	private void sendContent() {
		System.out.println("发送响应正文...");
		if(entity!=null) {
			try(
				FileInputStream fis 
					= new FileInputStream(entity);	
			){				
				byte[] data = new byte[1024*10];
				int len = -1;
				while((len = fis.read(data))!=-1) {
					out.write(data, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("发送响应正文完毕");
	}
	/**
	 * 向客户端发送一行字符串
	 * 在发送给定字符串后会自动在后面发送CRLF
	 * @param line
	 */
	private void println(String line) {
		try {
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);//written CR
			out.write(10);//written LF
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public File getEntity() {
		return entity;
	}

	public void setEntity(File entity) {
		if(entity!=null) {
			/*
			 * 对应添加两个针对该响应正文的响应头
			 * Content-Type:说明该文件类型
			 * Content-Length:说明该文件长度
			 */
			headers.put("Content-Length", entity.length()+"");
			//设置Content-Type要根据文件后缀决定值是什么
			/*
			 * 获取文件的后缀
			 * 思路:
			 * 从文件名最后一个"."之后的第一个字符开始截取到
			 * 末尾。
			 */
			String name = entity.getName();
			int index = name.lastIndexOf(".")+1;
			String ext = name.substring(index);
			String type = HttpContext.getMimeType(ext);
			headers.put("Content-Type", type);
		}
		this.entity = entity;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	
	
	
	
}






