package com.taobao.matrix.eagle.claw.resource;

import java.io.InputStream;
import java.net.URL;

public interface Resource {
	
	InputStream asInputStream();
	
	URL asURL();
	
}
