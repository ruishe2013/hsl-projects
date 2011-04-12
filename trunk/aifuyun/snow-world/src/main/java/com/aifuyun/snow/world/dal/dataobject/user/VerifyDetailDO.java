package com.aifuyun.snow.world.dal.dataobject.user;

import com.aifuyun.snow.world.dal.dataobject.BaseDO;
import com.aifuyun.snow.world.dal.dataobject.enums.VerifyTypeEnum;

/**
 * 
 * ��֤����
 * @author pister
 *
 */
public class VerifyDetailDO extends BaseDO {

	private static final long serialVersionUID = 2921301597337019293L;
	
	private long id;
	
	/**
	 * �û�id
	 */
	private long userId;
	
	/**
	 * ����
	 * @VerifyTypeEnum
	 */
	private int type;
	
	/**
	 * ��ϸ��Ϣ
	 */
	private String detail;
	
	/**
	 * ��֤;��
	 * ��������֤��֤������ֶ������֤����
	 * ������ʼ���֤������ֶ����ʼ���ַ
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
