package com.aifuyun.snow.world.dal.dataobject.enums;

public enum UserStatusEnum {

	NORMAL(0, "Õý³£")
	
	;
	
	private UserStatusEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	private final int value;
	
	private final String name;

	public static UserStatusEnum valueOf(int value) {
		for (UserStatusEnum e : values()) {
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
