package com.tedu.webserver.core;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 服务端环境类，用于保存服务端相关信息
 * @author tarena
 *
 */
public class ServerContext {
	/**
	 * Servlet映射信息
	 * key：请求路径
	 * value：处理该请求的Servlet的名字
	 */
	private static Map<String,String> SERVLET_MAPPING = new HashMap<String,String>();
	
	static{
		initServletMapping();
	}
	/**
	 * 初始化Servlet映射关系
	 */
	private static void initServletMapping(){
		try{
			/*
			 * 解析config/servlets.xml
			 * 将根标签servlets下所有的servlet标签解析出来，
			 * 将每个servlet标签中的属性url的值作为key，将
			 * className属性的值作为value存入到SERVLET_MAPPING中
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
	 * 根据请求获取对应的Servlet的名字
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
