package com.htc.model;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.*;

import com.htc.bean.BeanForEqOrder;
import com.htc.bean.BeanForEqTypeCount;
import com.htc.bean.BeanForSms;
import com.htc.dao.iface.*;
import com.htc.domain.*;

/**
 * @ ManaService.java
 * ���� : �����ܷ���. �������, ��������, �������͹���, ����ƫ�����
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
public class ManaService {
	
	private Log log = LogFactory.getLog(ManaService.class);

	//private static ManaService manaService = new ManaService();

	private WorkPlaceDao workplaceDao;
	private EquipDataDao equipDataDao;
	private EquiTypeDao equitypeDao;
	private BasePageDao basePageDao;// ������ҳdao
	//DaoManager daoMgr = DaoConfig.getDaoManager();

	// ���췽��
	public ManaService() {
	}
	//ע��service -- spring ioc
	public void setWorkplaceDao(WorkPlaceDao workplaceDao) {
		this.workplaceDao = workplaceDao;
	}
	public void setEquipDataDao(EquipDataDao equipDataDao) {
		this.equipDataDao = equipDataDao;
	}
	public void setEquitypeDao(EquiTypeDao equitypeDao) {
		this.equitypeDao = equitypeDao;
	}
	public void setBasePageDao(BasePageDao basePageDao) {
		this.basePageDao = basePageDao;
	}	

	/**
	 * @describe:	��ȡ ManaService  ������
	 * @date:2009-11-5
	 */
//	public static ManaService getInstance() {
//		//manaService = manaService == null ? new ManaService() :  manaService ;
//		return manaService;
//	}

	// Workplace
	/**
	 * @describe: ��ȡ���е�������Ϣ	
	 * @param useless=1:�������õ�����		useless=0:�������õ�����(������ɾ����)
	 * @date:2010-1-29
	 */
	public List<Workplace> getAllWorkplace(int useless) {
		
		// ʵ����������
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
		
		//CopyOnWriteArrayList ����Щ���Ͻ��в����޸��ǰ�ȫ��
		List<Workplace> workPlaceList = new CopyOnWriteArrayList<Workplace>();
		try {
			workPlaceList = workplaceDao.findAllWorkplace(useless);
		} catch (Exception e) {
			log.error("��ȡ�����б� ʱ����:" + e.getMessage()+e.toString());
		}
		
		return workPlaceList;
	}

	public Workplace getWorkplaceById(int placeId) {
		
		// ʵ����������
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;		
		
		Workplace workplace = new Workplace();
		workplace.setPlaceId(placeId);
		try {
			workplace = workplaceDao.findWorkplace(workplace);
		} catch (Exception e) {
			workplace = null;
			log.error("����ID��ȡ���� ʱ����:" + e.getMessage()+e.toString());
		}
		return workplace;
	}

	private Workplace getWorkplaceByName(String placeName) {

		// ʵ����������
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;		
		
		Workplace workplace = new Workplace();
		workplace.setPlaceName(placeName);
		try {
			workplace = workplaceDao.findWorkplace(workplace);
		} catch (Exception e) {
			log.error("����Name��ȡ���� ʱ����:" + e.getMessage()+e.toString());
		}
		return workplace;
	}

	public boolean insertWorkplace(Workplace workplace) {
		boolean existFlag = (getWorkplaceByName(workplace.getPlaceName()) != null);
		if (existFlag) {
			return false;
		} else {
			existFlag = true;
			// ʵ����������
			//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
			
			try {
				workplaceDao.insertWorkplace(workplace);
			} catch (Exception e) {
				existFlag = false;
				log.error("�������� ʱ����:" + e.getMessage()+e.toString());
			}
			return existFlag;
		}
	}
	
	public boolean insertWorkplaceBatch(List<Workplace> worklist){
		boolean rsbool = true;
		// ʵ����������
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
		try {
			workplaceDao.insertworkplaceBatch(worklist);
		} catch (Exception e) {
			rsbool = false;
			log.error("������������ ʱ����:" + e.getMessage()+e.toString());
		}
		
		return rsbool;
	}

	public boolean updateWorkplace(Workplace workplace){
		Workplace tempPlace = getWorkplaceByName(workplace.getPlaceName());
		boolean existFlag = true;
		if ((tempPlace!=null)&&(tempPlace.getPlaceId()!= workplace.getPlaceId())){
			existFlag = false;
		}else{
			// ʵ����������
			//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
					try {
						workplaceDao.updateWorkplace(workplace);
					} catch (Exception e) {
						log.error("�޸����� ʱ����:" + e.getMessage()+e.toString());
					}			
		}
		return existFlag;
	}
	
	public void updateWorkplaceGms(Workplace workplace){
		// ʵ����������
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
		try {
			workplaceDao.updateWorkplace(workplace);
		} catch (Exception e) {
			log.error("�޸����� ���Ź���ʱ����:" + e.getMessage()+e.toString());
		}			
	}
	
	

	/**
	 * @describe: ����������, ɾ������
	 * @param placeName  ������
	 * @return: true:����Ԫ�أ�ɾ���ɹ� false ����Ԫ�أ�ɾ�����ɹ� --����ɾ��
	 * @date:2009-11-5
	 */
	public boolean deleteWorkplaceEx(String placeName) {
		
		// ʵ���������� 
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
		
		boolean hasChildFlag = true;
		try {
			//daoMgr.startTransaction();
			workplaceDao.deleteWorkplace(placeName);
			//daoMgr.commitTransaction();
		} catch (Exception e) {
			hasChildFlag = false;
			log.error("ɾ������ ʱ����:" + e.getMessage()+e.toString());
		} finally {
			//daoMgr.endTransaction();
		}
		return hasChildFlag;
	}

	/**
	 * @describe: ��������������, ɾ��һ������
	 * @param placeNames ����������
	 * @return true:����Ԫ�أ�ɾ���ɹ� false:����Ԫ�أ�ɾ�����ɹ� --����ɾ��
	 * @date:2009-11-5
	 */
	public boolean deleteWorkplaceBatchEx(String[] placeNames){
		
		// ʵ����������
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
		
		boolean hasChildFlag = true;
		try {
			//daoMgr.startTransaction();
			workplaceDao.deleteBatch(placeNames);
			//daoMgr.commitTransaction();
		} catch (Exception e) {
			hasChildFlag = false;
			log.error("����ɾ������ ʱ����:" + e.getMessage()+e.toString());
		} finally {
			//daoMgr.endTransaction();
		}
		return hasChildFlag;
	}

	/**
	 * @describe:	����������, ɾ������(���������)
	 * @param placeName ������
	 * @return true:����Ԫ�أ�ɾ���ɹ� false:����Ԫ�أ�ɾ�����ɹ� --��ɾ����ֻ������
	 * @date:2009-11-5
	 */
	public boolean deleteWorkplace(String placeName) {

		// ʵ����������
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
				
		boolean hasChildFlag = false;
		try {
			hasChildFlag = (workplaceDao.getChlidCount(placeName) == 0);
			if (hasChildFlag) {
				workplaceDao.deleteWorkplace(placeName);
			}
		} catch (Exception e) {
			log.error("ɾ������ ʱ����:" + e.getMessage()+e.toString());
		}

		return hasChildFlag;
	}

	/**
	 * @describe: ��������������, ɾ��һ������(���������)
	 * @param placeNames ����������
	 * @return true:����Ԫ�أ�ɾ���ɹ� false:����Ԫ�أ�ɾ�����ɹ� --����ɾ��
	 * @date:2009-11-5
	 */
	public boolean deleteWorkplaceBatch(String[] placeNames) {
		boolean hasChildFlag = true;
		for (String placeName : placeNames) {
			hasChildFlag = deleteWorkplace(placeName);
			if (!hasChildFlag) {
				break;
			}
		}
		return hasChildFlag;
	}

	public Map<Integer, String> getAllPlace() {
		Map<Integer, String> places = new HashMap<Integer, String>();

		for (Workplace workplace : getAllWorkplace(1)) {
			places.put(workplace.getPlaceId(), workplace.getPlaceName());
		}
		return places;
	}

	// EquipData
	// ������������ͳ�Ƹ����͵�����,���Ե������岼����
	public List<BeanForEqTypeCount> getEqCountByType(){
		// ʵ����������
		//equipDataDao = equipDataDao == null?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;
		List<BeanForEqTypeCount> rsCount = null;
		try {
			rsCount = equipDataDao.getEqCountByType();
		} catch (Exception e) {
			log.error("�����������ͻ�ȡ�����͵�����ʱ����:" + e.getMessage()+e.toString());
		}
		return rsCount;
	}
	
	
	public EquipData getEquipDataById(int equipmentId) {
		
		// ʵ����������
		//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;
		
		EquipData equipData = new EquipData();
		equipData.setEquipmentId(equipmentId);
		
		try {
			equipData = equipDataDao.findEquipData(equipData).get(0);
		} catch (Exception e) {
			equipData = null;
			log.error("����ID��ȡ����ʱ����:" + e.getMessage()+e.toString());
		}
		return equipData;
	}

	private List<EquipData> getEquipDataByName(int placeId, int address,String mark,String dsrsn) {
		
		// ʵ����������
		//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;
		
		EquipData equipData = new EquipData();
		equipData.setAddress(address);
		equipData.setPlaceId(placeId);
		equipData.setMark(mark);
		equipData.setDsrsn(dsrsn);
		
		List<EquipData> equipList = new ArrayList<EquipData>();
		try {
			equipList = equipDataDao.findEquipData(equipData);
		} catch (Exception e) {
			log.error("����Name��ȡ����ʱ����:" + e.getMessage()+e.toString());
		}
		return equipList;
	}

	public boolean insertEquipData(EquipData equipData) {
		
		boolean existFlag = getEquipDataByName(equipData.getPlaceId(),
				equipData.getAddress(), equipData.getMark(), equipData.getDsrsn()).size() > 0;
				
		if (existFlag) {
			return false;
		} else {
			existFlag = true;
			try {
				// ʵ����������
				//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;	
				equipDataDao.insertEquipData(equipData);
			} catch (Exception e) {
				existFlag = false;
				log.error("��������ʱ����:" + e.getMessage()+e.toString());
			}
			return existFlag;
		}
	}
	
	public boolean insertEquipDataBatch(List<EquipData> equipDataList){
		boolean rsbool = true;
		// ʵ����������
		//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;	
		try {
			equipDataDao.insertEquipDataBatch(equipDataList);
		} catch (Exception e) {
			rsbool = false;
			log.error("������������ʱ����:" + e.getMessage()+e.toString());
		}
		
		return rsbool;
	}	

	// 
	/**
	 * @describe:	����true:�޸ĳɹ� FALSE���޸�ʧ��
	 * @date:2009-11-5
	 */
	public boolean updateEquipData(EquipData equipData){
		
		List<EquipData> rsEquipData = getEquipDataByName(equipData.getPlaceId(), 
				equipData.getAddress(), equipData.getMark(), equipData.getDsrsn());
		
		boolean existFlag = false;
		
		if (rsEquipData.size() >= 2) {
			existFlag = false;
		} else if (rsEquipData.size() == 1) {
			existFlag = rsEquipData.get(0).getEquipmentId() == equipData
					.getEquipmentId();
		} else if (rsEquipData.size() == 0) {
			existFlag = true;
		}

		if (existFlag) {
			
			// ʵ����������
			//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;	
			
			try {
				equipDataDao.updateEquipData(equipData);
			} catch (Exception e) {
				log.error("�޸�����ʱ����:" + e.getMessage()+e.toString());
			}
		}
		return existFlag;
	}

	public void deleteEquipData(int equipmentId) {
		
		// ʵ����������
		//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;		
		
		try {
			equipDataDao.deleteEquipData(equipmentId);
		} catch (Exception e) {
			log.error("ɾ������ʱ����:" + e.getMessage()+e.toString());
		}
	}

	public void deleteEquipBatch(int[] equipmentIds) {
		
		// ʵ����������
		//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;
				
		try {
			equipDataDao.deleteBatch(equipmentIds);
		} catch (Exception e) {
			log.error("����ɾ������ʱ����:" + e.getMessage()+e.toString());
		}
	}

	public List<EquipData> selectEquiOrderStr(BeanForEqOrder eqorderBean) {
		
		// ʵ����������
		//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;		
		
		try {
			return equipDataDao.selcetEquiOrderStr(eqorderBean);
		} catch (Exception e) {
			log.error("��ȡ�����б�ʱ����:" + e.getMessage()+e.toString());
			return null;
		}
	}

	/**
	 * @describe: ����б�ֵ ��map(equipmentId : �ص� + ��� + ������ַ)	
	 * @param eqorderBean 
	 * 		SELECT_WITH_ORDERID:��������˳��������ȡ����  
	 * 		SELECT_WITH_EQUIPID�����������������ȡ���� 
	 * @date:2009-11-5
	 */
	public Map<Integer, String> getAllEquiString(BeanForEqOrder eqorderBean) {
		List<EquipData> equiOrder;
		Map<Integer, String> equiList = new TreeMap<Integer, String>();
		String tempStr;

		equiOrder = selectEquiOrderStr(eqorderBean);
		for (EquipData equi : equiOrder) {
			//tempStr = equi.getPlaceStr() + "-" + equi.getMark() + "-" + equi.getAddress();//������
			tempStr = equi.getPlaceStr() + "-" + equi.getMark();//��ʽ��
			equiList.put(equi.getEquipmentId(), tempStr);
		}
		equiOrder = null;
		return equiList;
	}

	/**
	 * @describe:  ���List(EquipData)ֵ	
	 * @param eqorderBean 
	 * 		SELECT_WITH_ORDERID:��������˳��������ȡ����  
	 * 		SELECT_WITH_EQUIPID�����������������ȡ���� 
	 * @return:
	 * @date:2009-11-5
	 */
	public List<EquipData> getAllEquiObject(BeanForEqOrder eqorderBean) {
		List<EquipData> equiOrder = null;

		equiOrder = selectEquiOrderStr(eqorderBean);
		return equiOrder;
	}
	
	public void updateEquiOrder(List<EquipData> equipDatas) {
		try {
			// ʵ����������
			//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;			
					
					equipDataDao.updateEquiOrder(equipDatas);
		} catch (Exception e) {
			log.error("����������Ϣʱ����:" + e.getMessage()+e.toString());
		}
	}

	public void updateEquiDev(EquipData equipData) {
		try {
			// ʵ����������
			//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;			
			
			equipDataDao.updateEquipDev(equipData);
		} catch (Exception e) {
			log.error("��������ƫ��ʱ����:" + e.getMessage()+e.toString());
		}
	}
	
	// Equitype
	public List<Equitype> getAllEquiTypes() {
		
		// ʵ����������
		//equitypeDao = equitypeDao == null ?	(EquiTypeDao) daoMgr.getDao(EquiTypeDao.class) :  equitypeDao ;
		
		List<Equitype> equiptypeList = new ArrayList<Equitype>(); 
		try {
			equiptypeList =  equitypeDao.findAllEquitype();
		} catch (Exception e) {
			log.error("��ȡȫ������ʱ����:" + e.getMessage()+e.toString());
		}
		return equiptypeList;
	}

	public Map<Integer, String> getAllEquiType() {
		Map<Integer, String> types = new HashMap<Integer, String>();

		for (Equitype equitype : getAllEquiTypes()) {
			types.put(equitype.getTyepId(), equitype.getTypename());
		}
		return types;
	}	  
	
	/**
	 * @describe: ������Id��ȡ������������,���ű�����Ҫ����Ϣ
	 * @date:2010-3-3
	 */
	public BeanForSms getPlacePhones(int equipmentId){
		BeanForSms rsBean = new BeanForSms();
		String rsStr = null;
		EquipData eqData = getEquipDataById(equipmentId);
		if (eqData!= null){
			Workplace place = getWorkplaceById(eqData.getPlaceId());
			rsBean.setEqName(eqData.getMark());
			if (place != null){
				rsStr = place.getRemark();
				if ( (rsStr != null) && (rsStr.length() <= 0) ){
					rsStr = null;
				}else{
					rsBean.setFillValue(true);
					rsBean.setPlaceName(place.getPlaceName());
					rsBean.setPhonelist(rsStr);
				}
			}
		}
		return rsBean;
	}
	
	/**
	 * @describe:  ��ҳ-��ȡһ�μ�¼	
	 * @param nameSpace Ϊ�ռ���. ȡWorkplace.xml�У�sqlMap namespace="Workplace" ��ֵ
	 * 							 ȡEquipData.xml�У�sqlMap namespace="EquipData" ��ֵ
	 * @param currentPage ��ǰҳ
	 * @param pagerMethod ��ҳ�ķ���. ��ǰһҳ,��һҳ,��ǰ,��� 
	 * @param pageSize ҳ����ʾ���������
	 * @param args ��Ҫ���������ǵĲ���. ������Ҫ����,xml��page��
	 * @return: Pager ��صķ�ҳ��Ϣ
	 * @date:2009-11-5
	 */
	public Pager getPager(String nameSpace, String currentPage,
			String pagerMethod, int pageSize, Map<String, Object> args) {
		// WorkplaceΪ�ռ��� ��ȡWorkplace.xml�У�<sqlMap namespace="Workplace">��ֵ
		// �� EquipDataΪ�ռ��� ��ȡEquipData.xml�У�<sqlMap namespace="EquipData">��ֵ
		
		//basePageDao = basePageDao == null ?(BasePageDao) daoMgr.getDao(BasePageDao.class) :  basePageDao ;
		
		this.basePageDao.setNameSpace(nameSpace);
		return this.basePageDao.getPager(currentPage, pagerMethod, pageSize, args);
	}

}
