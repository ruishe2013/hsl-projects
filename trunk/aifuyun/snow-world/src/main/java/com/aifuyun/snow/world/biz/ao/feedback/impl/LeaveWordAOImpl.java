package com.aifuyun.snow.world.biz.ao.feedback.impl;

import org.hsqldb.lib.StringUtil;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.feedback.LeaveWordAO;
import com.aifuyun.snow.world.biz.bo.feedback.LeaveWordBO;
import com.aifuyun.snow.world.dal.dataobject.feedback.LeaveWordDO;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultSupport;

public class LeaveWordAOImpl extends BaseAO implements LeaveWordAO {

	private LeaveWordBO leaveWordBO;
	
	public Result createLeaveWord(LeaveWordDO leaveWord) {
		Result result = new ResultSupport(false);
		try {
			if (StringUtil.isEmpty(leaveWord.getNick()) && this.isUserLogin()) {
				leaveWord.setNick(this.getLoginUsername() + "|" + this.getLoginUserId());
			}
			leaveWordBO.create(leaveWord);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("¡Ù—‘ ß∞‹", e);
		}
		return result;
	}

	public void setLeaveWordBO(LeaveWordBO leaveWordBO) {
		this.leaveWordBO = leaveWordBO;
	}
	
}
