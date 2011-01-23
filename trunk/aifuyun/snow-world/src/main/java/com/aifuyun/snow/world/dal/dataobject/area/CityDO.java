package com.aifuyun.snow.world.dal.dataobject.area;

import com.aifuyun.snow.world.dal.dataobject.BaseDO;

/**
 * 城市
 * @author pister
 * 2011-1-23
 */
public class CityDO extends BaseDO {
	
	private static final long serialVersionUID = 9175427980005938322L;

	private int id;
	
	/**
	 * 所在省份
	 */
	private int provinceId;

	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 拼音
	 */
	private String pinyin;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
}
