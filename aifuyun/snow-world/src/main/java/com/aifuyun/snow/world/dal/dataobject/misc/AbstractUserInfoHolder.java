package com.aifuyun.snow.world.dal.dataobject.misc;

import com.aifuyun.snow.world.dal.dataobject.BaseDO;
import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.SexEnum;

public abstract class AbstractUserInfoHolder extends BaseDO implements UserInfoHolder {

	private static final long serialVersionUID = -8099732072900184562L;

	/**
	 * ƴ��id
	 */
	private long orderId;
	
	/**
	 * ƴ������
	 */
	private int orderType;
	
	
	/**
	 * ��ʵ����
	 */
	private String realName;
	
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
	

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public BirthYearEnum getBirthYearEnum() {
		return BirthYearEnum.valueOf(this.birthYear);
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

}
