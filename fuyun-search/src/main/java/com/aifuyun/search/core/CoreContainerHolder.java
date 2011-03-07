package com.aifuyun.search.core;

import java.io.File;

import org.apache.solr.core.CoreContainer;

import com.aifuyun.search.SearchException;

public class CoreContainerHolder {
	
	private static CoreContainer coreContainer = null;
	
	public static CoreContainer getCoreContainer() {
		return coreContainer;
	}
	
	public synchronized static void setCoreContainer(CoreContainer input) {
		coreContainer = input;
	}
	
	public synchronized static CoreContainer createCoreContainer(String dir, String configFile) {
		try {
			coreContainer = new CoreContainer(dir, new File(configFile));
			return coreContainer;
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

}
