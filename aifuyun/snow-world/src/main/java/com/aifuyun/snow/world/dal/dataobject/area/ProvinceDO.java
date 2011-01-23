package com.aifuyun.snow.world.dal.dataobject.area;

import com.aifuyun.snow.world.dal.dataobject.BaseDO;

/**
 * Ê¡·Ý
 * @author pister
 * 2011-1-23
 */
public class ProvinceDO extends BaseDO {

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
