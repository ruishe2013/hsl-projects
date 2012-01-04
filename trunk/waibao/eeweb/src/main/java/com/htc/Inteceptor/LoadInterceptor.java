package com.htc.Inteceptor;

import com.htc.action.AbstractAction;
import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * @ LoadInterceptor.java
 * 作用 : 拦截器(用户登录和权限控制)
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class LoadInterceptor extends AbstractInterceptor {

	// 序列化号
	private static final long serialVersionUID = -5140048909055388142L;

	@Override
	public String intercept(ActionInvocation invocation) {
		try {
			// 创建ActionContext实例
			AbstractAction action = (AbstractAction) invocation.getAction();
			ActionContext ctx = ActionContext.getContext();
	
			// 获取HttpSession中的用户名属性
			String userName = (String) ctx.getSession().get(
					AbstractAction.LOGINSESSIONTAG);
			if ((userName == null) || (userName.length() < 0)) {
				return "not_login_yet";
			} else {
				// 获取HttpSession中的用户名的权限
				int userPower = (Integer) ctx.getSession().get(
						AbstractAction.user_POWER_TAG);
				
				//获取 页面权限级别与当前用户权限比较
				if (userPower < action.getPagerPower()) {   
					return "not_power_yet";
				} else {
						return invocation.invoke();
				}
			}// end if
		} catch (Exception e) {
			e.printStackTrace();
			return "page_err";
		}
		
		// try {
		// } catch (Exception e) {
		// e.printStackTrace();
		// return "not_login_yet";
		//			
		// }
	}

}
