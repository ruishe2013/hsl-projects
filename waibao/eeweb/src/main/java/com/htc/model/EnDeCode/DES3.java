package com.htc.model.EnDeCode;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @ DES3.java
 * 作用 : 加解密
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class DES3 {
	private static final String PASSWORD_CRYPT_KEY = "htceehtc";
	private static final String IV = "htceehtc";
	public DES3(){
		
	}
	
	/**
	 * 加密
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return 返回加密后的数据
	 * @throws Exception
	 */
	public static String encrypt(String message) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(PASSWORD_CRYPT_KEY.getBytes("GBK"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(IV.getBytes("GBK"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		byte[] b=cipher.doFinal(message.getBytes("GBK"));
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(b);
	}

	/**
	 * 解密
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public static String decrypt(String message) throws Exception {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bytesrc = decoder.decodeBuffer(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding"); 
		DESKeySpec desKeySpec = new DESKeySpec(PASSWORD_CRYPT_KEY.getBytes("GBK"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(IV.getBytes("GBK"));
		 
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);   
   
        byte[] retByte = cipher.doFinal(bytesrc);  
        return new String(retByte);
		
	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
//	public static void main(String[] args) throws Exception {
//		StringBuffer strbuf = new StringBuffer();
//		for (int i = 0; i < 200; i++) {
//			strbuf.append("i:"+i+"--"+"12345678901234567890abcdefghijklmnopqrstuvwxyz");
//		}
//		String cc = strbuf.toString();
//		//cc = "tes测试...";
//		String strd = DES3.encrypt(cc);
//		System.out.println(strd);
//		cc = DES3.decrypt(strd);
//		System.out.println(new String(cc.getBytes("GBK"),"GBK"));
//	}

}
