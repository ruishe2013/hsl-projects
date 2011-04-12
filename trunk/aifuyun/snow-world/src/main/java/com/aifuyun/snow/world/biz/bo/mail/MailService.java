package com.aifuyun.snow.world.biz.bo.mail;

public interface MailService {
	
	void sendMail(String toMail, String subject, String content);

}
