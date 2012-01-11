package com.htc.bean.query;

import java.io.Serializable;

public class BaseQuery implements Serializable {

	private static final long serialVersionUID = 4759856292466147745L;

	/**
	 * 页面大小
	 */
	private int pageSize = 20;
	
	/**
	 * 起始值
	 */
	private int startRow = 0;
	
	/**
	 * 页码
	 */
	private int pageNo = 1;
	
	/**
	 * 分页数
	 */
	private int totalPageCount;

	/**
	 * 总记录数
	 */
	private int totalResultCount;
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize <= 1) {
			pageSize = 1;
		}
		startRow = (pageNo - 1) * pageSize;
		this.pageSize = pageSize;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		if (pageNo <= 1) {
			pageNo = 1;
		}
		startRow = (pageNo - 1) * pageSize;
		this.pageNo = pageNo;
	}
	

	/**
	 * 最大页
	 * @return
	 */
	public int getMaxPage() {
		if (totalResultCount <= 1) {
			return 1;
		}
		if (pageSize <= 1) {
			return totalResultCount;
		}
		return (totalResultCount + pageSize - 1) / pageSize;
	}
	
	/**
	 * 获取分页数
	 * @return
	 */
	public int getPagesCount() {
		return getMaxPage();
	}
	
	/**
	 * 是否还有下一页
	 * @return
	 */
	public boolean hasNextPage() {
		int currentPage = getCurrentPage();
		return currentPage < getMaxPage();
	}
	
	/**
	 * 是否还有上一页
	 * @return
	 */
	public boolean hasPrevPage() {
		int currentPage = getCurrentPage();
		return currentPage > 1;
	}
	
	/**
	 * 下一页
	 * @return
	 */
	public int getNextPage() {
		int currentPage = getCurrentPage();
		if (hasNextPage()) {
			return (currentPage + 1);
		}
		return currentPage;
	}
	
	/**
	 * 上一页
	 * @return
	 */
	public int getPrevPage() {
		int currentPage = getCurrentPage();
		if (hasPrevPage()) {
			return currentPage - 1;
		}
		return currentPage;
	}
	
	/**
	 * 跳到下一页
	 * @return 是否跳转成功
	 */
	public boolean turnNext() {
		if (hasNextPage()) {
			pageNo = getNextPage();
			return true;
		}
		return false;
	}
	
	/**
	 * 跳到上一页
	 * @return 是否跳转成功
	 */
	public boolean turnPrev() {
		if (hasPrevPage()) {
			pageNo = getPrevPage();
			return true;
		}
		return false;
	}
	
	/**
	 * 跳到某一页
	 * @param page
	 * @return 是否真的跳转到page页
	 */
	public boolean turn(int page) {
		int maxPage = getMaxPage();
		if (page > maxPage) {
			pageNo = maxPage;
			return false;
		}
		if (page < 1) {
			pageNo = 1;
			return false;
		}
		pageNo = page;
		return true;
	}
	
	/**
	 * 获取当前记录数，范围在1~最大页数（闭区间）
	 * @return
	 */
	public int getCurrentPage() {
		int maxPage = getMaxPage();
		if (pageNo > maxPage) {
			return maxPage;
		}
		if (maxPage < 1) {
			return 1;
		}
		return pageNo;
	}
	public int getTotalPageCount() {
		return totalPageCount;
	}

	public int getTotalResultCount() {
		return totalResultCount;
	}

	public void setTotalResultCount(int totalResultCount) {
		this.totalResultCount = totalResultCount;
		totalPageCount = (totalResultCount + pageSize - 1) / pageSize + 1;
		if (pageNo > totalPageCount) {
			pageNo = totalPageCount;
		}
	}

	
}
