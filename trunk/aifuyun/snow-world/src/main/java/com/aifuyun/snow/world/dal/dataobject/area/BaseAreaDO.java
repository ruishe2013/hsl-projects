package com.aifuyun.snow.world.dal.dataobject.area;

import com.aifuyun.snow.world.common.SnowUtils;
import com.aifuyun.snow.world.dal.dataobject.BaseDO;

/**
 * @author ck
 *
 */
public abstract class BaseAreaDO extends BaseDO implements Comparable<BaseAreaDO> {
	
	private static final long serialVersionUID = 4534009432335748160L;

	private int id;
	
	/**
	 * √˚≥∆
	 */
	private String name;
	
	/**
	 * ∆¥“Ù
	 */
	private String pinyin;
	
	/**
	 * ≈≈–Ú»®÷ÿ
	 */
	private int seqNum;

	@Override
	public int compareTo(BaseAreaDO other) {
		if (other == null) {
			return 1;
		}
		int ret = other.seqNum - seqNum;
		if (ret != 0) {
			return ret;
		}
		ret = SnowUtils.compareString(pinyin, other.pinyin);
		if (ret == 0) {
			ret = SnowUtils.compareString(name, other.name);
		}
		if (ret == 0) {
			ret = id - other.id;
		}
		return ret;
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

	public int getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
