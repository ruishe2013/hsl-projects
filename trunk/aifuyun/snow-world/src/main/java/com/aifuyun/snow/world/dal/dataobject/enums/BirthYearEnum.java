package com.aifuyun.snow.world.dal.dataobject.enums;

public enum BirthYearEnum {
	
	YEAR_40S(1940, "40后"),
	YEAR_50S(1950, "50后"),
	YEAR_60S(1960, "60后"),
	YEAR_70S(1970, "70后"),
	YEAR_80S(1980, "80后"),
	YEAR_90S(1990, "90后"),
	YEAR_00S(2000, "00后");
	
	private BirthYearEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	private final int value;
	
	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	private final String name;

}
