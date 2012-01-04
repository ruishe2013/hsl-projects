package com.htc.domain;

import java.io.Serializable;
import java.util.*;

/**
 * 
 * @author : LuZhou
 * @Email : mailluzhou@163.com
 * @version : 1.0
 * @Date : 2009-5-10
 */
public class Pager implements Serializable {

	private static final long serialVersionUID = 2943407041328356292L;
	
	private int totalRows;				// 记录总数
	private int pageSize = 4;			// 每页显示记录数
	private int currentPage = 1;		// 当前页码
	private int totalPages;				// 总页数
	private int startRow;				// 当前页码开始记录数
	@SuppressWarnings("unchecked")
	private List elements;				// 获取的记录列表
	@SuppressWarnings("unused")
	private int pageSizeCurrentPage;	// 记录currentPage与pageSize的乘积
	
	// 在ibatis的sqlmap配置文件里要用到
	//----------模糊属性区---------start
	private String name ; //user
	private int power;//user
	
	private int placeId;//place
	private String mark;//place
	private int type;//place
	
	//时间
	private	Date timeStart;				//开始时间
	private	Date timeEnd;				//结束时间
	
	private String placeList;  	  		//当存在按地点查询时，地点列表（按逗号隔开）
	private String OrderByType;	  		//asc:代表按时间升序	desc按时间降序
	private int whichOrder;		  		//1报警时间 		2复位时间			--	
	
	
	//----------模糊属性区---------end	
	
	public Pager() {

	}

	public Pager(int totalRows, int pageSize) {
		this.pageSize = pageSize;
		pagerEx(totalRows);		
	}
	
	public void pagerEx(int totalRows){
		this.totalRows = totalRows;

		// 更新于2009-6-11 原先为 totalPages=totalRows/pageSize;
		// 解决totalRows为0时，页面出错！如下：
		// -----------------------------------------------
		totalPages = totalRows == 0 ? 1 : totalRows / pageSize;
		// -------------------------------------------

		int mod = totalRows % pageSize;
		if (mod > 0) {
			totalPages++;
		}
		currentPage = 1;
		startRow = 0;		
	}

	public void first() {
		currentPage = 1;
		startRow = 0;
	}

	public void previous() {
		if (currentPage == 1) {
			return;
		}
		currentPage--;
		startRow = (currentPage - 1) * pageSize;
	}

	public void next() {
		if (currentPage < totalPages) {
			currentPage++;
		}
		startRow = (currentPage - 1) * pageSize;
	}

	public void last() {
		currentPage = totalPages;
		startRow = (currentPage - 1) * pageSize;
	}

	public void refresh(int currentPage) {
		this.currentPage = currentPage;
		if (currentPage > totalPages)
			last();
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	/**
	 * @describe: 获取结果列表	
	 * @date:2009-11-6
	 */
	@SuppressWarnings("unchecked")
	public List getElements() {
		return elements;
	}

	@SuppressWarnings("unchecked")
	public void setElements(List elements) {
		this.elements = elements;
	}

	/*
	 * 一个错误，浪费了我大半天的时间，整个分页代码走查了好几遍，挨个又测试了一遍，原来在这出错。
	 * getPageSizeCurrentPage()方法原先写的时候 写顺手了，直接返回pageSizeCurrentPage，
	 * 其实应该返回pageSizecurrentPage，难怪sqlMap里老是得不到数据，郁闷了半天，终于搞定。
	 */
	public int getPageSizeCurrentPage() {
		// return pageSize*currentPage;原先分页语句中的参数，不过会出错。
		return pageSize * (currentPage - 1);
	}

	public void setPageSizeCurrentPage() {
		// this.pageSizeCurrentPage=pageSize*currentPage;
		this.pageSizeCurrentPage = pageSize * (currentPage - 1);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getPlaceId() {
		return placeId;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getPlaceList() {
		return placeList;
	}

	public void setPlaceList(String placeList) {
		this.placeList = placeList;
	}

	public String getOrderByType() {
		return OrderByType;
	}

	public void setOrderByType(String orderByType) {
		OrderByType = orderByType;
	}

	public int getWhichOrder() {
		return whichOrder;
	}

	public void setWhichOrder(int whichOrder) {
		this.whichOrder = whichOrder;
	}

	public void setPageSizeCurrentPage(int pageSizeCurrentPage) {
		this.pageSizeCurrentPage = pageSizeCurrentPage;
	}

}
