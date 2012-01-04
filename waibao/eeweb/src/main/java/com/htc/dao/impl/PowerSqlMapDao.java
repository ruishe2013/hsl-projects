package com.htc.dao.impl;

import java.util.List;

import com.htc.domain.Power;
import com.htc.dao.iface.PowerDao;

public class PowerSqlMapDao extends BaseSqlMapDao implements PowerDao {
	
	private String nameplace = "Power.";
	
	public PowerSqlMapDao() {
	}

//	public PowerSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}

	@SuppressWarnings("unchecked")
	public List<Power> findAllPower() throws Exception {
		return (List<Power>)getSqlMapClientTemplate().queryForList(nameplace + "selectAll");
	}	
	
}
