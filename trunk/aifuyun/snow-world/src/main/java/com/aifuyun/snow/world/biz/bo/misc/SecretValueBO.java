package com.aifuyun.snow.world.biz.bo.misc;

/**
 * 所有的签名密钥
 * @author pister
 *
 */
public interface SecretValueBO {
	
	String getVerifyMailSignKey();
	
	String getServiceMailPassword();
	
	String getServiceMailUsername();
	
}
