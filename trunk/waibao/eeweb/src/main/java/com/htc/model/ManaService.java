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
 * 作用 : 管理功能服务. 区域管理, 仪器管理, 仪器类型管理, 仪器偏差管理
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
public class ManaService {
	
	private Log log = LogFactory.getLog(ManaService.class);

	//private static ManaService manaService = new ManaService();

	private WorkPlaceDao workplaceDao;
	private EquipDataDao equipDataDao;
	private EquiTypeDao equitypeDao;
	private BasePageDao basePageDao;// 公共分页dao
	//DaoManager daoMgr = DaoConfig.getDaoManager();

	// 构造方法
	public ManaService() {
	}
	//注册service -- spring ioc
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
	 * @describe:	获取 ManaService  程序单例
	 * @date:2009-11-5
	 */
//	public static ManaService getInstance() {
//		//manaService = manaService == null ? new ManaService() :  manaService ;
//		return manaService;
//	}

	// Workplace
	/**
	 * @describe: 获取所有的区域信息	
	 * @param useless=1:所有有用的区域		useless=0:所有有用的区域(包括被删掉的)
	 * @date:2010-1-29
	 */
	public List<Workplace> getAllWorkplace(int useless) {
		
		// 实例化服务类
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
		
		//CopyOnWriteArrayList 对这些集合进行并发修改是安全的
		List<Workplace> workPlaceList = new CopyOnWriteArrayList<Workplace>();
		try {
			workPlaceList = workplaceDao.findAllWorkplace(useless);
		} catch (Exception e) {
			log.error("获取区域列表 时出错:" + e.getMessage()+e.toString());
		}
		
		return workPlaceList;
	}

	public Workplace getWorkplaceById(int placeId) {
		
		// 实例化服务类
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;		
		
		Workplace workplace = new Workplace();
		workplace.setPlaceId(placeId);
		try {
			workplace = workplaceDao.findWorkplace(workplace);
		} catch (Exception e) {
			workplace = null;
			log.error("根据ID获取区域 时出错:" + e.getMessage()+e.toString());
		}
		return workplace;
	}

	private Workplace getWorkplaceByName(String placeName) {

		// 实例化服务类
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;		
		
		Workplace workplace = new Workplace();
		workplace.setPlaceName(placeName);
		try {
			workplace = workplaceDao.findWorkplace(workplace);
		} catch (Exception e) {
			log.error("根据Name获取区域 时出错:" + e.getMessage()+e.toString());
		}
		return workplace;
	}

	public boolean insertWorkplace(Workplace workplace) {
		boolean existFlag = (getWorkplaceByName(workplace.getPlaceName()) != null);
		if (existFlag) {
			return false;
		} else {
			existFlag = true;
			// 实例化服务类
			//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
			
			try {
				workplaceDao.insertWorkplace(workplace);
			} catch (Exception e) {
				existFlag = false;
				log.error("插入区域 时出错:" + e.getMessage()+e.toString());
			}
			return existFlag;
		}
	}
	
	public boolean insertWorkplaceBatch(List<Workplace> worklist){
		boolean rsbool = true;
		// 实例化服务类
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
		try {
			workplaceDao.insertworkplaceBatch(worklist);
		} catch (Exception e) {
			rsbool = false;
			log.error("批量插入区域 时出错:" + e.getMessage()+e.toString());
		}
		
		return rsbool;
	}

	public boolean updateWorkplace(Workplace workplace){
		Workplace tempPlace = getWorkplaceByName(workplace.getPlaceName());
		boolean existFlag = true;
		if ((tempPlace!=null)&&(tempPlace.getPlaceId()!= workplace.getPlaceId())){
			existFlag = false;
		}else{
			// 实例化服务类
			//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
					try {
						workplaceDao.updateWorkplace(workplace);
					} catch (Exception e) {
						log.error("修改区域 时出错:" + e.getMessage()+e.toString());
					}			
		}
		return existFlag;
	}
	
	public void updateWorkplaceGms(Workplace workplace){
		// 实例化服务类
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
		try {
			workplaceDao.updateWorkplace(workplace);
		} catch (Exception e) {
			log.error("修改区域 短信关联时出错:" + e.getMessage()+e.toString());
		}			
	}
	
	

	/**
	 * @describe: 根据区域名, 删除区域
	 * @param placeName  区域名
	 * @return: true:无子元素，删除成功 false 有子元素，删除不成功 --真正删除
	 * @date:2009-11-5
	 */
	public boolean deleteWorkplaceEx(String placeName) {
		
		// 实例化服务类 
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
		
		boolean hasChildFlag = true;
		try {
			//daoMgr.startTransaction();
			workplaceDao.deleteWorkplace(placeName);
			//daoMgr.commitTransaction();
		} catch (Exception e) {
			hasChildFlag = false;
			log.error("删除区域 时出错:" + e.getMessage()+e.toString());
		} finally {
			//daoMgr.endTransaction();
		}
		return hasChildFlag;
	}

	/**
	 * @describe: 根据区域名数组, 删除一批区域
	 * @param placeNames 区域名数组
	 * @return true:无子元素，删除成功 false:有子元素，删除不成功 --真正删除
	 * @date:2009-11-5
	 */
	public boolean deleteWorkplaceBatchEx(String[] placeNames){
		
		// 实例化服务类
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
		
		boolean hasChildFlag = true;
		try {
			//daoMgr.startTransaction();
			workplaceDao.deleteBatch(placeNames);
			//daoMgr.commitTransaction();
		} catch (Exception e) {
			hasChildFlag = false;
			log.error("批量删除区域 时出错:" + e.getMessage()+e.toString());
		} finally {
			//daoMgr.endTransaction();
		}
		return hasChildFlag;
	}

	/**
	 * @describe:	根据区域名, 删除区域(无事务控制)
	 * @param placeName 区域名
	 * @return true:无子元素，删除成功 false:有子元素，删除不成功 --不删除，只是隐藏
	 * @date:2009-11-5
	 */
	public boolean deleteWorkplace(String placeName) {

		// 实例化服务类
		//workplaceDao = workplaceDao == null ?(WorkPlaceDao) daoMgr.getDao(WorkPlaceDao.class) :  workplaceDao ;
				
		boolean hasChildFlag = false;
		try {
			hasChildFlag = (workplaceDao.getChlidCount(placeName) == 0);
			if (hasChildFlag) {
				workplaceDao.deleteWorkplace(placeName);
			}
		} catch (Exception e) {
			log.error("删除区域 时出错:" + e.getMessage()+e.toString());
		}

		return hasChildFlag;
	}

	/**
	 * @describe: 根据区域名数组, 删除一批区域(无事务控制)
	 * @param placeNames 区域名数组
	 * @return true:无子元素，删除成功 false:有子元素，删除不成功 --真正删除
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
	// 根据仪器类型统计各类型的数量,用以调整整体布局用
	public List<BeanForEqTypeCount> getEqCountByType(){
		// 实例化服务类
		//equipDataDao = equipDataDao == null?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;
		List<BeanForEqTypeCount> rsCount = null;
		try {
			rsCount = equipDataDao.getEqCountByType();
		} catch (Exception e) {
			log.error("根据仪器类型获取各类型的数量时出错:" + e.getMessage()+e.toString());
		}
		return rsCount;
	}
	
	
	public EquipData getEquipDataById(int equipmentId) {
		
		// 实例化服务类
		//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;
		
		EquipData equipData = new EquipData();
		equipData.setEquipmentId(equipmentId);
		
		try {
			equipData = equipDataDao.findEquipData(equipData).get(0);
		} catch (Exception e) {
			equipData = null;
			log.error("根据ID获取仪器时出错:" + e.getMessage()+e.toString());
		}
		return equipData;
	}

	private List<EquipData> getEquipDataByName(int placeId, int address,String mark,String dsrsn) {
		
		// 实例化服务类
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
			log.error("根据Name获取仪器时出错:" + e.getMessage()+e.toString());
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
				// 实例化服务类
				//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;	
				equipDataDao.insertEquipData(equipData);
			} catch (Exception e) {
				existFlag = false;
				log.error("增加仪器时出错:" + e.getMessage()+e.toString());
			}
			return existFlag;
		}
	}
	
	public boolean insertEquipDataBatch(List<EquipData> equipDataList){
		boolean rsbool = true;
		// 实例化服务类
		//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;	
		try {
			equipDataDao.insertEquipDataBatch(equipDataList);
		} catch (Exception e) {
			rsbool = false;
			log.error("批量插入仪器时出错:" + e.getMessage()+e.toString());
		}
		
		return rsbool;
	}	

	// 
	/**
	 * @describe:	返回true:修改成功 FALSE：修改失败
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
			
			// 实例化服务类
			//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;	
			
			try {
				equipDataDao.updateEquipData(equipData);
			} catch (Exception e) {
				log.error("修改仪器时出错:" + e.getMessage()+e.toString());
			}
		}
		return existFlag;
	}

	public void deleteEquipData(int equipmentId) {
		
		// 实例化服务类
		//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;		
		
		try {
			equipDataDao.deleteEquipData(equipmentId);
		} catch (Exception e) {
			log.error("删除仪器时出错:" + e.getMessage()+e.toString());
		}
	}

	public void deleteEquipBatch(int[] equipmentIds) {
		
		// 实例化服务类
		//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;
				
		try {
			equipDataDao.deleteBatch(equipmentIds);
		} catch (Exception e) {
			log.error("批量删除仪器时出错:" + e.getMessage()+e.toString());
		}
	}

	public List<EquipData> selectEquiOrderStr(BeanForEqOrder eqorderBean) {
		
		// 实例化服务类
		//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;		
		
		try {
			return equipDataDao.selcetEquiOrderStr(eqorderBean);
		} catch (Exception e) {
			log.error("获取仪器列表时出错:" + e.getMessage()+e.toString());
			return null;
		}
	}

	/**
	 * @describe: 输出列表值 ：map(equipmentId : 地点 + 标号 + 仪器地址)	
	 * @param eqorderBean 
	 * 		SELECT_WITH_ORDERID:按照仪器顺序号升序获取数据  
	 * 		SELECT_WITH_EQUIPID：按照仪器号升序获取数据 
	 * @date:2009-11-5
	 */
	public Map<Integer, String> getAllEquiString(BeanForEqOrder eqorderBean) {
		List<EquipData> equiOrder;
		Map<Integer, String> equiList = new TreeMap<Integer, String>();
		String tempStr;

		equiOrder = selectEquiOrderStr(eqorderBean);
		for (EquipData equi : equiOrder) {
			//tempStr = equi.getPlaceStr() + "-" + equi.getMark() + "-" + equi.getAddress();//测试用
			tempStr = equi.getPlaceStr() + "-" + equi.getMark();//正式用
			equiList.put(equi.getEquipmentId(), tempStr);
		}
		equiOrder = null;
		return equiList;
	}

	/**
	 * @describe:  输出List(EquipData)值	
	 * @param eqorderBean 
	 * 		SELECT_WITH_ORDERID:按照仪器顺序号升序获取数据  
	 * 		SELECT_WITH_EQUIPID：按照仪器号升序获取数据 
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
			// 实例化服务类
			//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;			
					
					equipDataDao.updateEquiOrder(equipDatas);
		} catch (Exception e) {
			log.error("更新仪器信息时出错:" + e.getMessage()+e.toString());
		}
	}

	public void updateEquiDev(EquipData equipData) {
		try {
			// 实例化服务类
			//equipDataDao = equipDataDao == null ?(EquipDataDao) daoMgr.getDao(EquipDataDao.class) : equipDataDao;			
			
			equipDataDao.updateEquipDev(equipData);
		} catch (Exception e) {
			log.error("更新仪器偏差时出错:" + e.getMessage()+e.toString());
		}
	}
	
	// Equitype
	public List<Equitype> getAllEquiTypes() {
		
		// 实例化服务类
		//equitypeDao = equitypeDao == null ?	(EquiTypeDao) daoMgr.getDao(EquiTypeDao.class) :  equitypeDao ;
		
		List<Equitype> equiptypeList = new ArrayList<Equitype>(); 
		try {
			equiptypeList =  equitypeDao.findAllEquitype();
		} catch (Exception e) {
			log.error("获取全部仪器时出错:" + e.getMessage()+e.toString());
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
	 * @describe: 根据仪Id获取仪器所在区域,短信报警需要的信息
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
	 * @describe:  分页-获取一段记录	
	 * @param nameSpace 为空间名. 取Workplace.xml中，sqlMap namespace="Workplace" 的值
	 * 							 取EquipData.xml中，sqlMap namespace="EquipData" 的值
	 * @param currentPage 当前页
	 * @param pagerMethod 翻页的方法. 有前一页,后一页,最前,最后 
	 * @param pageSize 页面显示的最大数量
	 * @param args 需要条件搜索是的参数. 参数需要对照,xml和page类
	 * @return: Pager 相关的分页信息
	 * @date:2009-11-5
	 */
	public Pager getPager(String nameSpace, String currentPage,
			String pagerMethod, int pageSize, Map<String, Object> args) {
		// Workplace为空间名 ：取Workplace.xml中，<sqlMap namespace="Workplace">的值
		// 或 EquipData为空间名 ：取EquipData.xml中，<sqlMap namespace="EquipData">的值
		
		//basePageDao = basePageDao == null ?(BasePageDao) daoMgr.getDao(BasePageDao.class) :  basePageDao ;
		
		this.basePageDao.setNameSpace(nameSpace);
		return this.basePageDao.getPager(currentPage, pagerMethod, pageSize, args);
	}

}
