package com.aifuyun.snow.world.biz.ao.user;

import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.result.Result;

/**
 * @author pister
 * 2011-1-22
 */
public interface UserAO {

	/**
	 * 用户注册，必填的几个字段
	 * username
	 * password(未加密)
	 * @param userDO
	 * @return
	 */
	Result registerUser(BaseUserDO userDO);
	
}
