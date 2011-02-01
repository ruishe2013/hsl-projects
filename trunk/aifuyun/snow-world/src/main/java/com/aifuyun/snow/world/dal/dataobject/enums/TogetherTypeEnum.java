package com.aifuyun.snow.world.dal.dataobject.enums;

/**
 * @author pister
 *
 */
public enum TogetherTypeEnum {
	
	UNKNOWN(0, "Î´Öª"),
	TAXI(1, "Æ´µÄ")
	
	;
	
	private TogetherTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	private final int value;
	
	private final String name;

	public static TogetherTypeEnum valueOf(int value) {
		for (TogetherTypeEnum e : values()) {
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
