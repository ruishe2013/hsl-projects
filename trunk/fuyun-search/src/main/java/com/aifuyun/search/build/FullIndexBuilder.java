package com.aifuyun.search.build;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.solr.core.CoreDescriptor;
import org.apache.solr.core.SolrCore;
import org.apache.solr.update.CommitUpdateCommand;

import com.aifuyun.search.util.IndexUtil;
import com.aifuyun.search.util.PathUtil;

public class FullIndexBuilder extends AbstractIndexBuilder {

	private SolrCore oldCore;
	
	private SolrCore newCore;
	
	@Override
	protected void onBeforeDump() {

	}

	@Override
	protected void commitIndex() throws IOException {
		CommitUpdateCommand cmd = new CommitUpdateCommand(true);
		this.getUpdateHandler().commit(cmd);
	}

	private String getWriteDataDir(SolrCore oldCore) {
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
	
	@Override
	protected SolrCore initCore(SolrCore oldCore) {
		CoreDescriptor coreDesc = oldCore.getCoreDescriptor();
		String dirData = getWriteDataDir(oldCore);
		if (log.isDebugEnabled()) {
			log.debug("dirData:" + dirData);
		}
		try {
			// 如果原来有数据，则删除
			FileUtils.cleanDirectory(new File(dirData));
		} catch (IOException e) {
			log.warn("删除目录失败", e);
		}
		SolrCore newCore = new SolrCore(oldCore.getName(), dirData, oldCore.getSolrConfig(), oldCore.getSchema(), coreDesc);
		newCore.getCoreDescriptor().setDataDir(dirData);
		this.newCore = newCore;
		this.oldCore = oldCore;
		return newCore;
	}

	@Override
	protected void onAfterDump() {
		// 切换索引
		log.info("切换索引.");
		IndexUtil.swapCore(oldCore, newCore);
	}

}
