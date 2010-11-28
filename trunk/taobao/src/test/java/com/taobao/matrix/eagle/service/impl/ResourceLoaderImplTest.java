package com.taobao.matrix.eagle.service.impl;

import java.io.InputStream;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.taobao.matrix.eagle.claw.resource.Resource;
import com.taobao.matrix.eagle.claw.service.ResourceLoaderService;
import com.taobao.matrix.eagle.claw.service.impl.ResourceLoaderServiceImpl;

public class ResourceLoaderImplTest extends TestCase {

	private ResourceLoaderService resourceLoader = new ResourceLoaderServiceImpl();

	public void testLoaderResource() {
		Resource resource = resourceLoader.loadResource("testResourceFile.txt");
		InputStream is = resource.asInputStream();
		Assert.assertNotNull(is);
	}

}
