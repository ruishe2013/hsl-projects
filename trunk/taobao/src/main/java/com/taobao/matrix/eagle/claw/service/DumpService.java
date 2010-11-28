package com.taobao.matrix.eagle.claw.service;

import java.util.List;

import com.taobao.matrix.eagle.claw.configuration.Configuration;

public interface DumpService {
	
	void init(Configuration configuration);
	
	List<Dumper> getDumpers();
	
	Dumper getDumper(String name);
	
}
