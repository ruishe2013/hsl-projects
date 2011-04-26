package com.aifuyun.snow.world.dal.dataobject.enums;

/**
 * @author pister
 *
 */
public enum OrderTypeEnum {
	
	TAXI(1, "拼的士"),
	SFC(2, "顺风车"),
	WORK(3,"上下班拼车")
	;
	
	private OrderTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	private final int value;
	
	private final String name;

	public static OrderTypeEnum valueOf(int value) {
		for (OrderTypeEnum e : values()) {
			if (e.getValue() == value) {
				return e;
			}
		}
		return null;
	}
	
	public static OrderTypeEnum valueOfName(String name) {
		for (OrderTypeEnum e : values()) {
			if (e.getName().equals(name)) {
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
