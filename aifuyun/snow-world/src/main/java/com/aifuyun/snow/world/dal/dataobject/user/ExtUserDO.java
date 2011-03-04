package com.aifuyun.snow.world.dal.dataobject.user;

import java.util.Date;

import com.aifuyun.snow.world.dal.dataobject.BaseDO;

/**
 * @author ck
 *
 */
public class ExtUserDO extends BaseDO {

	private static final long serialVersionUID = -1489840647050766821L;

	private long userId;
	
	private String username;
	
	/**
	 * ���ѡ��ĳ���
	 */
	private String lastSelectCity;
	
	/**
	 * ���ѡ��ĳ���id
	 */
	private int lastSelectCityId;
	
	/**
	 * ����¼ip
	 */
	private String lastLoginIp;
	
	/**
	 * ����¼����
	 */
	private Date lastLoginDate;

	/**
	 * ��������
	 */
	private int goodScore;
	
	/**
	 * ��������
	 */
	private int badScore;
	
	/**
	 * ��������
	 */
	private int middleScore;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLastSelectCity() {
		return lastSelectCity;
	}

	public void setLastSelectCity(String lastSelectCity) {
		this.lastSelectCity = lastSelectCity;
	}

	public int getLastSelectCityId() {
		return lastSelectCityId;
	}

	public void setLastSelectCityId(int lastSelectCityId) {
		this.lastSelectCityId = lastSelectCityId;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public int getGoodScore() {
		return goodScore;
	}

	public void setGoodScore(int goodScore) {
		this.goodScore = goodScore;
	}

	public int getBadScore() {
		return badScore;
	}

	public void setBadScore(int badScore) {
		this.badScore = badScore;
	}

	public int getMiddleScore() {
		return middleScore;
	}

	public void setMiddleScore(int middleScore) {
		this.middleScore = middleScore;
	}
}
