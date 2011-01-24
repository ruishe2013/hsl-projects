package com.aifuyun.snow.world.common;

import com.zjuh.sweet.lang.ConvertUtil;
import com.zjuh.sweet.lang.StringUtil;

public class IpUtil {
	
	public static long stringToNumberAddress(String address) {
		if (StringUtil.isEmpty(address)) {
			return 0L;
		}
		String[] parts = address.split("\\.");
		if (parts.length < 4) {
			return 0L;
		}
		int part0 = ConvertUtil.toInt(parts[0], 0);
		int part1 = ConvertUtil.toInt(parts[1], 0);
		int part2 = ConvertUtil.toInt(parts[2], 0);
		int part3 = ConvertUtil.toInt(parts[3], 0);
		long ret = 0L;
		ret |= (part0 << 24);
		ret |= (part1 << 18);
		ret |= (part2 << 8);
		ret |= (part3);
		return ret;		
	}
	
	public static void main(String[] args) {
		System.out.println(stringToNumberAddress("122.234.5.107"));
	}

}
