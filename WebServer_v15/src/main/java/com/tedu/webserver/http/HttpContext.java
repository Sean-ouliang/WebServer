package com.tedu.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * HTTPЭ��������ݶ���
 * @author ta
 *
 */
public class HttpContext {
	/*
	 * ״̬�������Ӧ������ӳ���ϵ
	 */
	private static Map<Integer,String> STATUS_CODE_REASON_MAPPING = new HashMap<Integer,String>();
	/*
	 * ��������ӳ��
	 * key:��Դ��׺��
	 * value:Content-Type��Ӧ��ֵ
	 */
	private static Map<String,String> MIME_MAPPING = new HashMap<String,String>();
	
	static {
		//��ʼ��״̬������������ӳ��
		initStatusMapping();
		//��ʼ����������ӳ��
		initMimeMapping();
		//��ʼ����������
	}
	/**
	 * ��ʼ����������ӳ��
	 */
	private static void initMimeMapping() {
		/*
		 * 1:����Ŀ�е���dom4j��jar��
		 * 2:����ͨ��dom������ĿĿ¼��config/web.xml
		 * 3:��web.xml�и���ǩ��������Ϊ:mime-mapping
		 *   ���ӱ�ǩ��ȡ����
		 *   ע��:����ǩ�²���ֻ��mime-mapping���ֵ�
		 *   �ӱ�ǩ��
		 * 4:�������е�mime-mapping��ǩ�����ñ�ǩ��
		 *   �ӱ�ǩ:<extension>�м���ı���Ϊkey
		 *   �ӱ�ǩ:<mime-type>�м���ı���Ϊvalue
		 *   ���浽 MIME_MAPPING���map����ɳ�ʼ��
		 *    
		 * ��ʼ����Ϻ����׮�������Map��size��Ӧ��
		 * ��1000�����
		 * 
		 */
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("config/web.xml"));
			Element root = doc.getRootElement();
			//��ȡ����<mime-mapping>
			List<Element> mimeList = root.elements("mime-mapping");
			for(Element mime : mimeList) {
				String key = mime.elementText("extension");
				String value = mime.elementText("mime-type");
				MIME_MAPPING.put(key, value);
			}
			System.out.println(MIME_MAPPING.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ʼ��״̬������������ӳ��
	 */
	private static void initStatusMapping() {
		STATUS_CODE_REASON_MAPPING.put(200, "OK");
		STATUS_CODE_REASON_MAPPING.put(201, "Created");
		STATUS_CODE_REASON_MAPPING.put(202, "Accepted");
		STATUS_CODE_REASON_MAPPING.put(204, "No Content");
		STATUS_CODE_REASON_MAPPING.put(301, "Moved Permanently");
		STATUS_CODE_REASON_MAPPING.put(302, "Moved Temporarily");
		STATUS_CODE_REASON_MAPPING.put(304, "Not Modified");
		STATUS_CODE_REASON_MAPPING.put(400, "Bad Request");
		STATUS_CODE_REASON_MAPPING.put(401, "Unauthorized");
		STATUS_CODE_REASON_MAPPING.put(403, "Forbidden");
		STATUS_CODE_REASON_MAPPING.put(404, "Not Found");
		STATUS_CODE_REASON_MAPPING.put(500, "Internal Server Error");
		STATUS_CODE_REASON_MAPPING.put(501, "Not Implemented");
		STATUS_CODE_REASON_MAPPING.put(502, "Bad Gateway");
		STATUS_CODE_REASON_MAPPING.put(503, "Service Unavailable");
	}
	/**
	 * ���ݸ�����״̬���뷵�ض�Ӧ��״̬����
	 * @param statusCode
	 * @return
	 */
	public static String getStatusReason(int statusCode) {
		return STATUS_CODE_REASON_MAPPING.get(statusCode);
	}
	/**
	 * ���ݸ����ĺ�׺����ȡ��Ӧ�Ľ�������
	 * @param ext
	 * @return
	 */
	public static String getMimeType(String ext) {
		return MIME_MAPPING.get(ext);
	}
	
	
	public static void main(String[] args) {
		String type = getMimeType("png");
		System.out.println(type);
	}
	
	
}









