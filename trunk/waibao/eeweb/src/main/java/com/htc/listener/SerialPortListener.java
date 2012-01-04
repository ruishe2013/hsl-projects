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
	//ע��service -- spring ioc	
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
		//log.info("�̳�ServletContextListener�ĳ�ʼ��...");
//		WebApplicationContext wa = WebApplicationContextUtils.getWebApplicationContext(arg0.getServletContext());
//		commonDataUnit = (CommonDataUnit) wa.getBean("commonDataUnit");
//		first_Level = (Level_First_Serial) wa.getBean("first_Level");
//		System.out.println(comm.getEquiList().size()+"-------------------");
//		//startSerial();
//      //IXXXService xxxService = (IXXXService )wa.getBean("xxxService ");      
	}	
	
	public void onApplicationEvent(ApplicationEvent arg0) {		
		//log.info("�̳�ApplicationListener�ĳ�ʼ��...");
		startSerial();
	}
	/**
	 * @describe: ��ʼ����	
	 * @date:2010-4-1
	 */
	private void startSerial() throws NumberFormatException {
		// ���ʱ����һ�µķ���
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
		TimeZone.setDefault(tz);	
		
		if (ResetDatabase.checkDBExist()){
			// ��ʼ��ϵͳ: access �� ����&ʵʱ������صı� ��ش���
			commonDataUnit.resetSystem(false,false,true,false,true);
			
			// �򿪴��� (0:�������� 1:û�п��õ�����(˵��û���������) 2:�������ӳ���)
			int runRs = first_Level.startRunSerial();
			if (runRs==0){
				log.info("����������������...");
				if (Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_SHORT_MESSAGE))==2){
					// ��������ģ��
					simCard_Unit.openPort(first_Level.getPortStr(), 9600);
				}
			}else if (runRs==1){
				log.info("�����޷���������.ԭ��:û�з��������б�...");
			}else if (runRs==2){
				log.info("�����޷���������.ԭ��:�������ӳ���...");
			}
		}
	}
	
	public void contextDestroyed(ServletContextEvent arg0) {
		log.info("�̳�ServletContextListener���˳�...");
		if (ResetDatabase.checkDBExist()){
			first_Level.endTask();
			//Smslib_SendJob.getInstance().stopService();
			simCard_Unit.closePort();
			log.info("�����Ѿ��رմ�������...");
		}
	}

}
