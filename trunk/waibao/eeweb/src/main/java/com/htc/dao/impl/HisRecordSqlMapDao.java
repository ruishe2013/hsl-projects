package com.htc.dao.impl;

import java.sql.SQLException;
import java.util.*;

import com.htc.bean.BeanForRecord;
import com.htc.bean.BeanForSearchRecord;
import com.htc.common.FunctionUnit;
import com.htc.dao.iface.HisRecordDao;
import com.htc.domain.*;
import com.htc.model.MainService;

public class HisRecordSqlMapDao extends BaseSqlMapDao implements HisRecordDao {
	
	private String nameplace = "";
	private String nameplace_dailay = "HisRecordDailay.";
	private String nameplace_month = "HisRecordMonth.";
	
	public HisRecordSqlMapDao() {
	}
	
//	public HisRecordSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}
	
	public String getEarlyTime(int stattype){
		nameplace = stattype == MainService.TYPE_DAILY ? nameplace_dailay : nameplace_month ;
		return (String) getSqlMapClientTemplate().queryForObject(nameplace + "getEarlyTime");
	}
	
	public String getLastTime(int stattype){
		nameplace = stattype == MainService.TYPE_DAILY ? nameplace_dailay : nameplace_month ;
		return (String) getSqlMapClientTemplate().queryForObject(nameplace + "getLastTime");
	}
	
	@SuppressWarnings("unchecked")
	public List<BeanForRecord> selectStatItmes(BeanForSearchRecord beanForSearchRecord){
		nameplace = beanForSearchRecord.getStattype() == 1? nameplace_dailay : nameplace_month ;
		return (List<BeanForRecord>)getSqlMapClientTemplate().queryForList(nameplace + "selectStatItmes",beanForSearchRecord);
	}

	public void insertHisRecord(HisRecord hisRecord) throws SQLException {
		int hisType = hisRecord.getStattype();	//stattype (1:�����ձ�����	 2:�����±�����)
		nameplace = hisType == 1 ? nameplace_dailay : nameplace_month ;
		
		Date formTime = hisRecord.getAlarmStartFrom();
		Date endTime = hisRecord.getAlarmStartTo();
		Date tempTime = formTime; //��һ��ʱ��
		String recTimeStr = ""; //ʱ���ַ���
		
		getSqlMapClientTemplate().getSqlMapClient().startBatch();	//��ʼ����
		int batch = 0;//��������
		while (tempTime.before(endTime)) {
			try {
				if (hisType == 1){
					tempTime = FunctionUnit.nextTime(formTime, 1, FunctionUnit.Calendar_END_HOUR).getTime();
					recTimeStr = FunctionUnit.getDateToStr(formTime, 
							FunctionUnit.Calendar_END_HOUR, FunctionUnit.UN_SHOW_CHINESE);
				}else if (hisType == 2){
					tempTime = FunctionUnit.nextTime(formTime, 1, FunctionUnit.Calendar_END_DAY).getTime();		
					recTimeStr = FunctionUnit.getDateToStr(formTime, 
							FunctionUnit.Calendar_END_DAY, FunctionUnit.UN_SHOW_CHINESE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//����Ƭ�ε���ʼʱ��,������������
			hisRecord.setAlarmStartFrom(formTime);
			hisRecord.setAlarmStartTo(tempTime);
			hisRecord.setRecTimeStr(recTimeStr);
			getSqlMapClientTemplate().insert(nameplace + "insertHisRecord", hisRecord);
			formTime = tempTime;
			
			batch++;			// ÿ500�������ύһ�Ρ� 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
				batch = 0; 
			} 
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();	//�ύ����
			
	}

	public void updateHisRecord(HisRecord hisRecord) throws Exception {
		nameplace = hisRecord.getStattype() == 1 ? nameplace_dailay : nameplace_month ;
		getSqlMapClientTemplate().update(nameplace + "updateHisRecord", hisRecord);
	}			

	public void deleteHisRecord(int recId, int stattype) throws Exception {
		nameplace = stattype == MainService.TYPE_DAILY ? nameplace_dailay : nameplace_month ;
		getSqlMapClientTemplate().delete(nameplace + "deleteHisRecordById", recId);
	}
	
	public void deleteHisRecordBatch(int[] recIds, int stattype) throws Exception {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();			//��ʼ����
		int batch = 0;			//��������
		for (int recId : recIds) {
			deleteHisRecord(recId, stattype);
			batch++;			// ÿ500�������ύһ�Ρ� 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
				batch = 0; 
			} 			
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();			//�ύ����
	}

}
