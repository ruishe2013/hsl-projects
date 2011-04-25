package com.aifuyun.snow.world.biz.bo.misc.impl;

import com.aifuyun.snow.world.biz.bo.misc.ConfigurationService;

public class ConfigurationServiceImpl implements ConfigurationService {
	
	private String importOrderDataPath;

	private String randomUsersPath;
	
	public String getImportOrderDataPath() {
		return importOrderDataPath;
	}

	public void setImportOrderDataPath(String importOrderDataPath) {
		this.importOrderDataPath = importOrderDataPath;
	}

	@Override
	public String getRandomUsersPath() {
		return randomUsersPath;
	}

	public void setRandomUsersPath(String randomUsersPath) {
		this.randomUsersPath = randomUsersPath;
	}

}
