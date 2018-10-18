package com.tedu.webserver.core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebServer主类
 * @author ta
 *
 */
public class WebServer {
	private ServerSocket server;
	
	private ExecutorService threadPool;
	
	public WebServer() {
		try {
			server = new ServerSocket(8088);
			threadPool = Executors.newFixedThreadPool(50);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		try {
			/*
			 * 循环接收多次客户端连接
			 */
			while(true) {
				//等待客户端连接
				Socket socket = server.accept();
				//启动一个线程处理该客户端请求
				ClientHandler handler = new ClientHandler(socket);
				//将ClientHanler交给线程池处理
				threadPool.execute(handler);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start();
	}
}












