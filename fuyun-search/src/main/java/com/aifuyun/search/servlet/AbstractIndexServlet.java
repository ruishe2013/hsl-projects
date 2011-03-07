package com.aifuyun.search.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.core.CoreContainer;
import org.apache.solr.core.SolrCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.search.build.DataProvider;
import com.aifuyun.search.build.IndexBuilder;
import com.aifuyun.search.build.IndexBuilderFactory;
import com.aifuyun.search.core.CoreContainerHolder;
import com.aifuyun.search.util.StringUtil;

public abstract class AbstractIndexServlet extends HttpServlet {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private static final long serialVersionUID = -1576437701958018219L;
	
	private static final String INCR = "incr";
	
	private static final String FULL = "full";

	private IndexBuilderFactory indexBuilderFactory = new IndexBuilderFactory();
	
	protected abstract DataProvider getFullDataProvider(String coreName);
	
	protected abstract DataProvider getIncrDataProvider(String coreName);
	
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("gbk");
		resp.setCharacterEncoding("gbk");
		PrintWriter out = resp.getWriter();
		String coreName = req.getParameter("core");
		String type = req.getParameter("type");
		if (StringUtil.isEmpty(coreName)) {
			out.println("core不能为空");
			out.close();
			return;
		}
		if (StringUtil.isEmpty(type)) {
			out.println("type不能为空");
			out.close();
			return;
		}
		
		CoreContainer cores = CoreContainerHolder.getCoreContainer();
		SolrCore core = cores.getCore(coreName);
		if (core == null) {
			out.println("core不存在！");
			out.close();
			return;
		}
		IndexBuilder indexBuilder = null;
		if (INCR.equals(type)) {
			DataProvider dataProvider = getIncrDataProvider(coreName);
			indexBuilder = indexBuilderFactory.createIncrIndexBuilder(coreName, dataProvider);
		} else if (FULL.equals(type)) {
			DataProvider dataProvider = getFullDataProvider(coreName);
			indexBuilder = indexBuilderFactory.createFullIndexBuilder(coreName, dataProvider);
		} else {
			out.println("type不能必须是incr或是full");
			out.close();
			return;
		}
		final IndexBuilder toBeBuild = indexBuilder;
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				toBeBuild.build();
			}
		});
		log.info(coreName +"调度" + type + "成功.");
		out.println(coreName +"调度" + type + "成功.");
		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
