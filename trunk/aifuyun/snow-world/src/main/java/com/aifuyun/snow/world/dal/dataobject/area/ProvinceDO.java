package com.aifuyun.snow.world.dal.dataobject.area;

import com.aifuyun.snow.world.common.SnowUtils;
import com.aifuyun.snow.world.dal.dataobject.BaseDO;

/**
 * Ê¡·Ý
 * @author pister
 * 2011-1-23
 */
public class ProvinceDO extends BaseDO implements Comparable<ProvinceDO> {

	private static final long serialVersionUID = 2536492791406861113L;

	private int id;
	
	/**
	 * Ãû³Æ
	 */
	private String name;
	
	/**
	 * Æ´Òô
	 */
	private String pinyin;
	
	@Override
	public int compareTo(ProvinceDO other) {
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
