package com.aifuyun.snow.world.biz.ao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zjuh.sweet.author.LoginContext;

/**
 * @author pister
 * 2011-1-22
 */
public abstract class BaseAO {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected long getLoginUserId() {
		return LoginContext.getUserId();
	}
	
	protected String getLoginUsername() {
		return LoginContext.getUsername();
	}
	
	protected boolean isUserLogin() {
		return LoginContext.isUserLogin();
	}
	
}
