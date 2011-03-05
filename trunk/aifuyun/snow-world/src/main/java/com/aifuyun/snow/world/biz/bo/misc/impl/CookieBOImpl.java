package com.aifuyun.snow.world.biz.bo.misc.impl;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.aifuyun.snow.world.biz.bo.misc.CookieBO;
import com.zjuh.splist.core.SplistContext;
import com.zjuh.sweet.lang.StringUtil;

public class CookieBOImpl implements CookieBO {
	
	public String getCookieValue(String name, String defaultValue) {
		Cookie ck = getCookie(name);
		if (ck == null) {
			return defaultValue;
		}
		String value = ck.getValue();
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	@Override
	public Cookie getCookie(String name) {
		Cookie[] cookies = SplistContext.getRequest().getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie ck : cookies) {
			if (ck == null) {
				continue;
			}
			if (StringUtil.equals(name, ck.getName())) {
				return ck;
			}
		}
		return null;
	}

	@Override
	public String getCookieValue(String name) {
		return getCookieValue(name, null);
	}

	@Override
	public Cookie[] getCookies() {
		return SplistContext.getRequest().getCookies();
	}

	@Override
	public void writeCookie(Cookie cookie) {
		SplistContext.getResponse().addCookie(cookie);
	}

	@Override
	public void writeCookies(List<Cookie> cookies) {
		HttpServletResponse response = SplistContext.getResponse();
		for (Cookie cookie : cookies) {
			response.addCookie(cookie);
		}
	}

}
