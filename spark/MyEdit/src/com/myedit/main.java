package com.myedit;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s1 = new String();
		String s2 = "billryan";
		int s2Len = s2.length();
		System.out.println( s2.substring(4, 8)); // return "ryan"
		StringBuilder s3 = new StringBuilder(s2.substring(4, 8));
		System.out.println(s3);
		System.out.println(s3.append("bill"));
		String s2New = s3.toString(); // return "ryanbill"
		// convert String to char array
		char[] s2Char = s2.toCharArray();
		// char at index 4
		char ch = s2.charAt(4); // return 'r'
		// find index at first
		int index = s2.indexOf('r'); 

	}

}
