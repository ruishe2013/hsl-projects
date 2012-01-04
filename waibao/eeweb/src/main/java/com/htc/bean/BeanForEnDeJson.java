package com.htc.bean;

import java.util.*;

import com.htc.domain.EquipData;
import com.htc.domain.GprsSet;
import com.htc.domain.SysParam;
import com.htc.domain.Workplace;

/**
 * @ BeanForEnDeJson.java
 * ���� : ��[����,����,ϵͳ����,���Ÿ�ʽ]��װ��json��ʽ.
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-1-28     YANGZHONLI       create
 */
public class BeanForEnDeJson {
	
	// ������Ϣ
	private int workplaceCount;
	private List<Workplace> workplaceList = new ArrayList<Workplace>();
	// ������Ϣ
	private int equipCount;
	private List<EquipData> equipDataList = new ArrayList<EquipData>();
	// ϵͳ��Ϣ
	private int systemCount;
	private List<SysParam> sysParamList = new ArrayList<SysParam>();
	// ������Ϣ
	private int gprsCount;
	private List<GprsSet> gprsSetList = new ArrayList<GprsSet>();
	
	// get,set,add����
	// ������Ϣ
	public List<Workplace> getWorkplaceList() {
		return workplaceList;
	}
	public void setWorkplaceList(List<Workplace> workplaceList) {
		this.workplaceList = workplaceList;
	}
	public void addWorkPlace(Workplace workplace){
		this.workplaceList.add(workplace);
	}
	public int getWorkplaceCount() {
		return workplaceCount;
	}
	public void setWorkplaceCount(int workplaceCount) {
		this.workplaceCount = workplaceCount;
	}
	// ������Ϣ
	public List<EquipData> getEquipDataList() {
		return equipDataList;
	}
	public void setEquipDataList(List<EquipData> equipDataList) {
		this.equipDataList = equipDataList;
	}
	public void addEquipData(EquipData equipData){
		this.equipDataList.add(equipData);
	}	
	public int getEquipCount() {
		return equipCount;
	}
	public void setEquipCount(int equipCount) {
		this.equipCount = equipCount;
	}
	// ϵͳ��Ϣ
	public List<SysParam> getSysParamList() {
		return sysParamList;
	}
	public void setSysParamList(List<SysParam> sysParamList) {
		this.sysParamList = sysParamList;
	}
	public void addSysParam(SysParam sysParam){
		this.sysParamList.add(sysParam);
	}
	public int getSystemCount() {
		return systemCount;
	}
	public void setSystemCount(int systemCount) {
		this.systemCount = systemCount;
	}
	// ������Ϣ
	public List<GprsSet> getGprsSetList() {
		return gprsSetList;
	}
	public void setGprsSetList(List<GprsSet> gprsSetList) {
		this.gprsSetList = gprsSetList;
	}
	public void addGprsSet(GprsSet gprsSet){
		this.gprsSetList.add(gprsSet);
	}
	public int getGprsCount() {
		return gprsCount;
	}
	public void setGprsCount(int gprsCount) {
		this.gprsCount = gprsCount;
	}		
}
