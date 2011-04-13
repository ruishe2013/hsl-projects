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
		String content = "<div>����<a href=\"$clickUrl\" target=\"_blank\">$clickUrl</a>���������֤��������ܵ�����븴�ƴ˵���������ʣ�лл ��</div>" +
				"<div style=\"text-align:right;\">һ����ƴ����</div>";
		content = content.replaceAll("\\$clickUrl", "http://yiliangche.com/search/search-order.htm?fromAddr=%BA%BC%D6%DD&arriveAddr=&fromTime=");
		mailService.sendMail(email, "һ����ƴ���������ʼ������֤", content);
		System.out.println(email);
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

}
