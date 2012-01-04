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

	// ��ȡ��ѯ���ݵ��ܼ�¼�����ڼ���ҳ��ʱ�õ�
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

	// ���ݱ��ֲ㴫������ҳ��ͱ��ֲ㴫�����ķ�����ȡ�ض�ҳ������ݣ��ڿ��Ʋ�action��������������
	public Pager getPager(String currentPage, String pagerMethod, int pageSize,
			Map<String, Object> args, int limitCount) {
		int totalRows = 0;
		Pager pager;

		pager = new Pager(totalRows, pageSize);

		// ����ģ������
		if (args != null) {
			fuzzySet(pager, args);
		}

		totalRows = this.getCounts(pager);// �Ȼ�ȡ�ܼ�¼����Ҳ����sql����ȡ��

		// ������limitCountʱ�������������������������
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
		 * ͨ��sqlmap��ѯ�ض����ݣ�"getListByPager"��Ҫ���������������������ʼ����mssql2000��ҳsql����õ�����������
		 * pagesize��cureentpage����������������װ��pager�����Ϊsql�����Ҫ�õ�pagesize��cureentpage�ĳ˻���
		 * �������ļ���ֱ�ӻ�ȡ�����������ҽ�pagesize��cureentpage�ĳ˻���ŵ���һ�������� ����pager�����ܿ���������pageSizeCurrentPage��
		 */
		// items = (ArrayList) smcTemplate.queryForList("getListByPager",pager);--spring
		// items = (ArrayList) queryForList(nameSpace + "getListByPager", pager);
		// ����ȡ���б���pager��elements��Ȼ�󷵻ظ�ǰ̨��
		pager.setElements(getSqlMapClientTemplate().queryForList(nameSpace + "getListByPager", pager));
		return pager;

	}

	/**
	 * @describe: ����ģ������
	 * @param pager
	 * @param args
	 *            : ����map(String, Object)
	 * @date:2009-11-6
	 */
	 
	private void fuzzySet(Pager pager, Map<String, Object> args) {
		Set<String> elements = args.keySet();
		Iterator<String> iterator = elements.iterator();
		String tagStr = ""; // ����ʶ���

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
