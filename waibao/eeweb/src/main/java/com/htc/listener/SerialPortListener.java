package com.htc.listener;

import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.htc.bean.BeanForSysArgs;
import com.htc.common.CommonDataUnit;
import com.htc.model.seriaPort.Level_First_Serial;
import com.htc.model.seriaPort.SimCard_Unit;
import com.htc.model.seriaPort.Smslib_SendJob;
import com.htc.model.tool.ResetDatabase;

public class SerialPortListener implements ServletContextListener, ApplicationListener {

	private Log log = LogFactory.getLog(SerialPortListener.class);
	private CommonDataUnit commonDataUnit;
	private Level_First_Serial first_Level;
	private SimCard_Unit simCard_Unit;
	//注册service -- spring ioc	
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}
	public void setFirst_Level(Level_First_Serial first_Level) {
		this.first_Level = first_Level;
	}
	public void setSimCard_Unit(SimCard_Unit simCard_Unit) {
		this.simCard_Unit = simCard_Unit;
	}
	
	public void contextInitialized(ServletContextEvent arg0) {
		//log.info("继承ServletContextListener的初始化...");
//		WebApplicationContext wa = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());
//		commonDataUnit = (CommonDataUnit) wa.getBean("commonDataUnit");
//		first_Level = (Level_First_Serial) wa.getBean("first_Level");
//		System.out.println(comm.getEquiList().size()+"-------------------");
//		//startSerial();
//      //IXXXService xxxService = (IXXXService )wa.getBean("xxxService ");      
	}	
	
	public void onApplicationEvent(ApplicationEvent arg0) {		
		//log.info("继承ApplicationListener的初始化...");
		startSerial();
	}
	/**
	 * @describe: 开始串口	
	 * @date:2010-4-1
	 */
	private void startSerial() throws NumberFormatException {
		// 解决时区不一致的方案
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
		TimeZone.setDefault(tz);	
		
		if (ResetDatabase.checkDBExist()){
			// 初始化系统: access 和 总览&实时曲线相关的表 相关处理
			commonDataUnit.resetSystem(false,false,true,false,true);
			
			// 打开串口 (0:运行正常 1:没有可用的仪器(说明没有添加仪器) 2:串口连接出错)
			int runRs = first_Level.startRunSerial();
			if (runRs==0){
				log.info("程序启动串口正常...");
				if (Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_SHORT_MESSAGE))==2){
					// 开启短信模块
					simCard_Unit.openPort(first_Level.getPortStr(), 9600);
				}
			}else if (runRs==1){
				log.info("程序无法启动串口.原因:没有发现仪器列表...");
			}else if (runRs==2){
				log.info("程序无法启动串口.原因:串口连接出错...");
			}
		}
	}
	
	public void contextDestroyed(ServletContextEvent arg0) {
		log.info("继承ServletContextListener的退出...");
		if (ResetDatabase.checkDBExist()){
			first_Level.endTask();
			//Smslib_SendJob.getInstance().stopService();
			simCard_Unit.closePort();
			log.info("程序已经关闭串口连接...");
		}
	}

}
