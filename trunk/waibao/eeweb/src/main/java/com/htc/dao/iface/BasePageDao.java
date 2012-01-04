package com.htc.dao.iface;

import java.util.Map;

import com.htc.domain.Pager;

/**
 * @ BasePageDao.java
 * 作用 : 用来分页的Dao
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
public interface BasePageDao {
	
	public String getNameSpace();

	public void setNameSpace(String nameSpace);	
	
	/**
	 * @describe: 用于计算页数时候用.
	 * @param pager 分页信息包
	 * @return: 获取查询数据的总记录
	 * @date:2009-11-6
	 */
	public int getCounts(Pager pager);
	
	/**
	 * @describe: 根据表现层传过来的页面和表现层传过来的方法获取特定页面的数据.
	 * 			  在控制层action会调用这个方法.	
	 * @param currentPage 当前页面
	 * @param pagerMethod 翻页的方法. 有前一页,后一页,最前,最后 
	 * @param pageSize 页面显示的最大数量  
	 * @return: Pager 相关的分页信息
	 * @date:2009-11-6
	 */
	public Pager getPager(String currentPage, String pagerMethod,int pageSize);
	
	/**
	 * @describe: 根据表现层传过来的页面和表现层传过来的方法获取特定页面的数据.	
	 * 			  在控制层action会调用这个方法.	
	 * @param currentPage 当前页面
	 * @param pagerMethod 翻页的方法. 有前一页,后一页,最前,最后 
	 * @param pageSize	页面显示的最大数量 
	 * @param args 查询关键字
	 * @return: Pager 相关的分页信息
	 * @date:2009-11-6
	 */
	public Pager getPager(String currentPage, String pagerMethod,int pageSize,Map<String, Object> args); 
	/**
	 * @describe: 根据表现层传过来的页面和表现层传过来的方法获取特定页面的数据.
	 * 			  在控制层action会调用这个方法.		
	 * @param currentPage 当前页面
	 * @param pagerMethod 翻页的方法. 有前一页,后一页,最前,最后 
	 * @param pageSize	页面显示的最大数量 
	 * @param args	查询关键字
	 * @param limitCount 最大查询记录条数
	 * @return:  Pager 相关的分页信息
	 * @date:2009-11-6
	 */
	public Pager getPager(String currentPage, String pagerMethod,int pageSize,Map<String, Object> args,int limitCount); 

}
