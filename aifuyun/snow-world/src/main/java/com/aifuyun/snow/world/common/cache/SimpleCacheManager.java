package com.aifuyun.snow.world.common.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zjuh.sweet.lang.CollectionUtil;

public class SimpleCacheManager implements CacheManager {

	private Map<Integer, Map<Object, Object>> lruCaches = CollectionUtil.newHashMap();
	
	protected Map<Object, Object> getCache(int namespace) {
		return lruCaches.get(namespace);
	}
	
	@Override
	public void put(int namespace, Object key, Object value) {
		getCache(namespace).put(key, value);
	}

	@Override
	public void put(int namespace, Object key, Object value, int expire) {
		getCache(namespace).put(key, value);
	}

	@Override
	public Object get(int namespace, Object key) {
		return getCache(namespace).get(key);
	}

	@Override
	public List<Object> mget(int namespace, List<Object> keys) {
		List<Object> ret = CollectionUtil.newArrayList();
		Map<Object, Object> cache = getCache(namespace);
		for (Object key : keys) {
			ret.add(cache.get(key));
		}
		return ret;
	}

	@Override
	public void delete(int namespace, Object key) {
		getCache(namespace).remove(key);
	}

	@Override
	public void mdelete(int namespace, List<Object> keys) {
		Map<Object, Object> cache = getCache(namespace);
		for (Object key : keys) {
			cache.remove(key);
		}
	}
	
	public synchronized void setNamespaces(List<Integer> namespaces) {
		lruCaches.clear();
		for (Integer namespace : namespaces) {
			// 每个cache需要是线程安全的
			lruCaches.put(namespace, new ConcurrentHashMap<Object, Object>());
		}
	}
	
}
