package com.aifuyun.snow.world.common.cache;

import java.util.LinkedHashMap;

/**
 * 
 * 简单的lru缓存时间
 * @author ck
 *
 */
public class LRUCache extends LinkedHashMap<Object, Object> {

	private static final long serialVersionUID = -2839285880136234083L;

	private int maxCapacity = 1024;

	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<Object, Object> eldest) {
		return size() > maxCapacity;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		if (maxCapacity <= 10) {
			// 不能太小
			maxCapacity = 10;
		}
		this.maxCapacity = maxCapacity;
	}
	
}
