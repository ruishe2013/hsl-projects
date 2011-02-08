package com.aifuyun.snow.world.dal.dataobject.together;

import com.aifuyun.snow.world.dal.dataobject.BaseDO;
import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserRoleEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.SexEnum;

/**
 * 
 * 拼单 用户 关联
 * @author ally
 *
 */
public class OrderUserDO extends BaseDO {

	private static final long serialVersionUID = -7849160311599213849L;
	
	private long id;
	
	/**
	 * 用户id
	 */
	private long userId;
	
	/**
	 * 拼单id
	 */
	private long orderId;
	
	/**
	 * 拼单类型
	 */
	private int orderType;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 角色
	 */
	private int role;
	
	/**
	 * 真实姓名
	 */
	private String realName;
	
	/**
	 * 性别
	 */
	private int sex;
	
	/**
	 * 出生年代
	 */
	private int birthYear;
	
	/**
	 * 手机号码
	 */
	private String phone;
	
	/**
	 * 职业
	 */
	private String career;
	
	/**
	 * qq号
	 */
	private String qq;
	
	/**
	 * 电子邮件
	 */
	private String email;
	
	/**
	 * 状态
	 */
	private int status;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public OrderUserRoleEnum getRoleEnum() {
		return OrderUserRoleEnum.valueOf(role);
	}
	
	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
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


}
