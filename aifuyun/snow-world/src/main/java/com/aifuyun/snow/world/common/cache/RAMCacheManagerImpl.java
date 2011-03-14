package com.aifuyun.snow.world.common.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zjuh.sweet.lang.CollectionUtil;

/**
 * @author ck
 *
 */
public class RAMCacheManagerImpl implements CacheManager {

	private Map<Integer, Map<Object, Entry>> cacheHolder = CollectionUtil.newHashMap();
	
	private static final int DEFAULT_EXPIRE = 60 * 60; // 默认一个小时失效
	
	protected Map<Object, Entry> getCache(int namespace) {
		return cacheHolder.get(namespace);
	}
	
	@Override
	public void put(int namespace, Object key, Object value) {
		put(namespace, key, value, DEFAULT_EXPIRE);
	}

	@Override
	public void put(int namespace, Object key, Object value, int expire) {
		getCache(namespace).put(key, new Entry(value, expire));
	}

	@Override
	public Object get(int namespace, Object key) {
		Map<Object, Entry> cmap = getCache(namespace);
		Entry entry = cmap.get(key);
		if (entry == null) {
			return null;
		}
		if (!entry.hasExpired()) {
			return entry.getValue();
		}
		cmap.remove(key);
		return null;
	}

	@Override
	public List<Object> mget(int namespace, List<Object> keys) {
		List<Object> ret = CollectionUtil.newArrayList();
		for (Object key : keys) {
			Object obj = get(namespace, key);
			ret.add(obj);
		}
		return ret;
	}

	@Override
	public void delete(int namespace, Object key) {
		getCache(namespace).remove(key);
	}

	@Override
	public void mdelete(int namespace, List<Object> keys) {
		Map<Object, Entry> cache = getCache(namespace);
		for (Object key : keys) {
			cache.remove(key);
		}
	}
	
	public synchronized void setNamespaces(List<Integer> namespaces) {
		cacheHolder.clear();
		for (Integer namespace : namespaces) {
			// 每个cache需要是线程安全的
			cacheHolder.put(namespace, new ConcurrentHashMap<Object, Entry>());
		}
	}
	
	static class Entry {
		
		/**
		 * 最后更新时间
		 */
		private long lastUpdate;
		
		/**
		 * 过期时间，秒
		 */
		private int expire;
		
		/**
		 * 值
		 */
		private Object value;
		
		public Entry(Object value, int expire) {
			this.value = value;
			this.expire = expire;
			this.lastUpdate = System.currentTimeMillis();;
		}
		
		public boolean hasExpired() {
			long now = System.currentTimeMillis();
			if (now > (lastUpdate + expire * 1000)) {
				return true;
			}
			return false;
		}
		
		public Object getValue() {
			return value;
		}
		
	}

}
