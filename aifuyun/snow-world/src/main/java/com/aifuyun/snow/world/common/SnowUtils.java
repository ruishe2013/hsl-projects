package com.aifuyun.snow.world.common;

import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.misc.UserInfoHolder;

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
	
}
