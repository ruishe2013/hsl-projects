package com.htc.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.htc.bean.BeanForSysArgs;
import com.htc.common.CommonDataUnit;
import com.htc.common.FunctionUnit;
import com.htc.domain.PhoneList;
import com.htc.domain.SmsRecord;
import com.htc.model.MainService;
import com.htc.model.SetSysService;

/**
 * @ SmsDataAction.java
 * ���� : ��������. ��ѯ
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class SmsDataAction extends AbstractActionForHigh {

	// ������
	private MainService mainService;
	private SetSysService setSysService;
	
	// ҳ����ʾ
	private String phoneShow;				// ������Ϣ�б� - ��ѯ��ʾ��

	// ��ѯ�������
	private String typeStr;					// ��������(1:���� 2:����) 
	private String phoneStr;				// ѡ����ֻ��б�
	private String startTime;				// ������ʼʱ��
	private String endTime;					// ��������ʱ��
	
	// ��ѯ�����ʾ
	private List<SmsRecord> smsRecList;		// �����Ϣ�б� - �����ʾ��
	
	// ��������-ϸ��
	private String headTitle;				 
	private String headDetail;	
	
	// ���췽��
	public SmsDataAction() {
	}
	//ע��service -- spring ioc
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	public void setSetSysService(SetSysService setSysService) {
		this.setSysService = setSysService;
	}
	
	@Override
	public String execute() {
		phoneShow = fillGmsArea();  // ҳ����ʾ��
		return SUCCESS;
	}
	
	// ��ȡ������ʾ�б�
	public String fillGmsArea(){
		StringBuffer strBuf = new StringBuffer();
		// ��ȡ���ź����б� 
		List<PhoneList> phonelists = setSysService.getAllPhoneLists();
		String phoneStr = "";
		String label = "";
		
		for (int i = 0; i < phonelists.size(); i++) {
			phoneStr = phonelists.get(i).getPhone();
			label = phonelists.get(i).getName() + "[" + phonelists.get(i).getPhone()+"]";
			strBuf.append(CommonDataUnit.create_Html_P(phoneStr, label, false));
		}	
		return strBuf.toString();
	}

	// ��ѯ
	public String searchRec(){
		smsRecList = mainService.searchSmsRecs(fillSearchBean());
		
		if ( (null == smsRecList) || (smsRecList.size() <= 0)){ // ��ѯ��������
			nodataInfo();
			return NODATARETURN;			
		}else{
			// ���ñ���
			// headTitle��ʽ:��˾�� -�������ݲ�ѯ��� 
			headTitle = commonDataUnit.getSysArgsByKey(BeanForSysArgs.COMPANY_NAME) + "-�������ݲ�ѯ��� ";
			// headDetail��ʽ:ʱ�䷶Χ: 2009��11��06�� 12ʱ~ 2009��11��06�� 23ʱ		
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("ʱ�䷶Χ:");
			strbuf.append(startTime);
			strbuf.append("~");
			strbuf.append(endTime);
			headDetail = strbuf.toString();			
			return PRINTOUT;
		}
	}
	
	// ����ѯbean
	public SmsRecord fillSearchBean(){
		SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);
		SmsRecord searchBean = new SmsRecord();
		
		searchBean.setSmsphone(phoneStr);
		searchBean.setTypeStr(typeStr);
		try {
			searchBean.setSmsStart(dateFormat.parse(startTime));
			searchBean.setSmsTo(dateFormat.parse(endTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return searchBean;
	}
	
	private void nodataInfo() {
		StringBuffer strbuf = new StringBuffer();
		headTitle = "����������¼...";
		strbuf.append("����:");
		strbuf.append("<br/>");
		strbuf.append("<label>");
		strbuf.append("    �������ʼʱ��ͽ���ʱ��");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<label>");
		strbuf.append("    ��ѡ���ʵ����ֻ�����");
		strbuf.append("</label><br/>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("*���������,��Ȼû�в�ѯ���,��ô���ʱ����ȷʵû�����ݼ�¼����...");
		headDetail = strbuf.toString();
	}	
	
	// ----------------��ͨ get��set����----------------
	public String getTypeStr() {
		return typeStr;
	}
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	public String getPhoneStr() {
		return phoneStr;
	}
	public void setPhoneStr(String phoneStr) {
		this.phoneStr = phoneStr;
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
	public void setHeadTitle(String headTitle) {
		this.headTitle = headTitle;
	}
	public String getHeadDetail() {
		return headDetail;
	}
	public void setHeadDetail(String headDetail) {
		this.headDetail = headDetail;
	}
	public void setSmsRecList(List<SmsRecord> smsRecList) {
		this.smsRecList = smsRecList;
	}
	public List<SmsRecord> getSmsRecList() {
		return smsRecList;
	}
	public String getPhoneShow() {
		return phoneShow;
	}
	public void setPhoneShow(String phoneShow) {
		this.phoneShow = phoneShow;
	}
	
}
