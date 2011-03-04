package com.yiliangce.simplesearch.index;

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
		
		//�ر�dest
		while(!newCore.isClosed()){
			newCore.close();
		}
		
		CoreContainer coreContainer = oldCore.getCoreDescriptor().getCoreContainer();
		try{
			coreContainer.reload(coreName);
		} catch (Exception e) {
			log.error("���´�SolrCoreʧ�ܡ���������", e);
			throw new RuntimeException("���´�destʧ�ܣ�solr core�޷�����ʹ�á�solr core:" + oldCore.getName());
		}
		
		//�ȹر�src��Ȼ��reload
		while(!oldCore.isClosed()){
			oldCore.close();
		}
		
		if(oldCore.getCoreDescriptor().getCoreContainer().isPersistent()){
			oldCore.getCoreDescriptor().getCoreContainer().persist();
		}
		
		//ɾ��ԭ����coreʹ�õ�dataĿ¼�µ���������
		try {
			cleanDir(new File(oldDataDir));
		} catch (IOException e) {
			log.error("�����������Ŀ¼ʧ�ܡ�dataDir:" + oldDataDir, e);
		}
		return coreContainer.getCore(coreName);
	}
	
	public static void cleanDir(File dataDir) throws IOException{
		FileUtils.cleanDirectory(dataDir);
	}

}
