package com.aifuyun.snow.world.biz.query;

import com.zjuh.sweet.query.Query;

public class UserOrderQuery extends Query {

	private static final long serialVersionUID = 2426860551946516402L;
	
	private int role;
	
	private long userId;

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}
