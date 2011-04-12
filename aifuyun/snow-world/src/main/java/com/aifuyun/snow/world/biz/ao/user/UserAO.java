package com.aifuyun.snow.world.biz.ao.user;

import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.result.Result;

/**
 * @author pister
 * 2011-1-22
 */
public interface UserAO {

	/**
	 * �û�ע�ᣬ����ļ����ֶ�
	 * username
	 * password(δ����)
	 * @param userDO
	 * @return
	 */
	Result registerUser(BaseUserDO userDO);
	
	/**
	 * ����޸ĸ�����Ϣ
	 * @return
	 */
	Result viewModifyPersonalInfo();
	
	/**
	 * �޸ĸ�����Ϣ
	 * @param inputBaseUser
	 * @return
	 */
	Result modifyPersonalInfo(BaseUserDO inputBaseUser);
	
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
	
}
