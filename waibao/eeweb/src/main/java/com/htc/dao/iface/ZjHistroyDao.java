package com.htc.dao.iface;

import java.util.List;

import com.htc.bean.BeanForZjHisRec;
import com.htc.domain.ZjHistory;

public interface ZjHistroyDao {
	
	// ��ѯ�ϴ�����ʷ��¼
	public List<ZjHistory> selectZjHisRec(BeanForZjHisRec zjBean) throws Exception;

}
