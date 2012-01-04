package com.htc.model.tool;

import java.sql.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;

/**
 * @ ResetDatabase.java
 * ���� : �����ݿ�ָ�����������.
 * ע������ : ��������ݿ�,����.
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class ResetDatabase {
	
	private static Log log = LogFactory.getLog(ResetDatabase.class);
	/**
	 * @describe : 	�ָ����ݿ⵽��������
	 * 				1: �����û�,������Ȩ��
	 *  			2: �������ݿ�
	 *  			3: ����ϵͳ��Ҫ��Ĭ�����ݵ����ݿ� 
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
			String sql = "grant all privileges on *.* to yanghtcwebzl@localhost identified by 'yanghtcwebzl'";//����һ�����ݿ��û�
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url, username,password);
			
			// ����һ�����ݿ��û�
			try {
				stmt = conn.createStatement();
				stmt.executeQuery(sql);
			} catch (Exception e) {
				log.error("�������ݿ��û�htcweb����:" + e.getMessage()+e.toString());
				rsbool = false;				
			}
			
			// ����Ĭ�����ݿ�
			if (rsbool){
				try {
					ScriptRunner runner = new ScriptRunner(conn, false, false);
					runner.setErrorLogWriter(null);
					runner.setLogWriter(null);
					// �������ݿ�
					runner.runScript(Resources.getResourceAsReader("ddl/mysql/htcweb-mysql-schema.sql"));
					// ����ϵͳ��Ҫ��Ĭ�����ݵ����ݿ� 
					runner.runScript(Resources.getResourceAsReader("ddl/mysql/htcweb-mysql-dataload.sql"));
				} catch (Exception e) {
					log.error("��ʼ�����ݿ�ʱ����-ִ��SQL������:" + e.getMessage()+e.toString());
					rsbool = false;
				}
			}
		} catch (Exception e) {
			log.error("�������ݿ����:" + e.getMessage()+e.toString());
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
	
	// �ж������Ƿ����
	/**
	 * @describe:	�ж������Ƿ����
	 * @return: true:���ݿ����	false:���ݿⲻ����
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
				// �ж����ݿ��Ƿ����,�����ڲų�ʼ�����ݿ�
				if (res.next()){
					rsbool = true;
				}
			} catch (Exception e) {
				log.error("�ж����ݿ��Ƿ����ʱ����-ִ��SQL���:" + e.getMessage()+e.toString());
			} finally {
				if (res != null){res.close();}
				if (stmt != null) {stmt.close();}
				if (conn != null){conn.close();}
			}
		} catch (Exception e) {
			log.error("�������ݿ����:" + e.getMessage()+e.toString());
		}		
		return rsbool;
	}
	
	/**
	 * @describe: ��ȡ���ݿ⵱ǰ״̬	
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
				// �ж����ݿ��Ƿ����,�����ڲų�ʼ�����ݿ�
		      while(res.next()){ 
		          System.out.println(res.getString(3));
		          log.error(res.getString(1));
		      } 
			} catch (Exception e) {
				log.error("�ж����ݿ�-ִ��Status SQL���:" + e.getMessage()+e.toString());
			} finally {
				if (res != null){res.close();}
				if (stmt != null) {stmt.close();}
				if (conn != null){conn.close();}
			}
		} catch (Exception e) {
			log.error("�������ݿ����:" + e.getMessage()+e.toString());
		}
		
		try {
			int total = (int)Runtime.getRuntime().totalMemory()/1024; 
			int free = (int)Runtime.getRuntime().freeMemory()/1024;
			String str = "��ǰʹ���ڴ�:" + free + "/" +total;
			log.error(str);
		} catch (Exception e){}
	}
	
//	public static void main(String[] args){
//		if (resetData()){
//			System.out.println("���ݿ��ʼ�����...");
//		}else{
//			System.out.println("���ݿ��ʼ��ʧ��...");
//		}
//	}
	
}

