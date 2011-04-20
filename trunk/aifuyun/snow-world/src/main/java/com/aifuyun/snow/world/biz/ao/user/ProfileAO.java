package com.aifuyun.snow.world.biz.ao.user;

import com.zjuh.sweet.result.Result;

public interface ProfileAO {
	
	/**
	 * �鿴��˾�ʼ���֤ҳ
	 * @return
	 */
	Result viewCorpVerifyMailPage();
	
	/**
	 * ���͹�˾��֤�ʼ�
	 * @param corpEmail
	 * @return
	 */
	Result sendCorpVerifyMail(String corpEmail);
	
	/**
	 * ��˾�ʼ���֤
	 * @param onwerCorpMailParam
	 * @return
	 */
	Result handleCorpVerifyMail(OnwerCorpMailParam onwerCorpMailParam);
	
	/**
	 * �޸�����
	 * @param oldPassword
	 * @param password
	 * @return
	 */
	Result changePassword(String oldPassword, String password);

}
