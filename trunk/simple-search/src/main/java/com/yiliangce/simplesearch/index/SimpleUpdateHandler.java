package com.yiliangce.simplesearch.index;

import java.io.IOException;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.core.SolrCore;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.update.AddUpdateCommand;
import org.apache.solr.update.CommitUpdateCommand;
import org.apache.solr.update.DocumentBuilder;
import org.apache.solr.update.UpdateHandler;

import com.yiliangce.simplesearch.dump.BuildException;
import com.yiliangce.simplesearch.util.NumberUtil;

public class SimpleUpdateHandler {
	
	private static final String BOOST_NAME = "$BOOST";
	
	private float DEFAULT_BOOST = 1.0f;
	
	private UpdateHandler updateHandler;
	
	private SolrCore solrCore;
	
	public SimpleUpdateHandler(SolrCore solrCore) {
		super();
		this.updateHandler = solrCore.getUpdateHandler();
		this.solrCore = solrCore;
	}

	public void addDocument(Map<String, String> row) {
		String boost = row.get(BOOST_NAME);
		UpdateHandler updateHandler = solrCore.getUpdateHandler();
		AddUpdateCommand cmd = new AddUpdateCommand();
		cmd.allowDups = false;
		cmd.overwriteCommitted = true;
		cmd.overwritePending = true;
		float boostValue = DEFAULT_BOOST;
		if (boost != null) {
			boostValue = NumberUtil.toFloat(boost, DEFAULT_BOOST);
		}
		
		SolrInputDocument solrDoc = new SolrInputDocument();
		cmd.solrDoc = solrDoc;
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
		cmd.doc = DocumentBuilder.toDocument(solrDoc, indexSchema);
		try {
			updateHandler.addDoc(cmd);
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}

	public void commit(boolean optimize) {
		CommitUpdateCommand commitUpdateCommand = new CommitUpdateCommand(optimize);
		try {
			updateHandler.commit(commitUpdateCommand);
		} catch (IOException e) {
			throw new BuildException(e);
		}
	}
	
	public void close() {
		if (updateHandler != null) {
			try {
				updateHandler.close();
			} catch (IOException e) {
				throw new BuildException(e);
			}
		}
		updateHandler = null;
	}
	
}
