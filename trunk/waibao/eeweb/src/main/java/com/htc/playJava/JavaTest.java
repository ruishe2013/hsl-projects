package com.htc.playJava;

import java.io.*;
import java.sql.Time;
import java.text.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.htc.bean.BeanForRecord;
import com.htc.common.FunctionUnit;

/**
 * @ DateTest.java 作用 : java 代码 学习 测试. 注意事项 : 无 VERSION DATE BY CHANGE/COMMENT
 * 1.0 2009-11-6 YANGZHONLI create
 */
public class JavaTest {

	public static void main(String[] args) throws Exception {

		/*
		 * long start; System.out.println("start...");
		 * 
		 * // 测试 时间功能 start = System.currentTimeMillis(); testTime();
		 * System.out.println("测试 时间功能 运行:" + (System.currentTimeMillis() -
		 * start) + "毫秒");
		 * 
		 * // 创建和htcweb中表thisrecorddailay结构一样的SQL语句. start =
		 * System.currentTimeMillis(); createSqlMonth(80000,"2006-01-01 0:0:0",
		 * 2 , 1, "table"); System.out.println("创建 createSqlMonth SQL 运行:" +
		 * (System.currentTimeMillis() - start) + "毫秒");
		 * 
		 * // 创建unionSQL语句 start = System.currentTimeMillis();
		 * createSqlUnion("2009-09-07 10:38:50"
		 * ,"2009-09-11 10:38:50",5,"unionSQL");
		 * System.out.println("创建 createSqlUnion  运行:" +
		 * (System.currentTimeMillis() - start) + "毫秒");
		 * 
		 * System.out.println("/** OK! /");
		 */
		
		// 分析异常时会不会返回值的情况
		// System.out.println(tryCatchTest());
		// System.out.println(FunctionUnit.FLOAT_DATA_FORMAT.format(10.223));
		// 读取系统的环境变量
		// readSystemProperty();
//		System.out.println(FunctionUnit.getCurTime(Calendar.HOUR_OF_DAY));
//		Date endTime = FunctionUnit.nextTime(new Date(), -12, FunctionUnit.Calendar_END_MONTH).getTime();
//	 	String endRecTime = FunctionUnit.getDateToStr(endTime, FunctionUnit.Calendar_END_SECOND, FunctionUnit.UN_SHOW_CHINESE);
//	 	System.out.println(endRecTime);
//	 	
//	 	long longtime = new Date().getTime();
//	 	System.out.println(longtime);
//	 	System.out.println(FunctionUnit.mill2DateStr(longtime, FunctionUnit.DATA_FORMAT_LONG));
//	 	System.out.println(FunctionUnit.mill2DateStr(longtime, FunctionUnit.DATA_FORMAT_SHORT));
//	 	
//        String path=JavaTest.class.getResource("/").getPath();
//        System.out.println(path);
//        path=path.substring(1,path.indexOf("/WebRoot")+1)+"WebRoot/Excel/";
//        System.out.println(path);
//        System.out.println(1/2 +1);
//        
//        
//        Date date1 = FunctionUnit.getStrToDate("2010-1-18 12:15:16");
//        Date date2 = FunctionUnit.getStrToDate("2010-1-18 12:15:16");
//        System.out.println(date1.before(date2));
//        System.out.println(date1.compareTo(date2));
//        
//        String subStr = "c:/htc/backup/2010年/1月/20日/2010年01月20日(未定义)(2_11).xls";
//        System.out.println(subStr.replace("/", "//"));
//        subStr = subStr.trim();
//        System.out.println(subStr.substring(subStr.lastIndexOf("/")+1));
//        
//        System.out.println(FunctionUnit.nextTime(new Date(), -12+1, FunctionUnit.Calendar_END_MONTH).getTime().getMonth());
//        
//        
//        String s = "FFF";
//        if (s.length() == 3);{
//        	
//        } 
//        int aa = Integer.parseInt(s, 16);
//        System.out.println(aa);
        
//        int currentMinute = FunctionUnit.getCurTime(Calendar.MINUTE); // 当前时间的分钟数
//        currentMinute = 56;
//        currentMinute = currentMinute - currentMinute % 10;
//        System.out.println(currentMinute);
//        System.out.println(currentMinute % 10);
//        System.out.println(currentMinute / 10);
//        
//        Date dd = new Date();
//        System.out.println(dd.toLocaleString());
//        
//        int i =1;
//        System.out.println(i++);
//        System.out.println(i);
		System.out.println(fillStringTo4("01"));
	}
	
	public static String fillStringTo4(String str){
		StringBuffer strbuf = new StringBuffer();
		str = String.valueOf((Integer.parseInt(str)+1)%10000);
		int len = str.length();
		for (int i = len; i < 4; i++) {
			strbuf.append("0");
		}
		strbuf.append(str);
		return strbuf.toString();
	}	
	
	/**
	 * @describe: 读取系统的环境变量
	 * @date:2009-11-15
	 */
	private static void readSystemProperty() {
		Properties p = System.getProperties();
		Enumeration<?> pn = p.propertyNames();

		// Enumeration en = p.elements();
		while (pn.hasMoreElements()) {
			Object o = pn.nextElement();
			System.out.println(o.toString() + "=" + p.get(o.toString()));
		}
	}

	/**
	 * @describe: 读取系统的环境变量
	 * @date:2009-11-15
	 */
	private static void readSystemPropertyB() {
		System.out.println(System.getenv("java_home_home"));
		System.out.println(System.getenv("java_home"));

		Runtime ObjRunTime = Runtime.getRuntime();
		byte[] env = new byte[1000];
		try {
			Process ObjPrcess = ObjRunTime.exec("cmd   /c   echo   %java_home_home%");
			InputStream in = ObjPrcess.getInputStream();
			in.read(env);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(new String(env).trim());
	}

	/**
	 * @describe: 分析异常时会不会返回值的情况.
	 * @date:2009-11-7
	 */
	public static int tryCatchTest() {
		int a = 0;
		try {
			a = 7 / 0;
		} catch (Exception e) {
			a = -1;
			e.printStackTrace();
		}
		return a;
	}

	/**
	 * @describe: Calendar 时间功能
	 */
	private static void testTime() throws Exception {
		String form = "2009-09-07 23:43:31";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = df.parse(form);

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);

		System.out.println(calendar.getTime().toLocaleString());
		System.out.print("YEAR: " + calendar.get(Calendar.YEAR));
		System.out.print("  MONTH: " + calendar.get(Calendar.MONTH));
		System.out.print("  DATE: " + calendar.get(Calendar.DATE));
		System.out
				.print("  HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
		System.out.print("  MINUTE: " + calendar.get(Calendar.MINUTE));
		System.out.print("  SECOND: " + calendar.get(Calendar.SECOND));
		System.out.println();
	}

	/**
	 * @describe: 创建和htcweb中表thisrecorddailay结构一样的SQL语句.可以选择加密或者不加密
	 * @param len
	 *            需要创建SQL语句的长度
	 * @param dateStr
	 *            开始时间(字符串)
	 * @param type
	 *            1: 小时作为间隔 2: 天作为间隔
	 * @param encode
	 *            SQL语句需不需要加密. 1: 加密 2: 不加密
	 * @param table
	 *            insert中的 表名, 并会在根目录下面,生成"table".txt的文件.
	 * @date:2009-11-7
	 */
	 

	public static void createSqlMonth(int len, String dateStr, int type,
			int encode, String table) throws Exception {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date dateEx = sdFormat.parse(dateStr);

		StringBuffer strbuf = new StringBuffer();
		DecimalFormat deformat = new DecimalFormat("####.00");
		String temp;

		String pre_A = "";
		String pre_B = "";
		String end = "";
		if (encode == 1) {
			pre_A = "ENCODE('";
			pre_B = "','htc'),";
			end = "','htc')";
		} else {
			pre_A = "'";
			pre_B = "',";
			end = "'";
		}
		strbuf.append("use htcweb;\n");
		for (int i = 0; i < len; i++) {
			temp = deformat.format(Math.random() * 40 + 20);
			// strbuf.append("II-"+ i + ":"
			strbuf
					.append(""
							+ "insert into "
							+ table
							+ "(equipmentId,recTime,tempavg,tempmax,tempmin,humiavg,humimax,humimin) values("
							+ (i % 2 + 1) + ",");
			if (type == 1) {
				dateEx = nextHour(dateEx.toLocaleString());
			} else if (type == 2) {
				dateEx = nextDay(dateEx.toLocaleString());
			}
			temp = dateEx.toLocaleString();
			strbuf.append("'" + temp + "',");

			temp = deformat.format(Math.random() * 60 + 1);
			strbuf.append(pre_A + temp + pre_B);
			temp = deformat.format(Math.random() * 60 + 1);
			strbuf.append(pre_A + temp + pre_B);
			temp = deformat.format(Math.random() * 60 + 1);
			strbuf.append(pre_A + temp + pre_B);
			temp = deformat.format(Math.random() * 110 - 10);
			strbuf.append(pre_A + temp + pre_B);
			temp = deformat.format(Math.random() * 110 - 10);
			strbuf.append(pre_A + temp + pre_B);
			temp = deformat.format(Math.random() * 110 - 10);
			strbuf.append(pre_A + temp + end);
			strbuf.append(");\n");
		}
		writeFile(table + ".txt", strbuf.toString());
	}

	/**
	 * @describe: 创建unionSQL语句
	 * @param startData
	 *            开始时间(字符串)
	 * @param endData
	 *            结束时间(字符串)
	 * @param interval
	 *            间隔时间
	 * @param fileName
	 *            生成的SQL所要保存的文件名
	 * @date:2009-11-7
	 */
	 

	public static void createSqlUnion(String startData, String endData,
			int interval, String fileName) throws Exception {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer strbuf = new StringBuffer();

		Date dateFrom = sdFormat.parse(startData);
		Date dateEnd = sdFormat.parse(endData);
		Date datetemp = dateFrom;

		datetemp = FunctionUnit.nextTime(dateFrom, interval,
				FunctionUnit.Calendar_END_MINUTE).getTime();
		strbuf.append(getString(dateFrom, datetemp));
		dateFrom = datetemp;
		while (datetemp.before(dateEnd)) {
			datetemp = FunctionUnit.nextTime(dateFrom, interval,
					FunctionUnit.Calendar_END_MINUTE).getTime();
			strbuf.append("union all\n");
			strbuf.append(getString(dateFrom, datetemp));
			dateFrom = datetemp;
		}

		writeFile(fileName + ".txt", strbuf.toString());
	}

	/**
	 * @describe: unionSQL 语句单体
	 * @date:2009-11-7
	 */
	 

	private static String getString(Date dataA, Date dataB) {
		StringBuffer strbuf = new StringBuffer();
		String dataStrA = FunctionUnit.getDateToStr(dataA,
				FunctionUnit.Calendar_END_MINUTE, FunctionUnit.UN_SHOW_CHINESE);
		String dataStrB = FunctionUnit.getDateToStr(dataB, FunctionUnit.Calendar_END_MINUTE, FunctionUnit.UN_SHOW_CHINESE);
		strbuf.append(" select equipmentId,'" + dataStrA + "' as timea, ");
		strbuf.append(" max(temperature), min(temperature), format(avg(temperature),2),");
		strbuf.append(" max(humidity), min(humidity),\n format(avg(humidity),2) from trecord");
		strbuf.append(" where recTime between '" + dataStrA + "'and '" + dataStrB + "' group by equipmentId");
		strbuf.append("\n");
		return strbuf.toString();
	}

	/**
	 * @describe: 写文件
	 * @param fileName
	 *            文件名
	 * @param args
	 *            写入的内容
	 * @date:2009-11-7
	 */
	 

	private static void writeFile(String fileName, String args)
			throws IOException {
		FileWriter fw = new FileWriter(fileName);
		fw.write(args);
		fw.close();
	}

	// 获取下一天(24小时制)
	private static Date nextDay(String dateStr) throws Exception {
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateEx = sdFormat.parse(dateStr);
		calendar.setTime(dateEx);
		// 下一个天
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
		return calendar.getTime();
	}

	// 获取下一天(24小时制)
	private static Date nextHour(String dateStr) throws Exception {
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH");
		Date dateEx = sdFormat.parse(dateStr);
		calendar.setTime(dateEx);
		// 下一个天
		calendar.set(Calendar.HOUR_OF_DAY,
				calendar.get(Calendar.HOUR_OF_DAY) + 1);
		return calendar.getTime();
	}

}

