package com.ltmonitor.jt809.tool;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.model.ParameterModel;

public class Tools {
	public static long sendCount = 0L;

	/**
	 * 得出CRC计算结果
	 * 
	 * @param buf
	 *            要计算CRC的字符串
	 * @return 字符串,2个字节
	 */
	public static String getCRCString(String buff) {
		int crc = 0xFFFF; // initial value
		int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)

		byte[] data = HexString2Bytes(buff);

		for (int j = 0; j < data.length; j++) {
			// char b = buf.charAt(j);
			byte b = data[j];
			for (int i = 0; i < 8; i++) {
				boolean bit = ((b >> (7 - i) & 1) == 1);
				boolean c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				if (c15 ^ bit)
					crc ^= polynomial;
			}
		}

		crc &= 0xffff;
		String str = ToHexString(crc, 2);// "" + (char) (crc / 256) + (char)
											// (crc % 256);
		return str;
	}

	public static String CRC(String msg) {
		byte[] data = HexString2Bytes(msg);
		int crc = CRC(data);
		String str = ToHexString(crc, 2);
		return str;

	}

	public static int CRC(byte[] buffer) {
		int wTemp = 0;
		int crc = 0xffff;

		for (int i = 0; i < buffer.length; i++) {
			for (int j = 0; j < 8; j++) {
				wTemp = (short) (((buffer[i] << j) & 0x80) ^ ((crc & 0x8000) >> 8));

				crc <<= 1;

				if (wTemp != 0) {
					crc ^= 0x1021;
				}
			}
		}

		return (crc);
	}

	public static long getSendCount() {
		if (sendCount > 1000000000L)
			sendCount = 1L;
		else {
			sendCount += 1L;
		}
		return sendCount;
	}

	public static String turnDataLength(String data, int length) {
		int data_length = data.length();
		for (int i = data_length; i < length; i++) {
			data = "0" + data;
		}
		return data;
	}

	public static String turnStrLength(String data, int length) {
		int data_length = data.length();
		for (int i = data_length; i < length; i++) {
			data = data + "0";
		}
		return data;
	}

	public static String ToHexString(byte[] bts) {
		StringBuilder strBuild = new StringBuilder();

		for (int i = 0; i < bts.length; i++) {
			strBuild.append(ToHexString(bts[i]));
		}
		return strBuild.toString();
	}

	public static String hex2Ascii(String hex) {
		String result = "";
		int len = hex.length() / 2;
		for (int i = 0; i < len; i++) {
			int tmp = Integer.valueOf(hex.substring(2 * i, 2 * i + 2), 16)
					.intValue();
			result = result + (char) tmp;
		}
		return result;
	}

	public static byte[] HexString2Bytes(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;

		for (int i = 0; i < b.length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);

			b[i] = ((byte) (parse(c0) << 4 | parse(c1)));
			// int start = i * 2;
			// int c = Integer.parseInt(hexstr.substring(start, start + 2));
			// b[i] = (byte)c;
		}

		return b;
	}

	private static int parse(char c) {
		if (c >= 'a') {
			return c - 'a' + 10 & 0xF;
		}

		if (c >= 'A') {
			return c - 'A' + 10 & 0xF;
		}

		return c - '0' & 0xF;
	}

	public static String TurnISN(String str) {
		String str1 = "";
		byte[] b = (byte[]) null;
		try {
			b = str.getBytes("gbk");
		} catch (UnsupportedEncodingException e) {
			System.out.println("异常信息(ControllerReport TurnISN)"
					+ e.getMessage());
		}
		System.out.println();
		int i = 0;
		for (int max = b.length; i < max; i++) {
			str1 = str1 + Integer.toHexString(b[i] & 0xFF);
		}
		return str1.toUpperCase();
	}

	// 将数字转换成16进制字符串，不足补零
	public static String ToHexString(byte data) {
		return Integer.toHexString(data & 0xff | 0x100).substring(1);
	}

	// 将数字转换成16进制字符串，不足补零
	public static String ToHexString(Short data) {
		return Integer.toHexString(data & 0xffff | 0x10000).substring(1);
	}

	public static String ToHexString(long val) {
		String tmp = Long.toHexString(val);
		StringBuilder sb = new StringBuilder("0000000000000000");
		sb.replace(16 - tmp.length(), 16, tmp);
		return sb.toString();
	}

	// 将数字转换成16进制字符串，不足补零
	public static String ToHexString(int data) {
		String tmp = Integer.toHexString(data);
		StringBuilder sb = new StringBuilder("00000000");
		sb.replace(8 - tmp.length(), 8, tmp);
		return sb.toString();
	}

	public static String ToHexString(long data, int byteNum) {

		StringBuilder sb = new StringBuilder("");
		for (int m = 0; m < byteNum; m++) {
			sb.append("00");
		}
		int totalLen = byteNum * 2;
		String tmp = Long.toHexString(data);
		if (tmp.length() <= totalLen) {
			sb.replace(totalLen - tmp.length(), totalLen, tmp);
		} else {
			return tmp.substring(tmp.length() - totalLen, tmp.length());
		}
		return sb.toString();
	}

	/**
	 * 将字符串转换成16进制串
	 */
	public static String ToHexString(String str) {
		String str1 = "";
		try {
			byte[] b = str.getBytes("gbk");
			int i = 0;
			int max = b.length;
			for (; i < max; i++) {
				str1 = str1 + Integer.toHexString(b[i] & 0xFF);
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("异常信息ToHexString" + e.getMessage());
		}
		return str1;
	}

	/**
	 * 将字符串转换成16进制串，长度不足补零
	 */
	public static String ToHexString(String str, int length) {
		if (str == null)
			str = "";
		String str1 = "";
		byte[] b = (byte[]) null;
		try {
			b = str.getBytes("gbk");
		} catch (UnsupportedEncodingException e) {
			System.out.println("异常信息(ControllerReport TurnISN)"
					+ e.getMessage());
		}
		System.out.println();
		int i = 0;
		int max = b.length;
		for (; i < max; i++) {
			str1 = str1 + Integer.toHexString(b[i] & 0xFF);
		}
		str1 = str1.toUpperCase();
		return turnStrLength(str1, length * 2);

	}

	public static String getStringFromHex(String str1) {
		try {
			str1 = new String(HexString2Bytes(str1), "gbk");
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
		}
		return str1;
	}

	public static int getYear(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String year = format.format(date);
		return Integer.valueOf(year.substring(0, 4)).intValue();
	}

	public static int getMonth(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String year = format.format(date);
		return Integer.valueOf(year.substring(4, 6)).intValue();
	}

	public static int getDay(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String year = format.format(date);
		return Integer.valueOf(year.substring(6, 8)).intValue();
	}

	public static int getHour(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String year = format.format(date);
		return Integer.valueOf(year.substring(8, 10)).intValue();
	}

	public static int getMinute(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String year = format.format(date);
		return Integer.valueOf(year.substring(10, 12)).intValue();
	}

	public static int getSeconds(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
		String year = format.format(date);
		return Integer.valueOf(year.substring(12, 14)).intValue();
	}

	public static String getDateBCDStr(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
		return format.format(date);
	}

	public static String getHexDateTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String hexdate = format.format(date);

		String year = turnDataLength(Integer.toString(
				Integer.valueOf(hexdate.substring(0, 4)).intValue(), 16), 4);
		String month = turnDataLength(Integer.toString(
				Integer.valueOf(hexdate.substring(4, 6)).intValue(), 16), 2);
		String day = turnDataLength(Integer.toString(
				Integer.valueOf(hexdate.substring(6, 8)).intValue(), 16), 2);
		String hour = turnDataLength(Integer.toString(
				Integer.valueOf(hexdate.substring(8, 10)).intValue(), 16), 2);
		String minute = turnDataLength(Integer.toString(
				Integer.valueOf(hexdate.substring(10, 12)).intValue(), 16), 2);
		String seconds = turnDataLength(Integer.toString(
				Integer.valueOf(hexdate.substring(12, 14)).intValue(), 16), 2);

		return day + month + year + hour + minute + seconds;
	}

	// 对于参数指定的日期和时间，返回自 1970 年 1 月 1 日 00:00:00 GMT 以来的毫秒数
	public static String getUTC(Date date) {

		// long dt = date.getTime()/1000;//

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		long dt = date.UTC(date.getYear(), date.getMonth(), date.getDay(),
				calendar.get(Calendar.HOUR_OF_DAY) - 8, date.getMinutes(),
				date.getSeconds());
		return ToHexString(dt / 1000, 8);
	}

	public static long myround(double num) {
		BigDecimal b = new BigDecimal(num);
		num = b.setScale(2, 4).longValue();
		return (long) num;
	}

	public static Date strToDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(str);
		} catch (ParseException e) {
			System.out.println("异常信息（strToDate）:" + e.toString());
		}
		return null;
	}

	public static String encoderStringEscape(String strEscape) {
		strEscape = strEscape.toUpperCase();
		int byteNum = strEscape.length() / 2;
		StringBuilder sb = new StringBuilder();
		for (int m = 0; m < byteNum; m++) {
			int start = m * 2;

			String temp = strEscape.substring(start, start + 2);
			if (temp.equals("5A"))
				sb.append("5A02");
			else if (temp.equals("5B"))
				sb.append("5A01");
			else if (temp.equals("5E"))
				sb.append("5E02");
			else if (temp.equals("5D"))
				sb.append("5E01");
			else
				sb.append(temp);
		}
		// strEscape = strEscape.replaceAll("5A", "5A02");
		// strEscape = strEscape.replaceAll("5B", "5A01");
		// strEscape = strEscape.replaceAll("5E", "5E02");
		// strEscape = strEscape.replaceAll("5D", "5E01");
		return sb.toString();
	}

	public static String decoderStringEscape(String strEscape) {
		strEscape = strEscape.toUpperCase();
		strEscape = strEscape.replaceAll("5A01", "5B");
		strEscape = strEscape.replaceAll("5a01", "5B");
		strEscape = strEscape.replaceAll("5A02", "5A");
		strEscape = strEscape.replaceAll("5a02", "5A");
		strEscape = strEscape.replaceAll("5E01", "5D");
		strEscape = strEscape.replaceAll("5e01", "5D");
		strEscape = strEscape.replaceAll("5E02", "5E");
		strEscape = strEscape.replaceAll("5e02", "5E");
		return strEscape;
	}

	public static String getHeaderAndFlag(long counter, String body,
			int dataType, long centerId,boolean isEncrypt) {
		if (body == null)
			body = "";
		ParameterModel pm = GlobalConfig.parModel;

		String header = "5B";

		String ver = turnDataLength(pm.getProtocolVer(), 6);

		String encryptFlag = "00";

		String encryptKey = "00000000";
		String endFlag = "5D";
		int length = body.length() / 2; // 字节长度

		long DateLength = length + 26;

		if (isEncrypt) {
			encryptFlag = "01"; //加密标志位
			body = Tools.encrypt(pm.getEncryptKey(), body);
			encryptKey = ToHexString(pm.getEncryptKey(), 4);
		}
		// 消息体
		String message = ToHexString(DateLength, 4) + ToHexString(counter, 4)
				+ ToHexString(dataType, 2) + ToHexString(centerId, 4) + ver
				+ encryptFlag + encryptKey + body;

		message = message + getCRCString(message);
		
		int mesLength = message.length();

		message = encoderStringEscape(message);

		message = header + message + endFlag;
		return message;
	}

	public static Timestamp convertToTimestamp(String message) {
		long time = Long.valueOf(message, 16).longValue();

		int year = (int) (time >> 26 & 0x3F) + 2000;
		int month = (int) (time >> 22 & 0xF);
		int day = (int) (time >> 17 & 0x1F);
		int hour = (int) (time >> 12 & 0x1F);
		int minute = (int) (time >> 6 & 0x3F);
		int second = (int) (time & 0x3F);
		Calendar cd = Calendar.getInstance();
		cd.set(year, month - 1, day, hour, minute, second);
		return new Timestamp(cd.getTimeInMillis());
	}
	private final static long MAX = (1L << 32) - 1;  
	public static String encrypt(int _key, String str) {
		long key = _key;
		int m1 = 10000000;
		int a1 = 20000000;
		int c1 = 30000000;

		byte[] b = HexString2Bytes(str);
		int size = b.length;
		if (key == 0)
			key = 1;
		int i = 0;
		while (i < size) {
			key = (a1 * (key % m1) + c1) & MAX;
			short d = (short) ((key >> 20) & 0xFF);
			b[i++] ^= d;
			byte v = b[i-1];
		}
		String result = ToHexString(b);

		return result;
	}

	public static void main(String[] args) {
		
		String str = "宽容的心";
		str = Tools.getStringFromHex("bfedc8ddb5c4d0c4");
		String hexString = Tools.ToHexString(str);
		hexString = "b2e241303030303100000000000000000000000000019505000000913132333435360000000041504e000000000000000000000000000000000061646d696e000000000000000000000000000000000000000000000000000000000000000000000000000000000000000061646d696e00000000000000000000000000000000003132322e3139322e33352e35310000000000000000000000000000000000000004d200000000000054f57cef";
		String encryptString = Tools.encrypt(223344, hexString);
		//encryptString = "ed1f4f4440aae414";
		//String decryptString = Tools.encrypt(223344, encryptString);
		//String finalString = Tools.getStringFromHex(decryptString);
		Date date = new Date();
		
		String str1 = Tools.ToHexString(82);
		String str2 = Tools.ToHexString(182);
		

		System.out.println(date.toString());
		System.out.println(date.getYear() + "," + date.getMonth() + ","
				+ date.getDay() + "," + date.getHours() + ","
				+ date.getMinutes() + "," + date.getSeconds());
		long dt = Date.UTC(date.getYear(), (date.getMonth()), 29,
				date.getHours() - 8, date.getMinutes(), date.getSeconds());

		Date date1 = new Date(dt);

		System.out.println(date1.toString());
	}
}
