package com.aifuyun.snow.world.common.cache;

import java.util.LinkedHashMap;

/**
 * 
 * �򵥵�lru����ʱ��
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
			// ����̫С
			maxCapacity = 10;
		}
		this.maxCapacity = maxCapacity;
	}
	
}
