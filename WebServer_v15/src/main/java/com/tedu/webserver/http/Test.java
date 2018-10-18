package com.tedu.webserver.http;

public class Test {
	public static int a = 1;
	static{
		System.out.println("222");
		System.out.println(a);
	}
	public static void move(){
		System.out.println("111");
	}
	public static void moves(){
		System.out.println("333");
	}
}
class foo {
	public static void main(String[] args) {
		Test.move();
	}
}
