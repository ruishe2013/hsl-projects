package com.aifuyun.snow.world.dal.dataobject.enums;

public enum CarOwnerTypeEnum {
	
	
	CAR_OWNER(1, "����"),
	PASSENGER(2, "�˿�")
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
	
	public static CarOwnerTypeEnum valueOfName(String name) {
		for (CarOwnerTypeEnum s : CarOwnerTypeEnum.values()) {
			if (s.getName().equals(name)) {
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
