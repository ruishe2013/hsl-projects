package com.htc.dao.iface;

import java.util.List;
import com.htc.domain.Power;

public interface PowerDao {

	public List<Power> findAllPower() throws Exception;

}
