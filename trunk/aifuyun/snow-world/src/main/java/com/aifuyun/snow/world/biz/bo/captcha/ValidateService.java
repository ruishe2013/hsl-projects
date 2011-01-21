package com.aifuyun.snow.world.biz.bo.captcha;

import java.io.OutputStream;

/**
 * @author pister
 * 2011-1-21
 */
public interface ValidateService {
	
	boolean check(String sessionId, String value);
	
	void generate(String sessionId, OutputStream os);

}
