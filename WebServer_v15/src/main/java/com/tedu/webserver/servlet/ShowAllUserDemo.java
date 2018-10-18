package com.tedu.webserver.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 显示所有注册用户信息
 * @author ta
 *
 */
public class ShowAllUserDemo {
	public static void main(String[] args) throws IOException {
		/*
		 * 输出格式:
		 * name,password,nickname,age
		 */
		RandomAccessFile raf
			= new RandomAccessFile("user.dat","r");
		
		for(int i=0;i<raf.length()/100;i++) {
			//读用户名
			byte[] data = new byte[32];
			raf.read(data);//一次读32字节
			String name = new String(data,"UTF-8").trim();
			
			//读取密码
			raf.read(data);
			String password = new String(data,"UTF-8").trim();
			
			//读取昵称
			raf.read(data);
			String nickname = new String(data,"UTF-8").trim();
			
			//读取年龄
			int age = raf.readInt();
			System.out.println(name+","+password+","+nickname+","+age);
		}
	
		raf.close();	
		
	}
}







