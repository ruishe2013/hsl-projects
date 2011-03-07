package com.aifuyun.search.date;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import org.apache.solr.core.SolrCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.search.SearchException;
import com.aifuyun.search.util.DateUtil;

public class FileDatetimeStore implements DatetimeStore {

	private static final Logger log = LoggerFactory.getLogger(FileDatetimeStore.class);
	
	private String filePath;
	
	public FileDatetimeStore() {
		super();
	}

	public FileDatetimeStore(SolrCore solrCore) {
		super();
		String solrHome = solrCore.getCoreDescriptor().getCoreContainer().getSolrHome();
		if (solrHome.endsWith(File.separator)) {
			filePath = solrHome + "incr_times";
		} else {
			filePath = solrHome + File.separator + "incr_times";
		}
	}

	private String getIncrTimeFilePath(String coreName) {
		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdirs();
		} else if (!dir.isDirectory()) {
			throw new SearchException(filePath + "不是一个目录");
		}
		
		if (filePath.endsWith(File.separator)) {
			return filePath + coreName + "_inct";
		} else {
			return filePath + File.separator + coreName + "_inct";
		}
	}
	
	@Override
	public Date load(String coreName) {
		BufferedReader reader = null;
		try {
			String path = getIncrTimeFilePath(coreName);
			File pathFile = new File(path);
			Date todayStart = DateUtil.todayStart();
			if (!pathFile.exists()) {
				return todayStart;
			}
			reader = new BufferedReader(new FileReader(path));
			String dateString = reader.readLine();
			return DateUtil.parseDate(dateString, todayStart);
		} catch (IOException e) {
			log.error("read file error", e);
			return null;
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
				// ignore
			}
		}
	}

	@Override
	public void save(String coreName, Date date) {
		PrintWriter writer = null;
		try {
			String dateString = DateUtil.formatDate(date);
			writer = new PrintWriter(new FileWriter(getIncrTimeFilePath(coreName)));
			writer.println(dateString);
			writer.flush();
		} catch (IOException e) {
			log.error("read file error", e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
				// ignore
			}
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
