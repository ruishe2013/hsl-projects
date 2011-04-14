package com.aifuyun.snow.world.biz.bo.mail.impl;

import java.util.Date;
import java.util.List;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.aifuyun.snow.world.biz.bo.mail.MailService;
import com.aifuyun.snow.world.biz.bo.misc.SecretValueBO;
import com.zjuh.sweet.lang.CollectionUtil;

public class MailServiceImpl implements MailService, InitializingBean {

	private String fromMail;
	
	private String fromMailName;
	
	private SecretValueBO secretValueBO;
	
	private JavaMailSender javaMailSender;
	
	private String host;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		String serviceMailPassword = secretValueBO.getServiceMailPassword();
		String username = secretValueBO.getServiceMailUsername();
		JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
		javaMailSenderImpl.setHost(host);
		javaMailSenderImpl.setUsername(username);
		javaMailSenderImpl.setPassword(serviceMailPassword);
		
		javaMailSender = javaMailSenderImpl;
	}
	
	@Override
	public void sendMail(String toMail, String subject, String content) {
		List<String> toMails = CollectionUtil.newArrayList();
		toMails.add(toMail);
		sendMail(toMails, subject, content);
	}
	
	public void sendMail(List<String> toMails, String subject, String content) {
		if (toMails == null || toMails.isEmpty()) {
			return;
		}
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			
			mimeMessage.setSubject(subject, "gbk");
			mimeMessage.setSender(new InternetAddress(fromMail, fromMailName));
			mimeMessage.setSentDate(new Date());
			mimeMessage.setText(content, "gbk", "html");
			for (String toMail : toMails) {
				mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(toMail));
			}
			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setFromMailName(String fromMailName) {
		this.fromMailName = fromMailName;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	public void setSecretValueBO(SecretValueBO secretValueBO) {
		this.secretValueBO = secretValueBO;
	}

}
