package com.aifuyun.snow.world.dal.dataobject.enums;

public enum CareerTypeEnum {
	
	OFFICE_WORKER("�ϰ���"),
	STUDENT("ѧ����"),
	OTHER("����")
	;
	
	private CareerTypeEnum( String name) {
		this.name = name;
	}
	
	
	private final String name;


	public String getName() {
		return name;
	}

}
