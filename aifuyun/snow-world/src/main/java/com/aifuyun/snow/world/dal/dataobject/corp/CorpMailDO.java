package com.aifuyun.snow.world.dal.dataobject.corp;

import com.aifuyun.snow.world.dal.dataobject.BaseDO;

public class CorpMailDO extends BaseDO {

	private static final long serialVersionUID = 8172340118143366147L;
	
	private int id;
	
	/**
	 * 邮件服务器（邮箱后缀）
	 */
	private String mailHost;
	
	/**
	 * 公司名
	 */
	private String corpName;
	
	/**
	 * 公司联系方式
	 */
	private String contact;

	/**
	 * 备注
	 */
	private String comments;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
