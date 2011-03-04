package com.aifuyun.snow.world.dal.daointerface.user;

import com.aifuyun.snow.world.dal.dataobject.user.ExtUserDO;

public interface ExtUserDAO {
	
	void create(ExtUserDO extUserDO);
	
	ExtUserDO queryByUserId(long userId);
	
	void delete(long userId);
	
	void update(ExtUserDO extUserDO);

}
