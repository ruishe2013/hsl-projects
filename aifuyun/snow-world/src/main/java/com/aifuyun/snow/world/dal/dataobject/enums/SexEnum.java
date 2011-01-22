package com.aifuyun.snow.world.dal.dataobject.enums;

public enum SexEnum {
	
	UNKNOWN(0, "保密"),
	MALE(1, "先生"),
	FEMAILE(2, "女士");
	
	private SexEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	private final int value;
	
	private final String name;

	public static SexEnum valueOf(int value) {
		for (SexEnum s : SexEnum.values()) {
			if (s.getValue() == value) {
				return s;
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
