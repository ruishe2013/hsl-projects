package com.taobao.matrix.eagle.claw.service.impl;

import java.net.URL;

import com.taobao.matrix.eagle.claw.resource.Resource;
import com.taobao.matrix.eagle.claw.resource.impl.NoneResource;
import com.taobao.matrix.eagle.claw.resource.impl.URLResource;
import com.taobao.matrix.eagle.claw.service.ResourceLoaderService;

public class ResourceLoaderServiceImpl implements ResourceLoaderService {

	public Resource loadResource(String name) {
		Class<ResourceLoaderServiceImpl> cls = ResourceLoaderServiceImpl.class;
		URL url = cls.getResource(name);
		if (url == null) {
			url = cls.getClassLoader().getResource(name);
		}
		if (url == null) {
			return new NoneResource(name);
		}
		return new URLResource(url);
	}

}
