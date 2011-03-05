package com.aifuyun.snow.world.biz.bo.misc;

import java.util.List;

import javax.servlet.http.Cookie;

public interface CookieBO {
	
	Cookie getCookie(String name);
	
	String getCookieValue(String name);
	
	String getCookieValue(String name, String defaultValue);
	
	Cookie[] getCookies();
	
	void writeCookie(Cookie cookie);
	
	void writeCookies(List<Cookie> cookies);

}
