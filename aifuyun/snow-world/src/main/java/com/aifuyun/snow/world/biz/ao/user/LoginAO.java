package com.aifuyun.snow.world.biz.ao.user;

import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.result.Result;

public interface LoginAO {

	/**
	 * µÇÂ¼
	 * @param userQuery
	 * @return
	 */
	Result handleLogin(BaseUserDO baseUserDO);
	
	/**
	 * ÍË³öµÇÂ½
	 * @return
	 */
	Result handleLogout();
	
}
