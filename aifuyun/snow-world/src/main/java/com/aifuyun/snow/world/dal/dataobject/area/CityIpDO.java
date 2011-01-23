package com.aifuyun.snow.world.dal.dataobject.area;

import com.aifuyun.snow.world.dal.dataobject.BaseDO;

/**
 * @author pister
 * 2011-1-23
 */
public class CityIpDO extends BaseDO {
	
	private static final long serialVersionUID = 4479741739859213551L;

	private long id;
	
	/**
	 * 城市id
	 */
	private int cityId;

	/**
	 * 城市名
	 */
	private String cityName;
	
	/**
	 * 起始ip
	 */
	private long ipStart;
	
	/**
	 * 结束ip
	 */
	private long ipEnd;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public long getIpStart() {
		return ipStart;
	}

	public void setIpStart(long ipStart) {
		this.ipStart = ipStart;
	}

	public long getIpEnd() {
		return ipEnd;
	}

	public void setIpEnd(long ipEnd) {
		this.ipEnd = ipEnd;
	}
	
}
