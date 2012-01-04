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
 * 作用 : 短信数据. 查询
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class SmsDataAction extends AbstractActionForHigh {

	// 服务类
	private MainService mainService;
	private SetSysService setSysService;
	
	// 页面显示
	private String phoneShow;				// 短信信息列表 - 查询显示用

	// 查询条件相关
	private String typeStr;					// 搜索类型(1:发送 2:接收) 
	private String phoneStr;				// 选择的手机列表
	private String startTime;				// 搜索开始时间
	private String endTime;					// 搜索结束时间
	
	// 查询结果显示
	private List<SmsRecord> smsRecList;		// 表格信息列表 - 结果显示用
	
	// 搜索标题-细节
	private String headTitle;				 
	private String headDetail;	
	
	// 构造方法
	public SmsDataAction() {
	}
	//注册service -- spring ioc
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	public void setSetSysService(SetSysService setSysService) {
		this.setSysService = setSysService;
	}
	
	@Override
	public String execute() {
		phoneShow = fillGmsArea();  // 页面显示用
		return SUCCESS;
	}
	
	// 获取短信显示列表
	public String fillGmsArea(){
		StringBuffer strBuf = new StringBuffer();
		// 获取短信号码列表 
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

	// 查询
	public String searchRec(){
		smsRecList = mainService.searchSmsRecs(fillSearchBean());
		
		if ( (null == smsRecList) || (smsRecList.size() <= 0)){ // 查询不到数据
			nodataInfo();
			return NODATARETURN;			
		}else{
			// 设置标题
			// headTitle格式:公司名 -报警数据查询结果 
			headTitle = commonDataUnit.getSysArgsByKey(BeanForSysArgs.COMPANY_NAME) + "-短信数据查询结果 ";
			// headDetail格式:时间范围: 2009年11月06日 12时~ 2009年11月06日 23时		
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("时间范围:");
			strbuf.append(startTime);
			strbuf.append("~");
			strbuf.append(endTime);
			headDetail = strbuf.toString();			
			return PRINTOUT;
		}
	}
	
	// 填充查询bean
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
		headTitle = "搜索不到记录...";
		strbuf.append("建议:");
		strbuf.append("<br/>");
		strbuf.append("<label>");
		strbuf.append("    ●调整开始时间和结束时间");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<label>");
		strbuf.append("    ●选择适当的手机号码");
		strbuf.append("</label><br/>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("*如果调整后,仍然没有查询结果,那么这段时间内确实没有数据记录存在...");
		headDetail = strbuf.toString();
	}	
	
	// ----------------普通 get，set方法----------------
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
