package com.htc.bean;

/**
 * @ BeanForSms.java
 * 作用 : 发送报警短信是,查询手机信息主键列表,仪器名,区域名
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-3-3     YANGZHONLI       create
 */
public class BeanForSms {

	/**
	 * 手机信息主键列表
	 */
	private String phonelist; 	// 手机信息主键列表
	/**
	 * 仪器名
	 */
	private String eqName; 		// 仪器名
	/**
	 * 区域名
	 */
	private String placeName; 	// 区域名
	/**
	 * 是否有值输入标志
	 */
	private boolean fillValue = false; 	// 是否有值输入标志
	
	/**
	 * @describe:	手机信息主键列表
	 * @date:2010-3-3
	 */
	public String getPhonelist() {
		return phonelist;
	}
	public void setPhonelist(String phonelist) {
		this.phonelist = phonelist;
	}
	/**
	 * @describe:	仪器名
	 * @date:2010-3-3
	 */
	public String getEqName() {
		return eqName;
	}
	public void setEqName(String eqName) {
		this.eqName = eqName;
	}
	/**
	 * @describe:	区域名
	 * @date:2010-3-3
	 */
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	/**
	 * @describe:	是否有值输入标志
	 * @date:2010-3-3
	 */
	public boolean isFillValue() {
		return fillValue;
	}
	public void setFillValue(boolean fillValue) {
		this.fillValue = fillValue;
	}
}
