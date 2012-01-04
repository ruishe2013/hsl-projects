package com.htc.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.htc.domain.GprsSet;
import com.htc.domain.Pager;
import com.htc.model.SetSysService;
import com.htc.model.seriaPort.Level_First_Serial;
import com.htc.model.seriaPort.SimCard_Unit;
import com.htc.model.seriaPort.Smslib_SendJob;

/**
 * @ GprsTestAction.java
 * 作用 : Gprs - 测试功能action. 分页 - 连接 -断开连接 - 发送
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class GprsTestAction extends AbstractActionForHigh {

	//服务类
	private SetSysService setSysService;
	private Level_First_Serial first_Level;
	private SimCard_Unit simCard_Unit;
	
	// 分页
	private Pager pager; 							// 分页类
	private String currentPage="1";					// 当前页
	private String pagerMethod; 					// 页面方法，如：前一页，后一页，第一页，最后一页等.
	private int showTipMsg=0;						// 是否显示错误标记(0:不显示 1:显示)	
	
	// 页面显示
	private int portNo;								// 端口号(在用)
	private int baudRateNo = 9600;					// 波特率(在用)
	private String centerNo = "13800571500" ;		// 短信中心(在用)
	private String phoneNo ;						// 测试的手机号码
	private String sendcontent ;					// 待发送的内容
	private String msgShow ;						// ajax返回的状态运行结果
	private int linkFlag = 0;						// 端口连接状况 (0:没有连接 1:连接) -- 没有用到
	
	private List<GprsSet> gprsSetList;				// 表格信息列表(在用)
	private Map<Integer, String> portList;			// 端口号 列表 (在用)
	private Map<Integer, String> baudRatelList;		// 波特率列表  (在用)
	
	private int runFlag;							// 1:正在运行	2:没有运行
	private String stateStr="";						// 页面显示状态字符
	
	// 构造方法
	public GprsTestAction() {
	}
	//注册service -- spring ioc
	public void setSetSysService(SetSysService setSysService) {
		this.setSysService = setSysService;
	}
	public void setFirst_Level(Level_First_Serial first_Level) {
		this.first_Level = first_Level;
	}
	public void setSimCard_Unit(SimCard_Unit simCard_Unit) {
		this.simCard_Unit = simCard_Unit;
	}
	
	@Override
	public String execute() {
		//list();
		if (simCard_Unit.isRunFlag()){
		//if (Smslib_SendJob.getInstance().isSms_run_flag()){
			runFlag = 1;
			stateStr = "正在运行...";
		}else{
			runFlag = 2;
			stateStr = "还没有运行...";
		}
		return SUCCESS;
	}

	/**
	 * @describe: 显示列表	
	 * @date:2009-11-4
	 */
	@SuppressWarnings("unchecked")
	public void list(){
		
		// 实例化服务类
		//setSysService = setSysService == null ? new SetSysService() : setSysService;
		
		pager = setSysService.getPager("GprsSet", getCurrentPage(),getPagerMethod(), USERPAGESIZE,null);
		gprsSetList = (ArrayList) pager.getElements();
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
	}
	
	// 连接
	public String link() {
		String passport ="";
		showTipMsg = 1;
		if (simCard_Unit.isRunFlag()){
		//if (Smslib_SendJob.getInstance().isSms_run_flag()){
			stateStr = "正在运行...";
			runFlag = 1;			
		}else{
			if (first_Level.isRunningFlag() == true){
				passport = first_Level.getPortStr();
			}			
			//System.out.println("连接中...");
			if (simCard_Unit.openPort(passport, 9600)){
			//if (Smslib_SendJob.getInstance().startService(9600,passport)){
				stateStr = "正在运行...";
				runFlag = 1;
			}else{
				stateStr = "连接失败...";
				runFlag = 2;
			}
		}
		return SUCCESS;
	}
	
	// 断开连接
	public String dislink() {
		//System.out.println("断开连接中...");
		showTipMsg = 1;
		runFlag = 2;
		
		if (simCard_Unit.isRunFlag()){
		//if (Smslib_SendJob.getInstance().isSms_run_flag()){
			if (simCard_Unit.closePort()){
			//if (Smslib_SendJob.getInstance().stopService()){
				stateStr = "断开成功...";
			}else{
				stateStr = "连接失败...";
				runFlag = 1;
			}
		}else{
			stateStr = "断开成功...";
		}
		return SUCCESS;
	}
	
	// 测试发送短信 - 与正式运行分开
	public String send(){
		showTipMsg = 1;
		String phone,msg;
		try {
			if (simCard_Unit.isRunFlag()){
				msgShow = "短信模块正在运行,要测试短信发送,请先停止模块运行...";
			}else{
				String passport ="";
				centerNo = URLDecoder.decode(centerNo, "UTF-8");
				phone = URLDecoder.decode(phoneNo, "UTF-8");
				msg = URLDecoder.decode(sendcontent, "UTF-8");
				
				if (first_Level.isRunningFlag() == true){
					passport = first_Level.getPortStr();
				}		
				if (simCard_Unit.singleForTest(passport, 9600, centerNo, phone, msg)){
					msgShow = "发送成功...";
				}else{
					msgShow = "发送失败...";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	// 分页
	public String getList() {
		list();
		return SUCCESS;
	}
	
	// ----------------以下为get，set方法----------------
	//端口号
	public Map<Integer, String> getPortList() {
		portList = getMapSel("gprscom");
		return portList;
	}
	//波特率
	public Map<Integer, String> getBaudRatelList() {
		baudRatelList = getMapSel("gprsbaudRate");
		return baudRatelList;
	}

	// ----------------普通 get，set方法----------------
	public int getPortNo() {
		return portNo;
	}

	public void setPortNo(int portNo) {
		this.portNo = portNo;
	}

	public int getBaudRateNo() {
		return baudRateNo;
	}

	public void setBaudRateNo(int baudRateNo) {
		this.baudRateNo = baudRateNo;
	}

	public String getCenterNo() {
		return centerNo;
	}

	public void setCenterNo(String centerNo) {
		this.centerNo = centerNo;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public List<GprsSet> getGprsSetList() {
		return gprsSetList;
	}
	
	public void setGprsSetList(List<GprsSet> gprsSetList) {
		this.gprsSetList = gprsSetList;
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

	public int getShowTipMsg() {
		return showTipMsg;
	}

	public int getLinkFlag() {
		return linkFlag;
	}

	public void setLinkFlag(int linkFlag) {
		this.linkFlag = linkFlag;
	}

	public String getSendcontent() {
		return sendcontent;
	}

	public void setSendcontent(String sendcontent) {
		this.sendcontent = sendcontent;
	}

	public String getMsgShow() {
		return msgShow;
	}

	public int getRunFlag() {
		return runFlag;
	}

	public String getStateStr() {
		return stateStr;
	}

}
