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
 * ��Ӧ����
 * ��ÿһ��ʵ�����ڱ�ʾ���ͻ��˻�Ӧ��һ����׼��HTTP��Ӧ
 * @author ta
 *
 */
public class HttpResponse {
	/*
	 * ״̬�������Ϣ����
	 */
	//״̬����   Ĭ��:200
	private int statusCode = 200;
	
	/*
	 * ��Ӧͷ�����Ϣ����
	 */
	private Map<String,String> headers = new HashMap<String,String>();
	
	
	/*
	 * ��Ӧ���������Ϣ����
	 */
	//��Ӧʵ���ļ�
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
	 * ����ǰ��Ӧ����������Ӧ���ͻ���
	 */
	public void flush() {
		//1����״̬��
		sendStatusLine();
		//2������Ӧͷ
		sendHeaders();
		//3������Ӧ����
		sendContent();
	}
	/**
	 * ����״̬��
	 */
	private void sendStatusLine() {
		System.out.println("����״̬��...");
		try {
			String line = "HTTP/1.1"+" "+statusCode+" "+HttpContext.getStatusReason(statusCode);
			println(line);
			System.out.println("����״̬��:"+line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("����״̬�����");
	}
	/**
	 * ������Ӧͷ
	 */
	private void sendHeaders() {
		System.out.println("������Ӧͷ...");
		try {
			/*
			 * ����headers,����������Ӧͷ
			 */
			Set<Entry<String,String>> entrySet = headers.entrySet();
			for(Entry<String,String> header : entrySet) {
				String line = header.getKey()+": "+header.getValue();
				println(line);
				System.out.println("������Ӧͷ:"+line);
			}
			//��������CRLF��ʾ��Ӧͷ���ַ������
			println("");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("������Ӧͷ���");
	}
	/**
	 * ������Ӧ����
	 */
	private void sendContent() {
		System.out.println("������Ӧ����...");
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
		System.out.println("������Ӧ�������");
	}
	/**
	 * ��ͻ��˷���һ���ַ���
	 * �ڷ��͸����ַ�������Զ��ں��淢��CRLF
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
			 * ��Ӧ���������Ը���Ӧ���ĵ���Ӧͷ
			 * Content-Type:˵�����ļ�����
			 * Content-Length:˵�����ļ�����
			 */
			headers.put("Content-Length", entity.length()+"");
			//����Content-TypeҪ�����ļ���׺����ֵ��ʲô
			/*
			 * ��ȡ�ļ��ĺ�׺
			 * ˼·:
			 * ���ļ������һ��"."֮��ĵ�һ���ַ���ʼ��ȡ��
			 * ĩβ��
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






