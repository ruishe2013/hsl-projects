package com.aifuyun.snow.world.dal.dataobject.user;

import com.aifuyun.snow.world.dal.dataobject.BaseDO;
import com.aifuyun.snow.world.dal.dataobject.enums.SexEnum;
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
	 * �Ա�
	 */
	private int sex;
	
	/**
	 * �������
	 */
	private int birthYear;
	
	/**
	 * �ֻ�����
	 */
	private String phone;
	
	/**
	 * ְҵ
	 */
	private String career;
	
	/**
	 * qq��
	 */
	private String qq;
	
	/**
	 * �����ʼ�
	 */
	private String email;

	/**
	 * ע��ip
	 */
	private String registerIp;
	
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

	public SexEnum getSexEnum() {
		return SexEnum.valueOf(sex);
	}
	
	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getRegisterIp() {
		return registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}
}
