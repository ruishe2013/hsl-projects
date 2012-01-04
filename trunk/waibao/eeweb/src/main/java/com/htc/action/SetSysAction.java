package com.htc.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.htc.bean.BeanForSysArgs;
import com.htc.common.*;
import com.htc.domain.*;
import com.htc.model.ServiceAccess;
import com.htc.model.ManaService;
import com.htc.model.ServiceSqlServer;
import com.htc.model.SetSysService;
import com.htc.model.seriaPort.Level_First_Serial;
import com.htc.model.seriaPort.SimCard_Unit;

/**
 * @ SetSysAction.java
 * ���� : �������� �� ϵͳ���� action .
 * ע������ : ȡ�����ݱ��ݺͻ�ԭ,�Լ�ϵͳ��ɫ���� ����
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class SetSysAction extends AbstractActionForHigh {
	
	// ���л���
	private static final long serialVersionUID = -1969123516449503773L;

	// ������
	private SetSysService setSysService;
	private ManaService manaService;
	private ServiceAccess serviceAccess;
	private ServiceSqlServer serviceSqlServer;	
	private Level_First_Serial first_Level;
	private SimCard_Unit simCard_Unit;	
	//--------------------��ʷ����(��������)---Start--------------------//
	// ��ҳ
	private Pager pager; 							// ��ҳ��
	private String currentPage="1";					// ��ǰҳ
	private String pagerMethod; 					// ҳ�淽�����磺ǰһҳ����һҳ����һҳ�����һҳ��.
	
	// ҳ����ʾ
	private List<BackUpList> backUpLists;			// �����Ϣ�б�
	// --------------------��ʷ����(��������)---Start--------------------//	
	
	// ҳ����ʾ
	private Map<String, String> sysArgs;			// ϵͳ��Ϣ
	private Map<Integer, String>  baudRatelList;	// ������		-- ������
	private List<String> backuppath;				// ��ʷ�����Զ�����·��
	private Map<Integer,String> tempshowSel;		// �¶���ʾ���� (1:����,2:����) 
	private Map<Integer, String>  flashTimeList;	// ���ʴ��ڼ��	-- ������	
	private Map<Integer,String> alarmFileSel;  		// ���������ļ�	
	private Map<Integer,String> cnmitype;  			// ����ģ��,����ֱ�ӱ��浽��������:AT+CNMI=2,2(ֵΪ1)����AT+CNMI=2,1(ֵΪ2)	
	private String downfilename;					// ���ص��ļ���	
	private int oldRecTime;							// �޸�ϵͳ����ʱ, ʵʱ���ߵ�����
	
	// ҳ�� <s:checkbox/>  -- ��ҳ�渴ѡ���Ƿ�ѡ��,ӳ���Map����(Map<"��ѡ���ֵ",boolean>)
	private Map<String, Boolean> showGroup ;		// ��������������ֵ:msssage��pcsound��Ӧ,���ű��������������Ƿ�����
	private String KEY_MSSSAGE = "msssage" ;
	private String KEY_PCSOUND = "pcsound" ;
	private String KEY_ACCESS = "doaccess" ;
	
	// ҳ���Ƿ���ʾ��ʾ��Ϣ
	private int showTipMsg = 0;						// 0:����ʾ  1:��ʾ
	
	// ������
	private Map<Integer, String> searchAreaMap;		// ������ϢMap
	private String searchPlace = "ȫ��";				// ������
	private Map<String, Object> searchKey;			// ��ѯ�ؼ���
	private String timeFromStr;						// ������ʼʱ��-�ַ�
	private String timeToStr;						// ��������ʱ��-�ַ�
	private String startTime;						// ������ʼʱ��
	private String endTime;							// ��������ʱ��
	
	// ��������-ϸ��
	private String headTitle;						// ����
	private String headDetail;						// ��Ϣϸ��	
	
	// ���췽��
	public SetSysAction() {
	}
	//ע��service -- spring ioc
	public void setSetSysService(SetSysService setSysService) {
		this.setSysService = setSysService;
	}
	public void setManaService(ManaService manaService) {
		this.manaService = manaService;
	}
	public void setServiceAccess(ServiceAccess serviceAccess) {
		this.serviceAccess = serviceAccess;
	}
	public void setServiceSqlServer(ServiceSqlServer serviceSqlServer) {
		this.serviceSqlServer = serviceSqlServer;
	}
	public void setFirst_Level(Level_First_Serial first_Level) {
		this.first_Level = first_Level;
	}	
	public void setSimCard_Unit(SimCard_Unit simCard_Unit) {
		this.simCard_Unit = simCard_Unit;
	}	
	
	@Override
	public String execute() {
		return SUCCESS;
	}
	
	public String initSys(){
		showTipMsg = 0;
		
		//ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService ;
		sysArgs = setSysService.getSysParamMap();
		
		boolean tempBool = true;
		showGroup = showGroup == null ? new HashMap<String, Boolean>() : showGroup;
		
		tempBool = (sysArgs.get(BeanForSysArgs.OPEN_SHORT_MESSAGE)).equals("2");
		showGroup.put(KEY_MSSSAGE, tempBool);
		tempBool = (sysArgs.get(BeanForSysArgs.OPEN_PCSOUND)).equals("2");
		showGroup.put(KEY_PCSOUND, tempBool);
		tempBool = (sysArgs.get(BeanForSysArgs.OPEN_ACCESS_STORE)).equals("2");
		showGroup.put(KEY_ACCESS, tempBool);
		// �޸�ϵͳ����ʱ, ʵʱ���ߵ�����
		oldRecTime = Integer.parseInt(sysArgs.get(BeanForSysArgs.MAX_RS_COUNT_LINE));
		
		return SETTING;
	}
	
	public String update(){
		String tempStr;
		
		// ���ű����Ƿ��(1:�ر� 2:��)
		tempStr = showGroup.get(KEY_MSSSAGE)? "2" : "1";
		sysArgs.put(BeanForSysArgs.OPEN_SHORT_MESSAGE, tempStr);
		// ���������Ƿ��(1:�ر� 2:��)
		tempStr = showGroup.get(KEY_PCSOUND)? "2" : "1";
		sysArgs.put(BeanForSysArgs.OPEN_PCSOUND, tempStr);
		// Access���ݱ����Ƿ��(1:�ر� 2:��)
		tempStr = showGroup.get(KEY_ACCESS)? "2" : "1";
		sysArgs.put(BeanForSysArgs.OPEN_ACCESS_STORE, tempStr);
		
		//ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService ;
		// �ύ�޸�
		if (setSysService.updateSysParam(sysArgs)){
			//����ϵͳ����,�ﵽ���µ�Ŀ��
			if (oldRecTime == Integer.parseInt(sysArgs.get(BeanForSysArgs.MAX_RS_COUNT_LINE))){
				// ʵʱ���ߵ����� �ޱ仯, ������ ����&ʵʱ���߹��������ݿ�
				commonDataUnit.resetSystem(false,false,false,true,false);
			}else{
				// ʵʱ���ߵ����� �б仯, ���� ����&ʵʱ���߹��������ݿ�
				commonDataUnit.resetSystem(false,false,false,true,true);
				oldRecTime = Integer.parseInt(sysArgs.get(BeanForSysArgs.MAX_RS_COUNT_LINE));
			} 
			tempStr = "ϵͳ�����޸ĳɹ�.";
		}else{
			tempStr = "ϵͳ�����޸�ʧ��.";
		}
		showTipMsg = 1;
		addActionMessage(tempStr);
		return SETTING;
	}
	
	/**
	 * @describe: ������Ϣ�г�
	 * @param type: 1:����������		2:����������
	 * @date:2010-1-20
	 */
	@SuppressWarnings("unchecked")
	public void list(int type){
		if (searchKey == null){
			searchKey = new HashMap<String, Object>();
		}
		try {
			if ((startTime!=null)&&(endTime!=null)){
				SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);
				searchKey.put("timeStart", dateFormat.parse(startTime + " 0:00:00"));
				searchKey.put("timeEnd", dateFormat.parse(endTime +  " 23:59:59"));
				if (!searchPlace.equals("ȫ��")){
					searchKey.put("mark", searchPlace);
				}
			}		
			
			//ʵ����������
			//setSysService = setSysService == null ? new SetSysService() : setSysService ;
			if (type == 1){	// 1:����������	
				pager = setSysService.getPager("BackUpList", getCurrentPage(),getPagerMethod(), USERPAGESIZE, searchKey, USERPAGESIZE);
			}else{			// 2:����������
				pager = setSysService.getPager("BackUpList", getCurrentPage(),getPagerMethod(), USERPAGESIZE, searchKey);
			}			
			backUpLists = (ArrayList) pager.getElements();
			this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
	}	
	
	// �г������б�  -- ��ʼ��ʱ����
	public String getLists() {
		list(1);
		return BACKUP;
	}
	
	// �г������б� -- ��ҳ��������ʱ����
	public String getList() {
		list(2);
		return BACKUP;
	}
	
	// ������ʷ����
	public String downfile(){
		System.out.println(downfilename);
		return SUCCESS;
	}
	
	/**
	 * @describe: ��ȡ�ļ�·��,��ȡ�ļ�������
	 * @param temp: �ļ�����·��
	 * @date:2009-12-17
	 */
	public String subFileName(String filepath){
        String subStr = filepath.trim();
        subStr = subStr.substring(subStr.lastIndexOf("/")+1);		
		return subStr;
	}		
	
	/**
	 * @describe:	���access���ݿ����ͨ��
	 * @date:2010-3-19
	 */
	public String chkAccess (){
		if (!serviceAccess.testLink()){
			accessnolinkInfo();
			return NODATARETURN;
		}
		if (!serviceSqlServer.testLink()){
			sqlservernolinkInfo();
			return NODATARETURN;
		}
		linkInfo();
		return NODATARETURN;
	}
	
	/**
	 * ������ݲɼ��Ͷ���ģ���Ƿ���������
	 */
	public String chkSerial(){
		boolean haveSms = showGroup.get(KEY_MSSSAGE); 		// ���ű����Ƿ��(1:false-�ر� 2:true-��)
		boolean dataState = first_Level.isRunningFlag();	// ���ݲɼ������Ƿ�������
		boolean smsState = simCard_Unit.isRunFlag();		// ����ģ���Ƿ�������
		
		if(dataState == false){								// ���ݲɼ����û������,��������һ��
			dataState = first_Level.startRunSerial() == 0;	//  0:��������
		}
		if (haveSms&&smsState==false){						// ϵͳ��������ģ���ǰ����,���ԶԶ���ģ������һ��
			String passport = "";
			if (first_Level.isRunningFlag() == true){
				passport = first_Level.getPortStr();
			}			
			smsState = simCard_Unit.openPort(passport, 9600);
		}
		
		headTitle = "���״̬...";
		headDetail = serialTestInfo(dataState, smsState, haveSms);
		return NODATARETURN;
	}
	
	private void sqlservernolinkInfo() {
		StringBuffer strbuf = new StringBuffer();
		headTitle = "sqlserver���ݿ�����ʧ��...";
		
		strbuf.append("����:");
		strbuf.append("<br/>");
		strbuf.append("<label>");
		strbuf.append("    �ټ��ϵͳ���Ƿ�װ��sqlserver���ݿ�");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<br/>");
		strbuf.append("<label>");
		strbuf.append("    �ڼ��sqlserver���ݿ�:�û���:sa ����:sa");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("    �� ȷ��������Ϣ��,��Ȼ����. �п���������ϴ�������,�뾡����ϵ����...");	
		headDetail = strbuf.toString();
	}	
	
	/**
	 * @describe: access���ݿ�����ʧ��
	 * @date:2009-12-10
	 */
	private void accessnolinkInfo() {
		StringBuffer strbuf = new StringBuffer();
		headTitle = "access���ݿ�����ʧ��...";
		strbuf.append("����:");
		strbuf.append("<br/>");
		strbuf.append("<label>");
		strbuf.append("    ����D:\\eeField\\accessdbĿ¼��,�鿴DSR_DB.mdb�ļ��Ƿ����");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<label>");
		strbuf.append("    ���ҵ��ļ�,���ܴ�����ϵͳ����,������������");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<label>");
		strbuf.append("    ���Ҳ����ļ�,��װ�ļ������ҵ�DSR_DB.mdb(Ҳ������ϵ���ǻ�ȡ����ļ�)," +
				"������D:\\htc\\accessdbĿ¼��(���û�����Ŀ¼,���ȴ���),Ȼ����������");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<label>");
		strbuf.append("    �����ϲ�����,��Ȼ����ʧ��. �п�������ϴ�������,�뾡����ϵ����...");
		strbuf.append("</label>");
		strbuf.append("<br/>");
		headDetail = strbuf.toString();
	}
	
	/**
	 * @describe: access���ݿ����ӳɹ�
	 * @date:2009-12-10
	 */
	private void linkInfo() {
		StringBuffer strbuf = new StringBuffer();
		headTitle = "���Գɹ�,�����ʹ��...";
		strbuf.append("<label>");
		strbuf.append("access���ݿ�������ȷ...");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<label>");
		strbuf.append("sqlserver���ݿ�������ȷ...");
		strbuf.append("</label><br/><br/>");
		headDetail = strbuf.toString();
	}
	
	/**
	 * ���ڼ����Ϣ
	 * @param dataState ���ݲɼ����Ƿ�����
	 * @param smsState  ����ģ���Ƿ�����
	 * @param haveSms   ϵͳ���Ƿ��ж���ģ��
	 */
	private String serialTestInfo(boolean dataState, boolean smsState, boolean haveSms) {
		StringBuffer strbuf = new StringBuffer();
		
		strbuf.append("<label>");
		strbuf.append(dataState?"���ݲɼ�����������...":"���ݲɼ���û������...");
		strbuf.append("</label><br/><br/>");
		
		if (haveSms){
			strbuf.append("<label>");
			strbuf.append(smsState?"����ģ����������...":"����ģ��û������");
			strbuf.append("</label><br/><br/>");
		}
		
		if(!dataState || (!smsState && haveSms)){
			strbuf.append("<br/><br/>");
			strbuf.append("<label>");
			strbuf.append("    �����û������,�밴˳����. �ټ�����������Ƿ���ȷ->����������->����ϵ����...");
			strbuf.append("</label>");
		}
		return strbuf.toString();
	}
	
	// ----------------����Ϊget��set����----------------
	// ������
	public Map<Integer, String> getBaudRatelList() {
		baudRatelList = getMapSel("gprsbaudRate");
		return baudRatelList;
	}	
	
	// �¶���ʾ����  1:����,2:����
	public Map<Integer,String> getTempshowSel() {
		tempshowSel = getMapSel("systmpshow");
		return tempshowSel;
	}	
	
	// ���ʴ��ڼ��
	public Map<Integer, String> getFlashTimeList() {
		flashTimeList = getMapSel("syspicflash");
		return flashTimeList;
	}	
	
	// ���������ļ�	
	public Map<Integer, String> getAlarmFileSel() {
		alarmFileSel = getMapSel("sysalarmfile");
		return alarmFileSel;
	}
	// ��ʷ�����Զ�����·��
	public List<String> getBackuppath() {
		backuppath = getList("backuppath"); 
		return backuppath;
	}
	
	// ������Ϣ
	public Map<Integer, String> getSearchAreaMap() {
		searchAreaMap = manaService.getAllPlace();
		searchAreaMap.put(0, "ȫ��");
		return searchAreaMap;
	}	
	// ����ģ��,����ֱ�ӱ��浽��������:AT+CNMI=2,2(ֵΪ1)����AT+CNMI=2,1(ֵΪ2)	
	public Map<Integer, String> getCnmitype() {
		cnmitype = new HashMap<Integer, String>();
		cnmitype.put(1, "AT+CNMI=2,2");
		cnmitype.put(2, "AT+CNMI=2,1");
		return cnmitype;
	}
	// ----------------��ͨ get��set����----------------
	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getPagerMethod() {
		return pagerMethod;
	}

	public void setPagerMethod(String pagerMethod) {
		this.pagerMethod = pagerMethod;
	}
	
	public List<BackUpList> getBackUpLists() {
		return backUpLists;
	}

	public void setBackUpLists(List<BackUpList> backUpLists) {
		this.backUpLists = backUpLists;
	}

	public Map<String, Boolean> getShowGroup() {
		return showGroup;
	}

	public void setShowGroup(Map<String, Boolean> showGroup) {
		this.showGroup = showGroup;
	}

	public Map<String, String> getSysArgs() {
		return sysArgs;
	}

	public void setSysArgs(Map<String, String> sysArgs) {
		this.sysArgs = sysArgs;
	}

	public int getShowTipMsg() {
		return showTipMsg;
	}

	public void setShowTipMsg(int showTipMsg) {
		this.showTipMsg = showTipMsg;
	}

	public void setDownfilename(String downfilename) {
		this.downfilename = downfilename;
	}

	public String getSearchPlace() {
		return searchPlace;
	}

	public void setSearchPlace(String searchPlace) {
		this.searchPlace = searchPlace;
	}

	public String getTimeFromStr() {
		return timeFromStr;
	}

	public void setTimeFromStr(String timeFromStr) {
		this.timeFromStr = timeFromStr;
	}

	public String getTimeToStr() {
		return timeToStr;
	}

	public void setTimeToStr(String timeToStr) {
		this.timeToStr = timeToStr;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getHeadTitle() {
		return headTitle;
	}

	public String getHeadDetail() {
		return headDetail;
	}

	public int getOldRecTime() {
		return oldRecTime;
	}

	public void setOldRecTime(int oldRecTime) {
		this.oldRecTime = oldRecTime;
	}
	
}
