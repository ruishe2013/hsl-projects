package com.aifuyun.snow.world.common;

import java.util.Date;

import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.SexEnum;
import com.aifuyun.snow.world.dal.dataobject.misc.UserInfoHolder;
import com.zjuh.sweet.author.LoginContext;
import com.zjuh.sweet.lang.DateUtil;
import com.zjuh.sweet.lang.StringUtil;

public class SnowUtil {
	
	/**
	 * 获取最近的时间的分钟时
	 * 
	 * @param date
	 * @param minuteMode
	 * @return
	 */
	public static int getRecentMinute(Date date, int minuteMode) {
		if (minuteMode <= 0) {
			return 0;
		}
		int minute = DateUtil.getMinute(date);
		return (minute / minuteMode ) * minuteMode;
	}
	
	public static int getRecentHour(Date date, int hourMode) {
		if (hourMode <= 0) {
			return 0;
		}
		int minute = DateUtil.getHour(date);
		return (minute / hourMode ) * hourMode;
	}

	public static String getLoginUsername() {
		return LoginContext.getUsername();
	}
	
	public static long getLoginUserId() {
		return LoginContext.getUserId();
	}
	
	public static int getSelectedYear(UserInfoHolder userInfoHolder) {
		if (userInfoHolder == null) {
			return SnowConstants.DEFAULT_SEELECT_YEAR;
		}
		if (BirthYearEnum.inRange(userInfoHolder.getBirthYear())) {
			return userInfoHolder.getBirthYear();
		}
		return SnowConstants.DEFAULT_SEELECT_YEAR;
	}
	
	public static String familyNameAndSex(String realname, SexEnum sex) {
		return getFamilyName(realname) + sex.getName();
	}
	
	public static String getFamilyName(String realname) {
		if (StringUtil.isEmpty(realname)) {
			return realname;
		}
		return String.valueOf(realname.charAt(0));
	}
	
	public static int compareString(String str1, String str2) {
		if (str1 == null && str2 == null) {
			return 0;
		}
		if (str1 == null) {
			return -1;
		}
		if (str2 == null) {
			return 1;
		}
		return str1.compareTo(str2);
	}
	
}
