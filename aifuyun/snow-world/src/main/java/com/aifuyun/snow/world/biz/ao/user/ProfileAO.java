package com.aifuyun.snow.world.biz.ao.user;

import com.zjuh.sweet.result.Result;

public interface ProfileAO {
	
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
