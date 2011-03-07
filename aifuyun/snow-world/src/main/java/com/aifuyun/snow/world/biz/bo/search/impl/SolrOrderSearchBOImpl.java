package com.aifuyun.snow.world.biz.bo.search.impl;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.LBHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.SolrParams;

import com.aifuyun.snow.world.biz.BizException;
import com.aifuyun.snow.world.biz.bo.search.OrderSearchBO;
import com.aifuyun.snow.world.biz.query.search.FieldOrder;
import com.aifuyun.snow.world.biz.query.search.SearchQuery;
import com.aifuyun.snow.world.biz.query.search.SearchResult;
import com.aifuyun.snow.world.biz.query.search.SortField;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;

public class SolrOrderSearchBOImpl implements OrderSearchBO {

	private SolrServer solrServer;
	
	private String[] solrUrls;
	
	public void init() throws MalformedURLException {
		solrServer = new LBHttpSolrServer(solrUrls);
	}
	
	@Override
	public SearchResult<OrderDO> queryOrders(SearchQuery query) {
		try {
			SolrParams solrQuery = asSolrQuery(query);
			QueryResponse queryResponse = solrServer.query(solrQuery);
			SolrDocumentList results = queryResponse.getResults();
			SearchResult<OrderDO> orderResult = new SearchResult<OrderDO>();
			orderResult.setNumFound(results.getNumFound());
			List<OrderDO> orders = new ArrayList<OrderDO>();
			for (SolrDocument doc : results) {
				orders.add(solrDoc2Order(doc));
			}
			orderResult.setResult(orders);
			return orderResult;
		} catch (SolrServerException e) {
			throw new BizException(e);
		}
	}
	
	private OrderDO solrDoc2Order(SolrDocument doc) {
		SimpleSolrDocument simpleSolrDocument = new SimpleSolrDocument(doc);
		OrderDO orderDO = new OrderDO();
		orderDO.setId(simpleSolrDocument.getLongValue("id"));
		orderDO.setApproach(simpleSolrDocument.getStringValue("approach"));
		orderDO.setArriveAddr(simpleSolrDocument.getStringValue("arriveAddr"));
		orderDO.setArriveCity(simpleSolrDocument.getStringValue("arriveCity"));
		orderDO.setArriveCityId(simpleSolrDocument.getIntValue("arriveCityId"));
		// TODO
		orderDO.setArriveTime(null);
		orderDO.setCityId(simpleSolrDocument.getIntValue("cityId"));
		orderDO.setCreatorId(simpleSolrDocument.getLongValue("creatorId"));
		orderDO.setCreatorUsername(simpleSolrDocument.getStringValue("creatorUsername"));
		//orderDO.setDescription(description)
		orderDO.setFromAddr(simpleSolrDocument.getStringValue("fromAddr"));
		orderDO.setFromCity(simpleSolrDocument.getStringValue("fromCity"));
		// TODO
	//	orderDO.setFromTime(fromTime);
		orderDO.setStatus(simpleSolrDocument.getIntValue("status"));
		orderDO.setType(simpleSolrDocument.getIntValue("type"));
		return orderDO;
	}

	public void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}
	
	protected SolrParams asSolrQuery(SearchQuery query) {
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setRows(query.getRows());
		for (SortField sf : query.getSortFields()) {
			solrQuery.addSortField(sf.getName(), toSolrSort(sf.getFieldOrder()));
		}
		solrQuery.setQuery(query.getQ());
		return solrQuery;
	}
	
	private ORDER toSolrSort(FieldOrder fieldOrder) {
		if (FieldOrder.ASC == fieldOrder) {
			return ORDER.asc;
		}
		if (FieldOrder.DESC == fieldOrder) {
			return ORDER.desc;
		}
		return null;
	}

	public String[] getSolrUrls() {
		return solrUrls;
	}

	public void setSolrUrls(String[] solrUrls) {
		this.solrUrls = solrUrls;
	}
	
	
}
