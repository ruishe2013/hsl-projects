package com.aifuyun.search.servlet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.apache.solr.servlet.SolrDispatchFilter;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.search.build.scheduler.BuilderScheduler;
import com.aifuyun.search.core.ContextHolder;
import com.aifuyun.search.core.DataProviderFactory;

public abstract class XSolrDispatchFilter extends SolrDispatchFilter {

	private List<BuilderScheduler> builderSchedulers = new ArrayList<BuilderScheduler>();
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		super.init(config);
		ContextHolder.setCoreContainer(this.cores);
		ContextHolder.setDataProviderFactory(getDataProviderFactory());
		if (cores.getCoreNames() != null) {
			for (String coreName : cores.getCoreNames()) {
				BuilderScheduler builderScheduler = new BuilderScheduler(coreName);
				try {
					builderScheduler.init();
				} catch (SchedulerException e) {
					logger.error("µ÷¶ÈÊ§°Ü", e);
				} catch (ParseException e) {
					logger.error("½âÎöcronÊ§°Ü", e);
				}
				builderScheduler.startup();
				builderSchedulers.add(builderScheduler);
			}
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		for (BuilderScheduler b :builderSchedulers) {
			b.shutdown();
		}
	}

	protected abstract DataProviderFactory getDataProviderFactory();


}
