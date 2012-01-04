package com.htc.dao.iface;

import com.htc.domain.TLog;

public interface TLogDao {

  public void insertLog(TLog tlog)throws Exception;
  
  public void deleteLogById(int id) throws Exception;
  
  public void deleteLogBatch(int[] ids) throws Exception;

}
