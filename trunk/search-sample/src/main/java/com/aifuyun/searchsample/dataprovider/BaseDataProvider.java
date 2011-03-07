package com.aifuyun.searchsample.dataprovider;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.aifuyun.search.build.DataProvider;

public abstract class BaseDataProvider implements DataProvider {

	private BufferedReader reader = null;
	
	private String lastLine = "111 first line";
	
	protected abstract String getFilePath();
	
	@Override
	public void close() {
		if (reader != null) {
			try {
				System.out.println("close============================");
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean hasNext() {
		return lastLine != null;
	}

	@Override
	public void init() {
		try {
			System.out.println("init============================");
			reader = new BufferedReader(new FileReader(getFilePath()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, String> next() {
		try {
			lastLine = reader.readLine();
			if (lastLine == null) {
				return null;
			}
			String[] parts = lastLine.split("\\s+?");
			String id = parts[0];
			String name = parts[1];
			String content = parts[2];
			Map<String, String> ret = new HashMap<String, String>();
			ret.put("id", id);
			ret.put("name", name);
			ret.put("content", content);
			System.out.println("ret:" + ret);
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
