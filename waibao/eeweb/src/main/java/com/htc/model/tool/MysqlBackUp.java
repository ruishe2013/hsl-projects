package com.htc.model.tool;

import java.lang.Runtime;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @ Mysql.java
 * ���� : mysql ����-��ԭ����.
 * ע������ : ��ʱ����
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
public class MysqlBackUp {

	private static Log log = LogFactory.getLog(MysqlBackUp.class);
	
	/*-------------------------------------------mysql���ݿⱸ��  mysql ����
	ȫ������
	mysqldump htcweb -h localhost -uroot -padmin --opt --character-sets-dir=utf8 > D:/aaaaa.txt
	ֻ���ݽṹ����-d
	mysqldump -d htcweb -h localhost -uroot -padmin --opt --character-sets-dir=utf8 > D:/bbbbb.txt
	ֻ���ݼ�¼����-t
	mysqldump -t htcweb -h localhost -uroot -padmin --opt --character-sets-dir=utf8 > D:/ccccc.txt
	����xml��ʽ���� -X(��д)
	mysqldump -X htcweb -h localhost -uroot -padmin --opt --character-sets-dir=utf8 > D:/ddddd.xml
	mysqldump -d -X htcweb -h localhost -uroot -padmin --opt --character-sets-dir=utf8 > D:/ddddd.xml
	mysqldump -t -X htcweb -h localhost -uroot -padmin --opt --character-sets-dir=utf8 > D:/ddddd.xml
	----------------------------------------------*/
	
	/*-------------------------------------------mysql���ݿ⵼��  mysql ����
	mysql -uroot -padmin -h localhost --default-character-set=gb2312 htcweb < d:/struts.txt
	mysql -uroot -padmin -h localhost --default-character-set=gb2312 htcweb < d:/txt.txt
	----------------------------------------------*/
	
	/**
	 * @describe: ���ݼ���һ��sql�ļ��Ƿ�����������ļ��õ�һ���жϷ�����
	 * 			  �Ѹ�sql�ļ��ֱ��ü��±���ultra edit��, ������������ľ�����û������, 
	 * 			  ����������������Դ�ļ�(����sql�ļ��ı����ʽ���, Ҳ����db�ı����ʽ���)
	 * @describe: mysql-�����ݿⱸ�ݵ�ָ���ļ�
	 * 			��: "mysqldump htcweb -uroot -padmin tuser > d:/atest.txt";
	 * @param host ��ԭ��mysql���ڵ�Ip��ַ.���ؿ�����: localhost.
	 * @param DBName ������
	 * @param name ���ݿ������û���
	 * @param key  ���ݿ���������
	 * @param table ָ��Ҫ�����ı���.
	 * 				���Ҫ����ȫ�����ݿ�, ��table = "";
	 *				���Ҫ����ָ��ļ�����,���ÿո����,��: table ="tuser tpower"; 						
	 * @param file Ҫ������ļ�·��,�����ļ���
	 * @param append ���ӹ���. ��: append="-t"
	 * 				����Ϊ��,ȫ��.
	 * 				��-d, ֻ���ݽṹ.
	 * 				��-t, ֻ���ݼ�¼.
	 * @date:2009-11-6
	 */
	public static void ReStoreToFile_Case_A(String host, String DBName, String name, String key,
			String table, String file, String append) {
		
		// test�Ǳ��� ����û������������û��-p����root��� -p ����
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
			System.out.println("ReStoreToFile_Case_A - ���ݳɹ�");
		} catch (IOException e) {
			log.error("ReStoreToFile_Case_A����ʧ��:" + e.getMessage()+e.toString());
			System.out.println("ReStoreToFile_Case_A - ����ʧ��");
		}		
		
	}
	
	/**
	 * @describe: ���ݼ���һ��sql�ļ��Ƿ�����������ļ��õ�һ���жϷ�����
	 * 			  �Ѹ�sql�ļ��ֱ��ü��±���ultra edit��, ������������ľ�����û������, 
	 * 			  ����������������Դ�ļ�(����sql�ļ��ı����ʽ���, Ҳ����db�ı����ʽ���)
	 * @describe: mysql-�����ݿⱸ�ݵ�ָ���ļ�.
	 * 			��: "mysqldump htcweb -uroot -padmin tuser > d:/atest.txt";
	 * @param host ��ԭ��mysql���ڵ�Ip��ַ.���ؿ�����: localhost.
	 * @param DBName ������
	 * @param name ���ݿ������û���
	 * @param key  ���ݿ���������
	 * @param table ָ��Ҫ�����ı���.
	 * 				���Ҫ����ȫ�����ݿ�, ��table = "";
	 *				���Ҫ����ָ��ļ�����,���ÿո����,��: table ="tuser tpower"; 						
	 * @param file Ҫ������ļ�·��,�����ļ���
	 * @param append ���ӹ���. ��: append="-t"
	 * 				����Ϊ��,ȫ��.
	 *				��-d, ֻ���ݽṹ.
	 * 				��-t, ֻ���ݼ�¼.
	 * @date:2009-11-6
	 */
	public static void ReStoreToFile_Case_B(String host, String DBName, String name, String key,
			String table, String file, String append) {
		
		try {
			Runtime rt = Runtime.getRuntime();
			
			String command = "mysqldump " + append + " -h " + host + " " + DBName+ " -u" + name + " -p" + key 
			+ " --opt --character-sets-dir=utf8 " + table;

			// ���õ�������Ϊutf8
			//Process child = rt.exec("mysqldump htcweb -h localhost -uroot -padmin --opt --set-charset=utf8");
			Process child = rt.exec(command);
			
			// �ѽ���ִ���еĿ���̨�����Ϣд��.sql�ļ����������˱����ļ���
			// ע��������Կ���̨��Ϣ���ж�������ᵼ�½��̶����޷�����
			// ����̨�������Ϣ��Ϊ������
			InputStream in = child.getInputStream();
			// �������������Ϊutf8.���������utf8,��������ж����������
			InputStreamReader xx = new InputStreamReader(in, "utf8");

			StringBuffer sb = new StringBuffer("");
			String inStr;
			String outStr;
			// ��Ͽ���̨�����Ϣ�ַ���
			BufferedReader br = new BufferedReader(xx);
			while ((inStr = br.readLine()) != null) {
				sb.append(inStr + "\r\n");
			}
			outStr = sb.toString();

			// Ҫ�����������õ�sqlĿ���ļ���
			FileOutputStream fout = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fout, "utf8");
			writer.write(outStr);
			// ע����������û��巽ʽд���ļ��Ļ����ᵼ���������룬��flush()��������Ա���
			writer.flush();

			// �����ǹر����������
			in.close();
			xx.close();
			br.close();
			writer.close();
			fout.close();
			System.out.println("ReStoreToFile_Case_B - ���ݳɹ�");
		} catch (Exception e) {
			log.error("ReStoreToFile_Case_B - ����ʧ��:" + e.getMessage()+e.toString());
			System.out.println("ReStoreToFile_Case_B - ����ʧ��");
		}

	}

	/**
	 * @describe:	mysql-��ָ���ļ���ԭ���ݿ�
	 * @param host ��ԭ��mysql���ڵ�Ip
	 * @param DBName Ҫ��ԭ�����ݿ���
	 * @param name	���ݿ������û���
	 * @param key	���ݿ���������
	 * @param file ָ���ļ�·��,�����ļ���
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
			System.out.println("BackFromFile_Case_A - ��ԭ�ɹ�");
		} catch (IOException e) {
			log.error("BackFromFile_Case_A - ��ԭʧ��:" + e.getMessage()+e.toString());
			System.out.println("BackFromFile_Case_A - ��ԭʧ��");
		}
	}
	
	
	public static void BackFromFile_Case_B(String host, String DBName, String name, String key,
			String file) {
		try {

			// ���� mysql �� cmd:
			String command = "mysql -h " + host + " -u" + name + " -p" + key + 
				" --default-character-set=utf8 " + DBName;
			
			//Process child = rt.exec("mysql htcweb -h localhost -uroot -padmin --default-character-set=utf8");
			Runtime rt = Runtime.getRuntime();
			Process child = rt.exec(command);
			
			// ����̨��������Ϣ��Ϊ�����
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
			// ע����������û��巽ʽд���ļ��Ļ����ᵼ���������룬��flush()��������Ա���
			writer.flush();
			// �����ǹر����������
			out.close();
			br.close();
			writer.close();
			System.out.println("BackFromFile_Case_A - ��ԭ�ɹ�");
		} catch (Exception e) {
			log.error("BackFromFile_Case_B - ��ԭʧ��:" + e.getMessage()+e.toString());
			System.out.println("BackFromFile_Case_B - ��ԭʧ��");
		}

	}

/*	public static void main(String[] ages) {
		long start = System.currentTimeMillis();
		System.out.println("start...");
	
		// ���ݵ��ļ�
//		MysqlBackUp.ReStoreToFile_Case_A("localhost", "htcweb","root","admin","tuser tpower","d:/test_A.txt", "");
//		MysqlBackUp.ReStoreToFile_Case_B("localhost", "htcweb","root","admin","tuser tpower","d:/test_B.txt", "");
//		MysqlBackUp.ReStoreToFile_Case_A("localhost", "htcweb","root","admin","","d:/Case_A_ALl.txt", "");
//		MysqlBackUp.ReStoreToFile_Case_B("localhost", "htcweb","root","admin","","d:/Case_B_ALL.txt", "");
//		MysqlBackUp.ReStoreToFile_Case_A("localhost", "htcweb","root","admin","","d:/Case_A_T.txt", "-t");
//		MysqlBackUp.ReStoreToFile_Case_B("localhost", "htcweb","root","admin","","d:/Case_B_T.txt", "-t");
//		MysqlBackUp.ReStoreToFile_Case_A("localhost", "htcweb","root","admin","","d:/Case_A_D.txt", "-d");
//		MysqlBackUp.ReStoreToFile_Case_B("localhost", "htcweb","root","admin","","d:/Case_B_D.txt", "-d");
		
		//���ļ���ԭ
//		MysqlBackUp.BackFromFile_Case_A("127.0.0.1", "htcweb", "root", "admin", "d:/Case_A_ALl.txt");
//		MysqlBackUp.BackFromFile_Case_B("127.0.0.1", "htcweb", "root", "admin", "d:/Case_B_ALL.txt");
			
		start = System.currentTimeMillis() - start;
		System.out.println("����:" + start + "����");
		System.out.println("/** OK! /");
	}*/

}
