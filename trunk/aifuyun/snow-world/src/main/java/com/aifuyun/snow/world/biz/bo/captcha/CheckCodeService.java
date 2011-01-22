package com.aifuyun.snow.world.biz.bo.captcha;

import java.io.OutputStream;

/**
 * @author pister
 * 2011-1-21
 */
public interface CheckCodeService {
	
	boolean check(String value);
	
	boolean check(String sessionId, String value);
	
	void generateNext(String sessionId, OutputStream os);

}
