package com.aifuyun.snow.world.dal.dataobject.user;

import com.aifuyun.snow.world.dal.dataobject.BaseDO;
import com.aifuyun.snow.world.dal.dataobject.enums.UserStatusEnum;

public class BaseUserDO extends BaseDO {
	
	private static final long serialVersionUID = -8132857887020879215L;

	/**
	 * �û�id ����
	 */
	private long id;
	
	/**
	 * �û��� Ψһ
	 */
	private String username;
	
	/**
	 * ���룬 �Ѿ����ܸ���
	 */
	private String password;
	
	/**
	 * �����ʼ�
	 */
	private String email;

	/**
	 * ״̬
	 */
	private int status = UserStatusEnum.NORMAL.getValue();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserStatusEnum getUserStatusEnum() {
		return UserStatusEnum.valueOf(status);
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
