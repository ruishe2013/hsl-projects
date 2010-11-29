package com.taobao.matrix.eagle.claw.resource.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.taobao.matrix.eagle.claw.ClawException;
import com.taobao.matrix.eagle.claw.resource.Resource;

public class URLResource implements Resource {

	private URL url;
	
	public URLResource(URL url) {
		super();
		this.url = url;
	}

	public InputStream asInputStream() {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new ClawException(e);
		}
	}

	public URL asURL() {
		return url;
	}

}
