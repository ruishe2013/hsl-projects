package com.taobao.matrix.eagle.claw.configuration;

import java.util.ArrayList;
import java.util.List;

public class DumpRestoreConfig {
	
	private List<ConditionConfig> ConditionConfigs = new ArrayList<ConditionConfig>();

	public List<ConditionConfig> getConditionConfigs() {
		return ConditionConfigs;
	}

	public void setConditionConfigs(List<ConditionConfig> conditionConfigs) {
		ConditionConfigs = conditionConfigs;
	}

}
