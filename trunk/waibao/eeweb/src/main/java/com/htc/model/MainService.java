package com.htc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.bean.BeanForAlarmRs;
import com.htc.bean.BeanForPortData;
import com.htc.bean.BeanForRecord;
import com.htc.bean.BeanForSearchRecord;
import com.htc.common.FunctionUnit;
import com.htc.dao.iface.AlarmRecDao;
import com.htc.dao.iface.BasePageDao;
import com.htc.dao.iface.HisRecordDao;
import com.htc.dao.iface.MinRecordDao;
import com.htc.dao.iface.RecordDao;
import com.htc.dao.iface.SmsRecordDao;
import com.htc.dao.iface.TLogDao;
import com.htc.domain.AlarmRec;
import com.htc.domain.EquipData;
import com.htc.domain.HisRecord;
import com.htc.domain.Pager;
import com.htc.domain.Record;
import com.htc.domain.SmsRecord;
import com.htc.domain.TLog;

/**
 * @ MainService.java
 * 作用 : 即时数据, 历史数据(日报,月报), 警报数据, 系统日志. 相关操作
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class MainService {
	
	private Log log = LogFactory.getLog(MainService.class);

	//private static MainService mainService = new MainService();

	/**
	 * TYPE_NO_STAT :不统计只显示数据,即时数据用
	 */
	public static final int TYPE_NO_STAT = 1;
	/**
	 * TYPE_MIN_STAT :最小值,日报和月报用
	 */
	public static final int TYPE_MIN_STAT = 10;
	/**
	 * TYPE_AVG_STAT :平均值,日报和月报用
	 */
	public static final int TYPE_AVG_STAT = 100;
	/**
	 * TYPE_MAX_STAT :最大值,日报和月报用
	 */
	public static final int TYPE_MAX_STAT = 1000;
	/**
	 * TYPE_RECENT :流水记录类型
	 */
	public static final int TYPE_RECENT = 1;// 流水记录
	/**
	 * TYPE_DAILY :日报表类型
	 */
	public static final int TYPE_DAILY = 2;// 日报表
	/**
	 * TYPE_MONTH : 月报表类型
	 */
	public static final int TYPE_MONTH = 3;// 月报表

	//private DaoManager daoMgr = DaoConfig.getDaoManager();
	private AlarmRecDao alarmRecDao;
	private RecordDao recordDao;
	private HisRecordDao hisRecordDao;
	private MinRecordDao minRecordDao;
	private TLogDao reclogDao;	
	private SmsRecordDao smsRecordDao;
	private BasePageDao basePageDao;// 公共分页dao

	// 构造方法
	public MainService() {
	}
	//注册service -- spring ioc
	public void setAlarmRecDao(AlarmRecDao alarmRecDao) {
		this.alarmRecDao = alarmRecDao;
	}
	public void setRecordDao(RecordDao recordDao) {
		this.recordDao = recordDao;
	}
	public void setHisRecordDao(HisRecordDao hisRecordDao) {
		this.hisRecordDao = hisRecordDao;
	}
	public void setMinRecordDao(MinRecordDao minRecordDao) {
		this.minRecordDao = minRecordDao;
	}
	public void setReclogDao(TLogDao reclogDao) {
		this.reclogDao = reclogDao;
	}
	public void setSmsRecordDao(SmsRecordDao smsRecordDao) {
		this.smsRecordDao = smsRecordDao;
	}
	public void setBasePageDao(BasePageDao basePageDao) {
		this.basePageDao = basePageDao;
	}	

	/**
	 * @describe: 获取MainService类 单例
	 * @return
	 * @date:2009-11-4
	 */
//	public static MainService getInstance() {
//		return mainService;
//	}

	/**
	 * 获取警报列表(查询报警数据)
	 */
	public List<AlarmRec> getAllAlarmRec(AlarmRec alarmRec){
		// 实例化服务类
		//alarmRecDao = alarmRecDao == null ?(AlarmRecDao) daoMgr.getDao(AlarmRecDao.class) : alarmRecDao;
		
		List<AlarmRec> alarmList = new ArrayList<AlarmRec>();
		try {
			alarmList = alarmRecDao.selectAll(alarmRec);
		} catch (Exception e) {
			log.error("获取警报列表(查询报警数据)时出错:" + e.getMessage()+e.toString());
		}
	
		return alarmList;
	}
	
	/**
	 * 获取警报列表(动态报警数据)
	 */
	public List<AlarmRec> selectUndoRec(){
		List<AlarmRec> alarmList = null;
		try {
			alarmList = alarmRecDao.selectUndoRec();
		} catch (Exception e) {
			alarmList = null;
			log.error("获取警报列表(动态报警数据)时出错:" + e.getMessage()+e.toString());
		}
		return alarmList;		
	}
	
	/**
	 * 对当前的报警数据,应该执行哪种操作: 不执行任何操作, 插入或者修改
	 * @return
	 * 0:查询操作有错误,不执行任何操作<br>
	 * 1:执行插入<br>
	 * 2:执行修改<br>
	 */
	public BeanForAlarmRs getDoWhich(int equiId){
		BeanForAlarmRs rsBean = new BeanForAlarmRs();
		AlarmRec tempRec = null;
		try {
			tempRec = alarmRecDao.selectRec(equiId);
			if (tempRec != null){// 修改
				// 保存获得的报警编号,用来执行语句 (update talarmrec set alarmEnd = #alarmEnd# where	alarmId = #alarmId#)
				rsBean.setAlarmId(tempRec.getAlarmId());
				rsBean.setRsType(BeanForAlarmRs.UPDATE_ALARM);
			}else{// 增加
				rsBean.setRsType(BeanForAlarmRs.ADD_ALARM);
			}
		} catch (Exception e) {
			rsBean.setRsType(BeanForAlarmRs.NUDO_ALARM);
			log.error("判断警报数据操作:插入或者更新  时出错:" + e.getMessage()+e.toString());
		}
		return rsBean;
	}	
	
	/**
	 * @describe:	插入警报数据
	 * @param alarmRec : 警报数据包
	 * @date:2009-11-4
	 */
	public void insertAlarmRec(AlarmRec alarmRec){
		
		// 实例化服务类
		//alarmRecDao = alarmRecDao == null ?	(AlarmRecDao) daoMgr.getDao(AlarmRecDao.class) : alarmRecDao;
		
		try {
			alarmRecDao.insertAlarm(alarmRec);
		} catch (Exception e) {
			log.error("插入警报数据 时出错:" + e.getMessage() + e.toString());
		}
	}
	
	/**
	 * @describe:	修改 警报数据 -- 处理报警时用
	 * @param alarmRec : 警报数据包
	 * @date:2009-11-4
	 */
	public int updateAlarmRec(AlarmRec alarmRec) {
		int effectRow = -1; //影响的行数
		
		// 实例化服务类
		//alarmRecDao = alarmRecDao == null ?	(AlarmRecDao) daoMgr.getDao(AlarmRecDao.class) : alarmRecDao;
		
		try {
			effectRow = alarmRecDao.updateAlarm(alarmRec);
		} catch (Exception e) {
			effectRow = -1;
			log.error(" 修改 警报数据(处理报警)时出错:" + e.getMessage()+e.toString());
		}
		return effectRow;
	}
	
	/**
	 * @describe:	修改 警报数据 -- 修改最新报警的时间
	 * @param alarmRec : 警报数据包
	 * @date:2009-11-4
	 */
	public void updateAlarmRecing(AlarmRec alarmRec) {
		
		// 实例化服务类
		//alarmRecDao = alarmRecDao == null ?	(AlarmRecDao) daoMgr.getDao(AlarmRecDao.class) : alarmRecDao;
		
		try {
			alarmRecDao.updateAlarming(alarmRec);
		} catch (Exception e) {
			log.error(" 修改 警报数据(修改最新报警时间)时出错:" + e.getMessage()+e.toString());
		}
	}

	/**
	 * @describe: 根据ID删除 警报数据
	 * @param alarmId ID
	 * @date:2009-11-4
	 */
	public void deleteAlarmRecById(int alarmId) {
		
		// 实例化服务类
		//alarmRecDao = alarmRecDao == null ?	(AlarmRecDao) daoMgr.getDao(AlarmRecDao.class) : alarmRecDao;
		
		try {
			alarmRecDao.deleteAlarmById(alarmId);
		} catch (Exception e) {
			log.error("根据ID删除 警报数据 时出错:" + e.getMessage()+e.toString());
		}
	}

	/**
	 * @describe: 根据ID列表批量删除 警报数据
	 * @param alarmIds : ID列表
	 * @date:2009-11-4
	 */
	public void deleteAlarmRecBatch(int[] alarmIds){
		
		// 实例化服务类
		//alarmRecDao = alarmRecDao == null ?	(AlarmRecDao) daoMgr.getDao(AlarmRecDao.class) : alarmRecDao;
		
		try {
			alarmRecDao.deleteAlarmBatch(alarmIds);
		} catch (Exception e) {
			log.error("根据ID列表批量删除 警报数据时出错:" + e.getMessage()+e.toString());
		}
	}

	// Record 和 HisRecord
	/**
	 * @describe: 获取及时数据最早记录时间	
	 * @date:2010-1-5
	 */
	public Date getRecordEearlyTime(){
		Date rsDate = null;
		// 实例化服务类
		//recordDao = recordDao == null?(RecordDao) daoMgr.getDao(RecordDao.class) : recordDao;		
		rsDate = FunctionUnit.getStrToDate(recordDao.getEarlyTime());
		return rsDate;
	}
	
	/**
	 * @describe: 获取日报表或者月报表数据最早记录时间
	 * @param type: TYPE_DAILY:2(日报表类型) TYPE_MONTH:3(月报表类型)
	 * @date:2010-1-5
	 */
	public Date getHisRecEearlyTime(int type){
		Date rsDate = null;
		// 实例化服务类
		//hisRecordDao = hisRecordDao == null ?(HisRecordDao) daoMgr.getDao(HisRecordDao.class) : hisRecordDao ;	
		rsDate = FunctionUnit.getStrToDate(hisRecordDao.getEarlyTime(type));
		return rsDate;
	}
	
	/**
	 * @describe: 获取日报或者月报 最后的记录时间	
	 * @param type: TYPE_DAILY:2(日报表类型) TYPE_MONTH:3(月报表类型)
	 * @return:
	 * @date:2010-1-5
	 */
	public Date getHisRecLastTime(int type){
		Date rsDate = null;
		// 实例化服务类
		//hisRecordDao = hisRecordDao == null ?(HisRecordDao) daoMgr.getDao(HisRecordDao.class) : hisRecordDao ;
		try {
			rsDate = FunctionUnit.getStrToDate(hisRecordDao.getLastTime(type));
		} catch (Exception e) {
			log.error("获取"+(type==2?"日报":"月报")+"最后的记录时间出错:" + e.getMessage()+e.toString());
		}		
		return rsDate;
	} 
	
	/**
	 * @describe : 批量插入临时数据 
	 * @param mapRecords ：待插入的数据 Map<Integer, Record>
	 * @date:2009-11-4
	 */
	public void insertRecordBatch(Map<Integer, Record> mapRecords){
		// 实例化服务类
		//recordDao = recordDao == null?(RecordDao) daoMgr.getDao(RecordDao.class) : recordDao;
		
		try {
			recordDao.insertRecordBatch(mapRecords);
		} catch (Exception e) {
			log.error("批量插入临时数据 时出错:" + e.getMessage()+e.toString());
			//ResetDatabase.readDBStatus();
		}
	}
	
	/**
	 * @describe: 根据时间批量删除及时数据	
	 * @param endRecTime: 结束时间(删除在这个时间之前的数据)
	 * @date:2010-1-5
	 */
	public void deleteRecordBatch(String endRecTime){
		// 实例化服务类
		//recordDao = recordDao == null?(RecordDao) daoMgr.getDao(RecordDao.class) : recordDao;		
		try {
			recordDao.deleteRecordBatch(endRecTime);
		} catch (Exception e) {
			log.error("批量删除过期的临时数据出错:" + e.getMessage()+e.toString());
		}
	}
	
	/**
	 * @describe : 插入历史数据(日报和月报) 
	 * @param hisRecord : 待插入的数据HisRecord
	 * @date:2009-11-4
	 */
	public void insertHisRecord(HisRecord hisRecord) throws Exception {
		
		// 实例化服务类
		//hisRecordDao = hisRecordDao == null ?(HisRecordDao) daoMgr.getDao(HisRecordDao.class) : hisRecordDao ;
			
		hisRecordDao.insertHisRecord(hisRecord);
	}

	/**
	 * @describe : 清空临时数据表,而且这个操作会使计数器重新从1开始自动增长--暂时不用  
	 * @date:2009-11-4
	 */
	public void truncateRecord() {
		
		// 实例化服务类
		//recordDao = recordDao == null ?	(RecordDao) daoMgr.getDao(RecordDao.class) : recordDao;
		
		try {
			recordDao.truncateRecord();
		} catch (Exception e) {
			log.error("清空临时数据表时出错:" + e.getMessage()+e.toString());
		}
	}

	/**
	 * @describe : 根据RecordSearchBean封装类,搜索记录
	 * @param beanForSearchRecord: 封装了:仪器主键列表,开始时间,结束时间,搜索数据类型
	 * @return ：根据RecordSearchBean封装类,搜索记录的返回集合
	 * @throws Exception: 数据库异常
	 * @date:2009-11-3
	 */
	public List<BeanForRecord> searchRecords(BeanForSearchRecord  beanForSearchRecord ) {
		List<BeanForRecord> rsList = null; // 返回变量:数据库检索得到的结果集
		// 检索数据库
		if (beanForSearchRecord.getStattype() == 0){	//即时数据
			
			// 实例化服务类
			//recordDao = recordDao == null ?	(RecordDao) daoMgr.getDao(RecordDao.class) : recordDao;
			
			try {
				rsList = recordDao.selectAllBean(beanForSearchRecord);
			} catch (Exception e) {
				log.error("搜索即时数据时出错:" + e.getMessage()+e.toString());
			}
		}else{										//日报或者月报
			
			// 实例化服务类
			//hisRecordDao =  hisRecordDao == null ?(HisRecordDao) daoMgr.getDao(HisRecordDao.class) :  hisRecordDao;
			
			try {
				rsList = hisRecordDao.selectStatItmes(beanForSearchRecord);
			} catch (Exception e) {
				log.error("搜索日报或者月报时出错:" + e.getMessage()+e.toString());
			}
		}
		return rsList;
	}
	
	// MinRecord
	public int getMinCounts(){
		int rsint = -1;
		// 实例化服务类
		//minRecordDao = minRecordDao == null ?(MinRecordDao) daoMgr.getDao(MinRecordDao.class) : minRecordDao;
	
		try {
			rsint = minRecordDao.getCounts();
		} catch (Exception e) {
			rsint = -1;
			log.error("总览画面,返回最新数据(数据条数)时 错误:" + e.getMessage()+e.toString());
		}				
		return rsint;
	}
	
	public Map<Integer, BeanForPortData> getNewestMinRec(String userPlaceList) {
		// 实例化服务类
		//minRecordDao = minRecordDao == null ?(MinRecordDao) daoMgr.getDao(MinRecordDao.class) : minRecordDao;
		Map<Integer, BeanForPortData> rsMap = new HashMap<Integer, BeanForPortData>();
		
		// 过滤空字符串
		if ( (userPlaceList != null && userPlaceList.length()> 0) ){
			try {
				List<BeanForPortData> rsList = minRecordDao.getNewestRec(userPlaceList);
				for (BeanForPortData bean : rsList) {
					rsMap.put(bean.getEquipmentId(), bean);
				}
			} catch (Exception e) {
				rsMap = null;
				log.error("总览画面,返回最新数据(实时数据)时 错误:"  + e.getMessage()+e.toString());
			}
		}
		return rsMap;
	}
	
	public List<BeanForPortData> getNewestMinRecAll() {
		try {
			return minRecordDao.getNewestRecAll();
		} catch (Exception e) {
			throw new RuntimeException("返回最新数据错误:", e);
		}
	}
	
	public List<BeanForPortData> selectMinAllRec(String userPlaceList) {
		// 实例化服务类
		//minRecordDao = minRecordDao == null ?(MinRecordDao) daoMgr.getDao(MinRecordDao.class) : minRecordDao;
		List<BeanForPortData> rsList = new ArrayList<BeanForPortData>(); // 返回变量:数据库检索得到的结果集
		// 过滤空字符串
		if ( (userPlaceList != null && userPlaceList.length()> 0) ){
			try {
				rsList = minRecordDao.selectAllRec(userPlaceList);
			} catch (Exception e) {
				log.error("实时曲线,返回最新数据时 错误:" + e.getMessage()+e.toString());
			}		
		}
		return rsList;
	}
	
	public void insertMinRecord(List<EquipData> eqInfo, int repeat){
		Map<Integer, BeanForPortData> records = new HashMap<Integer, BeanForPortData>();
		BeanForPortData bean = null;
		for (EquipData equipData : eqInfo) {
			bean = new BeanForPortData();
			bean.setMark(0); // 0无数据,1有数据
			bean.setEquipmentId(equipData.getEquipmentId());
			bean.setAddress(equipData.getAddress());
			records.put(equipData.getEquipmentId(), bean);
		}
		try {
			minRecordDao.insertRecord(records, repeat);
		} catch (Exception e) {
			log.error("总览数据插入时出错:" + e.getMessage()+e.toString());
		}		
	}
	
	public void updateMinRecord(Map<Integer, BeanForPortData> records){
		// 实例化服务类
		//minRecordDao = minRecordDao == null ?(MinRecordDao) daoMgr.getDao(MinRecordDao.class) : minRecordDao;
		try {
			minRecordDao.updateRecord(records);
		} catch (Exception e) {
			log.error("总览数据修改时出错:" + e.getMessage()+e.toString());
		}
	}
	
	public void deleteMinAll(){
		// 实例化服务类
		//minRecordDao = minRecordDao == null ?(MinRecordDao) daoMgr.getDao(MinRecordDao.class) : minRecordDao;
		
		try {
			minRecordDao.deleteAll();
		} catch (Exception e) {
			log.error("总览数据删除时,出错:" + e.getMessage()+e.toString());
		}
	}
	
	public void truncateMinRecord() {
		// 实例化服务类
		//minRecordDao = minRecordDao == null ?(MinRecordDao) daoMgr.getDao(MinRecordDao.class) : minRecordDao;
		try {
			minRecordDao.truncateRecord();
		} catch (Exception e) {
			log.error("总览数据删除时.出错:" + e.getMessage()+e.toString());
		}		
	}

	// TLog
	/**
	 * @describe : 插入系统日志 
	 * @param tLog : 待插入的数据
	 * @date:2009-11-4
	 */
	public synchronized void packTlog(int type, String msg){
		TLog tLog = new TLog();
		tLog.setLogtype(type);
		if (msg.length() >=100){
			msg = msg.substring(0,100);
		}
		tLog.setLogcontent(msg);
		tLog.setLogtime(new Date());
		
		try {
			reclogDao.insertLog(tLog);
		} catch (Exception e) {
			log.error("插入系统日志  时出错:" + e.getMessage()+e.toString());
			//ResetDatabase.readDBStatus();
		}
	}
	
	/**
	 * @describe: 根据ID删除 警报数据
	 * @param id ID
	 * @date:2009-11-4
	 */
	public void deleteLogById(int id) {
		
		// 实例化服务类
		//tLogDao = tLogDao == null ?(TLogDao) daoMgr.getDao(TLogDao.class) : tLogDao;
		
		try {
			reclogDao.deleteLogById(id);
		} catch (Exception e) {
			log.error("根据ID删除系统日志 时出错:" + e.getMessage()+e.toString());
		}
	}

	/**
	 * @describe: 根据ID列表批量删除 系统日志
	 * @param ids : ID列表
	 * @date:2009-11-4
	 */
	public void deleteLogBatch(int[] ids){
		
		// 实例化服务类
		//tLogDao = tLogDao == null ?	(TLogDao) daoMgr.getDao(TLogDao.class) : tLogDao;
		
		try {
			reclogDao.deleteLogBatch(ids);
		} catch (Exception e) {
			log.error("根据ID列表批量删除 系统日志 时出错:" + e.getMessage()+e.toString());
		}
	}
	
	
	// smsRecordDao
	public List<SmsRecord> searchSmsRecs(SmsRecord searchBean){
		List<SmsRecord> smsRecs = new ArrayList<SmsRecord>();
		try {
			smsRecs = smsRecordDao.searchSmsRecs(searchBean);
		} catch (Exception e) {
			log.error("查询短信信息 时出错:" + e.getMessage()+e.toString());
		}
		return smsRecs;
	}	
	
	public void insertSmsRec(SmsRecord smsRec){
		try {
			smsRecordDao.insertSmsRec(smsRec);
		} catch (Exception e) {
			log.error("插入短信信息  时出错:" + e.getMessage() + e.toString());
		}		
	}
	
	/**
	 * @describe : 分页-获取一段记录
	 * @param nameSpace : 空间名. 
	 * 	取AlarmRec.xml中，sqlMap namespace="AlarmRec" 的值
	 * 	取TLog.xml中，sqlMap namespace="TLog" 的值
	 * @param currentPage : 当前页
	 * @param pagerMethod : 翻页的方法. 有前一页,后一页,最前,最后 
	 * @param pageSize : 页面显示的最大数量
	 * @param args : 需要条件搜索是的参数. 参数需要对照,xml和page类
	 * @param limitCount : 搜索结果的可以返回的最大数值
	 * @return Pager 相关的分页信息
	 * @date:2009-11-4
	 */
	public Pager getPager(String nameSpace, String currentPage,
			String pagerMethod, int pageSize, Map<String, Object> args,
			int limitCount) {
		
		// 实例化服务类
		//basePageDao = basePageDao == null ?	(BasePageDao) daoMgr.getDao(BasePageDao.class) :  basePageDao;
		
		// AlarmRec为空间名 ：取AlarmRec.xml中，<sqlMap namespace="AlarmRec">的值
		// TLog为空间名 ：取TLog.xml中，<sqlMap namespace="TLog">的值
		this.basePageDao.setNameSpace(nameSpace);
		return this.basePageDao.getPager(currentPage, pagerMethod, pageSize,
				args, limitCount);
	}

}
