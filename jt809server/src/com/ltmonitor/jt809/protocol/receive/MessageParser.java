package com.ltmonitor.jt809.protocol.receive;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class MessageParser {

	private String msg;

	private int index;

	public MessageParser(String _msg) {
		msg = _msg;
	}
	

	public MessageParser(String _msg, int _index) {
		msg = _msg;
		index = _index * 2;
	}

	public String getString(int byteNum) {
		int endIndex = index + byteNum * 2;
		String s = msg.substring(index, endIndex);
		index = endIndex;;
		s =  HexToString(s);
		int enchar = s.indexOf('\0');
		if(enchar >= 0)
			s = s.substring(0, enchar);
		
		return s;
	}
	
	public int getInt(int byteNum) {
		int endIndex = index + byteNum * 2;
		String s = msg.substring(index, endIndex);
		index = endIndex;
		return Integer.valueOf(s, 16);
	}
	
	public long getLong(){
		int endIndex = index + 8 * 2;
		String s = msg.substring(index, endIndex);
		index = endIndex;
		return Long.valueOf(s, 16);
	}
	
	public Date getUtcDate()
	{
		long utc = getLong() * 1000;
		return new Date(utc);
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			//d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;

	}

	// 转化十六进制编码为字符串
	public static String HexToString(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			int start = i * 2;
			baKeyword[i] = (byte) (0xff & Integer.parseInt(
					s.substring(start, start + 2), 16));
		}
		
	 
	    try {
			s = new String(baKeyword, "gbk");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

}
