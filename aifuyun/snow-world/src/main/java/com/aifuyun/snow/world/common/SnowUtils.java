package com.aifuyun.snow.world.common;

import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.SexEnum;
import com.aifuyun.snow.world.dal.dataobject.misc.UserInfoHolder;
import com.zjuh.sweet.lang.StringUtil;

public class SnowUtils {

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
	
}
