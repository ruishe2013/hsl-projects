package com.aifuyun.snow.world.biz.ao.user;

import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.result.Result;

public interface LoginAO {

	/**
	 * ��¼
	 * @param userQuery
	 * @return
	 */
	Result handleLogin(BaseUserDO baseUserDO);
	
	/**
	 * �˳���½
	 * @return
	 */
	Result handleLogout();
	
}
