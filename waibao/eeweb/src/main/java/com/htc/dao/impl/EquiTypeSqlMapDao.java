package com.htc.dao.impl;

import java.util.List;

import com.htc.domain.Equitype;
import com.htc.dao.iface.EquiTypeDao;

public class EquiTypeSqlMapDao extends BaseSqlMapDao implements EquiTypeDao {
	
	private String nameplace = "Equitype.";

	public EquiTypeSqlMapDao() {
	}
	
//	public EquiTypeSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}

	@SuppressWarnings("unchecked")
	public List<Equitype> findAllEquitype() throws Exception {
		return (List<Equitype>)getSqlMapClientTemplate().queryForList(nameplace + "selectAll");
	}	
	
}
