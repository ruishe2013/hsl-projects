package com.htc.bean;

import java.util.*;

import com.htc.domain.EquipData;
import com.htc.domain.GprsSet;
import com.htc.domain.SysParam;
import com.htc.domain.Workplace;

/**
 * @ BeanForEnDeJson.java
 * 作用 : 把[区域,仪器,系统参数,短信格式]封装成json格式.
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-1-28     YANGZHONLI       create
 */
public class BeanForEnDeJson {
	
	// 区域信息
	private int workplaceCount;
	private List<Workplace> workplaceList = new ArrayList<Workplace>();
	// 仪器信息
	private int equipCount;
	private List<EquipData> equipDataList = new ArrayList<EquipData>();
	// 系统信息
	private int systemCount;
	private List<SysParam> sysParamList = new ArrayList<SysParam>();
	// 短信信息
	private int gprsCount;
	private List<GprsSet> gprsSetList = new ArrayList<GprsSet>();
	
	// get,set,add方法
	// 区域信息
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
	// 仪器信息
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
	// 系统信息
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
	// 短信信息
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
