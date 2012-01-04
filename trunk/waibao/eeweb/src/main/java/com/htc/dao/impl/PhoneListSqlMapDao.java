package com.htc.dao.impl;

import java.util.List;

import com.htc.domain.PhoneList;
import com.htc.dao.iface.PhoneListDao;

public class PhoneListSqlMapDao extends BaseSqlMapDao implements PhoneListDao {
	
	private String nameplace = "PhoneList.";
	
	public PhoneListSqlMapDao() {
	}

//	public PhoneListSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}

	@SuppressWarnings("unchecked")
	public List<PhoneList> findAllPhoneList() throws Exception {
		return (List<PhoneList>)getSqlMapClientTemplate().queryForList(nameplace + "selectAll");
	}		
	
	@SuppressWarnings("unchecked")
	public List<PhoneList> findPhoneList(PhoneList phoneList) throws Exception {
		return (List<PhoneList>)getSqlMapClientTemplate().queryForList(nameplace + "selectPhoneList",phoneList);
	}
	
	@SuppressWarnings("unchecked")
	public List<PhoneList> findPhoneList(String listIds) throws Exception {
		return (List<PhoneList>)getSqlMapClientTemplate().queryForList(nameplace + "selectPhoneByIds",listIds);
	}

	public void insertPhoneList(PhoneList phoneList) throws Exception {
		getSqlMapClientTemplate().insert(nameplace + "insertPhoneList",phoneList);
	}

	public void updatePhoneList(PhoneList phoneList) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updatePhoneList",phoneList);
	}

	public void deletePhoneList(int phoneListId) throws Exception {
		getSqlMapClientTemplate().update(nameplace + "updateUseless",phoneListId);//不删除，只是隐藏
		//delete(nameplace + "deletePhoneListById", phoneListId);
	}

	public void deleteBatch(int[] phoneListIds) throws Exception {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		int batch = 0;//批量基数
		for (int i : phoneListIds) {
			deletePhoneList(i);
			batch++;			// 每500条批量提交一次。 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //提交批量
				batch = 0; 
			} 
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}
	
}
