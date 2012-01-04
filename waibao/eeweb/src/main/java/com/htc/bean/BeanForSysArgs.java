package com.htc.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @ BeanForSysArgs.java
 * ���� : ϵͳ���Ե�keyֵ
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-25     YANGZHONLI       create
 */
/**
 * @ BeanForSysArgs.java
 * ���� : 
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-25     YANGZHONLI       create
 */
public class BeanForSysArgs implements Serializable {

	private static final long serialVersionUID = -7933711967143068506L;
	
	/**
	 *  ��˾������
	 */
	public static String COMPANY_NAME = "company_name";	 
	/**
	 * ����ʱ�����ļ�
	 */
	public static String ALARM_PLAYFILE = "alarm_playfile"; 
	/**
	 * ���ű����Ƿ��(1:�ر� 2:��)(Ĭ�Ϲر�)
	 */
	public static String OPEN_SHORT_MESSAGE = "open_short_message"; 
	/**
	 * ���������Ƿ��(1:�ر� 2:��)(Ĭ�ϴ�)
	 */
	public static String OPEN_PCSOUND = "open_pcsound";	 
	/**
	 * �������ݵ�ˢ�¼��:��
	 */
	public static String DATA_FLASHTIME = "data_flashtime";	 
	/**
	 * ������ɫ
	 */
	public static String HIGH_COLORDEF = "high_colordef"; 
	/**
	 * ������ɫ	
	 */
	public static String NORMAL_COLORDEF = "normal_colordef";		
	/**
	 * ������ɫ
	 */
	public static String LOW_COLORDEF = "low_colordef"; 
	/**
	 * ������ʾʱ��y���������ӵ�ƫ����
	 */
	public static String Y_AXES_ADDVALUE = "y_axes_addvalue"; 		
	/**
	 * �¶���ʾ��ʽ(1:���� 2:����)
	 */
	public static String TEMP_SHOW_TYPE = "temp_show_type"; 		

	//*************************************** ѡ���ַʱ,��������
	//********���(��ʱ����:bar ʵʱ����:line ��ʷ����:table ��ʷ����:flash ��������:alarm)********
	/**
	 * ѡ���ַʱ-�������������ѡ�����������
	 */
	public static String MAX_SELECT_COUNT_BAR = "max_select_count_bar";	
	/**
	 * ѡ���ַʱ-��ʱ���������ѡ��ĵ���(flash��ʾ)
	 */
	public static String MAX_SELECT_COUNT_LINE = "max_select_count_line";
	/**
	 * ѡ���ַʱ-��ʷ���ݻ��������ѡ��ĵ���
	 */
	public static String MAX_SELECT_COUNT_TABLE = "max_select_count_table";
	/**
	 * ѡ���ַʱ-��ʷ���߻��������ѡ��ĵ���(flash��ʾ)
	 */
	public static String MAX_SELECT_COUNT_FLASH = "max_select_count_flash";
	/**
	 * ѡ���ַʱ-�������ݻ��������ѡ��ĵ���
	 */
	public static String MAX_SELECT_COUNT_ALARM = "max_select_count_alarm";	
	
	// ***************************************ʵʱ������ʾflashʱ,����ʾ��������
	/**
	 * ʵʱ������ʾflashʱ,����ʾ��������������
	 */
	public static String MAX_RS_COUNT_LINE = "max_rs_count_line";
	// *************************************** �������ʱ,��������
	/**
	 * ���������������-��ʷ���ݲ�ѯʱ�����������-��ʱ����
	 */
	public static String MAX_RS_COUNT_TABLE_NOW = "max_rs_count_table_now";	
	/**
	 * ���������������-��ʷ���ݲ�ѯʱ�����������-�ձ�
	 */
	public static String MAX_RS_COUNT_TABLE_DAY = "max_rs_count_table_day";	
	/**
	 * ���������������-��ʷ���ݲ�ѯʱ�����������-�±�
	 */
	public static String MAX_RS_COUNT_TABLE_MONTH = "max_rs_count_table_month";	
	/**
	 * ���������������-��ʷ���߲�ѯʱ�����������-��ʱ����
	 */
	public static String MAX_RS_COUNT_FLASH_NOW = "max_rs_count_flash_now";	 
	/**
	 * ���������������-��ʷ���߲�ѯʱ�����������-�ձ�
	 */
	public static String MAX_RS_COUNT_FLASH_DAY = "max_rs_count_flash_day";	 
	/**
	 * ���������������-��ʷ���߲�ѯʱ�����������-�±�
	 */
	public static String MAX_RS_COUNT_FLASH_MONTH = "max_rs_count_flash_month";	 
	/**
	 * ���������������-�������ݲ�ѯʱ�����������
	 */
	public static String MAX_RS_COUNT_ALARM = "max_rs_count_alarm";	 
	
	
	// ���ڲ�����
	/**
	 * ���ڲ�����
	 */
	public static String BAUDRATE_NUMBER = "baudrate_number";
	/**
	 * ��ʷ�б���·��
	 */
	public static String BACKUP_PATH = "backup_path";
	/**
	 * Access�洢�Ƿ��(1:�ر� 2:��)(Ĭ�Ϲر�)
	 */
	public static String OPEN_ACCESS_STORE = "open_access_store";
	/**
	 *  ����ͨ�Ŵ���ʱ, �ط��Ĵ���(Ĭ��5��)
	 */
	public static String SERI_RETRY_TIME = "seri_retry_time";
	/**
	 * ����ͨ��ʱ,һ��ͨ�Ÿ��ӵĺ�����(��Ҫ���ںͱ��Ӳ������,�赱�ĵ������ֵ,��С��֡��Ƶ��)(Ĭ��100)
	 */
	public static String SERI_ADDITION_TIME = "seri_addition_time";
	/**
	 * �ֻ�ͨѶʱ,�������ĵĺ���
	 */
	public static String SMS_CENTER_NUMBER = "sms_center_number";
	/**
	 *����ģ��cnmiָ�����,��Ҫ����ֱ�ӷ�������.�����ʽ��at+cnmi=2,2����at+cnmi=2,1
	 *type=1Ϊ:at+cnmi=2,2(Ĭ��) 
	 *type=2Ϊ:at+cnmi=2,1
	 */
	public static String SMS_STORE_TYPE = "sms_store_type";
	
	/**
	 * @describe: ��ȡĬ������
	 * @date:2009-11-25
	 */
	public static Map<String, String> getDefaultSysArgsMap(){
		Map<String, String> defaultArgs = new HashMap<String, String>();
		defaultArgs.put(COMPANY_NAME, "eeweb");
		defaultArgs.put(ALARM_PLAYFILE, "alarmHtc1.mp3");
		defaultArgs.put(OPEN_SHORT_MESSAGE, "1");		//(1:�ر� 2:��)
		defaultArgs.put(OPEN_PCSOUND, "2");				// (1:�ر� 2:��)
		defaultArgs.put(DATA_FLASHTIME, "10");			// ��
		defaultArgs.put(HIGH_COLORDEF, "#ff0000");
		defaultArgs.put(NORMAL_COLORDEF, "#0000ff");
		defaultArgs.put(LOW_COLORDEF, "#ff00ff");
		defaultArgs.put(Y_AXES_ADDVALUE, "5");			// y�ḡ���̶ȼ�5 -- ���ڲ���
		defaultArgs.put(TEMP_SHOW_TYPE, "1");
		defaultArgs.put(MAX_SELECT_COUNT_BAR, "50");
		defaultArgs.put(MAX_SELECT_COUNT_LINE, "5");
		defaultArgs.put(MAX_SELECT_COUNT_TABLE, "5");
		defaultArgs.put(MAX_SELECT_COUNT_FLASH, "5");
		defaultArgs.put(MAX_SELECT_COUNT_ALARM, "50");
		defaultArgs.put(MAX_RS_COUNT_LINE, "100");
		defaultArgs.put(MAX_RS_COUNT_TABLE_NOW, "2000");
		defaultArgs.put(MAX_RS_COUNT_TABLE_DAY, "1000");
		defaultArgs.put(MAX_RS_COUNT_TABLE_MONTH, "60");
		defaultArgs.put(MAX_RS_COUNT_FLASH_NOW, "2000");
		defaultArgs.put(MAX_RS_COUNT_FLASH_DAY, "1000");
		defaultArgs.put(MAX_RS_COUNT_FLASH_MONTH, "60");
		defaultArgs.put(MAX_RS_COUNT_ALARM, "100");
		defaultArgs.put(BAUDRATE_NUMBER, "9600");
		defaultArgs.put(BACKUP_PATH, "d:/htc/backup");
		defaultArgs.put(OPEN_ACCESS_STORE, "1");		// (1:�ر� 2:��)
		defaultArgs.put(SERI_RETRY_TIME, "5");
		defaultArgs.put(SERI_ADDITION_TIME, "100");
		defaultArgs.put(SMS_CENTER_NUMBER, "13800571500");
		defaultArgs.put(SMS_STORE_TYPE, "1");
		return defaultArgs;
	}
	
}
