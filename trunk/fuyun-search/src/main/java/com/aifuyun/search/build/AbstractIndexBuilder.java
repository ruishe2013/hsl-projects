package com.aifuyun.search.build;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.lucene.document.Document;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.update.AddUpdateCommand;
import org.apache.solr.update.DocumentBuilder;
import org.apache.solr.update.UpdateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.search.SearchException;
import com.aifuyun.search.build.util.Counts;
import com.aifuyun.search.core.ContextHolder;
import com.aifuyun.search.util.NumberUtil;

public abstract class AbstractIndexBuilder implements IndexBuilder {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private DataProvider dataProvider;
	
	private AtomicBoolean dumping = new AtomicBoolean(false);
	
	private String coreName;
	
	private SolrCore solrCore;
	
	private UpdateHandler updateHandler;
	
	private int showPercount = 1000;

	protected Document toDocument(SolrInputDocument solrDoc) {
		IndexSchema indexSchema = solrCore.getSchema();
		return DocumentBuilder.toDocument(solrDoc, indexSchema);
	}
	
	protected SolrInputDocument toSolrInputDocument(Map<String, String> row) {
		String boost = row.get(BuildConstant.BOOST_NAME);
		float boostValue = BuildConstant.DEFAULT_BOOST_VALUE;
		if (boost != null) {
			boostValue = NumberUtil.toFloat(boost, BuildConstant.DEFAULT_BOOST_VALUE);
		}
		SolrInputDocument solrDoc = new SolrInputDocument();
		IndexSchema indexSchema = solrCore.getSchema();
		for (Map.Entry<String, String> field : row.entrySet()) {
			String name = field.getKey();
			if (!indexSchema.getFields().containsKey(name)) {
				continue;
			}
			String value = field.getValue();
			SolrInputField solrValue = new SolrInputField(name);
			solrValue.setName(name);
			solrValue.setValue(value, boostValue);
			solrDoc.put(name, solrValue);
		}
		return solrDoc;
	}
	
	protected abstract void onBeforeDump();
	
	protected abstract void commitIndex() throws IOException;
	
	protected abstract SolrCore initCore(SolrCore oldCore);
	
	protected abstract void onAfterDump();
	
	protected void processDoc(SolrInputDocument solrDoc, Document doc) throws IOException {
		UpdateHandler updateHandler = this.getUpdateHandler();
		
		AddUpdateCommand cmd = new AddUpdateCommand();
		cmd.allowDups = false;
		cmd.overwriteCommitted = true;
		cmd.overwritePending = true;
		
		cmd.solrDoc = solrDoc;
		cmd.doc = doc;		
		updateHandler.addDoc(cmd);
	}
	
	private void onInit() {
		try {
			CoreContainer coreContainer = ContextHolder.getCoreContainer();
			SolrCore oldCore = coreContainer.getCore(coreName);
			this.solrCore = initCore(oldCore);
			
			updateHandler = solrCore.getUpdateHandler();
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}
	
	private boolean hasNext() {
		try {
			return dataProvider.hasNext();
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public void build() {
		if (dataProvider == null) {
			log.warn("dataProvider is null.");
			return;
		}
		if (!dumping.compareAndSet(false, true)) {
			log.warn("已经有dump程序在运行，直接返回.");
			return;
		}
		try {
			onInit();
			onBeforeDump();
			dataProvider.init();
			Counts counts = new Counts();
			
			while (hasNext()) {
				try {
					Map<String, String> row = dataProvider.next();
					if (row == null) {
						counts.incrFailed();
						continue;
					}
					
					SolrInputDocument solrDoc = toSolrInputDocument(row);
					Document doc = toDocument(solrDoc);
					processDoc(solrDoc, doc);
					
					counts.incrSuccess();
					if (counts.getSuccess() % showPercount == 0) {
						log.info("success: " + counts.getSuccess());
					}
				} catch (Exception e) {
					counts.incrFailed();
					log.warn("process doc failed: ", e);
				}
			}
			commitIndex();
			updateHandler.close();
			log.info(coreName + " build 完成: " + counts.reportToString());
			onAfterDump();
		} catch (Exception e) {
			log.error("build error", e);
		} finally {
			dataProvider.close();
			dumping.set(false);
		}
		
	}

	protected UpdateHandler getUpdateHandler() {
		return updateHandler;
	}

	protected SolrCore getSolrCore() {
		return solrCore;
	}

	public DataProvider getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	public String getCoreName() {
		return coreName;
	}

	public void setCoreName(String coreName) {
		this.coreName = coreName;
	}

}
