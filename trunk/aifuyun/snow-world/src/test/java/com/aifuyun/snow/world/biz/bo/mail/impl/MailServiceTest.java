package com.aifuyun.snow.world.biz.bo.mail.impl;

import com.aifuyun.snow.world.biz.bo.mail.MailService;
import com.zjuh.sweet.test.BaseTest;

public class MailServiceTest extends BaseTest {
	
	private MailService mailService;
	
	public void testSend() {
		mailService.sendMail("54025853@qq.com", "test2", "hello world!");
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

}
