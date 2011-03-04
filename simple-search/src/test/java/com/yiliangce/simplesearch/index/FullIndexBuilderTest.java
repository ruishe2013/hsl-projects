package com.yiliangce.simplesearch.index;

import java.io.File;

import junit.framework.TestCase;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;

import com.yiliangce.simplesearch.MyDataProvider;
import com.yiliangce.simplesearch.SearchService;
import com.yiliangce.simplesearch.impl.SimpleSearchServiceImpl;

public class FullIndexBuilderTest extends TestCase {
	
	private String dir = "d:/solr_home";
	
	private String configFile = "d:/solr_home/solr.xml";
	
	private FullIndexBuilder indexBuilder = new FullIndexBuilder();
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		indexBuilder = new FullIndexBuilder();
		CoreContainer coreContainer = new CoreContainer();
		coreContainer.load(dir, new File(configFile));
		indexBuilder.setCoreContainer(coreContainer);
		indexBuilder.setCoreName("search1");
		indexBuilder.setDataProvider(new MyDataProvider());
	}

	public void testBuild() {
		indexBuilder.init();
		indexBuilder.build();
		indexBuilder.close();
		
		SolrCore core = indexBuilder.getCore();
		SearchService service = new SimpleSearchServiceImpl(core);
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("content:ÖÐ¹ú");
		QueryResponse response = service.query(solrQuery);
		SolrDocumentList results = response.getResults();
		System.out.println(results.getNumFound());
		System.out.println(results);
	}

}
