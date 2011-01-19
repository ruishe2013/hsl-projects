package com.aifuyun.snow.world.dal.dataobject;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pister
 * 2011-1-19
 */
public abstract class BaseDO implements Serializable {
	
	private static final long serialVersionUID = -1573876777127095397L;
	
	protected Date gmtCreate;
	
	protected Date gmtModified;

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

}
