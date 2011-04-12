package com.aifuyun.snow.world.biz.bo.misc.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.snow.world.biz.bo.misc.SecretValueBO;

public class SecretValueBOImpl implements SecretValueBO {

	private static final Logger log = LoggerFactory.getLogger(SecretValueBOImpl.class);
	
	private String secretFilePath;
	
	private Properties properties;
	
	private String defaultValue = "nullValue";
	
	private static final String VERIFY_MAIL_SIGN_KEY = "verify.mail.sign.key";
	private static final String SERVICE_MAIL_USERNAME = "service.mail.username";
	private static final String SERVICE_MAIL_PASSWORD = "service.mail.password";
	
	public void init() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(secretFilePath));
		} catch (IOException e) {
			log.warn("secret文件不存在：" + secretFilePath + "，将使用默认的值代替。");
		}
		properties = prop;
	}
	
	protected String getValue(String key) {
		if (properties == null) {
			return defaultValue;
		}
		return properties.getProperty(key, defaultValue);
	}
	
	@Override
	public String getVerifyMailSignKey() {
		return getValue(VERIFY_MAIL_SIGN_KEY);
	}

	@Override
	public String getServiceMailPassword() {
		return getValue(SERVICE_MAIL_PASSWORD);
	}

	@Override
	public String getServiceMailUsername() {
		return getValue(SERVICE_MAIL_USERNAME);
	}

	public void setSecretFilePath(String secretFilePath) {
		this.secretFilePath = secretFilePath;
	}

}
