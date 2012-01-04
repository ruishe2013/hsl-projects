package com.htc.dao.iface;

import java.util.Map;

import com.htc.domain.Pager;

/**
 * @ BasePageDao.java
 * ���� : ������ҳ��Dao
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
public interface BasePageDao {
	
	public String getNameSpace();

	public void setNameSpace(String nameSpace);	
	
	/**
	 * @describe: ���ڼ���ҳ��ʱ����.
	 * @param pager ��ҳ��Ϣ��
	 * @return: ��ȡ��ѯ���ݵ��ܼ�¼
	 * @date:2009-11-6
	 */
	public int getCounts(Pager pager);
	
	/**
	 * @describe: ���ݱ��ֲ㴫������ҳ��ͱ��ֲ㴫�����ķ�����ȡ�ض�ҳ�������.
	 * 			  �ڿ��Ʋ�action������������.	
	 * @param currentPage ��ǰҳ��
	 * @param pagerMethod ��ҳ�ķ���. ��ǰһҳ,��һҳ,��ǰ,��� 
	 * @param pageSize ҳ����ʾ���������  
	 * @return: Pager ��صķ�ҳ��Ϣ
	 * @date:2009-11-6
	 */
	public Pager getPager(String currentPage, String pagerMethod,int pageSize);
	
	/**
	 * @describe: ���ݱ��ֲ㴫������ҳ��ͱ��ֲ㴫�����ķ�����ȡ�ض�ҳ�������.	
	 * 			  �ڿ��Ʋ�action������������.	
	 * @param currentPage ��ǰҳ��
	 * @param pagerMethod ��ҳ�ķ���. ��ǰһҳ,��һҳ,��ǰ,��� 
	 * @param pageSize	ҳ����ʾ��������� 
	 * @param args ��ѯ�ؼ���
	 * @return: Pager ��صķ�ҳ��Ϣ
	 * @date:2009-11-6
	 */
	public Pager getPager(String currentPage, String pagerMethod,int pageSize,Map<String, Object> args); 
	/**
	 * @describe: ���ݱ��ֲ㴫������ҳ��ͱ��ֲ㴫�����ķ�����ȡ�ض�ҳ�������.
	 * 			  �ڿ��Ʋ�action������������.		
	 * @param currentPage ��ǰҳ��
	 * @param pagerMethod ��ҳ�ķ���. ��ǰһҳ,��һҳ,��ǰ,��� 
	 * @param pageSize	ҳ����ʾ��������� 
	 * @param args	��ѯ�ؼ���
	 * @param limitCount ����ѯ��¼����
	 * @return:  Pager ��صķ�ҳ��Ϣ
	 * @date:2009-11-6
	 */
	public Pager getPager(String currentPage, String pagerMethod,int pageSize,Map<String, Object> args,int limitCount); 

}
