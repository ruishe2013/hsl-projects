package com.htc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CollectionUtil {
	
	public static <E> List<E> newArrayList() {
		return new ArrayList<E>();
	}

	public static <K, V> Map<K, V> newTreeMap() {
		return new TreeMap<K, V>();
	}
	
	public static <T> String join(Collection<T> input, String token) {
		if (input == null) {
			return "";
		}
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (T t: input) {
			if (first) {
				first = false;
			} else {
				sb.append(token);
			}
			sb.append(t);
		}
		return sb.toString();
	}
	
}
