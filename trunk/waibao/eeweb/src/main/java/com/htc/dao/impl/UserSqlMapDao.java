package com.htc.dao.impl;

import java.sql.SQLException;

import com.htc.domain.User;
import com.htc.dao.iface.UserDao;

public class UserSqlMapDao extends BaseSqlMapDao implements UserDao {
	
	private String nameplace = "User.";

	public UserSqlMapDao() {
	}
	
//	public UserSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}

	public User findUser(User user) {
		return (User) getSqlMapClientTemplate().queryForObject(nameplace + "selectUser", user);
	}

	public void insertUser(User user) {
		getSqlMapClientTemplate().insert(nameplace + "insertUser", user);
	}

	public void updateUser(User user) {
		getSqlMapClientTemplate().update(nameplace + "updateUser", user);
		
	}

	public void deleteUser(int userId) {
		getSqlMapClientTemplate().update(nameplace + "updateUseless", userId);//��ɾ����ֻ������
		//delete(nameplace + "deleteUserByName", username);
	}

	public void deleteBatch(int[] userIds) throws SQLException {
		getSqlMapClientTemplate().getSqlMapClient().startBatch();
		int batch = 0;//��������
		for (int userId : userIds) {
			deleteUser(userId);
			
			batch++;			// ÿ500�������ύһ�Ρ� 				
			if(batch == BATCH_SIZE){ 
				getSqlMapClientTemplate().getSqlMapClient().executeBatch(); //�ύ����
				batch = 0; 
			} 
		}
		getSqlMapClientTemplate().getSqlMapClient().executeBatch();
	}

}
