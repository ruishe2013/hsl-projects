package com.taobao.matrix.eagle.claw.resource.impl;

import java.io.InputStream;
import java.net.URL;

import com.taobao.matrix.eagle.claw.resource.Resource;
import com.taobao.matrix.eagle.claw.resource.ResourceNotFoundException;

public class NoneResource implements Resource {

	private String name;
	
	public NoneResource(String name) {
		super();
		this.name = name;
	}

	public InputStream asInputStream() {
		throw createException();
	}

	public URL asURL() {
		throw createException();
	}
	
	private ResourceNotFoundException createException() {
		return new ResourceNotFoundException("can not found the resouce:" + name);
	}

}
