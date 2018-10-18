package com.tedu.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * �����ÿһ��ʵ�����ڱ�ʾһ���ͻ��˷��͹�����HTTP��������
 * һ��������������
 * �����У���Ϣͷ����Ϣ����
 * @author ta
 *
 */
public class HttpRequest {
	/*
	 * �����������Ϣ����
	 */
	//����ʽ
	private String method;
	//�������Դ·��
	private String url;
	//����ʹ�õ�Э��汾
	private String protocol;
	
	//url�е����󲿷�
	private String requestURI;
	//url�еĲ�������
	private String queryString;
	//��������ÿһ������
	private Map<String,String> parameters = new HashMap<String,String>();
	
	
		
	/*
	 * ��Ϣͷ�����Ϣ����
	 */
	private Map<String,String> headers = new HashMap<String,String>();
	
	
	/*
	 * ��Ϣ���������Ϣ����
	 */
	
	
	private Socket socket;
	private InputStream in;
	/**
	 * ʵ�����������ͨ��������Socket��ȡ����������ȡ
	 * �ͻ��˷��͹������������ݣ����ڳ�ʼ���ö���
	 * @param socket
	 * @throws EmptyRequestException 
	 */
	public HttpRequest(Socket socket) throws EmptyRequestException {
		try {
			this.socket = socket;
			in = socket.getInputStream();
			/*
			 * 1����������
			 * 2������Ϣͷ
			 * 3������Ϣ����
			 */
			//1
			parseRequestLine();
			//2
			parseHeaders();
			//3
			parseContent();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(EmptyRequestException e) {
			//�������쳣�����׳���ClientHandler
			throw e;
		} catch(Exception e) {
			e.printStackTrace();
		}	
	}
	/**
	 * ����������
	 * @throws EmptyRequestException 
	 */
	private void parseRequestLine() throws EmptyRequestException {
		System.out.println("��ʼ����������");
		/*
		 * ����
		 * 1:ͨ����������ȡһ���ַ���(һ�������еĵ�һ��
		 *   ���ݾ�������������)
		 *   GET /index.html HTTP/1.1
		 * 2:���տո������в��Ϊ������
		 * 3:����ֺ�����ݷֱ����õ���Ӧ������:
		 *   method,url,protocol��
		 *   ����˽��������в���  
		 */
		String line = readLine();
		System.out.println("����������:"+line);
		
		/*
		 * �����п��ܳ��������±�Խ������
		 * HTTPЭ������ͻ��˷��Ϳ�����(���Ӻ�ʵ��û�з���
		 * һ����׼��HTTP����)����ʱ��ֱ�ӽ��������ж�ȡ��
		 * ����һ�����ַ�������ô��ֺ�ò����������ݡ�
		 * ��Կ��������ǾͲ����κδ����ˡ�
		 */
		String[] data = line.split("\\s");
		if(data.length<3) {
			//������
			throw new EmptyRequestException();
		}
		method = data[0];
		url = data[1];
		//��һ������url
		parseUrl();
		protocol = data[2];
		
		
		System.out.println("method:"+method);
		System.out.println("url:"+url);
		System.out.println("protocol:"+protocol);		
		System.out.println("�������������");
	}
	/**
	 * ��һ������url����
	 */
	private void parseUrl() {
		/*
		 * url���ܴ����������:
		 * 1:��������
		 * 2:������
		 * 
		 * ���������������ֱ�ӽ�url��ֵ��requestURI
		 * ���ɣ���queryString��parameters�����������
		 * ���������������Ҫ��һ������url����url�Ƿ�
		 * ���в������ֿ��Ը��ݸ�url���Ƿ���"?"�б�
		 * 
		 * ������в��������ȸ���"?"��url���Ϊ������
		 * ��һ���������󲿷֣���ֵ��requestURI,
		 * �ڶ�����Ϊ�������֣���ֵ��queryString��
		 * 
		 * ���һ�Ҫ�Բ������ֽ��н�һ������:
		 * ���������ְ���"&"���в�֣����Եõ�ÿһ��
		 * �������ٽ�ÿ����������"="���Ϊ�����֣�����
		 * ��һ����Ϊ���������ڶ�����Ϊ����ֵ�����ֱ�
		 * ��Ϊkey,value���浽parameters���map����
		 */
		if(url.indexOf("?")!=-1) {
			//�Ȱ���?���
			String[] data = url.split("\\?");
			requestURI = data[0];
			if(data.length>1) {
				//������������
				parseParameters(data[1]);
				
			}
		}else {
			//������
			requestURI = url;
		}
		System.out.println("requestURI:"+requestURI);
		System.out.println("queryString:"+queryString);
		System.out.println("parameters:"+parameters);
	}
	/**
	 * ��������
	 * �����ĸ�ʽӦ��Ϊ:name=value&name=value&....
	 * @param line
	 */
	private void parseParameters(String line) {
		//������������"%XX"�����ݻ�ԭΪ��Ӧ�ַ�
		try {
			System.out.println("����ǰqueryString:"+queryString);
			queryString = URLDecoder.decode(
					line, "UTF-8");
			System.out.println("�����queryString:"+queryString);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		//��һ����ֲ�������
		String[] paras = queryString.split("&");
		for(String para : paras) {
			//����"="���
			String[] arr = para.split("=");
			if(arr.length>1) {
				parameters.put(arr[0], arr[1]);
			}else {
				parameters.put(arr[0], null);
			}
		}
	}
	
	
	/**
	 * ������Ϣͷ
	 */
	private void parseHeaders() {
		System.out.println("��ʼ������Ϣͷ");
		/*
		 * ѭ������readLine������ȡ�����У�����parseRequestLine
		 * �����Ѿ����������ж�ȡ�������е�һ������(������)��
		 * ��ô������ʹ��readLine������ȡ�ľ�Ӧ������Ϣͷ
		 * �����ˡ�
		 * ��ÿ����Ϣͷ��ȡ�󣬰���": "���Ϊ�����֣���һ
		 * ����Ӧ������Ϣͷ�����֣��ڶ�����Ϊ��Ϣͷ��Ӧ��ֵ
		 * ������put��headers���map�м�����ɽ�����Ϣͷ����
		 * ������readLine�������ص���һ�����ַ���ʱ����ʾ����
		 * ��ȡ����CRLF����ôֱ��breakѭ����ֹͣ������Ϣͷ��
		 * �ּ��ɡ�
		 */
		String line = null;
		while(true) {
			line = readLine();
			System.out.println("line:"+line);
			if("".equals(line)) {
				break;
			}
			String[] data = line.split(": ");
			headers.put(data[0], data[1]);
			
		}		
		System.out.println("headers:"+headers);
		System.out.println("������Ϣͷ���");
	}
	/**
	 * ������Ϣ����
	 */
	private void parseContent() {
		System.out.println("��ʼ������Ϣ����");
		/*
		 * �б�ǰ�����Ƿ�����Ϣ���Ŀ��Է�����Ϣͷ
		 * ���Ƿ���Content-Length,Content-Type
		 */
		if(headers.containsKey("Content-Length")) {
			//������Ϣ����,����ͷ��ȡ���ĳ���(�ֽ���)
			int len = Integer.parseInt(
				headers.get("Content-Length")
			);
			try {
				byte[] data = new byte[len];
				in.read(data);//��ȡָ�����ֽ���
				/*
				 * �����ȡ������Ϣ�����ֽڱ�ʾ����ʲô
				 * Ҫͨ��������ϢͷContent-Type����
				 */
				String type = headers.get("Content-Type");
				//�ж��Ƿ�Ϊform���ύ���û�����
				if("application/x-www-form-urlencoded".equals(type)) {
					String line = new String(data,"ISO8859-1");
					parseParameters(line);
				}
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
		
		
		
		System.out.println("������Ϣ�������");
	}
	
	/**
	 * ͨ����������ȡһ���ַ�������CRLF��βΪһ���ַ�����
	 * ���ص��ַ����в���������CRLF��
	 * @return
	 */
	private String readLine() {
		StringBuilder builder = new StringBuilder();
		try {
			int d = -1;
			/*
			 * c1��ʾ�ϴζ�ȡ�����ַ�
			 * c2��ʾ���ζ�ȡ�����ַ�
			 */
			char c1='1',c2='1';
			while((d = in.read())!=-1) {
				c2 = (char)d;
				//�ж��ϴ�����ȡ��CR�����ζ�ȡ��LF��ֹͣ
				if(c1==13&&c2==10) {
					break;
				}
				builder.append(c2);
				//���´�ѭ��ǰ�������ζ����ַ���ֵ��c1
				c1 = c2;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.toString().trim();
	}
	public String getMethod() {
		return method;
	}
	public String getUrl() {
		return url;
	}
	public String getProtocol() {
		return protocol;
	}
	public String getHeader(String name) {
		return headers.get(name);
	}
	public String getRequestURI() {
		return requestURI;
	}
	public String getQueryString() {
		return queryString;
	}
	/**
	 * ���ݸ����Ĳ�������ȡ��Ӧ�Ĳ���ֵ
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	
	
}











