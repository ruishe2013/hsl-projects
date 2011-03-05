package com.aifuyun.snow.world.common.cache;

import java.util.List;

public interface CacheManager {
	
	void put(int namespace, Object key, Object value);
	
	void put(int namespace, Object key, Object value, int expire);
	
	Object get(int namespace, Object key);
	
	List<Object> mget(int namespace, List<Object> keys);
	
	void delete(int namespace, Object key);

	void mdelete(int namespace, List<Object> keys);
}
