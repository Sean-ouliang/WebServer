package com.tedu.webserver.core;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * ����˻����࣬���ڱ������������Ϣ
 * @author tarena
 *
 */
public class ServerContext {
	/**
	 * Servletӳ����Ϣ
	 * key������·��
	 * value������������Servlet������
	 */
	private static Map<String,String> SERVLET_MAPPING = new HashMap<String,String>();
	
	static{
		initServletMapping();
	}
	/**
	 * ��ʼ��Servletӳ���ϵ
	 */
	private static void initServletMapping(){
		try{
			/*
			 * ����config/servlets.xml
			 * ������ǩservlets�����е�servlet��ǩ����������
			 * ��ÿ��servlet��ǩ�е�����url��ֵ��Ϊkey����
			 * className���Ե�ֵ��Ϊvalue���뵽SERVLET_MAPPING��
			 */
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new FileInputStream("config/servlets.xml"));
			Element root = doc.getRootElement();
			List<Element> lists = root.elements("servlet");
			for(Element list:lists){
				String key = list.attributeValue("url");
				String value = list.attributeValue("className");
				SERVLET_MAPPING.put(key, value);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * ���������ȡ��Ӧ��Servlet������
	 * @param url
	 * @return
	 */
	public static String getServletName(String url){
		return SERVLET_MAPPING.get(url);
	}
	
//	public static void main(String[] args) {
//		System.out.println(SERVLET_MAPPING.get("/myweb/reg"));
//	}
}
