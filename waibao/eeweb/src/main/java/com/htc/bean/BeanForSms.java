package com.htc.bean;

/**
 * @ BeanForSms.java
 * ���� : ���ͱ���������,��ѯ�ֻ���Ϣ�����б�,������,������
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-3-3     YANGZHONLI       create
 */
public class BeanForSms {

	/**
	 * �ֻ���Ϣ�����б�
	 */
	private String phonelist; 	// �ֻ���Ϣ�����б�
	/**
	 * ������
	 */
	private String eqName; 		// ������
	/**
	 * ������
	 */
	private String placeName; 	// ������
	/**
	 * �Ƿ���ֵ�����־
	 */
	private boolean fillValue = false; 	// �Ƿ���ֵ�����־
	
	/**
	 * @describe:	�ֻ���Ϣ�����б�
	 * @date:2010-3-3
	 */
	public String getPhonelist() {
		return phonelist;
	}
	public void setPhonelist(String phonelist) {
		this.phonelist = phonelist;
	}
	/**
	 * @describe:	������
	 * @date:2010-3-3
	 */
	public String getEqName() {
		return eqName;
	}
	public void setEqName(String eqName) {
		this.eqName = eqName;
	}
	/**
	 * @describe:	������
	 * @date:2010-3-3
	 */
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	/**
	 * @describe:	�Ƿ���ֵ�����־
	 * @date:2010-3-3
	 */
	public boolean isFillValue() {
		return fillValue;
	}
	public void setFillValue(boolean fillValue) {
		this.fillValue = fillValue;
	}
}
