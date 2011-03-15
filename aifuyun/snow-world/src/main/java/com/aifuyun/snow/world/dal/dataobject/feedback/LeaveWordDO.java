package com.aifuyun.snow.world.dal.dataobject.feedback;

import com.aifuyun.snow.world.dal.dataobject.BaseDO;

public class LeaveWordDO extends BaseDO {

	private static final long serialVersionUID = -4936026142571324125L;

	private int id;
	
	private String nick;
	
	private String contact;
	
	private String fromIp;
	
	private String content;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getFromIp() {
		return fromIp;
	}

	public void setFromIp(String fromIp) {
		this.fromIp = fromIp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
