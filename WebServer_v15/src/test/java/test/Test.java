package test;

import java.io.UnsupportedEncodingException;

public class Test {
	public static void main(String[] args) throws UnsupportedEncodingException {
		String str = "·¶";
		byte[] data = str.getBytes("UTF-8");
		/*
		 * -24   -116   -125
		 * 
		 * 11101000   -24    E8       
		 * 10001100  -116    8C 
		 * 10000011  -125    83   
		 */
		for(byte b : data) {
			System.out.println(b);
		}
			
	}
}
