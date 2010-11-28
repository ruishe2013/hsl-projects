package com.taobao.matrix.eagle.claw.service.impl;

import java.util.List;

import com.taobao.matrix.eagle.claw.compoment.db.Table;
import com.taobao.matrix.eagle.claw.compoment.interceptor.Interceptor;
import com.taobao.matrix.eagle.claw.compoment.trigger.Trigger;
import com.taobao.matrix.eagle.claw.service.Dumper;
import com.taobao.matrix.eagle.claw.service.ExceptionHandler;
import com.taobao.matrix.eagle.claw.service.FullDumpRestoreService;
import com.taobao.matrix.eagle.claw.service.IncrDumpRestoreService;
import com.taobao.matrix.eagle.claw.service.RouteDispatcher;
import com.taobao.matrix.eagle.claw.service.ThreadService;

public class DumperImpl implements Dumper {

	private Table table;
	
	private Trigger trigger;
	
	private FullDumpRestoreService fullDumpRestoreService;
	
	private IncrDumpRestoreService incrDumpRestoreService;
	
	private ThreadService threadService;
	
	private List<Interceptor> interceptors;
	
	private ExceptionHandler exceptionHandler;
	
	private RouteDispatcher routeDispatcher;

	public void runFullDump() {
		// TODO Auto-generated method stub
		
	}

	public void runIncrDump() {
		// TODO Auto-generated method stub
		
	}
}
