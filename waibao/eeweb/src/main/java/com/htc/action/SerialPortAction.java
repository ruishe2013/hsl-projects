package com.htc.action;

import java.util.*;

import com.htc.domain.*;
import com.htc.model.seriaPort.*;

/**
 * @ SerialPortAction.java
 * 作用 : 串口运行 - 数据读取action.
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class SerialPortAction extends AbstractActionForOEM {
	
	// 页面
	private String stateStr="";						// 页面显示状态字符
	private int runFlag;							// 1:正在运行	2:没有运行
	private int showTipMsg=0;						// 是否显示错误标记(0:不显示 1:显示)
	private String infile;							// 导入的文件名

	private Level_First_Serial first_Level;
	//注册service -- spring ioc
	public void setFirst_Level(Level_First_Serial first_Level) {
		this.first_Level = first_Level;
	}
	//构造函数
	public SerialPortAction() {
	}
	
	// 页面显示 - 进入页面的3种状态
	public String execute() {
		if( getEquiListSize() == 0 ){
			setPageState(2,1);
		}else{
			if (getSerialRunState()){
				setPageState(1,2);
			}else{
				setPageState(2,3);
			}
		}
		return SUCCESS;
	}
	
	// 开启串口运行
	public String startRun(){
		int rs = first_Level.startRunSerial();
		if (rs == 0){				// 运行正常 
			setPageState(1,2);
		}else if(rs == 1){			// 没有可用的仪器(说明没有添加仪器)
			setPageState(2,1);
		}else if(rs == 2){			// 串口连接出错
			setPageState(2,3,"串口连接失败"); 	
		}
		return SUCCESS;
	}
	
	// 停止串口运行
	public String endRun(){
		if (first_Level.endRunSerial()){
			setPageState(2,3,"正常停止...");
		}else{
			setPageState(1,3,"停止失败...");
		}
		return SUCCESS;
	}	
	
	// 重启
	public String restart(){
		showTipMsg = 1;
		endRun();
		startRun();
		addActionMessage(stateStr);
		return SUCCESS;
	}
	
	/**
	 * @describe: 获取串口运行状态	
	 * @return: true:正在运行 false:没有运行
	 * @date:2009-12-22
	 */
	public boolean getSerialRunState(){
		boolean rsbool = true;
		
		// 1: 判断串口是否在运行
		rsbool = first_Level.isRunningFlag();
		
		return rsbool;
	}
	
	/**
	 * @describe: 获取可用仪器列表个数
	 * @date:2009-12-22
	 */
	public int getEquiListSize(){
		List<EquipData> equiList = commonDataUnit.getEquiList();
		return equiList.size();
	}	
	
	/**
	 * @describe: 设置页面显示信息	
	 * @param flag 运行状态[1:正在运行	2:没有运行]
	 * @param msgType:消息类型
	 * [1:仪器列表为空,暂时不能运行串口... 2:串口正在运行...	3:串口还没有开始运行...]
	 * @param more: 附加内容
	 * @date:2009-12-22
	 */
	public void setPageState(int flag, int msgType, String more){
		runFlag = flag;
		if (msgType == 1){
			stateStr = "仪器列表为空,暂时不能运行串口...";
		}else if (msgType == 2){
			stateStr = "串口正在运行...";
		}else if (msgType == 3){
			stateStr = "串口还没有开始运行...";
		}
		if (!more.equals("")){stateStr = more;} 
	}
	/**
	 * @describe: 设置页面显示信息	
	 * @param flag 运行状态[1:正在运行	2:没有运行]
	 * @param msgType:消息类型
	 * [1:仪器列表为空,暂时不能运行串口... 2:串口正在运行...	3:串口还没有开始运行...]
	 * @date:2009-12-22
	 */	
	public void setPageState(int flag, int msgType){
		setPageState(flag, msgType,"");
	}		
	
	// ----------------以下为get，set方法----------------
	public String getStateStr() {
		return stateStr;
	}

	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}

	public int getRunFlag() {
		return runFlag;
	}

	public void setRunFlag(int runFlag) {
		this.runFlag = runFlag;
	}

	public int getShowTipMsg() {
		return showTipMsg;
	}

	public void setShowTipMsg(int showTipMsg) {
		this.showTipMsg = showTipMsg;
	}

	public String getInfile() {
		return infile;
	}

	public void setInfile(String infile) {
		this.infile = infile;
	}
	
}
