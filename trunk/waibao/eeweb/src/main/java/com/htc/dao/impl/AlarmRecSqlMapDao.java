package com.htc.dao.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.bean.BeanForAlarm;
import com.htc.dao.iface.AlarmRecDao;
import com.htc.domain.AlarmRec;

public class AlarmRecSqlMapDao extends BaseSqlMapDao implements AlarmRecDao {

	private String nameplace = "AlarmRec.";
	//private Log log = LogFactory.getLog(AlarmRecSqlMapDao.class);
	
	public AlarmRecSqlMapDao() {
	}
	
//	public AlarmRecSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}

	@SuppressWarnings("unchecked")
	public List<AlarmRec> selectAll(AlarmRec alarmRec) throws Exception {
		return (List<AlarmRec>)getSqlMapClientTemplate().queryForList(nameplace + "selectAllBean", alarmRec);
	}	
	
	@SuppressWarnings("unchecked")
	public List<AlarmRec> selectUndoRec() throws Exception{
		return (List<AlarmRec>)getSqlMapClientTemplate().queryForList(nameplace + "selectUndoRec");
	}
	
	public AlarmRec selectRec(int equiId) throws Exception{
		BeanForAlarm beanForAlarm = new BeanForAlarm();
		beanForAlarm.setEquipmentId(equiId);
		beanForAlarm.setAlarmStart(selectMaxRec(equiId));// 获取主键为equiId的仪器最近记录的报警时间
		return (AlarmRec)getSqlMapClientTemplate().queryForObject(nameplace + "selectRec", beanForAlarm);
	}

	public long selectMaxRec(int equiId) throws Exception {
		Object obj = getSqlMapClientTemplate().queryForObject(nameplace + "selectMaxRec", equiId);
		if (obj == null){
			return  0;
		}else{
			return  (Long)obj;
		}
	}
	
	public void insertAlarm(AlarmRec alarmRec) throws Exception {
		getSqlMapClientTemplate().insert(nameplace + "insertAlarm", alarmRec);
	}

	public int updateAlarm(AlarmRec alarmRec) throws Exception {
		return getSqlMapClientTemplate().update(nameplace + "updateAlarm", alarmRec);
	}
	
	public void updateAlarming(AlarmRec alarmRec) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateAlarming", alarmRec);
	}

	public void deleteAlarmById(int alarmId) throws Exception {
		getSqlMapClientTemplate().delete(nameplace + "deleteAlarmById", alarmId);
	}

	public void deleteAlarmBatch(int[] alarmIds) throws Exception {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		int batch = 0;//批量基数
		for (int alarmId : alarmIds) {
			deleteAlarmById(alarmId);
			batch++;			// 每500条批量提交一次。 				
			if(batch == BATCH_SIZE){ 
				//提交批量
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); 
				batch = 0; 
			} 			
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}

}
