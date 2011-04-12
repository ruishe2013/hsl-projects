package com.aifuyun.snow.world.biz.ao.user.impl;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.user.OnwerCorpMailParam;
import com.aifuyun.snow.world.biz.ao.user.UserAO;
import com.aifuyun.snow.world.biz.bo.mail.MailService;
import com.aifuyun.snow.world.biz.bo.misc.SecretValueBO;
import com.aifuyun.snow.world.biz.bo.user.VerifyDetailBO;
import com.aifuyun.snow.world.biz.resultcodes.CommonResultCodes;
import com.aifuyun.snow.world.biz.resultcodes.UserResultCodes;
import com.aifuyun.snow.world.common.SnowUtil;
import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.VerifyTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.aifuyun.snow.world.dal.dataobject.user.VerifyDetailDO;
import com.zjuh.splist.core.SplistContext;
import com.zjuh.splist.core.module.URLModule;
import com.zjuh.splist.core.module.URLModuleContainer;
import com.zjuh.sweet.codec.MD5;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultSupport;
import com.zjuh.sweet.result.ResultTypeEnum;

public class UserAOImpl extends BaseAO implements UserAO {

	private VerifyDetailBO verifyDetailBO;
	
	private MailService mailService;
	
	private String verifyMailTitle;
	
	private String verifyMailContent;
	
	private SecretValueBO secretValueBO;
	
	@Override
	public Result viewCorpVerifyMailPage() {
		Result result = new ResultSupport(false);
		try {
			long userId = this.getLoginUserId();
			if (userId <= 0L) {
				result.setResultCode(CommonResultCodes.USER_NOT_LOGIN);
				return result;
			}
			
			VerifyDetailDO oldVerifyDetail = verifyDetailBO.queryByUserIdAndType(userId, VerifyTypeEnum.OWNER_CORP_EMAIL.getValue());
			if (oldVerifyDetail != null) {
				// 已经通过认证
			}
			
			result.getModels().put("oldVerifyDetail", oldVerifyDetail);			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("浏览认证公司邮件页面失败", e);
		}
		return result;
	}

	@Override
	public Result sendCorpVerifyMail(String corpEmail) {
		Result result = new ResultSupport(false);
		try {
			long userId = this.getLoginUserId();
			if (userId <= 0L) {
				result.setResultCode(CommonResultCodes.USER_NOT_LOGIN);
				return result;
			}
			long timestamp = System.currentTimeMillis();
			String content = makeCorpVerifyMailContent(userId, corpEmail, timestamp);
			mailService.sendMail(corpEmail, verifyMailTitle, content);
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("发送认证邮件失败", e);
		}
		return result;
	}

	private String makeCorpVerifyMailContent(long userId, String email, long timestamp) {
		String token = makeVerifyMailToken(userId, email, timestamp);
		String clickUrl = makeVerifyMailClickUrl(userId, email, timestamp, token);
		return verifyMailContent.replaceAll("$clickUrl", clickUrl);
	}
	
	private String makeVerifyMailClickUrl(long userId, String email, long timestamp, String token) {
		URLModuleContainer urlModuleContainer = SplistContext.getSplistComponent().getUrlModuleContainers().get("snowModule");
		URLModule um = urlModuleContainer.setTarget("user/verifyCorpMail");
		um.add("token", token).add("userId", userId).add("email", email).add("ts", timestamp);
		return um.render();
	}
	
	private String makeVerifyMailToken(long userId, String email, long timestamp) {
		StringBuilder sb = new StringBuilder();
		sb.append(userId);
		sb.append("-");
		sb.append(email);
		sb.append("-");
		sb.append(timestamp);
		sb.append("-");
		final String verifyMailSignKey = secretValueBO.getVerifyMailSignKey();
		sb.append(verifyMailSignKey);
		String orgValue = sb.toString();
		return MD5.encrypt(orgValue);
	}
	
	
	@Override
	public Result handleCorpVerifyMail(OnwerCorpMailParam onwerCorpMailParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result modifyPersonalInfo(BaseUserDO inputBaseUser) {
		Result result = new ResultSupport(false);
		try {
			long userId = this.getLoginUserId();
			if (userId <= 0L) {
				result.setResultCode(CommonResultCodes.USER_NOT_LOGIN);
				return result;
			}
			
			BaseUserDO baseUserDO = userBO.queryById(userId);
			if (baseUserDO == null) {
				result.setResultCode(UserResultCodes.USER_NOT_EXIST);
				return result;
			}
			
			if (inputBaseUser.getId() != baseUserDO.getId()) {
				result.setResultCode(UserResultCodes.CAN_NOT_MODIFY_OTHERS_INFO);
				return result;
			}
			
			assignProperties(baseUserDO, inputBaseUser);
			
			userBO.update(baseUserDO);
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("修改个人信息失败", e);
		}
		return result;
	}
	
	private void assignProperties(BaseUserDO destBaseUserDO, BaseUserDO srcBaseUserDO) {
		destBaseUserDO.setBirthYear(srcBaseUserDO.getBirthYear());
		destBaseUserDO.setCareer(srcBaseUserDO.getCareer());
		destBaseUserDO.setEmail(srcBaseUserDO.getEmail());
		destBaseUserDO.setPhone(srcBaseUserDO.getPhone());
		destBaseUserDO.setQq(srcBaseUserDO.getQq());
		destBaseUserDO.setRealName(srcBaseUserDO.getRealName());
		destBaseUserDO.setSex(srcBaseUserDO.getSex());
	}

	@Override
	public Result viewModifyPersonalInfo() {
		Result result = new ResultSupport(false);
		try {
			long userId = this.getLoginUserId();
			if (userId <= 0L) {
				result.setResultCode(CommonResultCodes.USER_NOT_LOGIN);
				return result;
			}
			
			BaseUserDO baseUserDO = userBO.queryById(userId);
			if (baseUserDO == null) {
				result.setResultCode(UserResultCodes.USER_NOT_EXIST);
				return result;
			}
			
			BirthYearEnum[] years = BirthYearEnum.values();
			
			int selectedYear = SnowUtil.getSelectedYear(baseUserDO);
			
			result.getModels().put("years", years);
			result.getModels().put("selectedYear", selectedYear);
			result.getModels().put("user", baseUserDO);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("确认个人信息失败", e);
		}
		return result;
	}

	@Override
	public Result registerUser(BaseUserDO userDO) {
		Result result = new ResultSupport(false);
		try {
			if (userBO.isUserInSensitivityList(userDO.getUsername())) {
				result.setResultCode(UserResultCodes.SENSITIVITY_USER);
				result.setResultTypeEnum(ResultTypeEnum.CURRENT_TARGET);
				return result;
			}
			
			if (isUserExist(userDO.getUsername())) {
				result.setResultCode(UserResultCodes.USERNAME_HAS_EXIST);
				result.setResultTypeEnum(ResultTypeEnum.CURRENT_TARGET);
				return result;
			}
			
			if (isUserExistByEmail(userDO.getEmail())) {
				result.setResultCode(UserResultCodes.EMAIL_HAS_EXIST);
				result.setResultTypeEnum(ResultTypeEnum.CURRENT_TARGET);
				return result;
			}
			
			String encryptedPassword = userBO.encryptPassword(userDO.getPassword());
			userDO.setPassword(encryptedPassword);
			
			long id = userBO.create(userDO);
			
			result.setDefaultModel(id);
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("注册用户失败", e);
		}
		return result;
	}
	
	private boolean isUserExist(String username) {
		return null != userBO.queryByUsernameIgnoreDeletedFlag(username);
	}
	
	private boolean isUserExistByEmail(String email) {
		return null != userBO.queryByEmailIgnoreDeletedFlag(email);
	}

	public void setVerifyDetailBO(VerifyDetailBO verifyDetailBO) {
		this.verifyDetailBO = verifyDetailBO;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public void setVerifyMailTitle(String verifyMailTitle) {
		this.verifyMailTitle = verifyMailTitle;
	}

	public void setVerifyMailContent(String verifyMailContent) {
		this.verifyMailContent = verifyMailContent;
	}

	public void setSecretValueBO(SecretValueBO secretValueBO) {
		this.secretValueBO = secretValueBO;
	}


}
