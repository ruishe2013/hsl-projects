package com.aifuyun.snow.world.biz.bo.user.impl;

import java.util.List;

import com.aifuyun.snow.world.biz.bo.user.VerifyDetailBO;
import com.aifuyun.snow.world.dal.daointerface.user.VerifyDetailDAO;
import com.aifuyun.snow.world.dal.dataobject.user.VerifyDetailDO;

public class VerifyDetailBOImpl implements VerifyDetailBO {

	private VerifyDetailDAO verifyDetailDAO;

	public long create(VerifyDetailDO verifyDetailDO) {
		return verifyDetailDAO.create(verifyDetailDO);
	}

	public VerifyDetailDO queryById(long id) {
		return verifyDetailDAO.queryById(id);
	}

	public List<VerifyDetailDO> queryByUserId(long userId) {
		return verifyDetailDAO.queryByUserId(userId);
	}

	public void update(VerifyDetailDO verifyDetailDO) {
		verifyDetailDAO.update(verifyDetailDO);
	}

	public void delete(long id) {
		verifyDetailDAO.delete(id);
	}

	public void deleteByUserId(long userId) {
		verifyDetailDAO.deleteByUserId(userId);
	}

	public VerifyDetailDO queryByUserIdAndType(long userId, int type) {
		return verifyDetailDAO.queryByUserIdAndType(userId, type);
	}

	public void setVerifyDetailDAO(VerifyDetailDAO verifyDetailDAO) {
		this.verifyDetailDAO = verifyDetailDAO;
	}
	
}
