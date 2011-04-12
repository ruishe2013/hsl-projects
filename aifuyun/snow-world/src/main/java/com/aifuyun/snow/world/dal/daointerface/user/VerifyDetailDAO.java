package com.aifuyun.snow.world.dal.daointerface.user;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.user.VerifyDetailDO;

public interface VerifyDetailDAO {
	
	long create(VerifyDetailDO verifyDetailDO);
	
	VerifyDetailDO queryById(long id);
	
	List<VerifyDetailDO> queryByUserId(long userId);
	
	void update(VerifyDetailDO verifyDetailDO);
	
	void delete(long id);
	
	void deleteByUserId(long userId);
	
	VerifyDetailDO queryByUserIdAndType(long userId, int type);
}
