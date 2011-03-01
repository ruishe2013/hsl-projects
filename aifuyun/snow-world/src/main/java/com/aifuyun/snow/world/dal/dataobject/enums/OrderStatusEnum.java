package com.aifuyun.snow.world.dal.dataobject.enums;

/**
 * @author pister
 *
 */
public enum OrderStatusEnum {
	
	NOT_EFFECT(0, "未生效"),
	WAIT_CONFIRM(10, "待确认"),
	HAS_CONFIRM(20, "已确认"),
	FINISH(30, "已完成");
	
	private OrderStatusEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	
	private final int value;
	
	private final String name;

	public static OrderStatusEnum valueOf(int value) {
		for (OrderStatusEnum e : values()) {
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
