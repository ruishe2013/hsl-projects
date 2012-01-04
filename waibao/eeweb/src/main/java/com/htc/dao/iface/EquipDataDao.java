package com.htc.dao.iface;

import java.util.List;

import com.htc.bean.BeanForEqOrder;
import com.htc.bean.BeanForEqTypeCount;
import com.htc.domain.EquipData;

public interface EquipDataDao {
	
  public List<BeanForEqTypeCount> getEqCountByType() throws Exception; 

  public List<EquipData> findEquipData(EquipData equipData)throws Exception;

  public void insertEquipData(EquipData equipData)throws Exception;
  
  public void insertEquipDataBatch(List<EquipData> equipDataList)throws Exception;

  public void updateEquipData(EquipData equipData)throws Exception;
  
  public void updateEquipDev(EquipData equipData)throws Exception;
  
  public void updateEquiOrder(List<EquipData> equipDatas)throws Exception;
  
  public void deleteEquipData(int equipmentId)throws Exception;
  
  public void deleteBatch(int[] addresss)throws Exception;
  
  public List<EquipData> selcetEquiOrderStr(BeanForEqOrder eqorderBean)throws Exception;
  
}
