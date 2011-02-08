package com.aifuyun.snow.world.dal.dataobject.together;

import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserRoleEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.misc.AbstractUserInfoHolder;

/**
 * 
 * 拼单 用户 关联
 * @author ally
 *
 */
public class OrderUserDO extends AbstractUserInfoHolder {

	private static final long serialVersionUID = -7849160311599213849L;
	
	private long id;
	
	/**
	 * 用户id
	 */
	private long userId;
	
	/**
	 * 状态
	 */
	private int status;
	
	/**
	 * 角色
	 */
	private int role;
	
	/**
	 * 用户名
	 */
	private String username;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getStatus() {
		return status;
	}
	
	public OrderUserStatusEnum getStatusEnum() {
		return OrderUserStatusEnum.valueOf(status);
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getRole() {
		return role;
	}
	
	public OrderUserRoleEnum getRoleEnum() {
		return OrderUserRoleEnum.valueOf(role);
	}

	public void setRole(int role) {
		this.role = role;
	}
	
	

}
