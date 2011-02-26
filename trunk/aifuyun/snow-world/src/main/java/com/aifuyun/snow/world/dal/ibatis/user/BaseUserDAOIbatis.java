package com.aifuyun.snow.world.dal.ibatis.user;

import java.util.List;

import com.aifuyun.snow.world.dal.daointerface.user.BaseUserDAO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.aifuyun.snow.world.dal.ibatis.BaseIbatisDAO;

public class BaseUserDAOIbatis extends BaseIbatisDAO implements BaseUserDAO {

	@Override
	public long create(BaseUserDO baseUserDO) {
		return (Long)getSqlMapClientTemplate().insert("BaseUserDAO.create", baseUserDO);
	}

	@Override
	public void delete(long id) {
		getSqlMapClientTemplate().update("BaseUserDAO.delete", id);
	}

	@Override
	public void update(BaseUserDO baseUserDO) {
		getSqlMapClientTemplate().update("BaseUserDAO.update", baseUserDO);
	}

	@Override
	public BaseUserDO queryById(long id) {
		return (BaseUserDO)getSqlMapClientTemplate().queryForObject("BaseUserDAO.queryById", id);
	}

	@Override
	public BaseUserDO queryByIdIgnoreDeletedFlag(long id) {
		return (BaseUserDO)getSqlMapClientTemplate().queryForObject("BaseUserDAO.queryByIdIgnoreDeletedFlag", id);
	}

	@SuppressWarnings("unchecked")
	public List<BaseUserDO> queryByIds(List<Long> ids) {
		return (List<BaseUserDO>)getSqlMapClientTemplate().queryForList("BaseUserDAO.queryByIds", ids);
	}
	
	@Override
	public BaseUserDO queryByUsername(String username) {
		return (BaseUserDO)getSqlMapClientTemplate().queryForObject("BaseUserDAO.queryByUsername", username);
	}

	@Override
	public BaseUserDO queryByUsernameIgnoreDeletedFlag(String username) {
		return (BaseUserDO)getSqlMapClientTemplate().queryForObject("BaseUserDAO.queryByUsernameIgnoreDeletedFlag", username);
	}

	@Override
	public BaseUserDO queryByEmail(String email) {
		return (BaseUserDO)getSqlMapClientTemplate().queryForObject("BaseUserDAO.queryByEmail", email);
	}

	@Override
	public BaseUserDO queryByEmailIgnoreDeletedFlag(String email) {
		return (BaseUserDO)getSqlMapClientTemplate().queryForObject("BaseUserDAO.queryByEmailIgnoreDeletedFlag", email);
	}

}
