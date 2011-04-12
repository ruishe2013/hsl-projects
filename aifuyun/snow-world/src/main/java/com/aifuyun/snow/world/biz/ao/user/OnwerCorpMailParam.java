package com.aifuyun.snow.world.biz.ao.user;

import java.io.Serializable;

public class OnwerCorpMailParam implements Serializable {

	private static final long serialVersionUID = 3197591204306791539L;
	
	private String email;
	
	private long userId;
	
	private String token;
	
	private long timestamp;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}
