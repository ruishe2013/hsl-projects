package com.htc.bean;

import java.io.Serializable;
import java.util.Date;

public class BeanForSearchRecord implements Serializable {
	
	private static final long serialVersionUID = 7952035463339056486L;
	
	// 搜索条件字段
	private String placeList;  	  		// 设置仪器主键列表（按逗号隔开）
	private	Date alarmStartFrom;		// 开始时间
	private	Date alarmStartTo;			// 结束时间
	private int stattype;				// 0:即时数据  1:代表日报类型	 2:代表月报类型
	private int orderByType;			// 排序类型: 1: order by recTime, equipmentId  2: order by equipmentId, recTime				
	
	public String getPlaceList() {
		return placeList;
	}
	/**
	 * @describe: 设置需要搜索的 仪器主键列表(按逗号隔开)
	 * @param placeList: 逗号隔开的字符串
	 * @date:2009-11-3
	 */
	public void setPlaceList(String placeList) {
		this.placeList = placeList;
	}
	public Date getAlarmStartFrom() {
		return alarmStartFrom;
	}
	/**
	 * @describe: 设置需要搜索的 开始时间
	 * @param alarmStartFrom: 开始时间
	 * @date:2009-11-3
	 */
	public void setAlarmStartFrom(Date alarmStartFrom) {
		this.alarmStartFrom = alarmStartFrom;
	}
	public Date getAlarmStartTo() {
		return alarmStartTo;
	}
	/**
	 * @describe : 设置需要搜索的 结束时间
	 * @param alarmStartTo:结束时间
	 * @date:2009-11-3
	 */
	public void setAlarmStartTo(Date alarmStartTo) {
		this.alarmStartTo = alarmStartTo;
	}
	/**
	 * @describe: 返回 搜索数据类型: 0: 即时数据 1:代表日报类型	 2:代表月报类型 		
	 * @date:2009-11-3
	 */
	public int getStattype() {
		return stattype;
	}
	/**
	 * @describe : 设置搜索数据类型: 0: 即时数据 1:代表日报类型	 2:代表月报类型 	
	 * @param stattype:  0: 即时数据 1:代表日报类型	 2:代表月报类型
	 * @date:2009-11-3
	 */
	public void setStattype(int stattype) {
		this.stattype = stattype;
	}
	public int getOrderByType() {
		return orderByType;
	}
	/**
	 * @describe: 排序类型: 显示为表格时选择 1; 显示为曲线时选择 2
	 * @param orderType: 1: order by recTime, equipmentId  2: order by equipmentId, recTime
	 * @date:2009-11-3
	 */
	public void setOrderByType(int orderByType) {
		this.orderByType = orderByType;
	}
	
}
