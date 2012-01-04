package com.htc.model;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.dao.iface.*;
import com.htc.domain.*;

/**
 * @ UserService.java
 * ���� : �û�����
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-5     YANGZHONLI       create
 */
public class UserService {

	private Log log = LogFactory.getLog(UserService.class);
	
	private UserDao userDao;
	private PowerDao powerDao;
	private BasePageDao basePageDao;// ������ҳdao
	//private DaoManager daoMgr = DaoConfig.getDaoManager();

	// ���췽��
	public UserService() {
	}
	//ע��service -- spring ioc
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public void setPowerDao(PowerDao powerDao) {
		this.powerDao = powerDao;
	}
	public void setBasePageDao(BasePageDao basePageDao) {
		this.basePageDao = basePageDao;
	}

	public User getUserById(int userId){
		
		// ʵ����������
		//userDao = userDao == null ? (UserDao) daoMgr.getDao(UserDao.class) : userDao;
		
		User user = new User();
		user.setUserId(userId);
		try {
			user = userDao.findUser(user);
		} catch (Exception e) {
			log.error("��ȡ�û�-byID ʱ����:" + e.getMessage()+e.toString());
		}
		return user;
	}

	public User getUserByName(String username) {
		// ʵ����������
		//userDao = userDao == null ? (UserDao) daoMgr.getDao(UserDao.class) : userDao;		
		User user = new User();
		user.setName(username);
		try {
			user = userDao.findUser(user);
		} catch (Exception e) {
			log.error("��ȡ�û�-byName ʱ����:" + e.getMessage()+e.toString());
		}
		return user;
	}

	public User getUserByName(String username, String password){
		
		// ʵ����������
		//userDao = userDao == null ? (UserDao) daoMgr.getDao(UserDao.class) : userDao;		
		
		User user = new User();
		user.setName(username);
		user.setPassword(password);
		try {
			user  = userDao.findUser(user);
		} catch (Exception e) {
			log.error("��ȡ�û�-byNamePass ʱ����:" + e.getMessage()+e.toString());
		}
		return user;
	}

	public boolean insertUser(User user){
		boolean rsbool = true;
		if (getUserByName(user.getName()) != null) {
			rsbool = false;
		} else {
			// ʵ����������
			//userDao = userDao == null ? (UserDao) daoMgr.getDao(UserDao.class) : userDao;
			try {
				userDao.insertUser(user);
			} catch (Exception e) {
				rsbool = false;
				log.error("�����û� ʱ����:" + e.getMessage()+e.toString());
			}
		}
		return rsbool;
	}

	public boolean updateUser(User user) {
		boolean rsbool = true;
		User userTemp = getUserByName(user.getName());
		if ( (user.getUserId() != 0) &&
			 (userTemp!= null)&& 
			 (userTemp.getUserId()!= user.getUserId()) ){
			rsbool = false;
		}else{
			// ʵ����������
			//userDao = userDao == null ? (UserDao) daoMgr.getDao(UserDao.class) : userDao;
			try {
				userDao.updateUser(user);
			} catch (Exception e) {
				rsbool = false;
				log.error("�޸��û� ʱ����:" + e.getMessage()+e.toString());
			}
		}
		return rsbool;
	}

	public void deleteUser(int userId) {
		// ʵ����������
		//userDao = userDao == null ? (UserDao) daoMgr.getDao(UserDao.class) : userDao;
		
		try {
			userDao.deleteUser(userId);
		} catch (Exception e) {
			log.error("ɾ���û� ʱ����:" + e.getMessage()+e.toString());
		}
	}

	public void deleteBatch(int[] userIds) {
		// ʵ����������
		//userDao = userDao == null ? (UserDao) daoMgr.getDao(UserDao.class) : userDao;
		try {
			userDao.deleteBatch(userIds);
		} catch (Exception e) {
			log.error("����ɾ���û� ʱ����:" + e.getMessage()+e.toString());
		}
	}
	
	// power
	public List<Power> getAllPowers() {
		// ʵ����������
		//powerDao = powerDao == null? (PowerDao) daoMgr.getDao(PowerDao.class) : powerDao;  
		List<Power> userpowerList = new ArrayList<Power>();
		try {
			userpowerList = powerDao.findAllPower();
		} catch (Exception e) {
			log.error("��ȡ�û�Ȩ���б� ʱ����:" + e.getMessage()+e.toString());
		}
		return userpowerList;
	}	
	
	public Map<Integer, String> getAllPower() {
		List<Power> powerList;
		Map<Integer, String> powerMap = new HashMap<Integer, String>();

		powerList = getAllPowers();
		for (Power power : powerList) {
			powerMap.put(power.getPowerId(), power.getPowerName());
		}
		return powerMap;
	}	

	/**
	 * @describe : ��ҳ-��ȡһ�μ�¼
	 * @param nameSpace : �ռ���. ȡUser.xml�У�sqlMap namespace="User" ��ֵ
	 * @param currentPage : ��ǰҳ
	 * @param pagerMethod : ��ҳ�ķ���. ��ǰһҳ,��һҳ,��ǰ,��� 
	 * @param pageSize : ҳ����ʾ���������
	 * @param args : ��Ҫ���������ǵĲ���. ������Ҫ����,xml��page��
	 * @param limitCount : ��������Ŀ��Է��ص������ֵ
	 * @return Pager ��صķ�ҳ��Ϣ
	 * @date:2009-11-4
	 */
	public Pager getPager(String currentPage, String pagerMethod,int pageSize,Map<String, Object> args) {
		// userΪ�ռ��� ��ȡUser.xml�У�<sqlMap namespace="User">��ֵ
		
		// ʵ����������
		//basePageDao = basePageDao == null ?(BasePageDao) daoMgr.getDao(BasePageDao.class) :  basePageDao;		
		
		this.basePageDao.setNameSpace("User");
		return this.basePageDao.getPager(currentPage, pagerMethod, pageSize,args);
	}

}
