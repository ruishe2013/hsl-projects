package com.htc.model;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.common.CommonDataUnit;
import com.htc.dao.iface.*;
import com.htc.domain.*;

/**
 * @ UserService.java
 * ���� : �����accsess��ص�ҵ��
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-5     YANGZHONLI       create
 */
public class ServiceAccess { 

	private Log log = LogFactory.getLog(ServiceAccess.class);
	
//	private DaoManager daoMgr = Dao4AccessConfig.getDaoManager();
//	private static ServiceAccess accessService = new ServiceAccess();
	private TAccessDao accessDao; 
	private CommonDataUnit commonDataUnit; 

	// ���췽��  
	public ServiceAccess() {
	} 
	//ע��service -- spring ioc
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}
	public void setAccessDao(TAccessDao accessDao) {
		this.accessDao = accessDao;
	}
	
	/**
	 * @describe:	��ȡ ManaService  ������
	 * @date:2009-11-5
	 */
//	public static ServiceAccess getInstance() {
//		return accessService;
//	}	

	public List<Data4Access> getList(){
		List<Data4Access> rslist = null;
		// ʵ����������
		//accessDao = accessDao == null ? (TAccessDao) daoMgr.getDao(TAccessDao.class) : accessDao;
		try {
			rslist = accessDao.getlist();
		} catch (Exception e) {
			rslist = null;
			log.error("access���ݿ�,��ȡ��̬�����б�ʱ ����:" + e.getMessage()+e.toString());
		}
		return rslist;
	}
	
	public boolean testLink(){
		boolean rsbool = true;
		// ʵ����������
		//accessDao = accessDao == null ? (TAccessDao) daoMgr.getDao(TAccessDao.class) : accessDao;
		try {
			accessDao.getlist();
			// ����ȫ�����ӱ���
			commonDataUnit.setAccessLinkState(true);
		} catch (Exception e) {
			// ����ȫ�����ӱ���
			commonDataUnit.setAccessLinkState(false);
			rsbool = false;
			log.error("access���ݿ�,������ͨ��ʱ ����:" + e.getMessage()+e.toString());
		}
		return rsbool;
	}
	
	public boolean insertBatch(List<Data4Access> datas){
		boolean rsbool = true;
		if ((datas == null) || (datas.size() == 0)){return rsbool;	}
		
		// ʵ����������
		// accessDao = accessDao == null ? (TAccessDao) daoMgr.getDao(TAccessDao.class) : accessDao;
		try {
			accessDao.insertBatch(datas);
		} catch (Exception e) {
			// ����ȫ�����ӱ���
			//if (! testLink()){commonDataUnit.setAccessLinkState(false);};			
			rsbool = false;
			log.error("access���ݿ�,���Ӷ�̬����ʱ ����:" + e.getMessage()+"<>"+e.toString());
		}
		return rsbool;
	}
	
	public boolean updataBatch(List<Data4Access> datas){
		boolean rsbool = true;
		if ((datas == null) || (datas.size() == 0)){return rsbool;}
		
		// ʵ����������
		// accessDao = accessDao == null ? (TAccessDao) daoMgr.getDao(TAccessDao.class) : accessDao;
		try {
			accessDao.updateBatch(datas);
		} catch (Exception e) {
			// ����ȫ�����ӱ���
			//if (! testLink()){commonDataUnit.setAccessLinkState(false);};
			rsbool = false;
			log.error("access���ݿ�,�޸Ķ�̬����ʱ ����:" + e.getMessage()+e.toString());
		}
		return rsbool;
	}

	public boolean deleteAll() {
		boolean rsbool = true;
		
		// ʵ����������
		// accessDao = accessDao == null ? (TAccessDao) daoMgr.getDao(TAccessDao.class) : accessDao;
		try {
			accessDao.deleteAll();
		} catch (Exception e) {
			// ����ȫ�����ӱ���
			//if (! testLink()){commonDataUnit.setAccessLinkState(false);};			
			rsbool = false;
			log.error("access���ݿ�,ɾ����̬����ʱ ����:" + e.getMessage()+e.toString());
		}
		return rsbool;
	}
	
}
