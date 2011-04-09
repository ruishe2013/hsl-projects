package com.aifuyun.snow.world.dal.dataobject.enums;

public enum CareerTypeEnum {
	
	OFFICE_WORKER("上班族"),
	STUDENT("学生族"),
	OTHER("其他")
	;
	
	private CareerTypeEnum( String name) {
		this.name = name;
	}
	
	
	private final String name;


	public String getName() {
		return name;
	}

}
