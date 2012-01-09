package com.htc.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ResourceJettyHackerFilter implements Filter {

	@Override
	public void destroy() {
		
	}

	private ServletContext servletContext;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		String r = req.getRequestURI();
		InputStream is = servletContext.getResourceAsStream(r);
		if (is != null) {
			OutputStream os = response.getOutputStream();
			IoUtil.copy(is, os);
			IoUtil.close(os);
			IoUtil.close(is);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		servletContext = filterConfig.getServletContext();
	}

}
