package com.aifuyun.snow.world.biz.ao.user.impl;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.user.OnwerCorpMailParam;
import com.aifuyun.snow.world.biz.ao.user.ProfileAO;
import com.aifuyun.snow.world.biz.bo.corp.CorpMailBO;
import com.aifuyun.snow.world.biz.bo.mail.MailService;
import com.aifuyun.snow.world.biz.bo.misc.SecretValueBO;
import com.aifuyun.snow.world.biz.bo.user.VerifyDetailBO;
import com.aifuyun.snow.world.biz.resultcodes.CommonResultCodes;
import com.aifuyun.snow.world.biz.resultcodes.UserResultCodes;
import com.aifuyun.snow.world.common.ClassInputStreamUtil;
import com.aifuyun.snow.world.common.SnowUtil;
import com.aifuyun.snow.world.dal.dataobject.corp.CorpMailDO;
import com.aifuyun.snow.world.dal.dataobject.enums.VerifyTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.aifuyun.snow.world.dal.dataobject.user.VerifyDetailDO;
import com.zjuh.splist.core.SplistContext;
import com.zjuh.splist.core.module.URLModule;
import com.zjuh.splist.core.module.URLModuleContainer;
import com.zjuh.sweet.codec.MD5;
import com.zjuh.sweet.lang.CollectionUtil;
import com.zjuh.sweet.lang.StringUtil;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultSupport;
import com.zjuh.sweet.result.ResultTypeEnum;

public class ProfileAOImpl extends BaseAO implements ProfileAO {

	private static final long VERIFY_EXPIRE_TIME = 2 * 24 * 3600 * 1000; // 默认为2天

	private ExecutorService executorService = Executors.newFixedThreadPool(5);

	private VerifyDetailBO verifyDetailBO;
	
	private MailService mailService;
	
	private String verifyMailTitle;
	
	private String verifyMailContentTemplate;
	
	private SecretValueBO secretValueBO;
	
	private CorpMailBO corpMailBO;
	
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
	public Result sendCorpVerifyMail(final String corpEmail) {
		Result result = new ResultSupport(false);
		try {
			final long userId = this.getLoginUserId();
			if (userId <= 0L) {
				result.setResultCode(CommonResultCodes.USER_NOT_LOGIN);
				return result;
			}
			final String username = this.getLoginUsername();
			long timestamp = System.currentTimeMillis();
			final String content = makeCorpVerifyMailContent(userId, username, corpEmail, timestamp);
			
			// TODO 最多一天只能认证3次
			
			String mailHost = SnowUtil.getMailHost(corpEmail);
			if (StringUtil.isEmpty(mailHost)) {
				result.setResultCode(UserResultCodes.INVALID_EMAIL);
				return result;
			}
			CorpMailDO corpMailDO = this.corpMailBO.queryByMailHost(mailHost);
			if (corpMailDO == null) {
				result.setResultCode(UserResultCodes.UNKNOWN_COPR_EMAIL);
				result.setResultTypeEnum(ResultTypeEnum.CURRENT_TARGET);
				return result;
			}
			corpMailBO.queryByMailHost(mailHost);
			
			executorService.submit(new Runnable() {
				
				@Override
				public void run() {
					mailService.sendMail(corpEmail, verifyMailTitle, content);
					log.warn("发送认证邮件成功: userId:" + userId + ", username:" + username + ",email:" + corpEmail);
				}
			});
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("发送认证邮件失败", e);
		}
		return result;
	}

	
	private String makeCorpVerifyMailContent(long userId, String username, String email, long timestamp) {
		String token = makeVerifyMailToken(userId, email, timestamp);
		String clickUrl = makeVerifyMailClickUrl(userId, email, timestamp, token);
		Map<String, Object> context = CollectionUtil.newHashMap();
		context.put("clickUrl", clickUrl);
		context.put("username", username);
		return renderTemplate(context, verifyMailContentTemplate);
		
	}
	
	private String renderTemplate(Map<String, Object> context, String templatePath) {
		try {
			Properties prop = new Properties();
			prop.setProperty(Velocity.ENCODING_DEFAULT, "gbk");
			prop.setProperty(Velocity.INPUT_ENCODING, "gbk");
			prop.setProperty(Velocity.OUTPUT_ENCODING, "gbk");
			Velocity.init(prop);
			
			VelocityContext velocityContext = new VelocityContext(context);
			StringWriter writer = new StringWriter();
			Reader reader = new InputStreamReader(ClassInputStreamUtil.getInputStream(templatePath), "gbk");
			Velocity.evaluate(velocityContext, writer, "inner_render.log", reader);
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private String makeVerifyMailClickUrl(long userId, String email, long timestamp, String token) {
		URLModuleContainer urlModuleContainer = SplistContext.getSplistComponent().getUrlModuleContainers().get("snowModule");
		URLModule um = urlModuleContainer.setTarget("profile/verifyCorpMail");
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
		Result result = new ResultSupport(false);
		try {
			boolean userLogined = this.isUserLogin();
			
			long ts = onwerCorpMailParam.getTimestamp();
			long nowTs = System.currentTimeMillis();
			long deltaTs = nowTs - ts;
			if (deltaTs > VERIFY_EXPIRE_TIME) {
				result.setResultCode(UserResultCodes.VERIFY_EXPIRE);
				return result;
			}
			
			final long userId = onwerCorpMailParam.getUserId();
			
			String token2 = makeVerifyMailToken(userId, onwerCorpMailParam.getEmail(), onwerCorpMailParam.getTimestamp());
			if (!StringUtil.equals(onwerCorpMailParam.getToken(), token2)) {
				result.setResultCode(UserResultCodes.VERIFY_INVALID_TOKEN);
				return result;
			}
			
			BaseUserDO user = userBO.queryById(userId);
			if (user == null) {
				result.setResultCode(UserResultCodes.USER_NOT_EXIST);
				return result;
			}
			
			// 认证成功
			user.setVerifyStatus(user.getVerifyStatus() | VerifyTypeEnum.OWNER_CORP_EMAIL.getValue());
			userBO.update(user);
			
			saveVerifyDetail(onwerCorpMailParam);
			
			if (log.isWarnEnabled()) {
				log.warn("认证成功:userId:" + userId);
			}
			
			result.getModels().put("userLogined", userLogined);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("邮件认证失败", e);
		}
		return result;
	}
	
	
	
	private String getCorpName(String email) {
		String mailHost = SnowUtil.getMailHost(email);
		if (StringUtil.isEmpty(mailHost)) {
			return null;
		}
		CorpMailDO corpMailDO = this.corpMailBO.queryByMailHost(mailHost);
		if (corpMailDO == null) {
			return "该公司已经被删除";
		}
		return corpMailDO.getCorpName();
	}
	
	private void saveVerifyDetail(OnwerCorpMailParam onwerCorpMailParam) {
		VerifyDetailDO verifyDetailDO = this.verifyDetailBO.queryByUserIdAndType(onwerCorpMailParam.getUserId(), VerifyTypeEnum.OWNER_CORP_EMAIL.getValue());
		if (verifyDetailDO == null) {
			// create
			verifyDetailDO = new VerifyDetailDO();
			verifyDetailDO.setApproach(onwerCorpMailParam.getEmail());
			verifyDetailDO.setDetail(getCorpName(onwerCorpMailParam.getEmail()));
			verifyDetailDO.setType(VerifyTypeEnum.OWNER_CORP_EMAIL.getValue());
			verifyDetailDO.setUserId(onwerCorpMailParam.getUserId());
			
			verifyDetailBO.create(verifyDetailDO);
			
		} else {
			verifyDetailDO.setApproach(onwerCorpMailParam.getEmail());
			verifyDetailDO.setDetail(getCorpName(onwerCorpMailParam.getEmail()));
			
			verifyDetailBO.update(verifyDetailDO);
		}
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

	public void setVerifyMailContentTemplate(String verifyMailContentTemplate) {
		this.verifyMailContentTemplate = verifyMailContentTemplate;
	}

	public void setSecretValueBO(SecretValueBO secretValueBO) {
		this.secretValueBO = secretValueBO;
	}

	public void setCorpMailBO(CorpMailBO corpMailBO) {
		this.corpMailBO = corpMailBO;
	}

}
