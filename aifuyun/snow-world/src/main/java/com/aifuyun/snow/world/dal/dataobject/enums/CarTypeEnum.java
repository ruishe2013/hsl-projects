package com.aifuyun.snow.world.dal.dataobject.enums;

public enum CarTypeEnum {
	
	CAR(1, "С�γ�"),
	CROSS_COUNTRY_VEHICLE(2, "ԽҰ��"),
	MINIBUS(3, "�����"),
	OTHER(100, "��������")
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
