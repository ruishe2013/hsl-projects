package com.aifuyun.snow.world.dal.dataobject.together;

import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserRoleEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.misc.AbstractUserInfoHolder;

/**
 * 
 * ƴ�� �û� ����
 * @author ally
 *
 */
public class OrderUserDO extends AbstractUserInfoHolder {

	private static final long serialVersionUID = -7849160311599213849L;
	
	private long id;
	
	/**
	 * �û�id
	 */
	private long userId;
	
	/**
	 * ״̬
	 */
	private int status;
	
	/**
	 * ��ɫ
	 */
	private int role;
	
	/**
	 * �û���
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
