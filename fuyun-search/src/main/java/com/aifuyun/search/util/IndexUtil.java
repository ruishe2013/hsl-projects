package com.aifuyun.search.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexUtil {
	
	private static final Logger log = LoggerFactory.getLogger(IndexUtil.class);
	
	public static SolrCore swapCore(SolrCore oldCore, SolrCore newCore) {
		if (log.isInfoEnabled()) {
			log.debug("swap cores: old core:" + oldCore.getDataDir() + ", new core:" + newCore.getDataDir());
		}
		
		String coreName = oldCore.getName();
		String oldDataDir = oldCore.getDataDir();
		
		oldCore.getCoreDescriptor().setDataDir(newCore.getCoreDescriptor().getDataDir());
		
		//关闭dest
		while(!newCore.isClosed()){
			newCore.close();
		}
		
		CoreContainer coreContainer = oldCore.getCoreDescriptor().getCoreContainer();
		try{
			coreContainer.reload(coreName);
		} catch (Exception e) {
			log.error("重新打开SolrCore失败。。。。。", e);
			throw new RuntimeException("重新打开dest失败，solr core无法继续使用。solr core:" + oldCore.getName());
		}
		
		//先关闭src，然后reload
		while(!oldCore.isClosed()){
			oldCore.close();
		}
		
		if(oldCore.getCoreDescriptor().getCoreContainer().isPersistent()){
			oldCore.getCoreDescriptor().getCoreContainer().persist();
		}
		
		//删除原来的core使用的data目录下的索引数据
		try {
			cleanDir(new File(oldDataDir));
		} catch (IOException e) {
			log.error("清空索引数据目录失败。dataDir:" + oldDataDir, e);
		}
		return coreContainer.getCore(coreName);
	}
	
	public static void cleanDir(File dataDir) throws IOException{
		FileUtils.cleanDirectory(dataDir);
	}

}
