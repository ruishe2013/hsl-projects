package com.htc.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.bean.BeanForZjHisRec;
import com.htc.dao.iface.ZjHistroyDao;
import com.htc.dao.iface.ZjMonitorDao;
import com.htc.dao.impl.BaseSqlMapDao;
import com.htc.domain.EquipData;
import com.htc.domain.ZjHistory;
import com.htc.domain.ZjMonitors;

/**
 * @ UserService.java
 * ���� : �û�����
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-5     YANGZHONLI       create
 */
public class ServiceSqlServer {

	private Log log = LogFactory.getLog(ServiceSqlServer.class);
	
	private ZjMonitorDao zjMonitorDao;
	private ZjHistroyDao zjHistroyDao;

	// ���췽��
	public ServiceSqlServer() {
	}
	//ע��service -- spring ioc
	public void setZjMonitorDao(ZjMonitorDao zjMonitorDao) {
		this.zjMonitorDao = zjMonitorDao;
	}
	public void setZjHistroyDao(ZjHistroyDao zjHistroyDao) {
		this.zjHistroyDao = zjHistroyDao;
	}

	/**
	 * @describe: ����sqlServer���ݿ������Ƿ�����
	 * @return: true: �������� false: ���Ӵ���
	 * @date:2010-4-15
	 */
	public boolean testLink(){
		boolean rsbool = true;
		try {
			zjMonitorDao.test4Link();
		} catch (Exception e) {
			rsbool = false;
			log.error("sqlServer���ݿ��������ʧ��:" + e.getMessage()+e.toString());
		}
		return rsbool;
	}
	
	/**
	 * @describe:	��ѯ�ϴ�����ʷ��¼
	 * @param zjBean ��ѯ������Bean
	 * @date:2010-4-13
	 */
	public List<ZjHistory> selectZjHisRec(BeanForZjHisRec zjBean){
		List<ZjHistory> rsList = null;
		try {
			rsList = zjHistroyDao.selectZjHisRec(zjBean);
		} catch (Exception e) {
			rsList = null;
			log.error("sqlServer���ݿ�,��ȡ�����б�ʱ ����:" + e.getMessage()+e.toString());
		}
		return rsList;
	}
	
	/**
	 * @describe:ˢ���е�������Ϣ<br>
	 * @param zjMonitorList ��Ҫ���µ����������б�<br>
	 * @return: true:���³ɹ�  false:����ʧ��
	 * @date:2010-4-13
	 */
	public boolean flashAllMonitors(List<EquipData> newEqList){
		boolean rsBool = true;
		if ((newEqList == null)||newEqList.size()<=0){return rsBool;}
		List<ZjMonitors> newMonitorList = new ArrayList<ZjMonitors>();// ��ȡ���������ݿ�����б�
		List<ZjMonitors> oldMonitorList = selectAllZjMonitor(); // ��ȡ���������ݿ�ľ��б�
		if (oldMonitorList == null){return rsBool;}
		List<ZjMonitors> tempList = new ArrayList<ZjMonitors>();
		
		for (EquipData eqData : newEqList) {	//  �������
			if (eqData.getShowAccess() == 1){
				newMonitorList.add(fillMonitor(eqData));
			}
		}
		
		// ɾ�� ������
		tempList = compareMonitor(oldMonitorList, newMonitorList, 2);
		if (tempList !=null && tempList.size() > 0){
			rsBool &= doBatchMonitor(tempList, BaseSqlMapDao.BATCH_DELETE);
			// log.info("Sqlserver - ����������ɾ��");
		}
		// ���� ������
		tempList = compareMonitor(newMonitorList, oldMonitorList, 1);
		if (tempList !=null && tempList.size() > 0){
			rsBool &= doBatchMonitor(tempList, BaseSqlMapDao.BATCH_UPDATE);
			// log.info("Sqlserver - �����������޸�");			
		}
		// ���� ������
		tempList = compareMonitor(newMonitorList, oldMonitorList, 2);
		if (tempList !=null && tempList.size() > 0){
			rsBool &= doBatchMonitor(tempList, BaseSqlMapDao.BATCH_INSERT);
			// log.info("Sqlserver - ��������������");			
		}
		
		return rsBool;
	}
	
	public ZjMonitors fillMonitor(EquipData eqData){
		ZjMonitors zjMonitor = new ZjMonitors();
		zjMonitor.setMonitorID(eqData.getEquipmentId());	// ��Ӧ����������ID
		zjMonitor.setMonitorName(eqData.getPlaceStr()+"-"+eqData.getMark());// ������
		zjMonitor.setStoreID(Integer.parseInt(eqData.getDsrsn()));// ��֪��ʲô����,��ʱ ��д LicenseNoһ��������
		zjMonitor.setT_High(eqData.getTempUp());	// ��ߵ������¶�
		zjMonitor.setT_Low(eqData.getTempDown());	// ��͵������¶�
		zjMonitor.setH_High(eqData.getHumiUp());	// ��ߵ�����ʪ��
		zjMonitor.setH_Low(eqData.getHumiDown());	// ��͵�����ʪ��
		
		// ������Ҫ��Access�е� strDSRSN�ֶζ�Ӧ -- ϵͳ��strDSRSN�� ҩ����·��ı�� ��Ӧ--->>>����Ҳ��д LicenseNoһ��������
		zjMonitor.setNoID(Integer.parseInt(eqData.getDsrsn()));
		
		zjMonitor.setLicenseNo(Integer.parseInt(eqData.getDsrsn()));// ��Ӧҩ����·��ı��
		zjMonitor.setSendcmd("");	// ��ʱ����
		zjMonitor.setZgCode("");	// ��ʱ����
		return zjMonitor;
	}
	
	
	/**
	 * @describe:��left��ȡ��right����ͬ����id��Ԫ�ؼ���
	 * @param type:
	 * 1:��ȡ��ͬ��Ԫ��
	 * 2:��ȡ����ͬ��Ԫ��
	 * @date:2010-4-14
	 */
	private List<ZjMonitors> compareMonitor(List<ZjMonitors> left, List<ZjMonitors> right, int type){
		if (left == null){return null;}
		boolean foundFlag = false;
		List<ZjMonitors> tempList = new ArrayList<ZjMonitors>();
		for (ZjMonitors zjMonitors : left) {// ��old��������������
			
			if (right == null){
				foundFlag = false;
			}else{
				for (ZjMonitors zjMoni : right) {// ��new���ݽ��бȽ�
					if (zjMonitors.getMonitorID() == zjMoni.getMonitorID()){
						foundFlag = true;
						break;
					}
				}// ��ѭ��
			}
			
			if (type == 1){// 1:��ȡ��ͬ��Ԫ��
				if(foundFlag){
					tempList.add(zjMonitors);
				}				
			}else if (type ==2){// 2:��ȡ����ͬ��Ԫ��
				if(!foundFlag){
					tempList.add(zjMonitors); 
				}				
			}
			foundFlag = false;
		}// ��ѭ��			
		return tempList;
	}
	
	private List<ZjMonitors> selectAllZjMonitor(){
		List<ZjMonitors> oldMonitorList = null;		
		try {
			oldMonitorList = zjMonitorDao.selectAllZjMonitor();
		} catch (Exception e) {
			oldMonitorList = null;
			log.error("sqlServer�о�������Ϣʱ ����:" + e.getMessage()+e.toString());
		}		
		return oldMonitorList;
	}
	
	/**
	 * @describe: ����������
	 * @param zjMoniList ������������
	 * @param batchType  ������������
	 * @return: true:����ɹ� false:�������
	 * @date:2010-4-14
	 */
	private boolean doBatchMonitor( List<ZjMonitors> zjMoniList, int batchType){
		boolean rsBool = true;
		try {
			zjMonitorDao.doBatchMonitor(zjMoniList, batchType);
		} catch (Exception e) {
			rsBool = false;
			switch (batchType) {
			case BaseSqlMapDao.BATCH_INSERT:
				log.error("sqlServer���������Ϣʱ ����:" + e.getMessage()+e.toString());
				break;
			case BaseSqlMapDao.BATCH_UPDATE:
				log.error("sqlServer�޸�������Ϣʱ ����:" + e.getMessage()+e.toString());
				break;
			case BaseSqlMapDao.BATCH_DELETE:
				log.error("sqlServer�޸�������Ϣʱ ����:" + e.getMessage()+e.toString());
				break;
			}
		}		
		return rsBool;
	}
	
}
