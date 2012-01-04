package com.htc.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.htc.common.FunctionUnit;
import com.htc.domain.*;
import com.htc.model.MainService;

/**
 * @ TLogAction.java
 * ���� : ϵͳ��־action. ��ҳ - ɾ�� - ����ɾ��
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class TLogAction extends AbstractActionForOEM {

	// ������
	private MainService mainService;

	// ��ҳ
	private Pager pager; 					// ��ҳ��
	private String currentPage="1";			// ��ǰҳ
	private String pagerMethod; 			// ҳ�淽�����磺ǰһҳ����һҳ����һҳ�����һҳ��.
	private int[] ids; 						// ѡ��ɾ����ѡ��(��ѡ)
	
	// ҳ����ʾ
	private List<TLog> logList = new ArrayList<TLog>();	// �����Ϣ�б�
	private Map<Integer, String>  logTypeMap;// ��־����
	private int logId;						// ��־ID
	private boolean initFlag = false;		// �Ƿ��ǵ�һ�ν���ҳ��
	private int maxRecords = 100;      		// ����¼��
	private int log_type=0;     			// ��־����
	

	// ��ѯ���
	private Map<String, Object> searchKey;	// ��ѯ�ؼ���
	private String timeFromStr;				// ������ʼʱ��-�ַ�
	private String timeToStr;				// ��������ʱ��-�ַ�
	private String startTime;				// ������ʼʱ��
	private String endTime;					// ��������ʱ��	
	
	// ���췽��
	public TLogAction() {
	}
	//ע��service -- spring ioc
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}

	@Override
	public String execute() {
		list(1);
		return SUCCESS;
	}

	// ��ҳ
	public String getList(){
		list(2);
		return SUCCESS;
	}
	
	/**
	 * @describe: ������Ϣ�г�
	 * @param type: 1:�̶���������		2:ҳ���������
	 * @date:2010-1-20
	 */
	@SuppressWarnings("unchecked")
	public void list(int type){
		initFlag = true;
		if (searchKey == null){
			searchKey = new HashMap<String, Object>();
		}
		try {
			if ((startTime!=null)&&(endTime!=null)){
				SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);
				searchKey.put("timeStart", dateFormat.parse(startTime + " 0:00:00"));
				searchKey.put("timeEnd", dateFormat.parse(endTime +  " 23:59:59"));
			}
			searchKey.put("type", log_type);
			// ʵ����������
			//mainService = mainService == null ? new MainService() : mainService;
			if (type == 1){
				pager = mainService.getPager("TLog", getCurrentPage(),	getPagerMethod(), USERPAGESIZE, searchKey, USERPAGESIZE);
			}else{
				pager = mainService.getPager("TLog", getCurrentPage(),	getPagerMethod(), USERPAGESIZE, searchKey, maxRecords);
			}
			logList = (ArrayList) pager.getElements();
			this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	// ɾ��
	public String delete() throws Exception {
		
		//ʵ����������
		//mainService = mainService == null ? new MainService() : mainService;
		
		mainService.deleteLogById(logId);
		list(2);
		return SUCCESS;
	}	

	// ����ɾ��
	public String deleteSelect() {
		
		//ʵ����������
		//mainService = mainService == null ? new MainService() : mainService;		
		
		if (ids != null) {
			mainService.deleteLogBatch(ids);
		}
		list(2);
		return SUCCESS;
	}
	// ----------------����Ϊget��set����----------------
	// ��־����[1:��¼��Ϣ 2:��ѯ��Ϣ 3:������Ϣ 4:������Ϣ]
	public Map<Integer, String> getLogTypeMap() {
		logTypeMap = new HashMap<Integer, String>();
		logTypeMap.put(0, "ȫ��");
		logTypeMap.put(1, "��¼��Ϣ");
		logTypeMap.put(2, "��ѯ��Ϣ");
		logTypeMap.put(3, "������Ϣ");
		logTypeMap.put(4, "������Ϣ");
		logTypeMap.put(5, "���ű���");
		return logTypeMap;
	}

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

	public int[] getIds() {
		return ids;
	}

	public void setIds(int[] ids) {
		this.ids = ids;
	}

	public List<TLog> getLogList() {
		return logList;
	}

	public void setLogList(List<TLog> logList) {
		this.logList = logList;
	}

	public int getMaxRecords() {
		return maxRecords;
	}

	public void setMaxRecords(int maxRecords) {
		this.maxRecords = maxRecords;
	}

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
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

	public boolean isInitFlag() {
		return initFlag;
	}

	public int getLog_type() {
		return log_type;
	}

	public void setLog_type(int log_type) {
		this.log_type = log_type;
	}

}
