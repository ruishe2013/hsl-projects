package com.htc.action;

import java.io.IOException;
import java.util.*;

import com.opensymphony.xwork2.*;
import com.htc.bean.BeanForSysArgs;
import com.htc.common.CommonDataUnit;
import com.ibatis.common.resources.Resources;

/**
 * @ AbstractAction.java
 * 作用 : 所有action类的父类.包含:
 * 		 1:厂家默认账号,struts2导航值;
 * 		 2:读取htcUnit.properties文件属性文件方法
 * 		 3:获取session名登陆名
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class AbstractAction extends ActionSupport {

	/**
	 * 厂家账户
	 */
	public String OEMNAME = "cqadmin"; 
	/**
	 * 保留账户不能登录
	 */
	public String OEM_NAME = "htc_htc_htc";	
	
	public static final String INDEX = "index";
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	public static final String LOGINOUT = "loginOut";
	public static final String LOGININ = "loginIn";
	public static final String SHOWORDER = "showOrder";
	
	/**
	 * 点新建的时候调用--不做任何事情
	 */
	public static final String PERCREATE = "percreate";
	/**
	 * 点修改的时候调用--返回目标对象
	 */
	public static final String PERUPDATE = "perupdate";
	/**
	 * 显示列表
	 */
	public static final String LISTTAG = "list";
	public static final String CREATETAG = "create";
	public static final String UPDATETAG = "update";
	
	public static final String PRINTOUT = "printOut";		// 有数据跳转页
	public static final String NODATARETURN = "noDataReturn"; // 无数据跳转页
	
	public static final String SETTING = "setting"; 		// 系统配置跳转页
	public static final String BACKUP = "backup"; 			// 系统备份跳转页
	
	/**
	 *  个人登录session key值
	 */
	public static final String LOGINSESSIONTAG = "managerName";
	/**
	 *  厂家登录时session 键值
	 */
	public static final String FACTORY_NAME = "厂家";
	/**
	 * 个人开启播放警报声音session (1:不播放 2:播放)。
	 * 用户登录时paly_SESSION_TAG为2(默认播放)
	 * 目前不需要2010-03-18
	 */
	public static final String paly_SESSION_TAG = "playSESSION";
	/**
	 * 个人权限session
	 */
	public static final String user_POWER_TAG = "userpowertag";//
	
	public static Properties props;
	
	/**
	 * 页面权限级别--1:管理员  2:高级管理员  86:厂家
	 */
	public int pagerPower = 1;
	
	/**
	 * 分页大小
	 */
	public int USERPAGESIZE = 10; 
	
	/**
	 * 页面显示的公司名
	 */
	private String comName;		
	/**
	 * 短信报警是否打开(1:关闭 2:打开)
	 */
	private int showSms = 1;
	/**
	 * 更新到药监相关内容是否要显示(1:关闭 2:打开) 
	 */
	private int showZj = 2;
	
	
	public CommonDataUnit commonDataUnit;
	//注册service -- spring ioc
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}

	/**
	 * @describe : 获取 页面权限级别		
	 * @return : 1:管理员  2:高级管理员  86:厂家
	 * @date:2009-11-4
	 */
	public int getPagerPower() {
		return pagerPower;
	}

	/**
	 * @describe: 设置 页面权限级别
	 * @param pagerPower: 1:管理员  2:高级管理员  86:厂家	
	 * @date:2009-11-4
	 */
	public void setPagerPower(int pagerPower) {
		this.pagerPower = pagerPower;
	}

	protected void setMessage(String value) {
		addActionMessage(value);
	}
	
	/**
	 * @describe: 获取 htcUnit.properties文件 没有解析过的属性	
	 * @param label 属性名
	 * @date:2009-11-4
	 */
	public static String getSelString(String label){
		String str="";
		try {
			props = getProps();
			str = props.getProperty(label);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return str;
	}
	
	/**
	 * @describe:	获取session名
	 * @date:2009-11-4
	 */
	public static String getSysUserName(){
		ActionContext ctx = ActionContext.getContext();
		String userName = (String) ctx.getSession().get(LOGINSESSIONTAG);
		return userName;
	}	
	
	public boolean isExist(String key) throws IOException{
		return getProps().containsKey(key);
	}

	public static Properties getProps() throws IOException {
		return Resources.getResourceAsProperties("properties/htcUnit.properties");
	}
	
	/**
	 * @describe :	获取属性值,适合有 键值对 的属性. 如: equitype=1:温湿度,2:单温度,3:单湿度
	 * @param key htcUnit.properties 文件中的 属性名
	 * @return: Map(Integer, String)属性值
	 * @date:2009-11-4
	 */
	public static Map<Integer,String> getMapSel(String key) {
		Map<Integer,String> rsMap = new TreeMap<Integer, String>();
		String[] elements = getSelString(key).split(",");
		String[] strTemp;
		for (int i = 0; i < elements.length; i++) {
			strTemp = elements[i].split(":");
			rsMap.put(Integer.parseInt(strTemp[0]), strTemp[1]);
		}
		return rsMap;
	}
	
	/**
	 * @describe :getMapSel(String)扩展方法, 只是键值都是String类型,	当 fillStr 不为空时,优先分析fillStr,而不分析
	 * 			htcUnit.properties 文件中的 属性名,主要用在 sysdefaultargs这个属性		
	 * @param key htcUnit.properties 文件中的 属性名
	 * @param fillStr 优先分析的字符串
	 * @return: Map(String, String)属性值
	 * @date:2009-11-4
	 */
	public static Map<String,String> getMapSelEx(String key,String fillStr) {
		Map<String,String> rsMap = new TreeMap<String, String>();
		String[] elements;
		if ( (fillStr == null) || (fillStr.length() <0) ){
			elements = getSelString(key).split(",");
		}else{
			elements = fillStr.split(",");
		}
		
		String[] strTemp;
		for (int i = 0; i < elements.length; i++) {
			strTemp = elements[i].split(":");
			rsMap.put(strTemp[0], strTemp[1]);
		}
		return rsMap;
	}
	
	/**
	 * @describe : 获取属性值,适合没有 键值对 的属性	如: gprscomNo=1,2,3,4,5,6,7,8,9 (10:自动) 
	 * @param key htcUnit.properties 文件中的 属性名
	 * @return: List(Integer)属性值
	 * @date:2009-11-4
	 */
	public List<Integer> getListSel(String key) {
		List<Integer> rsList = new ArrayList<Integer>();
		String[] elements = getSelString(key).split(",");
		for (int i = 0; i < elements.length; i++) {
			rsList.add(i, Integer.parseInt(elements[i]));
		}
		return rsList;
	}
	
	/**
	 * @describe : 获取属性值,适合没有 键值对 的属性
	 * @param key htcUnit.properties 文件中的 属性名
	 * @return: List(String)属性值
	 * @date:2009-11-4
	 */	
	public List<String> getList(String key) {
		List<String> rsList = new ArrayList<String>();
		String[] elements = getSelString(key).split(",");
		for (int i = 0; i < elements.length; i++) {
			rsList.add(elements[i]);
		}
		return rsList;
	}

	public String getComName() {
		comName = commonDataUnit.comName();
		return comName;
	}

	public int getShowSms() {
		// 短信报警是否打开(1:关闭 2:打开)
		showSms = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_SHORT_MESSAGE));
		return showSms;
	}
	
	public int getShowZj() {
		//和  更新到药监 这个功能关联(1:关闭 2:打开) 
		showZj = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_ACCESS_STORE));
		return showZj;
	}
	
}
