package com.aifuyun.snow.world.biz.ao.feedback;

import com.aifuyun.snow.world.dal.dataobject.feedback.LeaveWordDO;
import com.zjuh.sweet.result.Result;

public interface LeaveWordAO {
	
	Result createLeaveWord(LeaveWordDO leaveWord);	

}
