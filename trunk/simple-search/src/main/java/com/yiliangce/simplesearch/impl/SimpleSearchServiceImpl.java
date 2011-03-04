package com.yiliangce.simplesearch.impl;

import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;

import com.yiliangce.simplesearch.SearchException;
import com.yiliangce.simplesearch.SearchService;

public class SimpleSearchServiceImpl implements SearchService {

	private SolrServer solrServer;
	
	public SimpleSearchServiceImpl(CoreContainer coreContainer, String coreName) {
		solrServer = new EmbeddedSolrServer(coreContainer, coreName);
	}
	
	public SimpleSearchServiceImpl(SolrCore core) {
		this(core.getCoreDescriptor().getCoreContainer(), core.getName());
	}
	
	public SimpleSearchServiceImpl(SolrServer solrServer) {
		super();
		this.solrServer = solrServer;
	}

	@Override
	public UpdateResponse add(Collection<SolrInputDocument> docs) throws SearchException {
		try {
			return solrServer.add(docs);
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public UpdateResponse add(SolrInputDocument doc) throws SearchException {
		try {
			return solrServer.add(doc);
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public UpdateResponse commit() throws SearchException {
		try {
			return solrServer.commit();
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public UpdateResponse optimize() throws SearchException {
		try {
			return solrServer.optimize();
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public UpdateResponse commit(boolean waitFlush, boolean waitSearcher) throws SearchException {
		try {
			return solrServer.commit(waitFlush, waitSearcher);
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public UpdateResponse optimize(boolean waitFlush, boolean waitSearcher) throws SearchException {
		try {
			return solrServer.optimize(waitFlush, waitSearcher);
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public UpdateResponse rollback() throws SearchException {
		try {
			return solrServer.rollback();
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public UpdateResponse deleteById(String id) throws SearchException {
		try {
			return solrServer.deleteById(id);
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public UpdateResponse deleteById(List<String> ids) throws SearchException {
		try {
			return solrServer.deleteById(ids);
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public UpdateResponse deleteByQuery(String query) throws SearchException {
		try {
			return solrServer.deleteByQuery(query);
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public QueryResponse query(SolrParams params) throws SearchException {
		try {
			return solrServer.query(params);
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

	@Override
	public QueryResponse query(SolrParams params, METHOD method) throws SearchException {
		try {
			return solrServer.query(params, method);
		} catch (Exception e) {
			throw new SearchException(e);
		}
	}

}
