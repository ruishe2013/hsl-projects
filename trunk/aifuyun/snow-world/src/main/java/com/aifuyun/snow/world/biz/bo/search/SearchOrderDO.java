package com.aifuyun.snow.world.biz.bo.search;

import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;

public class SearchOrderDO extends OrderDO {
	
	private static final long serialVersionUID = 8485614513980524313L;
	
	private String creatorRealName;

	public String getCreatorRealName() {
		return creatorRealName;
	}

	public void setCreatorRealName(String creatorRealName) {
		this.creatorRealName = creatorRealName;
	}


}
