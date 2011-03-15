package com.aifuyun.snow.world.biz.bo.feedback.impl;

import com.aifuyun.snow.world.biz.bo.feedback.LeaveWordBO;
import com.aifuyun.snow.world.dal.daointerface.feedback.LeaveWordDAO;
import com.aifuyun.snow.world.dal.dataobject.feedback.LeaveWordDO;

public class LeaveWordBOImpl implements LeaveWordBO {
	
	private LeaveWordDAO leaveWordDAO;

	public int create(LeaveWordDO leaveWord) {
		return leaveWordDAO.create(leaveWord);
	}

	public void setLeaveWordDAO(LeaveWordDAO leaveWordDAO) {
		this.leaveWordDAO = leaveWordDAO;
	}

}
