package com.aifuyun.snow.world.biz.bo.mail.impl;

import com.aifuyun.snow.world.biz.bo.mail.MailService;
import com.zjuh.sweet.test.BaseTest;

public class MailServiceTest extends BaseTest {
	
	private MailService mailService;
	
	public void testSend() {
		send("81480773@qq.com");
		send("54025853@qq.com");
		send("253041869@qq.com");
		send("279074386@qq.com");
		send("57090023@qq.com");
	}
	
	private void send(String email) {
		String content = "<div>请点击<a href=\"$clickUrl\" target=\"_blank\">$clickUrl</a>继续完成认证，如果不能点击，请复制此到浏览器访问，谢谢 。</div>" +
				"<div style=\"text-align:right;\">一辆车拼车网</div>";
		content = content.replaceAll("\\$clickUrl", "http://yiliangche.com/search/search-order.htm?fromAddr=%BA%BC%D6%DD&arriveAddr=&fromTime=");
		mailService.sendMail(email, "一辆车拼车网——邮件身份认证", content);
		System.out.println(email);
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

}
