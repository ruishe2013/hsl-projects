package com.aifuyun.snow.world.dal.dataobject.area;

import com.aifuyun.snow.world.common.SnowUtils;
import com.aifuyun.snow.world.dal.dataobject.BaseDO;

/**
 * 城市
 * @author pister
 * 2011-1-23
 */
public class CityDO extends BaseDO implements Comparable<CityDO>  {
	
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
	

	@Override
	public int compareTo(CityDO other) {
		int ret = SnowUtils.compareString(pinyin, other.pinyin);
		if (ret == 0) {
			ret = SnowUtils.compareString(name, other.name);
		}
		if (ret == 0) {
			ret = id - other.id;
		}
		return ret;
	}
	

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
