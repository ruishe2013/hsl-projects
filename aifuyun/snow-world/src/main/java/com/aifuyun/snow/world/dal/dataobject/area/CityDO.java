package com.aifuyun.snow.world.dal.dataobject.area;


/**
 * ����
 * @author pister
 * 2011-1-23
 */
public class CityDO extends BaseAreaDO   {
	
	private static final long serialVersionUID = 9175427980005938322L;
	
	/**
	 * ����ʡ��
	 */
	private int provinceId;
	
	/**
	 * �Ƿ����ų���
	 */
	private boolean hotCity = false;

	public boolean isHotCity() {
		return hotCity;
	}

	public void setHotCity(boolean hotCity) {
		this.hotCity = hotCity;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
}
