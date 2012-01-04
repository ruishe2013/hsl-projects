package com.htc.dao.impl;

import com.htc.dao.iface.BasePageDao;
import com.htc.domain.Pager;

import java.util.*;

/**
 * @author : LuZhou
 * @E-mail : mailluzhou@163.com
 * @version : 1.0
 * @Date : 2009-5-10
 */
public class BasePageSqlMapDao extends BaseSqlMapDao implements BasePageDao {
	// public class BasePageDao extends SqlMapClientDaoSupport{

	private String nameSpace;

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		if ((nameSpace != null) && (nameSpace.length() > 0)) {
			this.nameSpace = nameSpace + ".";
		} else {
			this.nameSpace = "";
		}
	}

	// protected SqlMapClientTemplate smcTemplate =
	// this.getSqlMapClientTemplate();
	public BasePageSqlMapDao() {
	}
	
//	public BasePageSqlMapDao(DaoManager daoManager) {
//		super(daoManager);
//	}

	// 获取查询数据的总记录，用于计算页数时用到
	public int getCounts(Pager pager) {
		return (Integer) getSqlMapClientTemplate().queryForObject(nameSpace + "getCounts", pager);
		// return ((Integer)
		// smcTemplate.queryForObject("getCounts")).intValue();--spring
	}

	public Pager getPager(String currentPage, String pagerMethod, int pageSize) {
		return this.getPager(currentPage, pagerMethod, pageSize, null, 0);
	}

	public Pager getPager(String currentPage, String pagerMethod, int pageSize,
			Map<String, Object> args) {
		return this.getPager(currentPage, pagerMethod, pageSize, args, 0);
	}

	// 根据表现层传过来的页面和表现层传过来的方法获取特定页面的数据，在控制层action会调用这个方法，
	public Pager getPager(String currentPage, String pagerMethod, int pageSize,
			Map<String, Object> args, int limitCount) {
		int totalRows = 0;
		Pager pager;

		pager = new Pager(totalRows, pageSize);

		// 设置模糊属性
		if (args != null) {
			fuzzySet(pager, args);
		}

		totalRows = this.getCounts(pager);// 先获取总记录数，也是用sql语句获取。

		// 当设置limitCount时，根据这个数据限制数据总数
		if (limitCount != 0) {
			totalRows = (limitCount > totalRows) ? totalRows : limitCount;
			if (totalRows < pageSize){
				pager.setPageSize(totalRows == 0 ? 1 : totalRows);
			}
		}
		pager.pagerEx(totalRows);

		if (currentPage != null) {
			pager.refresh(Integer.parseInt(currentPage));
		}
		if (pagerMethod != null) {
			if (pagerMethod.equals("first")) {
				pager.first();
			} else if (pagerMethod.equals("previous")) {
				pager.previous();
			} else if (pagerMethod.equals("next")) {
				pager.next();
			} else if (pagerMethod.equals("last")) {
				pager.last();
			}
		}

		/*
		 * 通过sqlmap查询特定数据，"getListByPager"需要传给他两个参数，就是最开始讲的mssql2000分页sql语句用到得两个参数
		 * pagesize和cureentpage，这两个参数都封装在pager类里，因为sql语句中要得到pagesize和cureentpage的乘积，
		 * 在配置文件中直接获取不到，所以我将pagesize和cureentpage的乘积存放到另一个参数里 ，在pager类里能看到，就是pageSizeCurrentPage。
		 */
		// items = (ArrayList) smcTemplate.queryForList("getListByPager",pager);--spring
		// items = (ArrayList) queryForList(nameSpace + "getListByPager", pager);
		// 将获取的列表赋给pager的elements，然后返回给前台。
		pager.setElements(getSqlMapClientTemplate().queryForList(nameSpace + "getListByPager", pager));
		return pager;

	}

	/**
	 * @describe: 设置模糊属性
	 * @param pager
	 * @param args
	 *            : 属性map(String, Object)
	 * @date:2009-11-6
	 */
	 
	private void fuzzySet(Pager pager, Map<String, Object> args) {
		Set<String> elements = args.keySet();
		Iterator<String> iterator = elements.iterator();
		String tagStr = ""; // 属性识别符

		while (iterator.hasNext()) {
			tagStr = iterator.next();
			if (tagStr.equals("name")) {
				pager.setName((String) args.get("name"));
			} else if (tagStr.equals("power")) {
				pager.setPower((Integer) args.get("power"));
			} else if (tagStr.equals("type")) {
				pager.setType((Integer) args.get("type"));
			} else if (tagStr.equals("placeId")) {
				pager.setPlaceId((Integer) args.get("placeId"));
			} else if (tagStr.equals("mark")) {
				pager.setMark((String) args.get("mark"));
			} else if (tagStr.equals("timeStart")) {
				pager.setTimeStart((Date) args.get("timeStart"));
			} else if (tagStr.equals("timeEnd")) {
				pager.setTimeEnd((Date) args.get("timeEnd"));
			} else if (tagStr.equals("placeList")) {
				pager.setPlaceList((String) args.get("placeList"));
			} else if (tagStr.equals("OrderByType")) {
				pager.setOrderByType((String) args.get("OrderByType"));
			} else if (tagStr.equals("whichOrder")) {
				pager.setWhichOrder((Integer) args.get("whichOrder"));
			}
		}

	}

}
