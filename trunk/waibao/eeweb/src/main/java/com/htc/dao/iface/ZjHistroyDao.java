package com.htc.dao.iface;

import java.util.List;

import com.htc.bean.BeanForZjHisRec;
import com.htc.domain.ZjHistory;

public interface ZjHistroyDao {
	
	// 查询上传的历史纪录
	public List<ZjHistory> selectZjHisRec(BeanForZjHisRec zjBean) throws Exception;

}
