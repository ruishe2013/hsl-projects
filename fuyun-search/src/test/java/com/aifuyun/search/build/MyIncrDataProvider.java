package com.aifuyun.search.build;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyIncrDataProvider implements DataProvider, TimeSupport {
	
	private ArrayList<Map<String, String>> datas = new ArrayList<Map<String, String>>();
	
	private int pos = 0;
	
	private void init(String id, String name, String content) {
		Map<String, String> row = new HashMap<String, String>();
		row.put("id", id);
		row.put("name", name);
		row.put("content", content);
		datas.add(row);
	}

	public MyIncrDataProvider() {
		init("1", "longyi", "this is the first row");
		init("2", "fuweiwei", "中国人");
		init("3", "yusen", "来几个中文试试");
		init("4", "longyi", "the longyi second row.");
	}
	
	@Override
	public void setTime(Date start, Date end) {
		System.out.println("start: " + start);
		System.out.println("end: " + end);
	}

	@Override
	public void init() {
		System.out.println("init");
		pos = 0;
	}

	@Override
	public void close() {
		System.out.println("close");
	}

	@Override
	public boolean hasNext() {
		return pos < datas.size() - 1;
	}

	@Override
	public Map<String, String> next() {
		return datas.get(pos ++ );
	}

}
