package com.htc.dao.impl;

import java.util.List;

import com.htc.bean.BeanForZjHisRec;
import com.htc.dao.iface.ZjHistroyDao;
import com.htc.domain.ZjHistory;

public class ZjHistroySqlMapDao extends BaseSqlMapDao implements ZjHistroyDao {
	
	private String nameplace = "ZjHistory.";
	
	public ZjHistroySqlMapDao() {
	}

	@SuppressWarnings("unchecked")
	public List<ZjHistory> selectZjHisRec(BeanForZjHisRec zjBean)throws Exception {
		return (List<ZjHistory>)getSqlMapClientTemplate().queryForList(nameplace + "selectZjHisRec", zjBean);
	}	
	
}
