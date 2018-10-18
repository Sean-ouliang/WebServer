package com.tedu.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 该类的每一个实例用于表示一个客户端发送过来的HTTP请求内容
 * 一个请求有三部分
 * 请求行，消息头，消息正文
 * @author ta
 *
 */
public class HttpRequest {
	/*
	 * 请求行相关信息定义
	 */
	//请求方式
	private String method;
	//请求的资源路径
	private String url;
	//请求使用的协议版本
	private String protocol;
	
	//url中的请求部分
	private String requestURI;
	//url中的参数部分
	private String queryString;
	//保存具体的每一个参数
	private Map<String,String> parameters = new HashMap<String,String>();
	
	
		
	/*
	 * 消息头相关信息定义
	 */
	private Map<String,String> headers = new HashMap<String,String>();
	
	
	/*
	 * 消息正文相关信息定义
	 */
	
	
	private Socket socket;
	private InputStream in;
	/**
	 * 实例化请求对象，通过给定的Socket获取输入流并读取
	 * 客户端发送过来的请求内容，用于初始化该对象
	 * @param socket
	 * @throws EmptyRequestException 
	 */
	public HttpRequest(Socket socket) throws EmptyRequestException {
		try {
			this.socket = socket;
			in = socket.getInputStream();
			/*
			 * 1解析请求行
			 * 2解析消息头
			 * 3解析消息正文
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
			//空请求异常接续抛出给ClientHandler
			throw e;
		} catch(Exception e) {
			e.printStackTrace();
		}	
	}
	/**
	 * 解析请求行
	 * @throws EmptyRequestException 
	 */
	private void parseRequestLine() throws EmptyRequestException {
		System.out.println("开始解析请求行");
		/*
		 * 流程
		 * 1:通过输入流读取一行字符串(一个请求中的第一行
		 *   内容就是请求行内容)
		 *   GET /index.html HTTP/1.1
		 * 2:按照空格将请求行拆分为三部分
		 * 3:将拆分后的内容分别设置到对应的属性:
		 *   method,url,protocol上
		 *   完成了解析请求行操作  
		 */
		String line = readLine();
		System.out.println("请求行内容:"+line);
		
		/*
		 * 这里有可能出现数组下标越界的情况
		 * HTTP协议允许客户端发送空请求(连接后实际没有发送
		 * 一个标准的HTTP请求)，这时若直接解析请求行读取到
		 * 的是一个空字符串，那么拆分后得不到三项内容。
		 * 针对空请求我们就不做任何处理了。
		 */
		String[] data = line.split("\\s");
		if(data.length<3) {
			//空请求
			throw new EmptyRequestException();
		}
		method = data[0];
		url = data[1];
		//进一步解析url
		parseUrl();
		protocol = data[2];
		
		
		System.out.println("method:"+method);
		System.out.println("url:"+url);
		System.out.println("protocol:"+protocol);		
		System.out.println("解析请求行完毕");
	}
	/**
	 * 进一步解析url部分
	 */
	private void parseUrl() {
		/*
		 * url可能存在两种情况:
		 * 1:不带参数
		 * 2:带参数
		 * 
		 * 如果不带参数，则直接将url赋值给requestURI
		 * 即可，而queryString和parameters则无需操作。
		 * 如果带参数，则需要进一步解析url，该url是否
		 * 含有参数部分可以根据该url中是否含有"?"判别。
		 * 
		 * 如果含有参数，则先根据"?"将url拆分为两部分
		 * 第一部分是请求部分，赋值给requestURI,
		 * 第二部分为参数部分，赋值给queryString。
		 * 
		 * 并且还要对参数部分进行进一步解析:
		 * 将参数部分按照"&"进行拆分，可以得到每一个
		 * 参数，再将每个参数按照"="拆分为两部分，其中
		 * 第一部分为参数名，第二部分为参数值，并分别
		 * 作为key,value保存到parameters这个map保存
		 */
		if(url.indexOf("?")!=-1) {
			//先按照?拆分
			String[] data = url.split("\\?");
			requestURI = data[0];
			if(data.length>1) {
				//解析参数部分
				parseParameters(data[1]);
				
			}
		}else {
			//不含有
			requestURI = url;
		}
		System.out.println("requestURI:"+requestURI);
		System.out.println("queryString:"+queryString);
		System.out.println("parameters:"+parameters);
	}
	/**
	 * 解析参数
	 * 参数的格式应当为:name=value&name=value&....
	 * @param line
	 */
	private void parseParameters(String line) {
		//将参数部分中"%XX"的内容还原为对应字符
		try {
			System.out.println("解码前queryString:"+queryString);
			queryString = URLDecoder.decode(
					line, "UTF-8");
			System.out.println("解码后queryString:"+queryString);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		//进一步拆分参数部分
		String[] paras = queryString.split("&");
		for(String para : paras) {
			//按照"="拆分
			String[] arr = para.split("=");
			if(arr.length>1) {
				parameters.put(arr[0], arr[1]);
			}else {
				parameters.put(arr[0], null);
			}
		}
	}
	
	
	/**
	 * 解析消息头
	 */
	private void parseHeaders() {
		System.out.println("开始解析消息头");
		/*
		 * 循环调用readLine方法读取若干行，由于parseRequestLine
		 * 方法已经从输入流中读取了请求中第一行内容(请求行)，
		 * 那么这里再使用readLine方法读取的就应当是消息头
		 * 部分了。
		 * 将每个消息头读取后，按照": "拆分为两部分，第一
		 * 部分应当是消息头的名字，第二部分为消息头对应的值
		 * 将它们put到headers这个map中即可完成解析消息头工作
		 * 当调用readLine方法返回的是一个空字符串时，表示单独
		 * 读取到了CRLF，那么直接break循环，停止解析消息头部
		 * 分即可。
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
		System.out.println("解析消息头完毕");
	}
	/**
	 * 解析消息正文
	 */
	private void parseContent() {
		System.out.println("开始解析消息正文");
		/*
		 * 判别当前请求是否含有消息正文可以分析消息头
		 * 中是否含有Content-Length,Content-Type
		 */
		if(headers.containsKey("Content-Length")) {
			//存在消息正文,根据头获取正文长度(字节量)
			int len = Integer.parseInt(
				headers.get("Content-Length")
			);
			try {
				byte[] data = new byte[len];
				in.read(data);//读取指定的字节量
				/*
				 * 具体读取到的消息正文字节表示的是什么
				 * 要通过分析消息头Content-Type决定
				 */
				String type = headers.get("Content-Type");
				//判断是否为form表单提交的用户数据
				if("application/x-www-form-urlencoded".equals(type)) {
					String line = new String(data,"ISO8859-1");
					parseParameters(line);
				}
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
		
		
		
		System.out.println("解析消息正文完毕");
	}
	
	/**
	 * 通过输入流读取一行字符串，以CRLF结尾为一行字符串。
	 * 返回的字符串中不含有最后的CRLF。
	 * @return
	 */
	private String readLine() {
		StringBuilder builder = new StringBuilder();
		try {
			int d = -1;
			/*
			 * c1表示上次读取到的字符
			 * c2表示本次读取到的字符
			 */
			char c1='1',c2='1';
			while((d = in.read())!=-1) {
				c2 = (char)d;
				//判断上次若读取到CR，本次读取到LF就停止
				if(c1==13&&c2==10) {
					break;
				}
				builder.append(c2);
				//在下次循环前，将本次读的字符赋值给c1
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
	 * 根据给定的参数名获取对应的参数值
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	
	
}











