package com.taobao.matrix.eagle.claw.configuration;

import java.util.List;

public class DumperConfig {

	private TableConfig table;
	
	private TrigerConfig triger;
	
	private DumpRestoreConfig fullDumpInitRestore;
	
	private DumpRestoreConfig incrDumpInitRestore;
	
	private TypedAttributes thread;
	
	private List<TypedAttributes> interceptors;
	
	private TypedAttributes exceptionHandler;
	
	private DispatcherConfig dispatcher;

	public TableConfig getTable() {
		return table;
	}

	public void setTable(TableConfig table) {
		this.table = table;
	}

	public TrigerConfig getTriger() {
		return triger;
	}

	public void setTriger(TrigerConfig triger) {
		this.triger = triger;
	}

	public DumpRestoreConfig getFullDumpInitRestore() {
		return fullDumpInitRestore;
	}

	public void setFullDumpInitRestore(DumpRestoreConfig fullDumpInitRestore) {
		this.fullDumpInitRestore = fullDumpInitRestore;
	}

	public DumpRestoreConfig getIncrDumpInitRestore() {
		return incrDumpInitRestore;
	}

	public void setIncrDumpInitRestore(DumpRestoreConfig incrDumpInitRestore) {
		this.incrDumpInitRestore = incrDumpInitRestore;
	}

	public TypedAttributes getThread() {
		return thread;
	}

	public void setThread(TypedAttributes thread) {
		this.thread = thread;
	}

	public List<TypedAttributes> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(List<TypedAttributes> interceptors) {
		this.interceptors = interceptors;
	}

	public TypedAttributes getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(TypedAttributes exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public DispatcherConfig getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(DispatcherConfig dispatcher) {
		this.dispatcher = dispatcher;
	}
	
}
