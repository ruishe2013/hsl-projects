package com.taobao.matrix.eagle.claw.compoment.interceptor;

public interface InterceptorChain {
	
	void invokeNext(InterceptorParameter interceptorParameter);

}
