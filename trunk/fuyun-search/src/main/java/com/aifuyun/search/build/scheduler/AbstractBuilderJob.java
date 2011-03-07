package com.aifuyun.search.build.scheduler;

import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBuilderJob implements Job {

	protected final Logger log = LoggerFactory.getLogger(getClass());


}
