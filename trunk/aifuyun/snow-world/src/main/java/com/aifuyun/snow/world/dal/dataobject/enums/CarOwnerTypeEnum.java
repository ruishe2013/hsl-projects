package com.aifuyun.snow.world.dal.dataobject.enums;

public enum CarOwnerTypeEnum {
	
	
	CAR_OWNER(1, "³µÖ÷"),
	PASSENGER(2, "³Ë¿Í")
	;
	
	private CarOwnerTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	private final int value;
	
	private final String name;

	public static CarOwnerTypeEnum valueOf(int value) {
		for (CarOwnerTypeEnum s : CarOwnerTypeEnum.values()) {
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
