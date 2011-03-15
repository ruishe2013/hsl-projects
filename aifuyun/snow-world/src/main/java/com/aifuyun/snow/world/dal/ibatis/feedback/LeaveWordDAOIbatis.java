package com.aifuyun.snow.world.dal.ibatis.feedback;

import com.aifuyun.snow.world.dal.daointerface.feedback.LeaveWordDAO;
import com.aifuyun.snow.world.dal.dataobject.feedback.LeaveWordDO;
import com.aifuyun.snow.world.dal.ibatis.BaseIbatisDAO;

public class LeaveWordDAOIbatis extends BaseIbatisDAO implements LeaveWordDAO {

	@Override
	public int create(LeaveWordDO leaveWord) {
		return (Integer)this.getSqlMapClientTemplate().insert("LeaveWordDAO.create", leaveWord);
	}

}
