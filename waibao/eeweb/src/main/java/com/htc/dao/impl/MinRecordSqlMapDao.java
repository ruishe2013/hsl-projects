package com.htc.dao.impl;

import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

import com.htc.bean.BeanForMinRec;
import com.htc.bean.BeanForPortData;
import com.htc.dao.iface.MinRecordDao;

public class MinRecordSqlMapDao extends BaseSqlMapDao implements MinRecordDao {
	
	private String nameplace = "MinRecord.";
	
	public MinRecordSqlMapDao() {
	}

//	public MinRecordSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}
	
	public int getCounts(){
		return (Integer)getSqlMapClientTemplate().queryForObject(nameplace + "getCounts");
	}
	
	
	@SuppressWarnings("unchecked")
	public List<BeanForPortData> getNewestRec(String userPlaceList){
		BeanForMinRec beanMinRec = new BeanForMinRec();
		beanMinRec.setPlaceList(userPlaceList);
		beanMinRec.setRecLong(lastRecTime());// �õ������Ǽ�¼ʱ��
		return (List<BeanForPortData>)getSqlMapClientTemplate().queryForList(nameplace + "getNewestRec", beanMinRec);
	}
	
	public long lastRecTime(){
		Object obj = getSqlMapClientTemplate().queryForObject(nameplace + "lastRecTime");
		if (obj == null){return 0;
		}else{return (Long)obj;}
	}
	
	public long earlyRecTime(){
		Object obj = getSqlMapClientTemplate().queryForObject(nameplace + "earlyRecTime");
		if (obj == null){return 0;
		}else{return (Long)obj;}
	}
	
	@SuppressWarnings("unchecked")
	public List<BeanForPortData> selectAllRec(String userPlaceList){
		return (List<BeanForPortData>)getSqlMapClientTemplate().queryForList(nameplace + "selectAllRec", userPlaceList);
	}
	
	public void insertRecord(BeanForPortData record) {
		getSqlMapClientTemplate().insert(nameplace + "insertRecord", record);
	}
	public void insertRecord(Map<Integer, BeanForPortData> records, int repeat) throws SQLException {
		Date currentTime = new Date();
		long mill = currentTime.getTime();
//		getSqlMapClientTemplate().getSqlMapClient().startBatch();
//		int batch = 0;//��������
		
		for (int i = 0; i < repeat; i++) { // ����repeat(�ظ�����),��ӿ�����
			Set<Entry<Integer, BeanForPortData>> entrys= records.entrySet();
			BeanForPortData record;//ֵ
			mill = mill + 1;
			for(Entry<Integer, BeanForPortData> entry: entrys){
				record = (BeanForPortData) entry.getValue();
				record.setRecTime(currentTime);
				record.setRecLong(mill);
				insertRecord(record);
				
//				batch++;			// ÿ500�������ύһ�Ρ� 				
//				if(batch == BATCH_SIZE){ 
//					getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
//					batch = 0; 
//				}            
			}
		}
//		getSqlMapClientTemplate().getSqlMapClient().executeBatch();		
	}
	
	public void updateRecord(BeanForPortData record){
		getSqlMapClientTemplate().update(nameplace + "updateRecord", record);
	}		
	public void updateRecord(Map<Integer, BeanForPortData> records) throws SQLException {
		Date currentTime = new Date();
		long oldmill = earlyRecTime();
		long mill = currentTime.getTime();
//		getSqlMapClientTemplate().getSqlMapClient().startBatch();
//		int batch = 0;//��������
		
		Set<Entry<Integer, BeanForPortData>> entrys= records.entrySet();
		BeanForPortData record;//ֵ
        for(Entry<Integer, BeanForPortData> entry: entrys){
            record = null;     record = (BeanForPortData) entry.getValue();
            record.setRecTime(currentTime);
            record.setRecLong(mill);
            record.setOldrecLong(oldmill);
            updateRecord(record);

			//batch++;			// ÿ500�������ύһ�Ρ� 				
//			if(batch == BATCH_SIZE){ 
//				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
//				batch = 0; 
//			}            
        }
//        getSqlMapClientTemplate().getSqlMapClient().executeBatch();	
	}
	
	public void deleteAll() throws Exception {
		getSqlMapClientTemplate().delete(nameplace + "deleteAll");
	}

	public void truncateRecord() throws Exception {
		getSqlMapClientTemplate().delete(nameplace + "truncateRecord");
	}

}
