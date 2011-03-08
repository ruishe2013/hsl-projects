package com.aifuyun.searchsample;

import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.LBHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class HttpSolrServerTest {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 * @throws SolrServerException 
	 */
	public static void main(String[] args) throws MalformedURLException, SolrServerException {
		SolrServer solrServer = new LBHttpSolrServer("http://127.0.0.1:8080/search-sample/search1");
		SolrQuery query = new SolrQuery();
	/*	query.setHighlight(true);
		query.addHighlightField("content");
		query.setHighlightSimplePre("<span class=\"highline\">");
		query.setHighlightSimplePost("</span>");*/
		query.setSortField("id", ORDER.asc);
		query.setRows(4);
		query.setQuery("+content:ʲô");
		QueryResponse response = solrServer.query(query);
		SolrDocumentList results = response.getResults();
		System.out.println(results.getNumFound());
		for (SolrDocument solrDoc : results) {
			Object id = solrDoc.getFieldValue("id");
			Object name = solrDoc.getFieldValue("name");
			Object content = solrDoc.getFieldValue("content");
			System.out.println("id:" + id + ", name:" + name + ", content:" + content);
		}
		
	}

}
