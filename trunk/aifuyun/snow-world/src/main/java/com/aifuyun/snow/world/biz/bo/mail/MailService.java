package com.aifuyun.snow.world.biz.bo.mail;

import java.util.List;

public interface MailService {
	
	void sendMail(String toMail, String subject, String content);
	
	void sendMail(List<String> toMails, String subject, String content);

}
