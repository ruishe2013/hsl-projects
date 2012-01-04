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
 * ���� : ϵͳ����ģ��. gprs����. �����绰����. ���ݹ���. �������ù���. 
 * ע������ : ��
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
	private BasePageDao basePageDao;// ������ҳdao
	
	
	/**
	 * ���¾�������
	 */
	public static final int SYSTEM_UPDATE_ALARM = 1;
	/**
	 * ���±�������
	 */
	public static final int SYSTEM_UPDATE_BACKUP = 2;
	/**
	 * ������ɫ�����ʱ�䣬�¶���ʾ���͵�
	 */
	public static final int SYSTEM_UPDATE_SYS = 3;
	
	public SetSysService() {
	}
	
	//ע��service -- spring ioc
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

	// ��ȡ������
//	public static SetSysService getInstance() {
//		//setSysService = setSysService == null ?	new SetSysService() : setSysService ;
//		return setSysService;
//	}	

	//GprsSet-�༭
	public GprsSet getGprsSetById(int gprsSetId) {
		
		// ʵ����������
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
			log.error("��ȡGprsSet-�༭  ʱ����:" + e.getMessage()+e.toString());
		} 
		return gprsSet;
	}

	//GprsSet-numId��alias�Ǽ��Ψһ��	
	private List<GprsSet> getGprsSetByName(int numId, String alias){
		
		// ʵ����������
		//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;
				
		GprsSet gprsSet = new GprsSet();
		gprsSet.setNumId(numId);
		gprsSet.setAlias(alias);
		List<GprsSet> gprsSetList = new ArrayList<GprsSet>();
		try {
			gprsSetList = gprsSetDao.findGprsSet(gprsSet);
		} catch (Exception e) {
			log.error("��ȡGprsSet-name  ʱ����:" + e.getMessage()+e.toString());
		}
		return gprsSetList;
	}

	//����true:����ɹ�  FALSE������ʧ��
	public boolean insertGprsSet(GprsSet gprsSet){
		
		List<GprsSet> rsGprsSet = getGprsSetByName(gprsSet.getNumId(),gprsSet.getAlias());
		
		boolean existFlag = rsGprsSet.size() == 0;
		if (existFlag) {
			
			// ʵ����������
			//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;
			
			try {
				gprsSetDao.insertGprsSet(gprsSet);
			} catch (Exception e) {
				log.error("����GprsSet ʱ����:" + e.getMessage()+e.toString());
			}
		}
		return existFlag;
	}
	
	public boolean insertGprsSetBatch(List<GprsSet> gprsSetList){
		boolean rsbool = true;
		// ʵ����������
		//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;	
		try {
			gprsSetDao.insertEquipDataBatch(gprsSetList);
		} catch (Exception e) {
			rsbool = false;
			log.error("��������GprsSet ʱ����:" + e.getMessage()+e.toString());
		}
		
		return rsbool;
	}	
	
	//����true:�޸ĳɹ�  FALSE���޸�ʧ��
	public boolean updateGprsSet(GprsSet gprsSet){
		List<GprsSet> rsGprsSet = getGprsSetByName(gprsSet.getNumId(),gprsSet.getAlias());

		boolean existFlag = rsGprsSet.size() <= 1; 
		if (existFlag){
			
			// ʵ����������
			//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;
					
			try {
				gprsSetDao.updateGprsSet(gprsSet);
			} catch (Exception e) {
				log.error("�޸�GprsSet ʱ����:" + e.getMessage()+e.toString());
			}
		}
		return existFlag;
	}

	public void deleteGprsSet(int gprsSetId){
		
		// ʵ����������
		//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;
		
		try {
			gprsSetDao.deleteGprsSet(gprsSetId);
		} catch (Exception e) {
			log.error("ɾ��GprsSet ʱ����:" + e.getMessage()+e.toString());		
		}
	}

	public void deleteGprsSetBatch(int[] gprsSetIds) {
		
		// ʵ����������
		//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;
		
		try {
			gprsSetDao.deleteBatch(gprsSetIds);
		} catch (Exception e) {
			log.error("����ɾ��GprsSet ʱ����:" + e.getMessage()+e.toString());
		}
	}
	
	public List<GprsSet> getAllSetLists(){
		
		// ʵ����������
		//gprsSetDao = gprsSetDao == null ?(GprsSetDao) daoMgr.getDao(GprsSetDao.class) : gprsSetDao;
		
		List<GprsSet> grpsSetList = new ArrayList<GprsSet>(); 
		try {
			grpsSetList = (List<GprsSet>)gprsSetDao.findAllSetList();
		} catch (Exception e) {
			log.error("��ȡGprsSet�б� ʱ����:" + e.getMessage()+e.toString());
		}
		return grpsSetList;
	}	
	
	//------------------------------------------------//
	// �����ֻ������б��ȡ,�ֻ������б�
	public  List<String> getPhones(String listIds){
		List<String> phoneList = new ArrayList<String>();
		
		// ʵ����������
		//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;
		// ���˿��ַ���
		if ( (listIds != null && listIds.length() > 0) ){
			List<PhoneList> phonelists = new ArrayList<PhoneList>();
			try {
				phonelists = phoneListDao.findPhoneList(listIds);
			} catch (Exception e) {
				log.error("��ȡPhoneList-�����б�  ʱ����:" + e.getMessage()+e.toString());
			}
			// �Ӳ�ѯ����г�ȡ�ֻ�����
			if ( phonelists != null){
				for (PhoneList phone : phonelists) {
					phoneList.add(phone.getPhone());
				}
			}		
		}
		return phoneList;
	}
	
	//PhoneList-�༭
	public PhoneList getPhoneListById(int listId) {
		
		// ʵ����������
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
			log.error("��ȡPhoneList-�༭ ʱ����:" + e.getMessage()+e.toString());
		}
		return phoneList;
	}

	//PhoneList-name��phone�Ǽ��Ψһ��	
	private List<PhoneList> getPhoneListByName(String name, String phone) {
		
		// ʵ����������
		//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;		
		
		PhoneList phoneList = new PhoneList();
		phoneList.setName(name);
		phoneList.setPhone(phone);
		List<PhoneList> phonelist = new ArrayList<PhoneList>();
		try {
			phonelist = phoneListDao.findPhoneList(phoneList);
		} catch (Exception e) {
			log.error("��ȡPhoneList-byName ʱ����:" + e.getMessage()+e.toString());
		}
		return phonelist;
	}

	//����true:����ɹ�  FALSE������ʧ��
	public boolean insertPhoneList(PhoneList phoneList) {
		List<PhoneList> phoneListrs = getPhoneListByName(phoneList.getName(),phoneList.getPhone());
		
		boolean existFlag = phoneListrs.size() == 0;
		if (existFlag) {
			
			// ʵ����������
			//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;			
			
			try {
				phoneListDao.insertPhoneList(phoneList);
			} catch (Exception e) {
				log.error("����PhoneList ʱ����:" + e.getMessage()+e.toString());
			}
		}
		return existFlag;
	}
	
	//����true:�޸ĳɹ�  FALSE���޸�ʧ��
	public boolean updatePhoneList(PhoneList phoneList){
		
		List<PhoneList> phoneListrs = getPhoneListByName(phoneList.getName(),phoneList.getPhone());
		
		boolean existFlag = phoneListrs.size() <= 1;
		if (existFlag) {

			// ʵ����������
			//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;
			
			try {
				phoneListDao.updatePhoneList(phoneList);
			} catch (Exception e) {
				log.error("�޸�PhoneList ʱ����:" + e.getMessage()+e.toString());
			}
		}
		return existFlag;
	}

	public void deletePhoneList(int phoneListId) {
		
		// ʵ����������
		//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;		
		
		try {
			phoneListDao.deletePhoneList(phoneListId);
		} catch (Exception e) {
			log.error("ɾ��PhoneList ʱ����:" + e.getMessage()+e.toString());
		}
	}

	public void deletePhoneListBatch(int[] phoneListIds) {
		
		// ʵ����������
		//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;		
		
		try {
			phoneListDao.deleteBatch(phoneListIds);
		} catch (Exception e) {
			log.error("����ɾ��PhoneList ʱ����:" + e.getMessage()+e.toString());
		}
	}
	
	public List<PhoneList> getAllPhoneLists(){
		
		// ʵ����������
		//phoneListDao = phoneListDao == null ?(PhoneListDao) daoMgr.getDao(PhoneListDao.class) : phoneListDao ;		
		
		List<PhoneList> phonelist = new ArrayList<PhoneList>();
		try {
			phonelist = (List<PhoneList>)phoneListDao.findAllPhoneList();
		} catch (Exception e) {
			log.error("��ȡPhoneList-list ʱ����:" + e.getMessage()+e.toString());
		}
		return phonelist;
	}
	
	/**
	 * @describe : ��ȡ�绰�����б�
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
	 * @describe: ��ȡ ���ı���ʱ��	
	 * @return:
	 * @date:2010-1-5
	 */
	public Date getBackUpLastTime(){
		Date rsDate = null;
		// ʵ����������
		//backUpListDao = backUpListDao == null ?(BackUpListDao) daoMgr.getDao(BackUpListDao.class) : backUpListDao ;
		try {
			rsDate = FunctionUnit.getStrToDate(backUpListDao.getLastTime());
		} catch (Exception e) {
			log.error("��ȡ ���ı���ʱ��ʱ����:" + e.getMessage()+e.toString());
		}		
		return rsDate;
	} 	
	
	public void insertBackUpList(BackUpList backUpList){
		
		// ʵ����������
		//backUpListDao = backUpListDao == null ?(BackUpListDao) daoMgr.getDao(BackUpListDao.class) : backUpListDao ;		
		
		try {
			backUpListDao.insertBackUpList(backUpList);
		} catch (Exception e) {
			log.error("������ʷ����excelʱ����:" + e.getMessage()+e.toString());
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
		
		// ʵ����������
		//backUpListDao = backUpListDao == null ?(BackUpListDao) daoMgr.getDao(BackUpListDao.class) : backUpListDao ;			
		
		try {
			backUpListDao.deleteBackUpListById(backId);
		} catch (Exception e) {
			log.error("ɾ������ ʱ����:" + e.getMessage()+e.toString());
		}
	}

	public void deleteBackUpBatch(int[] backIds) {
		
		// ʵ����������
		//backUpListDao = backUpListDao == null ?(BackUpListDao) daoMgr.getDao(BackUpListDao.class) : backUpListDao ;			
		
		try {
			backUpListDao.deleteBatch(backIds);
		} catch (Exception e) {
			log.error("����ɾ������ ʱ����:" + e.getMessage()+e.toString());
		}
	}
	
	// SysParam
	public List<SysParam> selectAllParam() {
				
		// ʵ����������
		//sysParamDao = sysParamDao == null ?(SysParamDao) daoMgr.getDao(SysParamDao.class) : sysParamDao;
		
		List<SysParam> sysParamList = null; 
		try {
			sysParamList = sysParamDao.selectAllParam();
		} catch (Exception e) {
			log.error("��ȡ����������Ϣʱ����:" + e.getMessage()+e.toString());
		}
		return sysParamList;
	}
	
	public Map<String, String> getSysParamMap() {
		
		Map<String, String> argsMap = new HashMap<String, String>();
		
		List<SysParam> sysParamList = selectAllParam();
		if ( (sysParamList == null)||(sysParamList.size()<=0) ){	// ��ȡʧ�ܾ�ʹ��Ĭ�ϲ���
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
		
		// ʵ����������
		//sysParamDao = sysParamDao == null ?(SysParamDao) daoMgr.getDao(SysParamDao.class) : sysParamDao;		
		
		try {
			sysParamDao.updateSysParam(argsMap);
		} catch (Exception e) {
			bool = false;
			log.error("��������������Ϣʱ����:" + e.getMessage()+e.toString());
		}
		return bool;
	}
	
	public boolean updateSysParam(List<SysParam> sysParamList) {
		boolean bool = true;
		// ʵ����������
		//sysParamDao = sysParamDao == null ?(SysParamDao) daoMgr.getDao(SysParamDao.class) : sysParamDao;		
				
		try {
			sysParamDao.updateSysParam(sysParamList);
		} catch (Exception e) {
			bool = false;
			log.error("��������������Ϣʱ����:" + e.getMessage()+e.toString());
		}
		return bool;
	}
	
	/**
	 * @describe : ��ҳ-��ȡһ�μ�¼
	 * @param nameSpace : �ռ���. ȡGprsSet.xml�У�sqlMap namespace="GprsSet"��ֵ
	 * 							  ȡPhoneList.xml�У�sqlMap namespace="PhoneList"��ֵ	
	 * 							  ȡBackUpList.xml�У�sqlMap namespace="BackUpList"��ֵ	
	 * @param currentPage : ��ǰҳ
	 * @param pagerMethod : ��ҳ�ķ���. ��ǰһҳ,��һҳ,��ǰ,��� 
	 * @param pageSize : ҳ����ʾ���������
	 * @param args : ��Ҫ���������ǵĲ���. ������Ҫ����,xml��page��
	 * @param limitCount : ��������Ŀ��Է��ص������ֵ
	 * @return Pager ��صķ�ҳ��Ϣ
	 * @date:2009-11-4
	 */
	public Pager getPager(String nameSpace,String currentPage, String pagerMethod, int pageSize,Map<String, Object> args) {
		// GprsSetΪ�ռ��� ��ȡGprsSet.xml�У�<sqlMap namespace="GprsSet">��ֵ
		// PhoneListΪ�ռ��� ��ȡPhoneList.xml�У�<sqlMap namespace="PhoneList">��ֵ
		// BackUpListΪ�ռ��� ��ȡBackUpList.xml�У�<sqlMap namespace="BackUpList">��ֵ
		
		// ʵ����������
		//basePageDao = basePageDao == null ?(BasePageDao) daoMgr.getDao(BasePageDao.class) :  basePageDao;		
				
				this.basePageDao.setNameSpace(nameSpace);
				return this.basePageDao.getPager(currentPage, pagerMethod, pageSize, args);
	}
	
	/**
	 * @describe : ��ҳ-��ȡһ�μ�¼
	 * @param nameSpace : �ռ���. ȡGprsSet.xml�У�sqlMap namespace="GprsSet"��ֵ
	 * 							  ȡPhoneList.xml�У�sqlMap namespace="PhoneList"��ֵ	
	 * 							  ȡBackUpList.xml�У�sqlMap namespace="BackUpList"��ֵ	
	 * @param currentPage : ��ǰҳ
	 * @param pagerMethod : ��ҳ�ķ���. ��ǰһҳ,��һҳ,��ǰ,��� 
	 * @param pageSize : ҳ����ʾ���������
	 * @param args : ��Ҫ���������ǵĲ���. ������Ҫ����,xml��page��
	 * @param limitCount : ��������Ŀ��Է��ص������ֵ
	 * @return Pager ��صķ�ҳ��Ϣ
	 * @date:2009-11-4
	 */
	public Pager getPager(String nameSpace,String currentPage, String pagerMethod, 
			int pageSize,Map<String, Object> args,int limitCount) {
		// GprsSetΪ�ռ��� ��ȡGprsSet.xml�У�<sqlMap namespace="GprsSet">��ֵ
		// PhoneListΪ�ռ��� ��ȡPhoneList.xml�У�<sqlMap namespace="PhoneList">��ֵ
		// BackUpListΪ�ռ��� ��ȡBackUpList.xml�У�<sqlMap namespace="BackUpList">��ֵ
		
		// ʵ����������
		//basePageDao = basePageDao == null ? (BasePageDao) daoMgr.getDao(BasePageDao.class) :  basePageDao;		
		
		this.basePageDao.setNameSpace(nameSpace);
		return this.basePageDao.getPager(currentPage, pagerMethod, pageSize, args, limitCount);
	}

}
