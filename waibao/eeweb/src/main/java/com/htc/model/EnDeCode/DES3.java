package com.htc.model.EnDeCode;
import java.lang.reflect.Method;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

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
	
	private static final Class<?> BYTE_ARRAY_CLASS = new byte[0].getClass();
	
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
		
		Class<?> encoderClazz = Class.forName("sun.misc.BASE64Encoder");
		Object encoder = encoderClazz.newInstance();
		Method m = encoderClazz.getMethod("encode", new Class[]{ BYTE_ARRAY_CLASS });
		return (String)m.invoke(encoder, new Object[] { b });
	}

	/**
	 * 解密
	 * @param src 数据源
	 * @param key 密钥，长度必须是8的倍数
	 * @return 返回解密后的原始数据
	 * @throws Exception
	 */
	public static String decrypt(String message) throws Exception {
		Class<?> decodeClazz = Class.forName("sun.misc.BASE64Decoder");
		Object decode = decodeClazz.newInstance();
		Method m = decodeClazz.getMethod("decodeBuffer", new Class<?>[] {String.class});
		byte[] bytesrc = (byte[])m.invoke(decode, new Object[] {message});
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
