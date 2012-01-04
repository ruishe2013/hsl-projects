package com.htc.common;

import java.text.*;
import java.util.*;

/**
 * @ FunctionUnit.java
 * ���� : ������Ԫ����.Ŀǰ�����¼�����������:
 * 		1: ��ȡ��ǰϵͳ�ĺ����� 
 * 		2: �����ַ���������������"yyyy-MM-dd HH:mm:ss"��ʽ��һ�� ���ڱ��� 
 * 		3: �����������ں͸�ʽ����,�����Ӧ�������ַ���
 * 		4: �����������������,��ϸ�ʽ����,������ټ��������ڱ���
 * 		5: �����趨�� �ַ� ����, �����ַ��� (Ӣ���ַ�һ���ֽ�,��������)
 * 		6: �����趨�� �ֽ� ����, �����ַ��� (��������ȽϾ�ȷ)
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class FunctionUnit {

	/**
	 * Calendar_END_MONTH:����Ϊֹ. eg:2007-05
	 */
	public static int Calendar_END_MONTH = 1;
	/**
	 * Calendar_END_DAY : ����Ϊֹ. eg:2007-05-01
	 */
	public static int Calendar_END_DAY = 2;
	/**
	 * ��СʱΪֹ. eg:2007-05-01 05:00(�����":00")
	 * 
	 */
	public static int Calendar_END_HOUR = 3;
	/**
	 * Calendar_END_MINUTE : ����Ϊֹ. eg:2007-05-01 05:12
	 */
	public static int Calendar_END_MINUTE = 4;
	/**
	 * Calendar_END_SECOND : ����Ϊֹ. eg:2007-05-01 05:12:32
	 */
	public static int Calendar_END_SECOND = 5;

	/**
	 * float ȫ�ָ�ʽ����: ####.00
	 */
	public static DecimalFormat FLOAT_DATA_FORMAT = new DecimalFormat("0.00");
	/**
	 * ���� ȫ�ָ�ʽ����: yyyy-MM-dd HH:mm:ss(��������)
	 */
	public static String DATA_FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
	/**
	 * ���� ȫ�ָ�ʽ����: yyyy-MM-dd HH:mm(��������)
	 */
	public static String DATA_FORMAT_MIDDLE = "yyyy-MM-dd HH:mm";
	/**
	 * ���� ȫ�ָ�ʽ����: HH:mm:ss (��������)
	 */
	public static String DATA_FORMAT_SHORT = "HH:mm:ss";
	/**
	 * ���ڸ�ʽ:��ʾ����
	 */
	public static int SHOW_CHINESE = 1;
	/**
	 * ���ڸ�ʽ:����ʾ����
	 */
	public static int UN_SHOW_CHINESE = 2;
	
	/**
	 * ͳ�����
	 */
	public static int STAT_MAX = 1;
	/**
	 * ͳ��ƽ��
	 */
	public static int STAT_AVG = 2; 
	/**
	 * ͳ����С
	 */
	public static int STAT_MIN = 3;

	/**
	 * @describe:��ȡ��ǰϵͳ�ĺ�����
	 * @return ��ǰϵͳ�ĺ�����
	 * @date:2009-10-31
	 */
	public static long getSystemCurrentMSEL() {
		return new Date().getTime();
	}
	
	/**
	 * @describe: : ��ȡʱ���ַ���(��������)
	 * @date:2009-10-31
	 */
	public static String getTime_Long_Str(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT_LONG);
		return dateFormat.format(date) ;
	}
	
	/**
	 * ���� ȫ�ָ�ʽ����: yyyy-MM-dd HH:mm(��������)
	 * @date:2009-10-31
	 */
	public static String getTime_Middle_Str(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT_MIDDLE);
		return dateFormat.format(date) ;
	}
	
	/**
	 * @describe: : ��ȡʱ���ַ���(��������)
	 * @date:2009-10-31
	 */
	public static String getTime_Short_Str(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT_SHORT);
		return dateFormat.format(date) ;
	}

	/**
	 * @describe: �����ַ�������ȡһ������
	 * @param dateStr : "yyyy-MM-dd HH:mm:ss"��ʽ�������ַ���. ��:"2009-10-31 12:20:10"
	 * @return ��ʽ���������(java.util.Date)����
	 * @date:2009-10-31
	 */
	public static Date getStrToDate(String dateStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT_LONG);
		Date dateTemp = null;
		if (dateStr != null){
			try {
				dateTemp = dateFormat.parse(dateStr);
			} catch (ParseException e) {
				dateTemp = null;
				e.printStackTrace();
			}
		}
		return dateTemp;
	}

	/**
	 * @describe: ����java.util.Date�������ں͸�ʽ����,�����Ӧ�������ַ��� ��:getDateToStr(newDate(),<br>
	 *            Calendar_END_MINUTE),�õ���:2009-10-31 12:20:10<br>
	 *            Calendar_END_TypeΪCalendar_END_HOURʱ,�������00<br>
	 * @param date :��Ҫ��ʽ��java.util.Date����<br>
	 * @param Calendar_END_Type :��ʽ����. ��: Calendar_END_MONTH<br>
	 * @param type :��ʾ����. ��: SHOW_CHINESE, UN_SHOW_CHINESE<br>
	 * @return ��ʽ�����ַ���<br>
	 * @date:2009-10-31<br>
	 */
	public static String getDateToStr(Date date, int Calendar_END_Type, int chinese_type) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		StringBuffer strBuf = new StringBuffer();
		int trmpInt;

		strBuf.append(calendar.get(Calendar.YEAR));
		strBuf.append(chinese_type==SHOW_CHINESE?"��":"");
		if (Calendar_END_Type >= Calendar_END_MONTH) { // ����Ϊֹ. eg:2007-05
			strBuf.append(chinese_type==SHOW_CHINESE?"":"-");
			trmpInt = calendar.get(Calendar.MONTH) + 1;
			strBuf.append(trmpInt <= 9 ? "0" : "");
			strBuf.append(trmpInt);
			strBuf.append(chinese_type==SHOW_CHINESE?"��":"");
		}
		if (Calendar_END_Type >= Calendar_END_DAY) { // ����Ϊֹ. eg:2007-05-01
			strBuf.append(chinese_type==SHOW_CHINESE?"":"-");
			trmpInt = calendar.get(Calendar.DATE);
			strBuf.append(trmpInt <= 9 ? "0" : "");
			strBuf.append(trmpInt);
			strBuf.append(chinese_type==SHOW_CHINESE?"��":"");
		}
		if (Calendar_END_Type >= Calendar_END_HOUR) { // ��СʱΪֹ. eg:2007-05-01 05
			strBuf.append(" ");
			trmpInt = calendar.get(Calendar.HOUR_OF_DAY);
			strBuf.append(trmpInt <= 9 ? "0" : "");
			strBuf.append(trmpInt);
			strBuf.append(chinese_type==SHOW_CHINESE?"ʱ":"");
		}
		if (Calendar_END_Type >= Calendar_END_MINUTE) { // ����Ϊֹ. eg:2007-05-01 05:12
			strBuf.append(chinese_type==SHOW_CHINESE?"":":");
			trmpInt = calendar.get(Calendar.MINUTE);
			strBuf.append(trmpInt <= 9 ? "0" : "");
			strBuf.append(trmpInt);
			strBuf.append(chinese_type==SHOW_CHINESE?"��":"");
		}
		if (Calendar_END_Type >= Calendar_END_SECOND) { // ����Ϊֹ. eg:2007-05-01 05:12:56
			strBuf.append(chinese_type==SHOW_CHINESE?"":":");
			trmpInt = calendar.get(Calendar.SECOND);
			strBuf.append(trmpInt <= 9 ? "0" : "");
			strBuf.append(trmpInt);
			strBuf.append(chinese_type==SHOW_CHINESE?"��":"");
		}
		if (Calendar_END_Type == Calendar_END_HOUR) { // ����ǵ�СʱΪֹ.���ں����00
			strBuf.append(chinese_type==SHOW_CHINESE?"":":00");
		}
		calendar = null;
		
		return strBuf.toString();
	}

	/**
	 * @describe:�����������������,��ϸ�ʽ����,���interval��������������
	 * @param date :��Ҫ��ʽ��java.util.Date����
	 * @param interval :ʱ����
	 * @param Calendar_END_Type :��ʽ����. ��: Calendar_END_MONTH
	 * @return ��ʽ�������������
	 * @throws Exception
	 * @date:2009-10-31
	 */
	public static Calendar nextTime(Date date, int interval,
			int Calendar_END_Type) throws Exception {
		SimpleDateFormat sdFormat = null;
		if (Calendar_END_Type == Calendar_END_SECOND) {
			sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if (Calendar_END_Type == Calendar_END_MINUTE) {
			sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		} else if (Calendar_END_Type == Calendar_END_HOUR) {
			sdFormat = new SimpleDateFormat("yyyy-MM-dd HH");
		} else if (Calendar_END_Type == Calendar_END_DAY) {
			sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		} else if (Calendar_END_Type == Calendar_END_MONTH) {
			sdFormat = new SimpleDateFormat("yyyy-MM");
		}
		
		if (date == null){return null;}
		
		String dateString = sdFormat.format(date);
		Date dateEx = sdFormat.parse(dateString);
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(dateEx);
		if (Calendar_END_Type == Calendar_END_SECOND) { // ��һ������
			calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND)	+ interval);
		} else if (Calendar_END_Type == Calendar_END_MINUTE) { // ��һ������
			calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + interval);
		} else if (Calendar_END_Type == Calendar_END_HOUR) {// ��һ��Сʱ
			calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + interval);
		} else if (Calendar_END_Type == Calendar_END_DAY) {// ��һ������
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + interval);
		} else if (Calendar_END_Type == Calendar_END_MONTH) {// ��һ������
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + interval);
		}
		sdFormat = null;
		return calendar;
	}
	
	
	/**
	 * @describe: Calendar ʱ�书��
	 * ��ȡ��ǰʱ��Ƭ�Σ�<br>
	 * Calendar.YEAR:��<br>
	 * Calendar.MONTH:��<br>
	 * Calendar.DATE:��<br>
	 * Calendar.HOUR_OF_DAY:ʱ<br>
	 * Calendar.MINUTE:��<br>
	 * Calendar.SECOND:��<br>
	 */
	public static int getCurTime(int type) throws Exception {
		int rsInt = getTypeTime(new Date(),type);
		return rsInt;
	}	
	
	/**
	 * @describe: Calendar ʱ�书��,��ȡ��ǰʱ��Ƭ��
	 * @param date ����ʱ��
	 * @param type ����:����
	 * Calendar.YEAR:��<br>
	 * Calendar.MONTH:��<br>
	 * Calendar.DATE:��<br>
	 * Calendar.HOUR_OF_DAY:ʱ<br>
	 * Calendar.MINUTE:��<br>
	 * Calendar.SECOND:��<br>
	 */
	public static int getTypeTime(Date date, int type) throws Exception {
		int rsInt = 0;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		rsInt = calendar.get(type);
		if (type == Calendar.MONTH){
			rsInt = rsInt + 1;
		}
		return rsInt;
	}	
	
	/**
	 * ���ڶ��Ž������ʱ����ʾֻ�к�����λ,����:2010,ֻ��ʾ10,����perYear��Ҫ��ֵΪ20		
	 */
	public static String getPerYear() {
		String rsStr = "20";
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		rsStr = String.valueOf(calendar.get(Calendar.YEAR));
		rsStr = rsStr.substring(0, 2);
		return rsStr;
	}	
	
	/**
	 * @describe: �Ѻ�����ת��������ʦ�ַ�
	 * @param millisecond Ҫת���ĺ�����
	 * @param dataFormat ��Ҫ��ʽ������������.��:yyyy-MM-dd HH:mm:ss<br>
	 * ʾ��: FunctionUnit.mill2DateStr(longtime, FunctionUnit.DATA_FORMAT_LONG)
	 * @date:2010-1-16
	 */
	public static String mill2DateStr(long millisecond,String dataFormat){
	 	SimpleDateFormat sdf=new SimpleDateFormat(dataFormat); 
	 	return sdf.format(millisecond);		
	}
	
	/**
	 * @describe: ��ȡ���챸���б�洢���ļ��в�����Ϣ.��:2010��/1��/20��
	 * @date:2010-1-20
	 */
	public static String getTypeTime(Date date) throws Exception {
		//Date date = new Date();
		String rsStr = getTypeTime(date, Calendar.YEAR)  + "��" + "/" 
					+  getTypeTime(date, Calendar.MONTH) + "��" + "/" 
					+  getTypeTime(date, Calendar.DATE) + "��";		
		return rsStr;
	}

	/**
	 * @describe : �����趨�� �ַ� ����, �����ַ��� (Ӣ���ַ�һ���ֽ�,��������)
	 * @param str ��Ҫ������ַ���, 
	 * @param size ��Ҫ�����ĳ���
	 * @return : �����ַ���. ����ַ�̫����ֻ��ʾsize���Ȳ���,Ȼ������3��	
	 * @date:2009-11-4
	 */
	public static String truncationString(String str,int size){
		String strTemp = str;
		int len = str.length();
		if (len > size){
			strTemp = str.substring(0, size)+ "..." ;
		}
		return strTemp;
	}
	
	/**
	 * �����趨�� �ֽ� ����, �����ַ��� (��������ȽϾ�ȷ)
	 * @param str 		�������ַ���
	 * @param toCount   ��ȡ����
	 * @param more      ��׺�ַ���
	 * @return String
	 */
	public static String substringByByte(String str, int toCount, String more) {
		int reInt = 0;
		//String reStr = "";
		StringBuffer reStr = new StringBuffer();
		
		if (str == null)
			return "";
		char[] tempChar = str.toCharArray();
		for (int kk = 0; (kk < tempChar.length && toCount > reInt); kk++) {
			String s1 = String.valueOf(tempChar[kk]);
			byte[] b = s1.getBytes();
			reInt += b.length;
			reStr.append(tempChar[kk]);
			//reStr = reStr + tempChar[kk];
		}
		if (toCount == reInt || (toCount == reInt - 1))
			reStr.append(more);
			//reStr += more;
		return reStr.toString();
		//return reStr;
	}
	
	/**
	 * @describe:	��char����ת����16�����ַ���
	 * @param bArray  char��������
	 * @date:2009-11-7
	 */
	public static final String bytesToHexString(char[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;

		for (int i = 0; i < bArray.length; i++) {

			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}

			sb.append(sTemp.toUpperCase() + " ");
		}
		return sb.toString();
	}
	
	/**
	 * @describe: ���븴��Ƭ��-(ת����16���Ƶ�����,10���Ƶ��ַ���,��ת��10��������:
	 * 	��0x18->12)BCD�ã�������abcdef,��������������(hexNo & 0x99),��֤���������ĸ 
	 * @param hexNo ��Ҫת����16��������
	 * @date:2009-11-5
	 */
	public static int reverseHexToDec(int hexNo){
		//hexNo = hexNo & 0x99;
		int decNo = Integer.parseInt(Integer.toHexString(hexNo));
		return decNo;
	}
	
	/**
	 * @describe: �ַ������ݶ��ŷָ���List(Integer)
	 * @param placeListStr Ŀ���ַ���
	 * @date:2009-11-4
	 */
	public static List<Integer> getIntListByStr(String str){
		List<Integer> intList = new ArrayList<Integer>();
		if (str == null){return intList;}
		
		String [] strArr = str.split(",");
		int intTemp;
		
		for (int i = 0; i < strArr.length; i++) {
			if ( (strArr[i].trim()).length()<=0 ){continue;}
			intTemp = Integer.parseInt(strArr[i].trim());
			intList.add(intTemp);
		}
		return intList;
	}	
	
	/**
	 * @describe:	����stat_type��ȡ����Float���ݵ�max,min��avg
	 * @param strA �Ƚ�����A
	 * @param strB �Ƚ�����B
	 * @param stat_type stat_type:STAT_MAX,STAT_MIN,STAT_AVG
	 * @param temp_show_type �¶���ʾ��ʽ(0:ʪ��(����ʾ) 1:���� 2:����)
	 * @return:
	 * @date:2009-12-7
	 */
	public static String getStatFloat(String strA,String strB, int stat_type, int temp_show_type){
		String rsStr = "";
		if(temp_show_type == 2){
			strA = FunctionUnit.FLOAT_DATA_FORMAT.format(Float.parseFloat(strA) *9/5 + 32);			
		}
		if (stat_type == STAT_MAX){
			rsStr = Float.parseFloat(strA)>=Float.parseFloat(strB)?strA:strB;
		}else if (stat_type == STAT_MIN){
			rsStr = Float.parseFloat(strA)<=Float.parseFloat(strB)?strA:strB;
		}else if (stat_type == STAT_AVG){
			rsStr =	FLOAT_DATA_FORMAT.format((Float.parseFloat(strA)+Float.parseFloat(strB))/2);
		}
		return rsStr;
	}
	
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
			if (str.length() ==2){str = "00" + str;}// strÿ������4λ
			//strBuf.append("\\u");
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
			gbk.append((char) data); 			
			beginIndex+=4;
		}
		return gbk.toString();
	}	
	
}
