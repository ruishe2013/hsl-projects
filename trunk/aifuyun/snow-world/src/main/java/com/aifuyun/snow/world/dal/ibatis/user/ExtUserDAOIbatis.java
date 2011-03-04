package com.aifuyun.snow.world.dal.ibatis.user;

import com.aifuyun.snow.world.dal.daointerface.user.ExtUserDAO;
import com.aifuyun.snow.world.dal.dataobject.user.ExtUserDO;
import com.aifuyun.snow.world.dal.ibatis.BaseIbatisDAO;

public class ExtUserDAOIbatis extends BaseIbatisDAO implements ExtUserDAO {

	@Override
	public void create(ExtUserDO extUserDO) {
		this.getSqlMapClientTemplate().insert("ExtUserDAO.create", extUserDO);
	}

	@Override
	public ExtUserDO queryByUserId(long userId) {
		return (ExtUserDO)this.getSqlMapClientTemplate().queryForObject("ExtUserDAO.queryByUserId", userId);
	}

	@Override
	public void delete(long userId) {
		this.getSqlMapClientTemplate().update("ExtUserDAO.delete", userId);
	}

	@Override
	public void update(ExtUserDO extUserDO) {
		this.getSqlMapClientTemplate().update("ExtUserDAO.update", extUserDO);
	}

}
