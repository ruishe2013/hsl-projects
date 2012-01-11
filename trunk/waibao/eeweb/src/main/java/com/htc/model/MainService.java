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
 * ���� : ��ʱ����, ��ʷ����(�ձ�,�±�), ��������, ϵͳ��־. ��ز���
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class MainService {
	
	private Log log = LogFactory.getLog(MainService.class);

	//private static MainService mainService = new MainService();

	/**
	 * TYPE_NO_STAT :��ͳ��ֻ��ʾ����,��ʱ������
	 */
	public static final int TYPE_NO_STAT = 1;
	/**
	 * TYPE_MIN_STAT :��Сֵ,�ձ����±���
	 */
	public static final int TYPE_MIN_STAT = 10;
	/**
	 * TYPE_AVG_STAT :ƽ��ֵ,�ձ����±���
	 */
	public static final int TYPE_AVG_STAT = 100;
	/**
	 * TYPE_MAX_STAT :���ֵ,�ձ����±���
	 */
	public static final int TYPE_MAX_STAT = 1000;
	/**
	 * TYPE_RECENT :��ˮ��¼����
	 */
	public static final int TYPE_RECENT = 1;// ��ˮ��¼
	/**
	 * TYPE_DAILY :�ձ�������
	 */
	public static final int TYPE_DAILY = 2;// �ձ���
	/**
	 * TYPE_MONTH : �±�������
	 */
	public static final int TYPE_MONTH = 3;// �±���

	//private DaoManager daoMgr = DaoConfig.getDaoManager();
	private AlarmRecDao alarmRecDao;
	private RecordDao recordDao;
	private HisRecordDao hisRecordDao;
	private MinRecordDao minRecordDao;
	private TLogDao reclogDao;	
	private SmsRecordDao smsRecordDao;
	private BasePageDao basePageDao;// ������ҳdao

	// ���췽��
	public MainService() {
	}
	//ע��service -- spring ioc
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
	 * @describe: ��ȡMainService�� ����
	 * @return
	 * @date:2009-11-4
	 */
//	public static MainService getInstance() {
//		return mainService;
//	}

	/**
	 * ��ȡ�����б�(��ѯ��������)
	 */
	public List<AlarmRec> getAllAlarmRec(AlarmRec alarmRec){
		// ʵ����������
		//alarmRecDao = alarmRecDao == null ?(AlarmRecDao) daoMgr.getDao(AlarmRecDao.class) : alarmRecDao;
		
		List<AlarmRec> alarmList = new ArrayList<AlarmRec>();
		try {
			alarmList = alarmRecDao.selectAll(alarmRec);
		} catch (Exception e) {
			log.error("��ȡ�����б�(��ѯ��������)ʱ����:" + e.getMessage()+e.toString());
		}
	
		return alarmList;
	}
	
	/**
	 * ��ȡ�����б�(��̬��������)
	 */
	public List<AlarmRec> selectUndoRec(){
		List<AlarmRec> alarmList = null;
		try {
			alarmList = alarmRecDao.selectUndoRec();
		} catch (Exception e) {
			alarmList = null;
			log.error("��ȡ�����б�(��̬��������)ʱ����:" + e.getMessage()+e.toString());
		}
		return alarmList;		
	}
	
	/**
	 * �Ե�ǰ�ı�������,Ӧ��ִ�����ֲ���: ��ִ���κβ���, ��������޸�
	 * @return
	 * 0:��ѯ�����д���,��ִ���κβ���<br>
	 * 1:ִ�в���<br>
	 * 2:ִ���޸�<br>
	 */
	public BeanForAlarmRs getDoWhich(int equiId){
		BeanForAlarmRs rsBean = new BeanForAlarmRs();
		AlarmRec tempRec = null;
		try {
			tempRec = alarmRecDao.selectRec(equiId);
			if (tempRec != null){// �޸�
				// �����õı������,����ִ����� (update talarmrec set alarmEnd = #alarmEnd# where	alarmId = #alarmId#)
				rsBean.setAlarmId(tempRec.getAlarmId());
				rsBean.setRsType(BeanForAlarmRs.UPDATE_ALARM);
			}else{// ����
				rsBean.setRsType(BeanForAlarmRs.ADD_ALARM);
			}
		} catch (Exception e) {
			rsBean.setRsType(BeanForAlarmRs.NUDO_ALARM);
			log.error("�жϾ������ݲ���:������߸���  ʱ����:" + e.getMessage()+e.toString());
		}
		return rsBean;
	}	
	
	/**
	 * @describe:	���뾯������
	 * @param alarmRec : �������ݰ�
	 * @date:2009-11-4
	 */
	public void insertAlarmRec(AlarmRec alarmRec){
		
		// ʵ����������
		//alarmRecDao = alarmRecDao == null ?	(AlarmRecDao) daoMgr.getDao(AlarmRecDao.class) : alarmRecDao;
		
		try {
			alarmRecDao.insertAlarm(alarmRec);
		} catch (Exception e) {
			log.error("���뾯������ ʱ����:" + e.getMessage() + e.toString());
		}
	}
	
	/**
	 * @describe:	�޸� �������� -- ������ʱ��
	 * @param alarmRec : �������ݰ�
	 * @date:2009-11-4
	 */
	public int updateAlarmRec(AlarmRec alarmRec) {
		int effectRow = -1; //Ӱ�������
		
		// ʵ����������
		//alarmRecDao = alarmRecDao == null ?	(AlarmRecDao) daoMgr.getDao(AlarmRecDao.class) : alarmRecDao;
		
		try {
			effectRow = alarmRecDao.updateAlarm(alarmRec);
		} catch (Exception e) {
			effectRow = -1;
			log.error(" �޸� ��������(������)ʱ����:" + e.getMessage()+e.toString());
		}
		return effectRow;
	}
	
	/**
	 * @describe:	�޸� �������� -- �޸����±�����ʱ��
	 * @param alarmRec : �������ݰ�
	 * @date:2009-11-4
	 */
	public void updateAlarmRecing(AlarmRec alarmRec) {
		
		// ʵ����������
		//alarmRecDao = alarmRecDao == null ?	(AlarmRecDao) daoMgr.getDao(AlarmRecDao.class) : alarmRecDao;
		
		try {
			alarmRecDao.updateAlarming(alarmRec);
		} catch (Exception e) {
			log.error(" �޸� ��������(�޸����±���ʱ��)ʱ����:" + e.getMessage()+e.toString());
		}
	}

	/**
	 * @describe: ����IDɾ�� ��������
	 * @param alarmId ID
	 * @date:2009-11-4
	 */
	public void deleteAlarmRecById(int alarmId) {
		
		// ʵ����������
		//alarmRecDao = alarmRecDao == null ?	(AlarmRecDao) daoMgr.getDao(AlarmRecDao.class) : alarmRecDao;
		
		try {
			alarmRecDao.deleteAlarmById(alarmId);
		} catch (Exception e) {
			log.error("����IDɾ�� �������� ʱ����:" + e.getMessage()+e.toString());
		}
	}

	/**
	 * @describe: ����ID�б�����ɾ�� ��������
	 * @param alarmIds : ID�б�
	 * @date:2009-11-4
	 */
	public void deleteAlarmRecBatch(int[] alarmIds){
		
		// ʵ����������
		//alarmRecDao = alarmRecDao == null ?	(AlarmRecDao) daoMgr.getDao(AlarmRecDao.class) : alarmRecDao;
		
		try {
			alarmRecDao.deleteAlarmBatch(alarmIds);
		} catch (Exception e) {
			log.error("����ID�б�����ɾ�� ��������ʱ����:" + e.getMessage()+e.toString());
		}
	}

	// Record �� HisRecord
	/**
	 * @describe: ��ȡ��ʱ���������¼ʱ��	
	 * @date:2010-1-5
	 */
	public Date getRecordEearlyTime(){
		Date rsDate = null;
		// ʵ����������
		//recordDao = recordDao == null?(RecordDao) daoMgr.getDao(RecordDao.class) : recordDao;		
		rsDate = FunctionUnit.getStrToDate(recordDao.getEarlyTime());
		return rsDate;
	}
	
	/**
	 * @describe: ��ȡ�ձ�������±������������¼ʱ��
	 * @param type: TYPE_DAILY:2(�ձ�������) TYPE_MONTH:3(�±�������)
	 * @date:2010-1-5
	 */
	public Date getHisRecEearlyTime(int type){
		Date rsDate = null;
		// ʵ����������
		//hisRecordDao = hisRecordDao == null ?(HisRecordDao) daoMgr.getDao(HisRecordDao.class) : hisRecordDao ;	
		rsDate = FunctionUnit.getStrToDate(hisRecordDao.getEarlyTime(type));
		return rsDate;
	}
	
	/**
	 * @describe: ��ȡ�ձ������±� ���ļ�¼ʱ��	
	 * @param type: TYPE_DAILY:2(�ձ�������) TYPE_MONTH:3(�±�������)
	 * @return:
	 * @date:2010-1-5
	 */
	public Date getHisRecLastTime(int type){
		Date rsDate = null;
		// ʵ����������
		//hisRecordDao = hisRecordDao == null ?(HisRecordDao) daoMgr.getDao(HisRecordDao.class) : hisRecordDao ;
		try {
			rsDate = FunctionUnit.getStrToDate(hisRecordDao.getLastTime(type));
		} catch (Exception e) {
			log.error("��ȡ"+(type==2?"�ձ�":"�±�")+"���ļ�¼ʱ�����:" + e.getMessage()+e.toString());
		}		
		return rsDate;
	} 
	
	/**
	 * @describe : ����������ʱ���� 
	 * @param mapRecords ������������� Map<Integer, Record>
	 * @date:2009-11-4
	 */
	public void insertRecordBatch(Map<Integer, Record> mapRecords){
		// ʵ����������
		//recordDao = recordDao == null?(RecordDao) daoMgr.getDao(RecordDao.class) : recordDao;
		
		try {
			recordDao.insertRecordBatch(mapRecords);
		} catch (Exception e) {
			log.error("����������ʱ���� ʱ����:" + e.getMessage()+e.toString());
			//ResetDatabase.readDBStatus();
		}
	}
	
	/**
	 * @describe: ����ʱ������ɾ����ʱ����	
	 * @param endRecTime: ����ʱ��(ɾ�������ʱ��֮ǰ������)
	 * @date:2010-1-5
	 */
	public void deleteRecordBatch(String endRecTime){
		// ʵ����������
		//recordDao = recordDao == null?(RecordDao) daoMgr.getDao(RecordDao.class) : recordDao;		
		try {
			recordDao.deleteRecordBatch(endRecTime);
		} catch (Exception e) {
			log.error("����ɾ�����ڵ���ʱ���ݳ���:" + e.getMessage()+e.toString());
		}
	}
	
	/**
	 * @describe : ������ʷ����(�ձ����±�) 
	 * @param hisRecord : �����������HisRecord
	 * @date:2009-11-4
	 */
	public void insertHisRecord(HisRecord hisRecord) throws Exception {
		
		// ʵ����������
		//hisRecordDao = hisRecordDao == null ?(HisRecordDao) daoMgr.getDao(HisRecordDao.class) : hisRecordDao ;
			
		hisRecordDao.insertHisRecord(hisRecord);
	}

	/**
	 * @describe : �����ʱ���ݱ�,�������������ʹ���������´�1��ʼ�Զ�����--��ʱ����  
	 * @date:2009-11-4
	 */
	public void truncateRecord() {
		
		// ʵ����������
		//recordDao = recordDao == null ?	(RecordDao) daoMgr.getDao(RecordDao.class) : recordDao;
		
		try {
			recordDao.truncateRecord();
		} catch (Exception e) {
			log.error("�����ʱ���ݱ�ʱ����:" + e.getMessage()+e.toString());
		}
	}

	/**
	 * @describe : ����RecordSearchBean��װ��,������¼
	 * @param beanForSearchRecord: ��װ��:���������б�,��ʼʱ��,����ʱ��,������������
	 * @return ������RecordSearchBean��װ��,������¼�ķ��ؼ���
	 * @throws Exception: ���ݿ��쳣
	 * @date:2009-11-3
	 */
	public List<BeanForRecord> searchRecords(BeanForSearchRecord  beanForSearchRecord ) {
		List<BeanForRecord> rsList = null; // ���ر���:���ݿ�����õ��Ľ����
		// �������ݿ�
		if (beanForSearchRecord.getStattype() == 0){	//��ʱ����
			
			// ʵ����������
			//recordDao = recordDao == null ?	(RecordDao) daoMgr.getDao(RecordDao.class) : recordDao;
			
			try {
				rsList = recordDao.selectAllBean(beanForSearchRecord);
			} catch (Exception e) {
				log.error("������ʱ����ʱ����:" + e.getMessage()+e.toString());
			}
		}else{										//�ձ������±�
			
			// ʵ����������
			//hisRecordDao =  hisRecordDao == null ?(HisRecordDao) daoMgr.getDao(HisRecordDao.class) :  hisRecordDao;
			
			try {
				rsList = hisRecordDao.selectStatItmes(beanForSearchRecord);
			} catch (Exception e) {
				log.error("�����ձ������±�ʱ����:" + e.getMessage()+e.toString());
			}
		}
		return rsList;
	}
	
	// MinRecord
	public int getMinCounts(){
		int rsint = -1;
		// ʵ����������
		//minRecordDao = minRecordDao == null ?(MinRecordDao) daoMgr.getDao(MinRecordDao.class) : minRecordDao;
	
		try {
			rsint = minRecordDao.getCounts();
		} catch (Exception e) {
			rsint = -1;
			log.error("��������,������������(��������)ʱ ����:" + e.getMessage()+e.toString());
		}				
		return rsint;
	}
	
	public Map<Integer, BeanForPortData> getNewestMinRec(String userPlaceList) {
		// ʵ����������
		//minRecordDao = minRecordDao == null ?(MinRecordDao) daoMgr.getDao(MinRecordDao.class) : minRecordDao;
		Map<Integer, BeanForPortData> rsMap = new HashMap<Integer, BeanForPortData>();
		
		// ���˿��ַ���
		if ( (userPlaceList != null && userPlaceList.length()> 0) ){
			try {
				List<BeanForPortData> rsList = minRecordDao.getNewestRec(userPlaceList);
				for (BeanForPortData bean : rsList) {
					rsMap.put(bean.getEquipmentId(), bean);
				}
			} catch (Exception e) {
				rsMap = null;
				log.error("��������,������������(ʵʱ����)ʱ ����:"  + e.getMessage()+e.toString());
			}
		}
		return rsMap;
	}
	
	public List<BeanForPortData> getNewestMinRecAll() {
		try {
			return minRecordDao.getNewestRecAll();
		} catch (Exception e) {
			throw new RuntimeException("�����������ݴ���:", e);
		}
	}
	
	public List<BeanForPortData> selectMinAllRec(String userPlaceList) {
		// ʵ����������
		//minRecordDao = minRecordDao == null ?(MinRecordDao) daoMgr.getDao(MinRecordDao.class) : minRecordDao;
		List<BeanForPortData> rsList = new ArrayList<BeanForPortData>(); // ���ر���:���ݿ�����õ��Ľ����
		// ���˿��ַ���
		if ( (userPlaceList != null && userPlaceList.length()> 0) ){
			try {
				rsList = minRecordDao.selectAllRec(userPlaceList);
			} catch (Exception e) {
				log.error("ʵʱ����,������������ʱ ����:" + e.getMessage()+e.toString());
			}		
		}
		return rsList;
	}
	
	public void insertMinRecord(List<EquipData> eqInfo, int repeat){
		Map<Integer, BeanForPortData> records = new HashMap<Integer, BeanForPortData>();
		BeanForPortData bean = null;
		for (EquipData equipData : eqInfo) {
			bean = new BeanForPortData();
			bean.setMark(0); // 0������,1������
			bean.setEquipmentId(equipData.getEquipmentId());
			bean.setAddress(equipData.getAddress());
			records.put(equipData.getEquipmentId(), bean);
		}
		try {
			minRecordDao.insertRecord(records, repeat);
		} catch (Exception e) {
			log.error("�������ݲ���ʱ����:" + e.getMessage()+e.toString());
		}		
	}
	
	public void updateMinRecord(Map<Integer, BeanForPortData> records){
		// ʵ����������
		//minRecordDao = minRecordDao == null ?(MinRecordDao) daoMgr.getDao(MinRecordDao.class) : minRecordDao;
		try {
			minRecordDao.updateRecord(records);
		} catch (Exception e) {
			log.error("���������޸�ʱ����:" + e.getMessage()+e.toString());
		}
	}
	
	public void deleteMinAll(){
		// ʵ����������
		//minRecordDao = minRecordDao == null ?(MinRecordDao) daoMgr.getDao(MinRecordDao.class) : minRecordDao;
		
		try {
			minRecordDao.deleteAll();
		} catch (Exception e) {
			log.error("��������ɾ��ʱ,����:" + e.getMessage()+e.toString());
		}
	}
	
	public void truncateMinRecord() {
		// ʵ����������
		//minRecordDao = minRecordDao == null ?(MinRecordDao) daoMgr.getDao(MinRecordDao.class) : minRecordDao;
		try {
			minRecordDao.truncateRecord();
		} catch (Exception e) {
			log.error("��������ɾ��ʱ.����:" + e.getMessage()+e.toString());
		}		
	}

	// TLog
	/**
	 * @describe : ����ϵͳ��־ 
	 * @param tLog : �����������
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
			log.error("����ϵͳ��־  ʱ����:" + e.getMessage()+e.toString());
			//ResetDatabase.readDBStatus();
		}
	}
	
	/**
	 * @describe: ����IDɾ�� ��������
	 * @param id ID
	 * @date:2009-11-4
	 */
	public void deleteLogById(int id) {
		
		// ʵ����������
		//tLogDao = tLogDao == null ?(TLogDao) daoMgr.getDao(TLogDao.class) : tLogDao;
		
		try {
			reclogDao.deleteLogById(id);
		} catch (Exception e) {
			log.error("����IDɾ��ϵͳ��־ ʱ����:" + e.getMessage()+e.toString());
		}
	}

	/**
	 * @describe: ����ID�б�����ɾ�� ϵͳ��־
	 * @param ids : ID�б�
	 * @date:2009-11-4
	 */
	public void deleteLogBatch(int[] ids){
		
		// ʵ����������
		//tLogDao = tLogDao == null ?	(TLogDao) daoMgr.getDao(TLogDao.class) : tLogDao;
		
		try {
			reclogDao.deleteLogBatch(ids);
		} catch (Exception e) {
			log.error("����ID�б�����ɾ�� ϵͳ��־ ʱ����:" + e.getMessage()+e.toString());
		}
	}
	
	
	// smsRecordDao
	public List<SmsRecord> searchSmsRecs(SmsRecord searchBean){
		List<SmsRecord> smsRecs = new ArrayList<SmsRecord>();
		try {
			smsRecs = smsRecordDao.searchSmsRecs(searchBean);
		} catch (Exception e) {
			log.error("��ѯ������Ϣ ʱ����:" + e.getMessage()+e.toString());
		}
		return smsRecs;
	}	
	
	public void insertSmsRec(SmsRecord smsRec){
		try {
			smsRecordDao.insertSmsRec(smsRec);
		} catch (Exception e) {
			log.error("���������Ϣ  ʱ����:" + e.getMessage() + e.toString());
		}		
	}
	
	/**
	 * @describe : ��ҳ-��ȡһ�μ�¼
	 * @param nameSpace : �ռ���. 
	 * 	ȡAlarmRec.xml�У�sqlMap namespace="AlarmRec" ��ֵ
	 * 	ȡTLog.xml�У�sqlMap namespace="TLog" ��ֵ
	 * @param currentPage : ��ǰҳ
	 * @param pagerMethod : ��ҳ�ķ���. ��ǰһҳ,��һҳ,��ǰ,��� 
	 * @param pageSize : ҳ����ʾ���������
	 * @param args : ��Ҫ���������ǵĲ���. ������Ҫ����,xml��page��
	 * @param limitCount : ��������Ŀ��Է��ص������ֵ
	 * @return Pager ��صķ�ҳ��Ϣ
	 * @date:2009-11-4
	 */
	public Pager getPager(String nameSpace, String currentPage,
			String pagerMethod, int pageSize, Map<String, Object> args,
			int limitCount) {
		
		// ʵ����������
		//basePageDao = basePageDao == null ?	(BasePageDao) daoMgr.getDao(BasePageDao.class) :  basePageDao;
		
		// AlarmRecΪ�ռ��� ��ȡAlarmRec.xml�У�<sqlMap namespace="AlarmRec">��ֵ
		// TLogΪ�ռ��� ��ȡTLog.xml�У�<sqlMap namespace="TLog">��ֵ
		this.basePageDao.setNameSpace(nameSpace);
		return this.basePageDao.getPager(currentPage, pagerMethod, pageSize,
				args, limitCount);
	}

}
