package com.htc.model.tool;

import java.lang.Runtime;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @ Mysql.java
 * 作用 : mysql 备份-还原功能.
 * 注意事项 : 暂时不用
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
public class MysqlBackUp {

	private static Log log = LogFactory.getLog(MysqlBackUp.class);
	
	/*-------------------------------------------mysql数据库备份  mysql 命令
	全部备份
	mysqldump htcweb -h localhost -uroot -padmin --opt --character-sets-dir=utf8 > D:/aaaaa.txt
	只备份结构，用-d
	mysqldump -d htcweb -h localhost -uroot -padmin --opt --character-sets-dir=utf8 > D:/bbbbb.txt
	只备份记录，用-t
	mysqldump -t htcweb -h localhost -uroot -padmin --opt --character-sets-dir=utf8 > D:/ccccc.txt
	导出xml格式，用 -X(大写)
	mysqldump -X htcweb -h localhost -uroot -padmin --opt --character-sets-dir=utf8 > D:/ddddd.xml
	mysqldump -d -X htcweb -h localhost -uroot -padmin --opt --character-sets-dir=utf8 > D:/ddddd.xml
	mysqldump -t -X htcweb -h localhost -uroot -padmin --opt --character-sets-dir=utf8 > D:/ddddd.xml
	----------------------------------------------*/
	
	/*-------------------------------------------mysql数据库导入  mysql 命令
	mysql -uroot -padmin -h localhost --default-character-set=gb2312 htcweb < d:/struts.txt
	mysql -uroot -padmin -h localhost --default-character-set=gb2312 htcweb < d:/txt.txt
	----------------------------------------------*/
	
	/**
	 * @describe: 备份检验一个sql文件是否可以做导入文件用的一个判断方法：
	 * 			  把该sql文件分别用记事本和ultra edit打开, 如果看到的中文均正常没有乱码, 
	 * 			  则可以用来做导入的源文件(不管sql文件的编码格式如何, 也不管db的编码格式如何)
	 * @describe: mysql-把数据库备份到指点文件
	 * 			如: "mysqldump htcweb -uroot -padmin tuser > d:/atest.txt";
	 * @param host 还原的mysql所在的Ip地址.本地可以是: localhost.
	 * @param DBName 数据名
	 * @param name 数据库连接用户名
	 * @param key  数据库连接密码
	 * @param table 指定要导出的表名.
	 * 				如果要导出全部数据库, 则table = "";
	 *				如果要导出指点的几个表,则用空格隔开,如: table ="tuser tpower"; 						
	 * @param file 要输出从文件路径,包括文件名
	 * @param append 附加功能. 如: append="-t"
	 * 				可以为空,全部.
	 * 				用-d, 只备份结构.
	 * 				用-t, 只备份记录.
	 * @date:2009-11-6
	 */
	public static void ReStoreToFile_Case_A(String host, String DBName, String name, String key,
			String table, String file, String append) {
		
		// test是表名 这里没有用密码所以没有-p可在root后加 -p 密码
		// String command = "mysqldump htcweb -h loccalhost -uroot -padmin  --opt tuser > d:/atest.txt";
		String command = "mysqldump " + append + " -h " + host + " " + DBName+ " -u" + name + " -p" + key 
						+ " --opt --character-sets-dir=utf8 " + table +" > " + file;
		
		try {
			Process process = Runtime.getRuntime().exec("cmd /c " + command);
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line;
			while ((line = input.readLine()) != null)
				System.out.println(line);
			input.close();
			System.out.println("ReStoreToFile_Case_A - 备份成功");
		} catch (IOException e) {
			log.error("ReStoreToFile_Case_A备份失败:" + e.getMessage()+e.toString());
			System.out.println("ReStoreToFile_Case_A - 备份失败");
		}		
		
	}
	
	/**
	 * @describe: 备份检验一个sql文件是否可以做导入文件用的一个判断方法：
	 * 			  把该sql文件分别用记事本和ultra edit打开, 如果看到的中文均正常没有乱码, 
	 * 			  则可以用来做导入的源文件(不管sql文件的编码格式如何, 也不管db的编码格式如何)
	 * @describe: mysql-把数据库备份到指点文件.
	 * 			如: "mysqldump htcweb -uroot -padmin tuser > d:/atest.txt";
	 * @param host 还原的mysql所在的Ip地址.本地可以是: localhost.
	 * @param DBName 数据名
	 * @param name 数据库连接用户名
	 * @param key  数据库连接密码
	 * @param table 指定要导出的表名.
	 * 				如果要导出全部数据库, 则table = "";
	 *				如果要导出指点的几个表,则用空格隔开,如: table ="tuser tpower"; 						
	 * @param file 要输出从文件路径,包括文件名
	 * @param append 附加功能. 如: append="-t"
	 * 				可以为空,全部.
	 *				用-d, 只备份结构.
	 * 				用-t, 只备份记录.
	 * @date:2009-11-6
	 */
	public static void ReStoreToFile_Case_B(String host, String DBName, String name, String key,
			String table, String file, String append) {
		
		try {
			Runtime rt = Runtime.getRuntime();
			
			String command = "mysqldump " + append + " -h " + host + " " + DBName+ " -u" + name + " -p" + key 
			+ " --opt --character-sets-dir=utf8 " + table;

			// 设置导出编码为utf8
			//Process child = rt.exec("mysqldump htcweb -h localhost -uroot -padmin --opt --set-charset=utf8");
			Process child = rt.exec(command);
			
			// 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。
			// 注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行
			// 控制台的输出信息作为输入流
			InputStream in = child.getInputStream();
			// 设置输出流编码为utf8.这里必须是utf8,否则从流中读入的是乱码
			InputStreamReader xx = new InputStreamReader(in, "utf8");

			StringBuffer sb = new StringBuffer("");
			String inStr;
			String outStr;
			// 组合控制台输出信息字符串
			BufferedReader br = new BufferedReader(xx);
			while ((inStr = br.readLine()) != null) {
				sb.append(inStr + "\r\n");
			}
			outStr = sb.toString();

			// 要用来做导入用的sql目标文件：
			FileOutputStream fout = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fout, "utf8");
			writer.write(outStr);
			// 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免
			writer.flush();

			// 别忘记关闭输入输出流
			in.close();
			xx.close();
			br.close();
			writer.close();
			fout.close();
			System.out.println("ReStoreToFile_Case_B - 备份成功");
		} catch (Exception e) {
			log.error("ReStoreToFile_Case_B - 备份失败:" + e.getMessage()+e.toString());
			System.out.println("ReStoreToFile_Case_B - 备份失败");
		}

	}

	/**
	 * @describe:	mysql-从指点文件还原数据库
	 * @param host 还原的mysql所在的Ip
	 * @param DBName 要还原的数据库名
	 * @param name	数据库连接用户名
	 * @param key	数据库连接密码
	 * @param file 指点文件路径,包括文件名
	 * @date:2009-11-6
	 */
	public static void BackFromFile_Case_A(String host, String DBName, String name, String key,
			String file){
		try {
			//mysql  -h localhost -uroot -padmin --default-character-set=utf8 htcweb < d:/txt.txt
			String command = "mysql -h " + host + " -u" + name + " -p" + key + 
				" --default-character-set=utf8 " + DBName + " < " + file;
			
			System.out.println(command);
			
			Runtime.getRuntime().exec("cmd /c " + command);
			System.out.println("BackFromFile_Case_A - 还原成功");
		} catch (IOException e) {
			log.error("BackFromFile_Case_A - 还原失败:" + e.getMessage()+e.toString());
			System.out.println("BackFromFile_Case_A - 还原失败");
		}
	}
	
	
	public static void BackFromFile_Case_B(String host, String DBName, String name, String key,
			String file) {
		try {

			// 调用 mysql 的 cmd:
			String command = "mysql -h " + host + " -u" + name + " -p" + key + 
				" --default-character-set=utf8 " + DBName;
			
			//Process child = rt.exec("mysql htcweb -h localhost -uroot -padmin --default-character-set=utf8");
			Runtime rt = Runtime.getRuntime();
			Process child = rt.exec(command);
			
			// 控制台的输入信息作为输出流
			StringBuffer sb = new StringBuffer("");
			String inStr;
			String outStr;
			OutputStream out = child.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "utf8"));
			while ((inStr = br.readLine()) != null) {
				sb.append(inStr + "\r\n");
			}
			outStr = sb.toString();

			OutputStreamWriter writer = new OutputStreamWriter(out, "utf8");
			writer.write(outStr);
			// 注：这里如果用缓冲方式写入文件的话，会导致中文乱码，用flush()方法则可以避免
			writer.flush();
			// 别忘记关闭输入输出流
			out.close();
			br.close();
			writer.close();
			System.out.println("BackFromFile_Case_A - 还原成功");
		} catch (Exception e) {
			log.error("BackFromFile_Case_B - 还原失败:" + e.getMessage()+e.toString());
			System.out.println("BackFromFile_Case_B - 还原失败");
		}

	}

/*	public static void main(String[] ages) {
		long start = System.currentTimeMillis();
		System.out.println("start...");
	
		// 备份到文件
//		MysqlBackUp.ReStoreToFile_Case_A("localhost", "htcweb","root","admin","tuser tpower","d:/test_A.txt", "");
//		MysqlBackUp.ReStoreToFile_Case_B("localhost", "htcweb","root","admin","tuser tpower","d:/test_B.txt", "");
//		MysqlBackUp.ReStoreToFile_Case_A("localhost", "htcweb","root","admin","","d:/Case_A_ALl.txt", "");
//		MysqlBackUp.ReStoreToFile_Case_B("localhost", "htcweb","root","admin","","d:/Case_B_ALL.txt", "");
//		MysqlBackUp.ReStoreToFile_Case_A("localhost", "htcweb","root","admin","","d:/Case_A_T.txt", "-t");
//		MysqlBackUp.ReStoreToFile_Case_B("localhost", "htcweb","root","admin","","d:/Case_B_T.txt", "-t");
//		MysqlBackUp.ReStoreToFile_Case_A("localhost", "htcweb","root","admin","","d:/Case_A_D.txt", "-d");
//		MysqlBackUp.ReStoreToFile_Case_B("localhost", "htcweb","root","admin","","d:/Case_B_D.txt", "-d");
		
		//从文件还原
//		MysqlBackUp.BackFromFile_Case_A("127.0.0.1", "htcweb", "root", "admin", "d:/Case_A_ALl.txt");
//		MysqlBackUp.BackFromFile_Case_B("127.0.0.1", "htcweb", "root", "admin", "d:/Case_B_ALL.txt");
			
		start = System.currentTimeMillis() - start;
		System.out.println("运行:" + start + "毫秒");
		System.out.println("/** OK! /");
	}*/

}
