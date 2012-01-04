package com.htc.model.quartz;


import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.htc.common.*;
import com.htc.domain.EquipData;
import com.htc.model.seriaPort.*;

/**
 * @ TaskForInstantData.java
 * 作用 : 即时数据作业任务.
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
//public class TaskForInstantData  implements Job {
public class TaskForInstantData  extends QuartzJobBean {
	
	private Log log = LogFactory.getLog(TaskForInstantData.class);
	private CommonDataUnit commonDataUnit;
	private Level_First_Serial first_Level;
	//注册service -- spring ioc
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}
	public void setFirst_Level(Level_First_Serial first_Level) {
		this.first_Level = first_Level;
	}

	// 需要一个空的构造函数，调度器在调度的时候可以用这个构造函数初始化对象
	public TaskForInstantData() {
	}

	// 即时数据作业
	//public void execute(JobExecutionContext context) throws JobExecutionException {
	public void executeInternal(JobExecutionContext ctx) throws JobExecutionException {	
		
		// 预备第一步:判断是否可以运行任务(主要应用于:暂停串口时,必须禁止执行串口任务的进入,不然串口关闭时,任然有串口通信,这样会出错)
		// log.info("①-init:开始新的任务...");	
		if (!first_Level.isCanDoTask()){
			log.info("①-init:即时任务暂停...");	
			return;
		}
		// 预备第二步:保证在串口运行下执行任务
		if (!first_Level.isRunningFlag()){
			log.info("①-init:即时任务没有开始,原因:串口没有打开...");	
			return;
		}

		// 预备第三步:保证前面不存在正在采集数据的任务
		if (commonDataUnit.isEqDoingData() == false){
			// 正在采集数据
			commonDataUnit.setEqDoingData(true);
		}else{
			log.info("①-0:运行时,前面的任务还没有结束...");	
			// 有线程在运行,退出
			return;
		}
		
		// 预备工作完成,进入采集任务
		//long start = System.currentTimeMillis();
		// 串口工作类
		//Level_First_Serial first_Level = Level_First_Serial.getInstance();
		
		int equipmentId;		// 仪器ID
		int address;			// 仪器硬件地址
		int runequiCount = 0;	// 在运行的仪器个数
		int allEqCount = 0;		// 全部仪器个数
			
		// 1:及时数据采集
		try {
			// 获取全部的仪器信息 -> 获取单个 仪器  ->  发送读取温湿度的串口命令
			//for (int i = 0; i < 2000; i++) {
				for (EquipData equipData : commonDataUnit.getEquiMap().values()) {
					allEqCount ++;
					if (equipData == null){continue;}
					equipmentId = equipData.getEquipmentId();
					address = equipData.getAddress();
					if (first_Level.writeReadSerial(address, equipmentId, Level_Second_Serial.READ_USEFULL_DATA, equipData.getConndata()) 
							!= Level_Final_Serial.Serial_right_Frame ){
						runequiCount++;
					};
				}// end for
			//}
			
			if (allEqCount == runequiCount){//所有仪器全部丢帧,可能是串口没有插好
				// 检查串口是否连接正常
				// first_Level.checkRunningPort(); // 暂时不检测
				if (allEqCount > 0){
					log.info("即时数据作业-通信异常-全部是丢帧或失帧...");
				}else{
					log.info("即时数据作业-失败--因为仪器列表为空...");
				}
			}
		}catch (Exception e) {
			log.error("即时数据采集时出错:" + e.getMessage()+e.toString());
		}
		//start = System.currentTimeMillis() - start;
		//log.info("①-1:即时数据作业运行一次>...>...>用时"+ start +"毫秒");
		
		// 2: 核对时间 
		if (runequiCount !=0 ){
			try {
				// 0点0分到0点1分内校准时间
				if (FunctionUnit.getCurTime(Calendar.HOUR_OF_DAY) == 0){
					if (FunctionUnit.getCurTime(Calendar.MINUTE) < 2){
						//start = System.currentTimeMillis();
						try {
							// 核对时间
							first_Level.writeTime(2);
						} catch (Exception e) {
							log.error("核对时间任务时出错:" + e.getMessage()+e.toString());
						}
						//start = System.currentTimeMillis() - start;
						//log.info("①-2:核对时间 作业运行一次>...>...>用时"+ start +"毫秒");								
					}
				}
			} catch (Exception e) {
				log.error("判断时间在0点0分到0点5分内时出错-[核对时间任务]:" + e.getMessage()+e.toString());
			}
		}//end
		
		// 采集数据结束
		commonDataUnit.setEqDoingData(false);
	}

}
