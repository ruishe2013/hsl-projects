package com.htc.dao.iface;

import java.util.List;
import com.htc.domain.AlarmRec;

public interface AlarmRecDao {

  public List<AlarmRec> selectAll(AlarmRec alarmRec) throws Exception;
  
  public List<AlarmRec> selectUndoRec() throws Exception;
  
  public AlarmRec selectRec(int equiId) throws Exception;
  
  public long selectMaxRec(int equiId) throws Exception;
	
  public void insertAlarm(AlarmRec alarmRec)throws Exception;
  
  public int updateAlarm(AlarmRec alarmRec) throws Exception;
  
  public void updateAlarming(AlarmRec alarmRec) throws Exception;
  
  public void deleteAlarmById(int alarmId) throws Exception;
  
  public void deleteAlarmBatch(int[] alarmIds) throws Exception;

}
