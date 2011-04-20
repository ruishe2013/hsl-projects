package com.aifuyun.snow.world.dal.dataobject.enums;

public enum CarTypeEnum {
	
	CAR(1, "小轿车"),
	CROSS_COUNTRY_VEHICLE(2, "越野车"),
	MINIBUS(3, "面包车"),
	OTHER(100, "其他车型")
	;
	
	private CarTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	private final int value;
	
	private final String name;

	public static CarTypeEnum valueOf(int value) {
		for (CarTypeEnum e : values()) {
			if (e.getValue() == value) {
				return e;
			}
		}
		return null;
	}
	
	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

}
