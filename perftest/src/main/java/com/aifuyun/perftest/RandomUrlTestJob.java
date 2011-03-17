package com.aifuyun.perftest;

import java.util.List;
import java.util.Random;

public class RandomUrlTestJob implements TestJob {

	private Random random = new Random();
	
	@Override
	public void doTest(List<String> urls) {
		if (urls.isEmpty()) {
			return;
		}
		int nextIndex = random.nextInt(urls.size());
		String url = urls.get(nextIndex);
		HttpUtil.requestUrl(url);
	}

}
