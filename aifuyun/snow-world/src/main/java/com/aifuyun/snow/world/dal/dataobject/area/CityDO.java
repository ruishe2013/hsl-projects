package com.aifuyun.snow.world.dal.dataobject.area;


/**
 * 城市
 * @author pister
 * 2011-1-23
 */
public class CityDO extends BaseAreaDO   {
	
	private static final long serialVersionUID = 9175427980005938322L;
	
	/**
	 * 所在省份
	 */
	private int provinceId;

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
}
