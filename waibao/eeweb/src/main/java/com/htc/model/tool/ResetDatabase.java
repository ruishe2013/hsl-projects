package com.htc.model.tool;

import java.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;

/**
 * @ ResetDatabase.java
 * 作用 : 把数据库恢复到出厂设置.
 * 注意事项 : 会清空数据库,慎用.
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class ResetDatabase {
	
	private static Log log = LogFactory.getLog(ResetDatabase.class);
	/**
	 * @describe : 	恢复数据库到出厂设置
	 * 				1: 增加用户,并设置权限
	 *  			2: 创建数据库
	 *  			3: 加载系统需要的默认数据到数据库 
	 * @param type:	
	 * @date:2009-11-5
	 */
	private static String url = "jdbc:mysql://127.0.0.1:3306/mysql?characterEncoding=GBK";
	private static String driver = "com.mysql.jdbc.Driver";
	private static String username = "root";
	private static String password = "admin";
	
	public static boolean resetData() {
		boolean rsbool = true;
		Connection conn = null;
		Statement stmt = null;
		try {
			String sql = "grant all privileges on *.* to yanghtcwebzl@localhost identified by 'yanghtcwebzl'";//创建一个数据库用户
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url, username,password);
			
			// 创建一个数据库用户
			try {
				stmt = conn.createStatement();
				stmt.executeQuery(sql);
			} catch (Exception e) {
				log.error("创建数据库用户htcweb出错:" + e.getMessage()+e.toString());
				rsbool = false;				
			}
			
			// 创建默认数据库
			if (rsbool){
				try {
					ScriptRunner runner = new ScriptRunner(conn, false, false);
					runner.setErrorLogWriter(null);
					runner.setLogWriter(null);
					// 创建数据库
					runner.runScript(Resources.getResourceAsReader("ddl/mysql/htcweb-mysql-schema.sql"));
					// 加载系统需要的默认数据到数据库 
					runner.runScript(Resources.getResourceAsReader("ddl/mysql/htcweb-mysql-dataload.sql"));
				} catch (Exception e) {
					log.error("初始化数据库时出错-执行SQL语句出错:" + e.getMessage()+e.toString());
					rsbool = false;
				}
			}
		} catch (Exception e) {
			log.error("连接数据库出错:" + e.getMessage()+e.toString());
			rsbool = false;
		}finally{
			try {
				if (stmt != null) {stmt.close();}
				if (conn != null){conn.close();}	
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rsbool;
	}
	
	// 判断数据是否存在
	/**
	 * @describe:	判断数据是否存在
	 * @return: true:数据库存在	false:数据库不存在
	 * @date:2009-12-7
	 */
	public static boolean checkDBExist() {
		boolean rsbool = false;
		
		try {
			String sql = "SELECT a.SCHEMA_NAME FROM information_schema.SCHEMATA as a where a.SCHEMA_NAME = 'htcweb'";
			
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url, username,password);
			Statement stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(sql);
			try {
				// 判断数据库是否存在,不存在才初始化数据库
				if (res.next()){
					rsbool = true;
				}
			} catch (Exception e) {
				log.error("判断数据库是否存在时出错-执行SQL语句:" + e.getMessage()+e.toString());
			} finally {
				if (res != null){res.close();}
				if (stmt != null) {stmt.close();}
				if (conn != null){conn.close();}
			}
		} catch (Exception e) {
			log.error("连接数据库出错:" + e.getMessage()+e.toString());
		}		
		return rsbool;
	}
	
	/**
	 * @describe: 读取数据库当前状态	
	 * @date:2010-1-4
	 */
	public static void readDBStatus() {
		try {
			String sql = "show innodb status";
			Class.forName(driver).newInstance();
			Connection conn = DriverManager.getConnection(url, username,password);
			Statement stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery(sql);
			try {
				// 判断数据库是否存在,不存在才初始化数据库
		      while(res.next()){ 
		          System.out.println(res.getString(3));
		          log.error(res.getString(1));
		      } 
			} catch (Exception e) {
				log.error("判断数据库-执行Status SQL语句:" + e.getMessage()+e.toString());
			} finally {
				if (res != null){res.close();}
				if (stmt != null) {stmt.close();}
				if (conn != null){conn.close();}
			}
		} catch (Exception e) {
			log.error("连接数据库出错:" + e.getMessage()+e.toString());
		}
		
		try {
			int total = (int)Runtime.getRuntime().totalMemory()/1024; 
			int free = (int)Runtime.getRuntime().freeMemory()/1024;
			String str = "当前使用内存:" + free + "/" +total;
			log.error(str);
		} catch (Exception e){}
	}
	
//	public static void main(String[] args){
//		if (resetData()){
//			System.out.println("数据库初始化完成...");
//		}else{
//			System.out.println("数据库初始化失败...");
//		}
//	}
	
}

