package com.aifuyun.snow.world.dal.ibatis.user;

import java.util.List;
import java.util.Map;

import com.aifuyun.snow.world.dal.daointerface.user.VerifyDetailDAO;
import com.aifuyun.snow.world.dal.dataobject.user.VerifyDetailDO;
import com.aifuyun.snow.world.dal.ibatis.BaseIbatisDAO;
import com.zjuh.sweet.lang.CollectionUtil;

public class VerifyDetailDAOIbatis extends BaseIbatisDAO implements	VerifyDetailDAO {

	@Override
	public long create(VerifyDetailDO verifyDetailDO) {
		return (Long)this.getSqlMapClientTemplate().insert("VerifyDetailDAO.create", verifyDetailDO);
	}

	@Override
	public VerifyDetailDO queryById(long id) {
		return (VerifyDetailDO)this.getSqlMapClientTemplate().queryForObject("VerifyDetailDAO.queryById", id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VerifyDetailDO> queryByUserId(long userId) {
		return (List<VerifyDetailDO>)this.getSqlMapClientTemplate().queryForList("VerifyDetailDAO.queryByUserId", userId);
	}
	
	public VerifyDetailDO queryByUserIdAndType(long userId, int type) {
		Map<String, Object> params = CollectionUtil.newHashMap();
		params.put("userId", userId);
		params.put("type", type);
		return (VerifyDetailDO)this.getSqlMapClientTemplate().queryForObject("VerifyDetailDAO.queryByUserIdAndType", params);
	}
	
	@Override
	public void update(VerifyDetailDO verifyDetailDO) {
		this.getSqlMapClientTemplate().update("VerifyDetailDAO.update", verifyDetailDO);
	}

	@Override
	public void delete(long id) {
		this.getSqlMapClientTemplate().update("VerifyDetailDAO.delete", id);
	}

	@Override
	public void deleteByUserId(long userId) {
		this.getSqlMapClientTemplate().update("VerifyDetailDAO.deleteByUserId", userId);
	}

}
