package com.htc.common;

import java.text.*;
import java.util.*;

/**
 * @ FunctionUnit.java
 * 作用 : 公共单元方法.目前有以下几个公共方法:
 * 		1: 获取当前系统的毫秒数 
 * 		2: 根据字符串，返回用类型"yyyy-MM-dd HH:mm:ss"格式化一个 日期变量 
 * 		3: 输入类型日期和格式类型,输出相应的日期字符串
 * 		4: 根据输入的日期类型,结合格式类型,输出多少间隔后的日期变量
 * 		5: 根据设定的 字符 长度, 剪切字符串 (英文字符一个字节,中文两个)
 * 		6: 根据设定的 字节 长度, 剪切字符串 (这个方法比较精确)
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class FunctionUnit {

	/**
	 * Calendar_END_MONTH:到月为止. eg:2007-05
	 */
	public static int Calendar_END_MONTH = 1;
	/**
	 * Calendar_END_DAY : 到天为止. eg:2007-05-01
	 */
	public static int Calendar_END_DAY = 2;
	/**
	 * 到小时为止. eg:2007-05-01 05:00(后面加":00")
	 * 
	 */
	public static int Calendar_END_HOUR = 3;
	/**
	 * Calendar_END_MINUTE : 到分为止. eg:2007-05-01 05:12
	 */
	public static int Calendar_END_MINUTE = 4;
	/**
	 * Calendar_END_SECOND : 到秒为止. eg:2007-05-01 05:12:32
	 */
	public static int Calendar_END_SECOND = 5;

	/**
	 * float 全局格式类型: ####.00
	 */
	public static DecimalFormat FLOAT_DATA_FORMAT = new DecimalFormat("0.00");
	/**
	 * 日期 全局格式类型: yyyy-MM-dd HH:mm:ss(长日期型)
	 */
	public static String DATA_FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 日期 全局格式类型: yyyy-MM-dd HH:mm(中日期型)
	 */
	public static String DATA_FORMAT_MIDDLE = "yyyy-MM-dd HH:mm";
	/**
	 * 日期 全局格式类型: HH:mm:ss (短日期型)
	 */
	public static String DATA_FORMAT_SHORT = "HH:mm:ss";
	/**
	 * 日期格式:显示中文
	 */
	public static int SHOW_CHINESE = 1;
	/**
	 * 日期格式:不显示中文
	 */
	public static int UN_SHOW_CHINESE = 2;
	
	/**
	 * 统计最大
	 */
	public static int STAT_MAX = 1;
	/**
	 * 统计平均
	 */
	public static int STAT_AVG = 2; 
	/**
	 * 统计最小
	 */
	public static int STAT_MIN = 3;

	/**
	 * @describe:获取当前系统的毫秒数
	 * @return 当前系统的毫秒数
	 * @date:2009-10-31
	 */
	public static long getSystemCurrentMSEL() {
		return new Date().getTime();
	}
	
	/**
	 * @describe: : 获取时间字符串(长日期型)
	 * @date:2009-10-31
	 */
	public static String getTime_Long_Str(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT_LONG);
		return dateFormat.format(date) ;
	}
	
	/**
	 * 日期 全局格式类型: yyyy-MM-dd HH:mm(中日期型)
	 * @date:2009-10-31
	 */
	public static String getTime_Middle_Str(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT_MIDDLE);
		return dateFormat.format(date) ;
	}
	
	/**
	 * @describe: : 获取时间字符串(短日期型)
	 * @date:2009-10-31
	 */
	public static String getTime_Short_Str(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATA_FORMAT_SHORT);
		return dateFormat.format(date) ;
	}

	/**
	 * @describe: 根据字符串，获取一个日期
	 * @param dateStr : "yyyy-MM-dd HH:mm:ss"格式的日期字符串. 如:"2009-10-31 12:20:10"
	 * @return 格式化后的日期(java.util.Date)类型
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
	 * @describe: 输入java.util.Date类型日期和格式类型,输出相应的日期字符串 如:getDateToStr(newDate(),<br>
	 *            Calendar_END_MINUTE),得到如:2009-10-31 12:20:10<br>
	 *            Calendar_END_Type为Calendar_END_HOUR时,后面添加00<br>
	 * @param date :需要格式的java.util.Date类型<br>
	 * @param Calendar_END_Type :格式类型. 如: Calendar_END_MONTH<br>
	 * @param type :显示类型. 如: SHOW_CHINESE, UN_SHOW_CHINESE<br>
	 * @return 格式化后字符串<br>
	 * @date:2009-10-31<br>
	 */
	public static String getDateToStr(Date date, int Calendar_END_Type, int chinese_type) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		StringBuffer strBuf = new StringBuffer();
		int trmpInt;

		strBuf.append(calendar.get(Calendar.YEAR));
		strBuf.append(chinese_type==SHOW_CHINESE?"年":"");
		if (Calendar_END_Type >= Calendar_END_MONTH) { // 到月为止. eg:2007-05
			strBuf.append(chinese_type==SHOW_CHINESE?"":"-");
			trmpInt = calendar.get(Calendar.MONTH) + 1;
			strBuf.append(trmpInt <= 9 ? "0" : "");
			strBuf.append(trmpInt);
			strBuf.append(chinese_type==SHOW_CHINESE?"月":"");
		}
		if (Calendar_END_Type >= Calendar_END_DAY) { // 到天为止. eg:2007-05-01
			strBuf.append(chinese_type==SHOW_CHINESE?"":"-");
			trmpInt = calendar.get(Calendar.DATE);
			strBuf.append(trmpInt <= 9 ? "0" : "");
			strBuf.append(trmpInt);
			strBuf.append(chinese_type==SHOW_CHINESE?"日":"");
		}
		if (Calendar_END_Type >= Calendar_END_HOUR) { // 到小时为止. eg:2007-05-01 05
			strBuf.append(" ");
			trmpInt = calendar.get(Calendar.HOUR_OF_DAY);
			strBuf.append(trmpInt <= 9 ? "0" : "");
			strBuf.append(trmpInt);
			strBuf.append(chinese_type==SHOW_CHINESE?"时":"");
		}
		if (Calendar_END_Type >= Calendar_END_MINUTE) { // 到分为止. eg:2007-05-01 05:12
			strBuf.append(chinese_type==SHOW_CHINESE?"":":");
			trmpInt = calendar.get(Calendar.MINUTE);
			strBuf.append(trmpInt <= 9 ? "0" : "");
			strBuf.append(trmpInt);
			strBuf.append(chinese_type==SHOW_CHINESE?"分":"");
		}
		if (Calendar_END_Type >= Calendar_END_SECOND) { // 到秒为止. eg:2007-05-01 05:12:56
			strBuf.append(chinese_type==SHOW_CHINESE?"":":");
			trmpInt = calendar.get(Calendar.SECOND);
			strBuf.append(trmpInt <= 9 ? "0" : "");
			strBuf.append(trmpInt);
			strBuf.append(chinese_type==SHOW_CHINESE?"秒":"");
		}
		if (Calendar_END_Type == Calendar_END_HOUR) { // 如果是到小时为止.则在后面加00
			strBuf.append(chinese_type==SHOW_CHINESE?"":":00");
		}
		calendar = null;
		
		return strBuf.toString();
	}

	/**
	 * @describe:根据输入的日期类型,结合格式类型,输出interval间隔后的日期类型
	 * @param date :需要格式的java.util.Date类型
	 * @param interval :时间间隔
	 * @param Calendar_END_Type :格式类型. 如: Calendar_END_MONTH
	 * @return 格式化后的日期类型
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
		if (Calendar_END_Type == Calendar_END_SECOND) { // 下一个秒钟
			calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND)	+ interval);
		} else if (Calendar_END_Type == Calendar_END_MINUTE) { // 下一个分钟
			calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + interval);
		} else if (Calendar_END_Type == Calendar_END_HOUR) {// 下一个小时
			calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + interval);
		} else if (Calendar_END_Type == Calendar_END_DAY) {// 下一个天数
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + interval);
		} else if (Calendar_END_Type == Calendar_END_MONTH) {// 下一个月数
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + interval);
		}
		sdFormat = null;
		return calendar;
	}
	
	
	/**
	 * @describe: Calendar 时间功能
	 * 获取当前时间片段：<br>
	 * Calendar.YEAR:年<br>
	 * Calendar.MONTH:月<br>
	 * Calendar.DATE:日<br>
	 * Calendar.HOUR_OF_DAY:时<br>
	 * Calendar.MINUTE:分<br>
	 * Calendar.SECOND:秒<br>
	 */
	public static int getCurTime(int type) throws Exception {
		int rsInt = getTypeTime(new Date(),type);
		return rsInt;
	}	
	
	/**
	 * @describe: Calendar 时间功能,获取当前时间片段
	 * @param date 输入时间
	 * @param type 类型:如下
	 * Calendar.YEAR:年<br>
	 * Calendar.MONTH:月<br>
	 * Calendar.DATE:日<br>
	 * Calendar.HOUR_OF_DAY:时<br>
	 * Calendar.MINUTE:分<br>
	 * Calendar.SECOND:秒<br>
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
	 * 由于短信接收年份时间显示只有后面两位,比如:2010,只显示10,这里perYear就要赋值为20		
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
	 * @describe: 把毫秒数转换成日期师字符
	 * @param millisecond 要转换的毫秒数
	 * @param dataFormat 需要格式化的日期类型.如:yyyy-MM-dd HH:mm:ss<br>
	 * 示例: FunctionUnit.mill2DateStr(longtime, FunctionUnit.DATA_FORMAT_LONG)
	 * @date:2010-1-16
	 */
	public static String mill2DateStr(long millisecond,String dataFormat){
	 	SimpleDateFormat sdf=new SimpleDateFormat(dataFormat); 
	 	return sdf.format(millisecond);		
	}
	
	/**
	 * @describe: 获取当天备份列表存储的文件夹部分信息.如:2010年/1月/20日
	 * @date:2010-1-20
	 */
	public static String getTypeTime(Date date) throws Exception {
		//Date date = new Date();
		String rsStr = getTypeTime(date, Calendar.YEAR)  + "年" + "/" 
					+  getTypeTime(date, Calendar.MONTH) + "月" + "/" 
					+  getTypeTime(date, Calendar.DATE) + "日";		
		return rsStr;
	}

	/**
	 * @describe : 根据设定的 字符 长度, 剪切字符串 (英文字符一个字节,中文两个)
	 * @param str 需要处理的字符串, 
	 * @param size 需要保留的长度
	 * @return : 剪切字符串. 如果字符太长，只显示size长度部分,然后后面加3点	
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
	 * 根据设定的 字节 长度, 剪切字符串 (这个方法比较精确)
	 * @param str 		被处理字符串
	 * @param toCount   截取长度
	 * @param more      后缀字符串
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
	 * @describe:	把char类型转换成16进制字符串
	 * @param bArray  char类型数组
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
	 * @describe: 代码复用片段-(转化成16进制的数字,10进制的字符串,再转成10进制数字:
	 * 	如0x18->12)BCD用，不能有abcdef,如果出现这里采用(hexNo & 0x99),保证不会出现字母 
	 * @param hexNo 需要转换是16进制数字
	 * @date:2009-11-5
	 */
	public static int reverseHexToDec(int hexNo){
		//hexNo = hexNo & 0x99;
		int decNo = Integer.parseInt(Integer.toHexString(hexNo));
		return decNo;
	}
	
	/**
	 * @describe: 字符串根据逗号分隔成List(Integer)
	 * @param placeListStr 目标字符串
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
	 * @describe:	根据stat_type获取两个Float数据的max,min或avg
	 * @param strA 比较数据A
	 * @param strB 比较数据B
	 * @param stat_type stat_type:STAT_MAX,STAT_MIN,STAT_AVG
	 * @param temp_show_type 温度显示格式(0:湿度(不显示) 1:摄氏 2:华氏)
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
	
	public static String getStatFloatAvg(String strA, int powerA, String strB, int powerB){
		return FLOAT_DATA_FORMAT.format((powerA *Float.parseFloat(strA)+ powerB * Float.parseFloat(strB))/(powerB+ powerA));
	}
	/**
	 * 中文转unicode
	 * @param source 需要转换的中文
	 * @return 反回unicode编码
	 */
	public static String toUnicode(String source) {
		StringBuffer strBuf = new StringBuffer();
		String str="";
		char[] c = source.toCharArray();   
		for(int   i=0;i<c.length;i++){
			str =Integer.toHexString((int)c[i]);
			if (str.length() ==2){str = "00" + str;}// str每个都是4位
			//strBuf.append("\\u");
			strBuf.append(str);
		}		
		return strBuf.toString();
	}
	
	/**
	 * unicode码转中文
	 * @param unicode unicode码
	 * @return 返回unicode对应的中文
	 */	
	public static String tozhCN(String unicode) {
		StringBuffer gbk = new StringBuffer();
		int data = 0 ;
		int len  = unicode.length();
		int beginIndex = 0;
		while (len > beginIndex) {
			// 将16进制数转换为 10进制的数据。
			data = Integer.parseInt(unicode.substring(beginIndex,beginIndex+4), 16);
			// 强制转换为char类型就是我们的中文字符了。
			gbk.append((char) data); 			
			beginIndex+=4;
		}
		return gbk.toString();
	}	
	
}
