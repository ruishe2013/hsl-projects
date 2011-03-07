package com.aifuyun.search.build;

import java.io.IOException;
import java.util.Date;

import org.apache.solr.core.SolrCore;
import org.apache.solr.update.CommitUpdateCommand;

import com.aifuyun.search.date.DatetimeStore;
import com.aifuyun.search.date.FileDatetimeStore;

public class IncrIndexBuilder extends AbstractIndexBuilder {

	private DatetimeStore datetimeStore;
	
	@Override
	protected void onBeforeDump() {
		if (datetimeStore == null) {
			datetimeStore = new FileDatetimeStore(this.getSolrCore());
		}
		DataProvider dataProvider = this.getDataProvider();
		if (TimeSupport.class.isInstance(dataProvider)) {
			Date startDate = datetimeStore.load(this.getSolrCore().getName());
			Date endDate = new Date();
			datetimeStore.save(this.getSolrCore().getName(), endDate);
			((TimeSupport)dataProvider).setTime(startDate, endDate);
		}
	}
	
	@Override
	protected void onAfterDump() {
	}

	@Override
	protected void commitIndex() throws IOException {
		CommitUpdateCommand cmd = new CommitUpdateCommand(false);
		this.getUpdateHandler().commit(cmd);
	}

	@Override
	protected SolrCore initCore(SolrCore oldCore) {
		return oldCore;
	}

	public void setDatetimeStore(DatetimeStore datetimeStore) {
		this.datetimeStore = datetimeStore;
	}

}
