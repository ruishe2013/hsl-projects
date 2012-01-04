package com.htc.Inteceptor;

import com.htc.action.AbstractAction;
import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * @ LoadInterceptor.java
 * ���� : ������(�û���¼��Ȩ�޿���)
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class LoadInterceptor extends AbstractInterceptor {

	// ���л���
	private static final long serialVersionUID = -5140048909055388142L;

	@Override
	public String intercept(ActionInvocation invocation) {
		try {
			// ����ActionContextʵ��
			AbstractAction action = (AbstractAction) invocation.getAction();
			ActionContext ctx = ActionContext.getContext();
	
			// ��ȡHttpSession�е��û�������
			String userName = (String) ctx.getSession().get(
					AbstractAction.LOGINSESSIONTAG);
			if ((userName == null) || (userName.length() < 0)) {
				return "not_login_yet";
			} else {
				// ��ȡHttpSession�е��û�����Ȩ��
				int userPower = (Integer) ctx.getSession().get(
						AbstractAction.user_POWER_TAG);
				
				//��ȡ ҳ��Ȩ�޼����뵱ǰ�û�Ȩ�ޱȽ�
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
