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
	
	private int totalRows;				// ��¼����
	private int pageSize = 4;			// ÿҳ��ʾ��¼��
	private int currentPage = 1;		// ��ǰҳ��
	private int totalPages;				// ��ҳ��
	private int startRow;				// ��ǰҳ�뿪ʼ��¼��
	@SuppressWarnings("unchecked")
	private List elements;				// ��ȡ�ļ�¼�б�
	@SuppressWarnings("unused")
	private int pageSizeCurrentPage;	// ��¼currentPage��pageSize�ĳ˻�
	
	// ��ibatis��sqlmap�����ļ���Ҫ�õ�
	//----------ģ��������---------start
	private String name ; //user
	private int power;//user
	
	private int placeId;//place
	private String mark;//place
	private int type;//place
	
	//ʱ��
	private	Date timeStart;				//��ʼʱ��
	private	Date timeEnd;				//����ʱ��
	
	private String placeList;  	  		//�����ڰ��ص��ѯʱ���ص��б������Ÿ�����
	private String OrderByType;	  		//asc:����ʱ������	desc��ʱ�併��
	private int whichOrder;		  		//1����ʱ�� 		2��λʱ��			--	
	
	
	//----------ģ��������---------end	
	
	public Pager() {

	}

	public Pager(int totalRows, int pageSize) {
		this.pageSize = pageSize;
		pagerEx(totalRows);		
	}
	
	public void pagerEx(int totalRows){
		this.totalRows = totalRows;

		// ������2009-6-11 ԭ��Ϊ totalPages=totalRows/pageSize;
		// ���totalRowsΪ0ʱ��ҳ��������£�
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
	 * @describe: ��ȡ����б�	
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
	 * һ�������˷����Ҵ�����ʱ�䣬������ҳ�����߲��˺ü��飬�����ֲ�����һ�飬ԭ���������
	 * getPageSizeCurrentPage()����ԭ��д��ʱ�� д˳���ˣ�ֱ�ӷ���pageSizeCurrentPage��
	 * ��ʵӦ�÷���pageSizecurrentPage���ѹ�sqlMap�����ǵò������ݣ������˰��죬���ڸ㶨��
	 */
	public int getPageSizeCurrentPage() {
		// return pageSize*currentPage;ԭ�ȷ�ҳ����еĲ��������������
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
