package com.htc.action;

import java.io.IOException;
import java.util.*;

import com.opensymphony.xwork2.*;
import com.htc.bean.BeanForSysArgs;
import com.htc.common.CommonDataUnit;
import com.ibatis.common.resources.Resources;

/**
 * @ AbstractAction.java
 * ���� : ����action��ĸ���.����:
 * 		 1:����Ĭ���˺�,struts2����ֵ;
 * 		 2:��ȡhtcUnit.properties�ļ������ļ�����
 * 		 3:��ȡsession����½��
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class AbstractAction extends ActionSupport {

	/**
	 * �����˻�
	 */
	public String OEMNAME = "eeadmin"; 
	/**
	 * �����˻����ܵ�¼
	 */
	public String OEM_NAME = "htc_htc_htc";	
	
	public static final String INDEX = "index";
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	public static final String LOGINOUT = "loginOut";
	public static final String LOGININ = "loginIn";
	public static final String SHOWORDER = "showOrder";
	
	/**
	 * ���½���ʱ�����--�����κ�����
	 */
	public static final String PERCREATE = "percreate";
	/**
	 * ���޸ĵ�ʱ�����--����Ŀ�����
	 */
	public static final String PERUPDATE = "perupdate";
	/**
	 * ��ʾ�б�
	 */
	public static final String LISTTAG = "list";
	public static final String CREATETAG = "create";
	public static final String UPDATETAG = "update";
	
	public static final String PRINTOUT = "printOut";		// ��������תҳ
	public static final String NODATARETURN = "noDataReturn"; // ��������תҳ
	
	public static final String SETTING = "setting"; 		// ϵͳ������תҳ
	public static final String BACKUP = "backup"; 			// ϵͳ������תҳ
	
	/**
	 *  ���˵�¼session keyֵ
	 */
	public static final String LOGINSESSIONTAG = "managerName";
	/**
	 *  ���ҵ�¼ʱsession ��ֵ
	 */
	public static final String FACTORY_NAME = "����";
	/**
	 * ���˿������ž�������session (1:������ 2:����)��
	 * �û���¼ʱpaly_SESSION_TAGΪ2(Ĭ�ϲ���)
	 * Ŀǰ����Ҫ2010-03-18
	 */
	public static final String paly_SESSION_TAG = "playSESSION";
	/**
	 * ����Ȩ��session
	 */
	public static final String user_POWER_TAG = "userpowertag";//
	
	public static Properties props;
	
	/**
	 * ҳ��Ȩ�޼���--1:����Ա  2:�߼�����Ա  86:����
	 */
	public int pagerPower = 1;
	
	/**
	 * ��ҳ��С
	 */
	public int USERPAGESIZE = 10; 
	
	/**
	 * ҳ����ʾ�Ĺ�˾��
	 */
	private String comName;		
	/**
	 * ���ű����Ƿ��(1:�ر� 2:��)
	 */
	private int showSms = 1;
	/**
	 * ���µ�ҩ����������Ƿ�Ҫ��ʾ(1:�ر� 2:��) 
	 */
	private int showZj = 2;
	
	
	public CommonDataUnit commonDataUnit;
	//ע��service -- spring ioc
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}

	/**
	 * @describe : ��ȡ ҳ��Ȩ�޼���		
	 * @return : 1:����Ա  2:�߼�����Ա  86:����
	 * @date:2009-11-4
	 */
	public int getPagerPower() {
		return pagerPower;
	}

	/**
	 * @describe: ���� ҳ��Ȩ�޼���
	 * @param pagerPower: 1:����Ա  2:�߼�����Ա  86:����	
	 * @date:2009-11-4
	 */
	public void setPagerPower(int pagerPower) {
		this.pagerPower = pagerPower;
	}

	protected void setMessage(String value) {
		addActionMessage(value);
	}
	
	/**
	 * @describe: ��ȡ htcUnit.properties�ļ� û�н�����������	
	 * @param label ������
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
	 * @describe:	��ȡsession��
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
	 * @describe :	��ȡ����ֵ,�ʺ��� ��ֵ�� ������. ��: equitype=1:��ʪ��,2:���¶�,3:��ʪ��
	 * @param key htcUnit.properties �ļ��е� ������
	 * @return: Map(Integer, String)����ֵ
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
	 * @describe :getMapSel(String)��չ����, ֻ�Ǽ�ֵ����String����,	�� fillStr ��Ϊ��ʱ,���ȷ���fillStr,��������
	 * 			htcUnit.properties �ļ��е� ������,��Ҫ���� sysdefaultargs�������		
	 * @param key htcUnit.properties �ļ��е� ������
	 * @param fillStr ���ȷ������ַ���
	 * @return: Map(String, String)����ֵ
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
	 * @describe : ��ȡ����ֵ,�ʺ�û�� ��ֵ�� ������	��: gprscomNo=1,2,3,4,5,6,7,8,9 (10:�Զ�) 
	 * @param key htcUnit.properties �ļ��е� ������
	 * @return: List(Integer)����ֵ
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
	 * @describe : ��ȡ����ֵ,�ʺ�û�� ��ֵ�� ������
	 * @param key htcUnit.properties �ļ��е� ������
	 * @return: List(String)����ֵ
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
		// ���ű����Ƿ��(1:�ر� 2:��)
		showSms = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_SHORT_MESSAGE));
		return showSms;
	}
	
	public int getShowZj() {
		//��  ���µ�ҩ�� ������ܹ���(1:�ر� 2:��) 
		showZj = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_ACCESS_STORE));
		return showZj;
	}
	
}
