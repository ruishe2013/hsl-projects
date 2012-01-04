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
 * @ DateTest.java ���� : java ���� ѧϰ ����. ע������ : �� VERSION DATE BY CHANGE/COMMENT
 * 1.0 2009-11-6 YANGZHONLI create
 */
public class JavaTest {

	public static void main(String[] args) throws Exception {

		/*
		 * long start; System.out.println("start...");
		 * 
		 * // ���� ʱ�书�� start = System.currentTimeMillis(); testTime();
		 * System.out.println("���� ʱ�书�� ����:" + (System.currentTimeMillis() -
		 * start) + "����");
		 * 
		 * // ������htcweb�б�thisrecorddailay�ṹһ����SQL���. start =
		 * System.currentTimeMillis(); createSqlMonth(80000,"2006-01-01 0:0:0",
		 * 2 , 1, "table"); System.out.println("���� createSqlMonth SQL ����:" +
		 * (System.currentTimeMillis() - start) + "����");
		 * 
		 * // ����unionSQL��� start = System.currentTimeMillis();
		 * createSqlUnion("2009-09-07 10:38:50"
		 * ,"2009-09-11 10:38:50",5,"unionSQL");
		 * System.out.println("���� createSqlUnion  ����:" +
		 * (System.currentTimeMillis() - start) + "����");
		 * 
		 * System.out.println("/** OK! /");
		 */
		
		// �����쳣ʱ�᲻�᷵��ֵ�����
		// System.out.println(tryCatchTest());
		// System.out.println(FunctionUnit.FLOAT_DATA_FORMAT.format(10.223));
		// ��ȡϵͳ�Ļ�������
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
//        String subStr = "c:/htc/backup/2010��/1��/20��/2010��01��20��(δ����)(2_11).xls";
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
        
//        int currentMinute = FunctionUnit.getCurTime(Calendar.MINUTE); // ��ǰʱ��ķ�����
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
	 * @describe: ��ȡϵͳ�Ļ�������
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
	 * @describe: ��ȡϵͳ�Ļ�������
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
	 * @describe: �����쳣ʱ�᲻�᷵��ֵ�����.
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
	 * @describe: Calendar ʱ�书��
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
	 * @describe: ������htcweb�б�thisrecorddailay�ṹһ����SQL���.����ѡ����ܻ��߲�����
	 * @param len
	 *            ��Ҫ����SQL���ĳ���
	 * @param dateStr
	 *            ��ʼʱ��(�ַ���)
	 * @param type
	 *            1: Сʱ��Ϊ��� 2: ����Ϊ���
	 * @param encode
	 *            SQL����費��Ҫ����. 1: ���� 2: ������
	 * @param table
	 *            insert�е� ����, �����ڸ�Ŀ¼����,����"table".txt���ļ�.
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
	 * @describe: ����unionSQL���
	 * @param startData
	 *            ��ʼʱ��(�ַ���)
	 * @param endData
	 *            ����ʱ��(�ַ���)
	 * @param interval
	 *            ���ʱ��
	 * @param fileName
	 *            ���ɵ�SQL��Ҫ������ļ���
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
	 * @describe: unionSQL ��䵥��
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
	 * @describe: д�ļ�
	 * @param fileName
	 *            �ļ���
	 * @param args
	 *            д�������
	 * @date:2009-11-7
	 */
	 

	private static void writeFile(String fileName, String args)
			throws IOException {
		FileWriter fw = new FileWriter(fileName);
		fw.write(args);
		fw.close();
	}

	// ��ȡ��һ��(24Сʱ��)
	private static Date nextDay(String dateStr) throws Exception {
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateEx = sdFormat.parse(dateStr);
		calendar.setTime(dateEx);
		// ��һ����
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
		return calendar.getTime();
	}

	// ��ȡ��һ��(24Сʱ��)
	private static Date nextHour(String dateStr) throws Exception {
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH");
		Date dateEx = sdFormat.parse(dateStr);
		calendar.setTime(dateEx);
		// ��һ����
		calendar.set(Calendar.HOUR_OF_DAY,
				calendar.get(Calendar.HOUR_OF_DAY) + 1);
		return calendar.getTime();
	}

}

