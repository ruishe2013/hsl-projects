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
	
	/**
	 * 浏览修改个人信息
	 * @return
	 */
	Result viewModifyPersonalInfo();
	
	/**
	 * 修改个人信息
	 * @param inputBaseUser
	 * @return
	 */
	Result modifyPersonalInfo(BaseUserDO inputBaseUser);
	
	/**
	 * 查看公司邮件认证页
	 * @return
	 */
	Result viewCorpVerifyMailPage();
	
	/**
	 * 发送公司认证邮件
	 * @param corpEmail
	 * @return
	 */
	Result sendCorpVerifyMail(String corpEmail);
	
	/**
	 * 公司邮件认证
	 * @param onwerCorpMailParam
	 * @return
	 */
	Result handleCorpVerifyMail(OnwerCorpMailParam onwerCorpMailParam);
	
}
