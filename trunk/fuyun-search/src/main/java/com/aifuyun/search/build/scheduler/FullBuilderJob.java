package com.aifuyun.search.build.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import com.aifuyun.search.build.DataProvider;
import com.aifuyun.search.build.IndexBuilder;
import com.aifuyun.search.build.IndexBuilderFactory;
import com.aifuyun.search.core.ContextHolder;
import com.aifuyun.search.core.DataProviderFactory;

public class FullBuilderJob extends AbstractBuilderJob  {

	private IndexBuilderFactory indexBuilderFactory = new IndexBuilderFactory();
	
	protected DataProvider getDataProvider(DataProviderFactory dataProviderFactory, String coreName) {
		return dataProviderFactory.createFullDataProvider(coreName);
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("dump 开始...");
		try {
			String coreName = (String)context.getScheduler().getContext().get("coreName");
			DataProvider dataProvider = getDataProvider(ContextHolder.getDataProviderFactory(), coreName);
			IndexBuilder indexBuilder = indexBuilderFactory.createFullIndexBuilder(coreName, dataProvider);
			indexBuilder.build();
		} catch (SchedulerException e) {
			log.error("dump出错", e);
		}
		log.info("dump 结束...");
	}


}
