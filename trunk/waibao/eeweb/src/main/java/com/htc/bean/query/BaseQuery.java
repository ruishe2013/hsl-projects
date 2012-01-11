package com.htc.bean.query;

import java.io.Serializable;

public class BaseQuery implements Serializable {

	private static final long serialVersionUID = 4759856292466147745L;

	/**
	 * ҳ���С
	 */
	private int pageSize = 20;
	
	/**
	 * ��ʼֵ
	 */
	private int startRow = 0;
	
	/**
	 * ҳ��
	 */
	private int pageNo = 1;
	
	/**
	 * ��ҳ��
	 */
	private int totalPageCount;

	/**
	 * �ܼ�¼��
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
	 * ���ҳ
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
	 * ��ȡ��ҳ��
	 * @return
	 */
	public int getPagesCount() {
		return getMaxPage();
	}
	
	/**
	 * �Ƿ�����һҳ
	 * @return
	 */
	public boolean hasNextPage() {
		int currentPage = getCurrentPage();
		return currentPage < getMaxPage();
	}
	
	/**
	 * �Ƿ�����һҳ
	 * @return
	 */
	public boolean hasPrevPage() {
		int currentPage = getCurrentPage();
		return currentPage > 1;
	}
	
	/**
	 * ��һҳ
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
	 * ��һҳ
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
	 * ������һҳ
	 * @return �Ƿ���ת�ɹ�
	 */
	public boolean turnNext() {
		if (hasNextPage()) {
			pageNo = getNextPage();
			return true;
		}
		return false;
	}
	
	/**
	 * ������һҳ
	 * @return �Ƿ���ת�ɹ�
	 */
	public boolean turnPrev() {
		if (hasPrevPage()) {
			pageNo = getPrevPage();
			return true;
		}
		return false;
	}
	
	/**
	 * ����ĳһҳ
	 * @param page
	 * @return �Ƿ������ת��pageҳ
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
	 * ��ȡ��ǰ��¼������Χ��1~���ҳ���������䣩
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
