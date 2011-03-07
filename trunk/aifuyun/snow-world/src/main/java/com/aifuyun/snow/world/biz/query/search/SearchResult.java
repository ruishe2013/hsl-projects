package com.aifuyun.snow.world.biz.query.search;

import java.util.List;

public class SearchResult<T> {

	private List<T> result;
	
	private long numFound;

	public String toString() {
		return "numFound: " + numFound + ", result:" + result;
	}
	
	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public long getNumFound() {
		return numFound;
	}

	public void setNumFound(long numFound) {
		this.numFound = numFound;
	}

	
}
