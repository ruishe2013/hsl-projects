package com.htc.action;

import com.htc.common.CommonDataUnit;

/**
 * @ ComNameAction.java
 * 作用 : 专门用来获取公司名的action
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-5     YANGZHONLI       create
 */
public class ComNameAction extends AbstractAction {

	// json 返回
	private String comName;						// json 输入参数: 被处理的仪器ID
	
	// 构造方法
	public ComNameAction() {
	}
	//注册service -- spring ioc
	
	@Override
	public String execute() {
		comName = commonDataUnit.comName();
		return SUCCESS;
	}
	
	public String getComName() {
		return comName;
	}

}
