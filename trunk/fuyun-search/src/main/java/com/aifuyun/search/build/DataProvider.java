package com.aifuyun.search.build;

import java.util.Map;

public interface DataProvider {
	
	void init();
	
	void close();
	
	boolean hasNext();
	
	Map<String, String> next();

}
