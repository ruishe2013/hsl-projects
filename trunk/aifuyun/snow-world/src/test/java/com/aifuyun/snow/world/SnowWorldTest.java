package com.aifuyun.snow.world;

import com.zjuh.sweet.author.LoginContext;
import com.zjuh.sweet.test.BaseTest;

public abstract class SnowWorldTest extends BaseTest {
	
	public void setLogin(long userId, String username) {
		LoginContext.setUser(userId, username);
	}
	
	public void setLogout() {
		LoginContext.removeUser();
	}

}
