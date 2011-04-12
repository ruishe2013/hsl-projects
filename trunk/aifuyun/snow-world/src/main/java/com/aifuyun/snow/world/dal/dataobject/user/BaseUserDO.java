package com.aifuyun.snow.world.dal.dataobject.user;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.enums.UserStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.VerifyTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.misc.AbstractUserInfoHolder;

public class BaseUserDO extends AbstractUserInfoHolder {
	
	private static final long serialVersionUID = -8132857887020879215L;

	/**
	 * �û�id ����
	 */
	private long id;
	
	/**
	 * �û���
	 */
	private String username;
	
	/**
	 * ���룬 �Ѿ����ܸ���
	 */
	private String password;
	
	/**
	 * ע��ip
	 */
	private String registerIp;
	
	/**
	 * ״̬
	 */
	private int status = UserStatusEnum.NORMAL.getValue();
	
	/**
	 * �����֤״̬����λ��ʾ�����û����֤������Ϊ0
	 * ��ͨ����֤��
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