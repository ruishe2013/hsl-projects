package com.htc.model;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.common.CommonDataUnit;
import com.htc.dao.iface.*;
import com.htc.domain.*;

/**
 * @ UserService.java
 * 作用 : 处理和accsess相关的业务
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-5     YANGZHONLI       create
 */
public class ServiceAccess { 

	private Log log = LogFactory.getLog(ServiceAccess.class);
	
//	private DaoManager daoMgr = Dao4AccessConfig.getDaoManager();
//	private static ServiceAccess accessService = new ServiceAccess();
	private TAccessDao accessDao; 
	private CommonDataUnit commonDataUnit; 

	// 构造方法  
	public ServiceAccess() {
	} 
	//注册service -- spring ioc
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}
	public void setAccessDao(TAccessDao accessDao) {
		this.accessDao = accessDao;
	}
	
	/**
	 * @describe:	获取 ManaService  程序单例
	 * @date:2009-11-5
	 */
//	public static ServiceAccess getInstance() {
//		return accessService;
//	}	

	public List<Data4Access> getList(){
		List<Data4Access> rslist = null;
		// 实例化服务类
		//accessDao = accessDao == null ? (TAccessDao) daoMgr.getDao(TAccessDao.class) : accessDao;
		try {
			rslist = accessDao.getlist();
		} catch (Exception e) {
			rslist = null;
			log.error("access数据库,获取动态数据列表时 出错:" + e.getMessage()+e.toString());
		}
		return rslist;
	}
	
	public boolean testLink(){
		boolean rsbool = true;
		// 实例化服务类
		//accessDao = accessDao == null ? (TAccessDao) daoMgr.getDao(TAccessDao.class) : accessDao;
		try {
			accessDao.getlist();
			// 设置全局连接变量
			commonDataUnit.setAccessLinkState(true);
		} catch (Exception e) {
			// 设置全局连接变量
			commonDataUnit.setAccessLinkState(false);
			rsbool = false;
			log.error("access数据库,测试连通性时 出错:" + e.getMessage()+e.toString());
		}
		return rsbool;
	}
	
	public boolean insertBatch(List<Data4Access> datas){
		boolean rsbool = true;
		if ((datas == null) || (datas.size() == 0)){return rsbool;	}
		
		// 实例化服务类
		// accessDao = accessDao == null ? (TAccessDao) daoMgr.getDao(TAccessDao.class) : accessDao;
		try {
			accessDao.insertBatch(datas);
		} catch (Exception e) {
			// 设置全局连接变量
			//if (! testLink()){commonDataUnit.setAccessLinkState(false);};			
			rsbool = false;
			log.error("access数据库,增加动态数据时 出错:" + e.getMessage()+"<>"+e.toString());
		}
		return rsbool;
	}
	
	public boolean updataBatch(List<Data4Access> datas){
		boolean rsbool = true;
		if ((datas == null) || (datas.size() == 0)){return rsbool;}
		
		// 实例化服务类
		// accessDao = accessDao == null ? (TAccessDao) daoMgr.getDao(TAccessDao.class) : accessDao;
		try {
			accessDao.updateBatch(datas);
		} catch (Exception e) {
			// 设置全局连接变量
			//if (! testLink()){commonDataUnit.setAccessLinkState(false);};
			rsbool = false;
			log.error("access数据库,修改动态数据时 出错:" + e.getMessage()+e.toString());
		}
		return rsbool;
	}

	public boolean deleteAll() {
		boolean rsbool = true;
		
		// 实例化服务类
		// accessDao = accessDao == null ? (TAccessDao) daoMgr.getDao(TAccessDao.class) : accessDao;
		try {
			accessDao.deleteAll();
		} catch (Exception e) {
			// 设置全局连接变量
			//if (! testLink()){commonDataUnit.setAccessLinkState(false);};			
			rsbool = false;
			log.error("access数据库,删除动态数据时 出错:" + e.getMessage()+e.toString());
		}
		return rsbool;
	}
	
}
