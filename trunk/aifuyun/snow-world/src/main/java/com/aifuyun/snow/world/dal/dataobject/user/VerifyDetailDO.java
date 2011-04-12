package com.aifuyun.snow.world.dal.dataobject.user;

import com.aifuyun.snow.world.dal.dataobject.BaseDO;
import com.aifuyun.snow.world.dal.dataobject.enums.VerifyTypeEnum;

/**
 * 
 * 认证详情
 * @author pister
 *
 */
public class VerifyDetailDO extends BaseDO {

	private static final long serialVersionUID = 2921301597337019293L;
	
	private long id;
	
	/**
	 * 用户id
	 */
	private long userId;
	
	/**
	 * 类型
	 * @VerifyTypeEnum
	 */
	private int type;
	
	/**
	 * 详细信息
	 */
	private String detail;
	
	/**
	 * 认证途径
	 * 如果是身份证认证，这个字段是身份证号码
	 * 如果是邮件认证，这个字段是邮件地址
	 */
	private String approach;

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
	
	public VerifyTypeEnum getVerifyTypeEnum() {
		return VerifyTypeEnum.valueOf(type);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getApproach() {
		return approach;
	}

	public void setApproach(String approach) {
		this.approach = approach;
	}

}
