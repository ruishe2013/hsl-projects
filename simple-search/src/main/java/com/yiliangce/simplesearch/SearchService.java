package com.yiliangce.simplesearch;

import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;

public interface SearchService {
	
	UpdateResponse add(Collection<SolrInputDocument> docs) throws SearchException;
	
	UpdateResponse add(SolrInputDocument doc) throws SearchException;
	
	UpdateResponse commit() throws SearchException;
	
	UpdateResponse optimize() throws SearchException;
	
	UpdateResponse commit(boolean waitFlush, boolean waitSearcher) throws SearchException;
	
	UpdateResponse optimize(boolean waitFlush, boolean waitSearcher) throws SearchException;
	
	UpdateResponse rollback() throws SearchException;
	
	UpdateResponse deleteById(String id) throws SearchException;
	
	UpdateResponse deleteById(List<String> ids) throws SearchException;
	
	UpdateResponse deleteByQuery(String query) throws SearchException;
	
	QueryResponse query(SolrParams params) throws SearchException;
	
	QueryResponse query(SolrParams params, METHOD method) throws SearchException;

}
