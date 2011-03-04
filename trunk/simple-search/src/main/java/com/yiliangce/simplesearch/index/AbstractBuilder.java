package com.yiliangce.simplesearch.index;


import java.util.Map;

import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.CoreDescriptor;
import org.apache.solr.core.SolrCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yiliangce.simplesearch.dump.BuildException;
import com.yiliangce.simplesearch.dump.DataProvider;
import com.yiliangce.simplesearch.util.PathUtil;

public abstract class AbstractBuilder implements Builder {

	private static final int NUMBER_OF_LINES = 1000;
	
	private CoreContainer coreContainer;
	
	private String coreName;
	
	private SolrCore newCore;
	
	private SolrCore oldCore;
	
	private DataProvider dataProvider;
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void init() {

	}

	@Override
	public final void build() {
		try {
			onBuildBefore();
			onBuild();
		} finally {
			onBuildAfter();
		}
	}
	
	protected String getWriteDataDir(SolrCore oldCore) {
		String dir = PathUtil.trimEndsSlashForPath(oldCore.getDataDir());
		String postName = null;
		// 在data-0和data-1接切换
		if (dir.endsWith("-0")) {
			postName = "-1";
		} else {
			postName = "-0";
		}
		return dir.substring(0, dir.length() - 2) + postName;
	}
	
	protected void prepareCore() {
		oldCore = coreContainer.getCore(coreName);
		if (oldCore == null) {
			throw new BuildException("core " + coreName + " not exist!");
		}
		CoreDescriptor oldCoreDescriptor = oldCore.getCoreDescriptor();
		String dataDir = getWriteDataDir(oldCore);
		if (log.isInfoEnabled()) {
			log.info("write dataDir: " + dataDir);
		}
		newCore = new SolrCore(oldCore.getName(), dataDir, oldCore.getSolrConfig(), oldCore.getSchema(), oldCoreDescriptor);
		// 如果是全量，需要删除原来的data目录
		newCore.getCoreDescriptor().setDataDir(dataDir);
	}
	
	protected synchronized void onBuild() {
		if (log.isInfoEnabled()) {
			log.info("starting build...");
		}
		if (dataProvider == null) {
			throw new BuildException("dataProvider can not be null!");
		}
		long startTime = System.currentTimeMillis();
		
		prepareCore();

		int errorNum = 0;
		int sucecessNum = 0;
		
		SimpleUpdateHandler simpleUpdateHandler = new SimpleUpdateHandler(newCore);
		while (dataProvider.hasNext()) {
			Map<String, String> row = dataProvider.next();
			if (row == null) {
				++errorNum;
				continue;
			}
			try {
				simpleUpdateHandler.addDocument(row);
				++sucecessNum;
				if (log.isInfoEnabled()) {
					int total = sucecessNum + errorNum;
					if (total % NUMBER_OF_LINES == 0) {
						log.info("dumping data, sucecess:" + sucecessNum + ", error: " + errorNum + ", total: " + total);
					}
				}
			} catch (Exception e) {
				++errorNum;
				log.info("error: ", e);
			}
		}
		
		int total = sucecessNum + errorNum;
		if (log.isInfoEnabled()) {
			log.info("dump data finish. sucecess:" + sucecessNum + ", error: " + errorNum + ", total: " + total);
			log.info("starting optimze index and commit...");
		}
		
		simpleUpdateHandler.commit(true);
		if (log.isInfoEnabled()) {
			log.info("optimze index and commit success!");
		}
		simpleUpdateHandler.close();
		
		switchIndex();
		long endTime = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			long escapeTime = endTime - startTime;
			log.info("starting dump finished. time escape: " + escapeTime + " ms.");
		}
	}
	
	private void switchIndex() {
		SolrCore core = IndexUtil.swapCore(oldCore, newCore);
		core.getCoreDescriptor().getCoreContainer().persist();
		
		newCore = null;
		oldCore = core;
	}
	
	public SolrCore getCore() {
		return oldCore;
	}

	protected void onBuildBefore() {
		init();
	}
	
	protected void onBuildAfter() {
		close();
	}
	
	@Override
	public void close() {

	}
	public CoreContainer getCoreContainer() {
		return coreContainer;
	}

	public void setCoreContainer(CoreContainer coreContainer) {
		this.coreContainer = coreContainer;
	}

	public String getCoreName() {
		return coreName;
	}

	public void setCoreName(String coreName) {
		this.coreName = coreName;
	}

	public DataProvider getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}
}
