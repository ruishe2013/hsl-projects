package com.aifuyun.snow.world.dal.dataobject.user;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.enums.UserStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.VerifyTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.misc.AbstractUserInfoHolder;

public class BaseUserDO extends AbstractUserInfoHolder {
	
	private static final long serialVersionUID = -8132857887020879215L;

	/**
	 * 用户id 主键
	 */
	private long id;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 密码， 已经加密个过
	 */
	private String password;
	
	/**
	 * 注册ip
	 */
	private String registerIp;
	
	/**
	 * 状态
	 */
	private int status = UserStatusEnum.NORMAL.getValue();
	
	/**
	 * 身份认证状态，按位表示，如果没有认证过，则为0
	 * 已通过认证的
	 * @see VerifyTypeEnum
	 */
	private int verifyStatus = 0;

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

	public String getRegisterIp() {
		return registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	public List<VerifyTypeEnum> getVerifyTypes() {
		return VerifyTypeEnum.valuesFrom(verifyStatus);
	}
	
	public int getVerifyStatus() {
		return verifyStatus;
	}

	public void setVerifyStatus(int verifyStatus) {
		this.verifyStatus = verifyStatus;
	}

}