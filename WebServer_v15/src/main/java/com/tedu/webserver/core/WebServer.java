package com.tedu.webserver.core;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebServer����
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
			 * ѭ�����ն�οͻ�������
			 */
			while(true) {
				//�ȴ��ͻ�������
				Socket socket = server.accept();
				//����һ���̴߳���ÿͻ�������
				ClientHandler handler = new ClientHandler(socket);
				//��ClientHanler�����̳߳ش���
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












