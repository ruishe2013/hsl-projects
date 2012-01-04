package com.htc.model;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.bean.BeanForSysArgs;
import com.htc.common.FunctionUnit;
import com.htc.dao.iface.BasePageDao;
import com.htc.dao.iface.SysParamDao;
import com.htc.domain.BackUpList;
import com.htc.domain.Pager;
import com.htc.domain.SysParam;

import com.htc.dao.iface.*;
import com.htc.domain.*;

/**
 * @ SetSysService.java
 * 作用 : 系统设置模块. gprs管理. 报警电话管理. 备份管理. 其余设置管理. 
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-5     YANGZHONLI       create
 */
public class SetSysService {
	
	private Log log = LogFactory.getLog(SetSysService.class);

	//private static SetSysService setSysService = new SetSysService();
	
	private GprsSetDao gprsSetDao;
	private PhoneListDao phoneListDao;
	private BackUpListDao backUpListDao;
	private SysParamDao sysParamDao;	
	//private DaoManager daoMgr = DaoConfig.getDaoManager();
	private BasePageDao basePageDao;// 公共分页dao
	
	
	/**
	 * 更新警报设置
	 */
	public static final int SYSTEM_UPDATE_ALARM = 1;
	/**
	 * 更新备份设置
	 */
	public static final int SYSTEM_UPDATE_BACKUP = 2;
	/**
	 * 更新颜色，间隔时间，温度显示类型等
	 */
	public static final int SYSTEM_UPDATE_SYS = 3;
	
	public SetSysService() {
	}
	
	//注册service -- spring ioc
	public void setGprsSetDao(GprsSetDao gprsSetDao) {
		this.gprsSetDao = gprsSetDao;
	}
	public void setPhoneListDao(PhoneListDao phoneListDao) {
		this.phoneListDao = phoneListDao;
	}
	public void setBackUpListDao(BackUpListDao backUpListDao) {
		this.backUpListDao = backUpListDao;
	}
	public void setSysParamDao(SysParamDao sysParamDao) {
		this.sysParamDao = sysParamDao;
	}
	public void setBasePageDao(BasePageDao basePageDao) {
		this.basePageDao = basePageDao;
	}

	// 获取程序单例
//	public static SetSysService getInstance() {
//		//setSysService = setSysService == null ?	new SetSysService() : setSysService ;
//		return setSysService;
//	}	

	//GprsSet-编辑
	public GprsSet getGprsSetById(int gprsSetId) {
		
		// 实例化服务类
		//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;
		
		GprsSet gprsSet = new GprsSet();
		gprsSet.setGprsSetId(gprsSetId);	
		
		try {
			if ( gprsSetDao.findGprsSet(gprsSet).size() <= 0 ){
				gprsSet = null;
			}else{
				gprsSet = gprsSetDao.findGprsSet(gprsSet).get(0);
			}
		} catch (Exception e) {
			log.error("获取GprsSet-编辑  时出错:" + e.getMessage()+e.toString());
		} 
		return gprsSet;
	}

	//GprsSet-numId和alias是检测唯一性	
	private List<GprsSet> getGprsSetByName(int numId, String alias){
		
		// 实例化服务类
		//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;
				
		GprsSet gprsSet = new GprsSet();
		gprsSet.setNumId(numId);
		gprsSet.setAlias(alias);
		List<GprsSet> gprsSetList = new ArrayList<GprsSet>();
		try {
			gprsSetList = gprsSetDao.findGprsSet(gprsSet);
		} catch (Exception e) {
			log.error("获取GprsSet-name  时出错:" + e.getMessage()+e.toString());
		}
		return gprsSetList;
	}

	//返回true:插入成功  FALSE：插入失败
	public boolean insertGprsSet(GprsSet gprsSet){
		
		List<GprsSet> rsGprsSet = getGprsSetByName(gprsSet.getNumId(),gprsSet.getAlias());
		
		boolean existFlag = rsGprsSet.size() == 0;
		if (existFlag) {
			
			// 实例化服务类
			//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;
			
			try {
				gprsSetDao.insertGprsSet(gprsSet);
			} catch (Exception e) {
				log.error("增加GprsSet 时出错:" + e.getMessage()+e.toString());
			}
		}
		return existFlag;
	}
	
	public boolean insertGprsSetBatch(List<GprsSet> gprsSetList){
		boolean rsbool = true;
		// 实例化服务类
		//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;	
		try {
			gprsSetDao.insertEquipDataBatch(gprsSetList);
		} catch (Exception e) {
			rsbool = false;
			log.error("批量增加GprsSet 时出错:" + e.getMessage()+e.toString());
		}
		
		return rsbool;
	}	
	
	//返回true:修改成功  FALSE：修改失败
	public boolean updateGprsSet(GprsSet gprsSet){
		List<GprsSet> rsGprsSet = getGprsSetByName(gprsSet.getNumId(),gprsSet.getAlias());

		boolean existFlag = rsGprsSet.size() <= 1; 
		if (existFlag){
			
			// 实例化服务类
			//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;
					
			try {
				gprsSetDao.updateGprsSet(gprsSet);
			} catch (Exception e) {
				log.error("修改GprsSet 时出错:" + e.getMessage()+e.toString());
			}
		}
		return existFlag;
	}

	public void deleteGprsSet(int gprsSetId){
		
		// 实例化服务类
		//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;
		
		try {
			gprsSetDao.deleteGprsSet(gprsSetId);
		} catch (Exception e) {
			log.error("删除GprsSet 时出错:" + e.getMessage()+e.toString());		
		}
	}

	public void deleteGprsSetBatch(int[] gprsSetIds) {
		
		// 实例化服务类
		//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;
		
		try {
			gprsSetDao.deleteBatch(gprsSetIds);
		} catch (Exception e) {
			log.error("批量删除GprsSet 时出错:" + e.getMessage()+e.toString());
		}
	}
	
	public List<GprsSet> getAllSetLists(){
		
		// 实例化服务类
		//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;
		
		List<GprsSet> grpsSetList = new ArrayList<GprsSet>(); 
		try {
			grpsSetList = (List<GprsSet>)gprsSetDao.findAllSetList();
		} catch (Exception e) {
			log.error("获取GprsSet列表 时出错:" + e.getMessage()+e.toString());
		}
		return grpsSetList;
	}	
	
	//------------------------------------------------//
	// 根据手机主键列表获取,手机号码列表
	public  List<String> getPhones(String listIds){
		List<String> phoneList = new ArrayList<String>();
		
		// 实例化服务类
		//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;
		// 过滤空字符串
		if ( (listIds != null && listIds.length() > 0) ){
			List<PhoneList> phonelists = new ArrayList<PhoneList>();
			try {
				phonelists = phoneListDao.findPhoneList(listIds);
			} catch (Exception e) {
				log.error("获取PhoneList-主键列表  时出错:" + e.getMessage()+e.toString());
			}
			// 从查询结果中抽取手机号码
			if ( phonelists != null){
				for (PhoneList phone : phonelists) {
					phoneList.add(phone.getPhone());
				}
			}		
		}
		return phoneList;
	}
	
	//PhoneList-编辑
	public PhoneList getPhoneListById(int listId) {
		
		// 实例化服务类
		//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;
		
		PhoneList phoneList = new PhoneList();
		phoneList.setListId(listId);
		try {
			if (phoneListDao.findPhoneList(phoneList).size()<=0){
				phoneList = null;
			}else{
				phoneList = phoneListDao.findPhoneList(phoneList).get(0);
			}
		} catch (Exception e) {
			log.error("获取PhoneList-编辑 时出错:" + e.getMessage()+e.toString());
		}
		return phoneList;
	}

	//PhoneList-name和phone是检测唯一性	
	private List<PhoneList> getPhoneListByName(String name, String phone) {
		
		// 实例化服务类
		//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;		
		
		PhoneList phoneList = new PhoneList();
		phoneList.setName(name);
		phoneList.setPhone(phone);
		List<PhoneList> phonelist = new ArrayList<PhoneList>();
		try {
			phonelist = phoneListDao.findPhoneList(phoneList);
		} catch (Exception e) {
			log.error("获取PhoneList-byName 时出错:" + e.getMessage()+e.toString());
		}
		return phonelist;
	}

	//返回true:插入成功  FALSE：插入失败
	public boolean insertPhoneList(PhoneList phoneList) {
		List<PhoneList> phoneListrs = getPhoneListByName(phoneList.getName(),phoneList.getPhone());
		
		boolean existFlag = phoneListrs.size() == 0;
		if (existFlag) {
			
			// 实例化服务类
			//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;			
			
			try {
				phoneListDao.insertPhoneList(phoneList);
			} catch (Exception e) {
				log.error("增加PhoneList 时出错:" + e.getMessage()+e.toString());
			}
		}
		return existFlag;
	}
	
	//返回true:修改成功  FALSE：修改失败
	public boolean updatePhoneList(PhoneList phoneList){
		
		List<PhoneList> phoneListrs = getPhoneListByName(phoneList.getName(),phoneList.getPhone());
		
		boolean existFlag = phoneListrs.size() <= 1;
		if (existFlag) {

			// 实例化服务类
			//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;
			
			try {
				phoneListDao.updatePhoneList(phoneList);
			} catch (Exception e) {
				log.error("修改PhoneList 时出错:" + e.getMessage()+e.toString());
			}
		}
		return existFlag;
	}

	public void deletePhoneList(int phoneListId) {
		
		// 实例化服务类
		//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;		
		
		try {
			phoneListDao.deletePhoneList(phoneListId);
		} catch (Exception e) {
			log.error("删除PhoneList 时出错:" + e.getMessage()+e.toString());
		}
	}

	public void deletePhoneListBatch(int[] phoneListIds) {
		
		// 实例化服务类
		//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;		
		
		try {
			phoneListDao.deleteBatch(phoneListIds);
		} catch (Exception e) {
			log.error("批量删除PhoneList 时出错:" + e.getMessage()+e.toString());
		}
	}
	
	public List<PhoneList> getAllPhoneLists(){
		
		// 实例化服务类
		//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;		
		
		List<PhoneList> phonelist = new ArrayList<PhoneList>();
		try {
			phonelist = (List<PhoneList>)phoneListDao.findAllPhoneList();
		} catch (Exception e) {
			log.error("获取PhoneList-list 时出错:" + e.getMessage()+e.toString());
		}
		return phonelist;
	}
	
	/**
	 * @describe : 获取电话号码列表
	 * @date:2009-11-5
	 */
	public Map<Integer, String> getAllPhoneList() {
		List<PhoneList> phoneListRs;
		Map<Integer, String> lists = new HashMap<Integer, String>();
		
		phoneListRs = getAllPhoneLists();
		for (PhoneList phoneList : phoneListRs) {
			lists.put(phoneList.getListId(), phoneList.getPhone());
		}
		
		return lists;
	}
	
	//----------------------------------------------//
	//BackUpListDao
	/**
	 * @describe: 获取 最后的备份时间	
	 * @return:
	 * @date:2010-1-5
	 */
	public Date getBackUpLastTime(){
		Date rsDate = null;
		// 实例化服务类
		//backUpListDao = backUpListDao == null ?(BackUpListDao) daoMgr.getDao(BackUpListDao.class) : backUpListDao ;
		try {
			rsDate = FunctionUnit.getStrToDate(backUpListDao.getLastTime());
		} catch (Exception e) {
			log.error("获取 最后的备份时间时出错:" + e.getMessage()+e.toString());
		}		
		return rsDate;
	} 	
	
	public void insertBackUpList(BackUpList backUpList){
		
		// 实例化服务类
		//backUpListDao = backUpListDao == null ?(BackUpListDao) daoMgr.getDao(BackUpListDao.class) : backUpListDao ;		
		
		try {
			backUpListDao.insertBackUpList(backUpList);
		} catch (Exception e) {
			log.error("增加历史备份excel时出错:" + e.getMessage()+e.toString());
		}
	}	
	
	public void packTBackUp(String fileName, String remark){
		BackUpList backUpList = new BackUpList();
		backUpList.setFileName(fileName);
		backUpList.setBacktime(new Date());
		backUpList.setRemark(remark);
		insertBackUpList(backUpList);
	}	
	
	public void deleteBackUpList(int backId){
		
		// 实例化服务类
		//backUpListDao = backUpListDao == null ?(BackUpListDao) daoMgr.getDao(BackUpListDao.class) : backUpListDao ;			
		
		try {
			backUpListDao.deleteBackUpListById(backId);
		} catch (Exception e) {
			log.error("删除备份 时出错:" + e.getMessage()+e.toString());
		}
	}

	public void deleteBackUpBatch(int[] backIds) {
		
		// 实例化服务类
		//backUpListDao = backUpListDao == null ?(BackUpListDao) daoMgr.getDao(BackUpListDao.class) : backUpListDao ;			
		
		try {
			backUpListDao.deleteBatch(backIds);
		} catch (Exception e) {
			log.error("批量删除备份 时出错:" + e.getMessage()+e.toString());
		}
	}
	
	// SysParam
	public List<SysParam> selectAllParam() {
				
		// 实例化服务类
		//sysParamDao = sysParamDao == null ?(SysParamDao) daoMgr.getDao(SysParamDao.class) : sysParamDao;
		
		List<SysParam> sysParamList = null; 
		try {
			sysParamList = sysParamDao.selectAllParam();
		} catch (Exception e) {
			log.error("获取仪器配置信息时出错:" + e.getMessage()+e.toString());
		}
		return sysParamList;
	}
	
	public Map<String, String> getSysParamMap() {
		
		Map<String, String> argsMap = new HashMap<String, String>();
		
		List<SysParam> sysParamList = selectAllParam();
		if ( (sysParamList == null)||(sysParamList.size()<=0) ){	// 获取失败就使用默认参数
			argsMap = BeanForSysArgs.getDefaultSysArgsMap();
		}else{
			for (SysParam sysParam : sysParamList) {
				argsMap.put(sysParam.getArgsKey(), sysParam.getArgsValue());
			}
		}
		return argsMap;
	}
	
	public boolean updateSysParam(Map<String, String> argsMap) {
		boolean bool = true;
		
		// 实例化服务类
		//sysParamDao = sysParamDao == null ?(SysParamDao) daoMgr.getDao(SysParamDao.class) : sysParamDao;		
		
		try {
			sysParamDao.updateSysParam(argsMap);
		} catch (Exception e) {
			bool = false;
			log.error("更新仪器配置信息时出错:" + e.getMessage()+e.toString());
		}
		return bool;
	}
	
	public boolean updateSysParam(List<SysParam> sysParamList) {
		boolean bool = true;
		// 实例化服务类
		//sysParamDao = sysParamDao == null ?(SysParamDao) daoMgr.getDao(SysParamDao.class) : sysParamDao;		
				
		try {
			sysParamDao.updateSysParam(sysParamList);
		} catch (Exception e) {
			bool = false;
			log.error("更新仪器配置信息时出错:" + e.getMessage()+e.toString());
		}
		return bool;
	}
	
	/**
	 * @describe : 分页-获取一段记录
	 * @param nameSpace : 空间名. 取GprsSet.xml中，sqlMap namespace="GprsSet"的值
	 * 							  取PhoneList.xml中，sqlMap namespace="PhoneList"的值	
	 * 							  取BackUpList.xml中，sqlMap namespace="BackUpList"的值	
	 * @param currentPage : 当前页
	 * @param pagerMethod : 翻页的方法. 有前一页,后一页,最前,最后 
	 * @param pageSize : 页面显示的最大数量
	 * @param args : 需要条件搜索是的参数. 参数需要对照,xml和page类
	 * @param limitCount : 搜索结果的可以返回的最大数值
	 * @return Pager 相关的分页信息
	 * @date:2009-11-4
	 */
	public Pager getPager(String nameSpace,String currentPage, String pagerMethod, int pageSize,Map<String, Object> args) {
		// GprsSet为空间名 ：取GprsSet.xml中，<sqlMap namespace="GprsSet">的值
		// PhoneList为空间名 ：取PhoneList.xml中，<sqlMap namespace="PhoneList">的值
		// BackUpList为空间名 ：取BackUpList.xml中，<sqlMap namespace="BackUpList">的值
		
		// 实例化服务类
		//basePageDao = basePageDao == null ?(BasePageDao) daoMgr.getDao(BasePageDao.class) :  basePageDao;		
				
				this.basePageDao.setNameSpace(nameSpace);
				return this.basePageDao.getPager(currentPage, pagerMethod, pageSize, args);
	}
	
	/**
	 * @describe : 分页-获取一段记录
	 * @param nameSpace : 空间名. 取GprsSet.xml中，sqlMap namespace="GprsSet"的值
	 * 							  取PhoneList.xml中，sqlMap namespace="PhoneList"的值	
	 * 							  取BackUpList.xml中，sqlMap namespace="BackUpList"的值	
	 * @param currentPage : 当前页
	 * @param pagerMethod : 翻页的方法. 有前一页,后一页,最前,最后 
	 * @param pageSize : 页面显示的最大数量
	 * @param args : 需要条件搜索是的参数. 参数需要对照,xml和page类
	 * @param limitCount : 搜索结果的可以返回的最大数值
	 * @return Pager 相关的分页信息
	 * @date:2009-11-4
	 */
	public Pager getPager(String nameSpace,String currentPage, String pagerMethod, 
			int pageSize,Map<String, Object> args,int limitCount) {
		// GprsSet为空间名 ：取GprsSet.xml中，<sqlMap namespace="GprsSet">的值
		// PhoneList为空间名 ：取PhoneList.xml中，<sqlMap namespace="PhoneList">的值
		// BackUpList为空间名 ：取BackUpList.xml中，<sqlMap namespace="BackUpList">的值
		
		// 实例化服务类
		//basePageDao = basePageDao == null ? (BasePageDao) daoMgr.getDao(BasePageDao.class) :  basePageDao;		
		
		this.basePageDao.setNameSpace(nameSpace);
		return this.basePageDao.getPager(currentPage, pagerMethod, pageSize, args, limitCount);
	}

}
