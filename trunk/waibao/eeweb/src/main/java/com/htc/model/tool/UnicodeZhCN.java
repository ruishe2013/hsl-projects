package com.htc.model.tool;

/**
 * @ DeEnUnicode2ZhCN.java
 * ���� : ���ĺ�Unicode��ת
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-4-28     YANGZHONLI       create
 */
public class UnicodeZhCN {
	/**
	 * ����תunicode
	 * @param source ��Ҫת��������
	 * @return ����unicode����
	 */
	public static String toUnicode(String source) {
		StringBuffer strBuf = new StringBuffer();
		String str="";
		char[] c = source.toCharArray();   
		for(int   i=0;i<c.length;i++){
			str =Integer.toHexString((int)c[i]);
			if (str.length() ==3){str = "0" + str;}		// ��֤ÿ��str����4λ
			if (str.length() ==2){str = "00" + str;}	// ��֤ÿ��str����4λ
			if (str.length() ==1){str = "000" + str;}	// ��֤ÿ��str����4λ
			//System.out.println(str+":"+c[i]);
			strBuf.append(str);
		}		
		return strBuf.toString();
	}
	
	/**
	 * unicode��ת����
	 * @param unicode unicode��
	 * @return ����unicode��Ӧ������
	 */	
	public static String tozhCN(String unicode) {
		StringBuffer gbk = new StringBuffer();
		int data = 0 ;
		int len  = unicode.length();
		int beginIndex = 0;
		while (len > beginIndex) {
			// ��16������ת��Ϊ 10���Ƶ����ݡ�
			data = Integer.parseInt(unicode.substring(beginIndex,beginIndex+4), 16);
			// ǿ��ת��Ϊchar���;������ǵ������ַ��ˡ�
			if(data!=8232){//�зָ��� (Unicode 8232)
				gbk.append((char) data); 			
			}
			beginIndex+=4;
		}
		return gbk.toString();
	}
	
	/**
	 * PDU 7bit����
	 */
	public static String decode7Bit(String src) {   
	    String result = null;   
	    int[] b;   
	    String temp = null;   
	    byte srcAscii;   
	    byte left = 0;   
	    if (src != null && src.length() % 2 == 0) {   
	        result = "";   
	        b = new int[src.length() / 2];   
	        temp = src + "0";   
	        for (int i = 0, j = 0, k = 0; i < temp.length() - 2; i += 2, j++) {   
	            b[j] = Integer.parseInt(temp.substring(i, i + 2), 16);   
	  
	            k = j % 7;   
	            srcAscii = (byte) (((b[j] << k) & 0x7F) | left);   
	            result += (char) srcAscii;   
	            left = (byte) (b[j] >>> (7 - k));   
	            if (k == 6) {   
	                result += (char) left;   
	                left = 0;   
	            }   
	            if (j == src.length() / 2)   
	                result += (char) left;   
	        }   
	    }   
	    return result;   
	}	

//	public static void main(String args[]) {
//		String source = "����123..����@@ O(��_��)O����~ 2009-11-19 12:23:56";
//		String str = UnicodeZhCN.toUnicode(source);
//		System.out.println(str);
//		System.out.println(source);
//		System.out.println(UnicodeZhCN.tozhCN(str));
//	}
}
