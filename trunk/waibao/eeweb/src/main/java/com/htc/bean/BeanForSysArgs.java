package com.htc.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @ BeanForSysArgs.java
 * 作用 : 系统属性的key值
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-25     YANGZHONLI       create
 */
/**
 * @ BeanForSysArgs.java
 * 作用 : 
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-25     YANGZHONLI       create
 */
public class BeanForSysArgs implements Serializable {

	private static final long serialVersionUID = -7933711967143068506L;
	
	/**
	 *  公司的名称
	 */
	public static String COMPANY_NAME = "company_name";	 
	/**
	 * 报警时播放文件
	 */
	public static String ALARM_PLAYFILE = "alarm_playfile"; 
	/**
	 * 短信报警是否打开(1:关闭 2:打开)(默认关闭)
	 */
	public static String OPEN_SHORT_MESSAGE = "open_short_message"; 
	/**
	 * 声卡报警是否打开(1:关闭 2:打开)(默认打开)
	 */
	public static String OPEN_PCSOUND = "open_pcsound";	 
	/**
	 * 访问数据的刷新间隔:秒
	 */
	public static String DATA_FLASHTIME = "data_flashtime";	 
	/**
	 * 上限颜色
	 */
	public static String HIGH_COLORDEF = "high_colordef"; 
	/**
	 * 正常颜色	
	 */
	public static String NORMAL_COLORDEF = "normal_colordef";		
	/**
	 * 下限颜色
	 */
	public static String LOW_COLORDEF = "low_colordef"; 
	/**
	 * 曲线显示时的y轴上下增加的偏移量
	 */
	public static String Y_AXES_ADDVALUE = "y_axes_addvalue"; 		
	/**
	 * 温度显示格式(1:摄氏 2:华氏)
	 */
	public static String TEMP_SHOW_TYPE = "temp_show_type"; 		

	//*************************************** 选择地址时,点数限制
	//********简称(即时画面:bar 实时曲线:line 历史数据:table 历史曲线:flash 报警数据:alarm)********
	/**
	 * 选择地址时-总览画面最多能选择的仪器个数
	 */
	public static String MAX_SELECT_COUNT_BAR = "max_select_count_bar";	
	/**
	 * 选择地址时-即时画面最多能选择的点数(flash显示)
	 */
	public static String MAX_SELECT_COUNT_LINE = "max_select_count_line";
	/**
	 * 选择地址时-历史数据画面最多能选择的点数
	 */
	public static String MAX_SELECT_COUNT_TABLE = "max_select_count_table";
	/**
	 * 选择地址时-历史曲线画面最多能选择的点数(flash显示)
	 */
	public static String MAX_SELECT_COUNT_FLASH = "max_select_count_flash";
	/**
	 * 选择地址时-报警数据画面最多能选择的点数
	 */
	public static String MAX_SELECT_COUNT_ALARM = "max_select_count_alarm";	
	
	// ***************************************实时曲线显示flash时,能显示的最多点数
	/**
	 * 实时曲线显示flash时,能显示的最多的数据条数
	 */
	public static String MAX_RS_COUNT_LINE = "max_rs_count_line";
	// *************************************** 搜索结果时,行数限制
	/**
	 * 搜索结果行数限制-历史数据查询时最大行数限制-即时数据
	 */
	public static String MAX_RS_COUNT_TABLE_NOW = "max_rs_count_table_now";	
	/**
	 * 搜索结果行数限制-历史数据查询时最大行数限制-日报
	 */
	public static String MAX_RS_COUNT_TABLE_DAY = "max_rs_count_table_day";	
	/**
	 * 搜索结果行数限制-历史数据查询时最大行数限制-月报
	 */
	public static String MAX_RS_COUNT_TABLE_MONTH = "max_rs_count_table_month";	
	/**
	 * 搜索结果行数限制-历史曲线查询时最大行数限制-即时数据
	 */
	public static String MAX_RS_COUNT_FLASH_NOW = "max_rs_count_flash_now";	 
	/**
	 * 搜索结果行数限制-历史曲线查询时最大行数限制-日报
	 */
	public static String MAX_RS_COUNT_FLASH_DAY = "max_rs_count_flash_day";	 
	/**
	 * 搜索结果行数限制-历史曲线查询时最大行数限制-月报
	 */
	public static String MAX_RS_COUNT_FLASH_MONTH = "max_rs_count_flash_month";	 
	/**
	 * 搜索结果行数限制-报警数据查询时最大行数限制
	 */
	public static String MAX_RS_COUNT_ALARM = "max_rs_count_alarm";	 
	
	
	// 串口采样率
	/**
	 * 串口采样率
	 */
	public static String BAUDRATE_NUMBER = "baudrate_number";
	/**
	 * 历史列表备份路径
	 */
	public static String BACKUP_PATH = "backup_path";
	/**
	 * Access存储是否打开(1:关闭 2:打开)(默认关闭)
	 */
	public static String OPEN_ACCESS_STORE = "open_access_store";
	/**
	 *  串口通信错误时, 重发的次数(默认5次)
	 */
	public static String SERI_RETRY_TIME = "seri_retry_time";
	/**
	 * 串口通信时,一次通信附加的毫秒数(主要用在和别的硬件兼容,设当的调节这个值,减小丢帧的频率)(默认100)
	 */
	public static String SERI_ADDITION_TIME = "seri_addition_time";
	/**
	 * 手机通讯时,短信中心的号码
	 */
	public static String SMS_CENTER_NUMBER = "sms_center_number";
	/**
	 *短信模块cnmi指令差异,主要数据直接发到串口.其命令方式是at+cnmi=2,2还是at+cnmi=2,1
	 *type=1为:at+cnmi=2,2(默认) 
	 *type=2为:at+cnmi=2,1
	 */
	public static String SMS_STORE_TYPE = "sms_store_type";
	
	/**
	 * @describe: 获取默认设置
	 * @date:2009-11-25
	 */
	public static Map<String, String> getDefaultSysArgsMap(){
		Map<String, String> defaultArgs = new HashMap<String, String>();
		defaultArgs.put(COMPANY_NAME, "eeweb");
		defaultArgs.put(ALARM_PLAYFILE, "alarmHtc1.mp3");
		defaultArgs.put(OPEN_SHORT_MESSAGE, "1");		//(1:关闭 2:打开)
		defaultArgs.put(OPEN_PCSOUND, "2");				// (1:关闭 2:打开)
		defaultArgs.put(DATA_FLASHTIME, "10");			// 秒
		defaultArgs.put(HIGH_COLORDEF, "#ff0000");
		defaultArgs.put(NORMAL_COLORDEF, "#0000ff");
		defaultArgs.put(LOW_COLORDEF, "#ff00ff");
		defaultArgs.put(Y_AXES_ADDVALUE, "5");			// y轴浮动刻度加5 -- 现在不用
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
		defaultArgs.put(OPEN_ACCESS_STORE, "1");		// (1:关闭 2:打开)
		defaultArgs.put(SERI_RETRY_TIME, "5");
		defaultArgs.put(SERI_ADDITION_TIME, "100");
		defaultArgs.put(SMS_CENTER_NUMBER, "13800571500");
		defaultArgs.put(SMS_STORE_TYPE, "1");
		return defaultArgs;
	}
	
}
