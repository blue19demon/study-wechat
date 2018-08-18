package com.wechat.utils;

import java.security.MessageDigest;

public class EncryptionUtil {
	 
	  public static String getSha1(String source){
	    // 用来将字节转换成 16 进制表示的字符
	 // System.out.println("进getSha1");
	 
	    char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	 
	    try {
	      MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
	      mdTemp.update(source.getBytes("UTF-8")); // 通过使用 update 方法处理数据,使指定的 byte数组更新摘要
	 
	      byte[] encryptStr = mdTemp.digest(); // 获得密文完成哈希计算,产生128 位的长整数
	 
	      int j=encryptStr.length;
	      char buf[] = new char[j * 2]; 
	 
	      int k = 0; // 表示转换结果中对应的字符位置
	 
	      for (int i = 0; i < j; i++) { // 从第一个字节开始，对每一个字节,转换成 j 进制字符的转换
	        byte byte0 = encryptStr[i]; // 取第 i 个字节
	        buf[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
	        buf[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
	      }
	     // System.out.println(buf.toString());
	      return new String(buf); // 换后的结果转换为字符串
	    } catch (Exception e) {
	    //System.out.println("getSha1->try...catch");
	      e.printStackTrace();
	    }
	    return null;
	  }
}
