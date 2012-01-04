package com.htc.model;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.dao.iface.*;
import com.htc.domain.*;

/**
 * @ UserService.java
 * 作用 : 用户管理
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-5     YANGZHONLI       create
 */
public class UserService {

	private Log log = LogFactory.getLog(UserService.class);
	
	private UserDao userDao;
	private PowerDao powerDao;
	private BasePageDao basePageDao;// 公共分页dao
	//private DaoManager daoMgr = DaoConfig.getDaoManager();

	// 构造方法
	public UserService() {
	}
	//注册service -- spring ioc
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
		
		// 实例化服务类
		//userDao = userDao == null ? (UserDao) daoMgr.getDao(UserDao.class) : userDao;
		
		User user = new User();
		user.setUserId(userId);
		try {
			user = userDao.findUser(user);
		} catch (Exception e) {
			log.error("获取用户-byID 时出错:" + e.getMessage()+e.toString());
		}
		return user;
	}

	public User getUserByName(String username) {
		// 实例化服务类
		//userDao = userDao == null ? (UserDao) daoMgr.getDao(UserDao.class) : userDao;		
		User user = new User();
		user.setName(username);
		try {
			user = userDao.findUser(user);
		} catch (Exception e) {
			log.error("获取用户-byName 时出错:" + e.getMessage()+e.toString());
		}
		return user;
	}

	public User getUserByName(String username, String password){
		
		// 实例化服务类
		//userDao = userDao == null ? (UserDao) daoMgr.getDao(UserDao.class) : userDao;		
		
		User user = new User();
		user.setName(username);
		user.setPassword(password);
		try {
			user  = userDao.findUser(user);
		} catch (Exception e) {
			log.error("获取用户-byNamePass 时出错:" + e.getMessage()+e.toString());
		}
		return user;
	}

	public boolean insertUser(User user){
		boolean rsbool = true;
		if (getUserByName(user.getName()) != null) {
			rsbool = false;
		} else {
			// 实例化服务类
			//userDao = userDao == null ? (UserDao) daoMgr.getDao(UserDao.class) : userDao;
			try {
				userDao.insertUser(user);
			} catch (Exception e) {
				rsbool = false;
				log.error("增加用户 时出错:" + e.getMessage()+e.toString());
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
			// 实例化服务类
			//userDao = userDao == null ? (UserDao) daoMgr.getDao(UserDao.class) : userDao;
			try {
				userDao.updateUser(user);
			} catch (Exception e) {
				rsbool = false;
				log.error("修改用户 时出错:" + e.getMessage()+e.toString());
			}
		}
		return rsbool;
	}

	public void deleteUser(int userId) {
		// 实例化服务类
		//userDao = userDao == null ? (UserDao) daoMgr.getDao(UserDao.class) : userDao;
		
		try {
			userDao.deleteUser(userId);
		} catch (Exception e) {
			log.error("删除用户 时出错:" + e.getMessage()+e.toString());
		}
	}

	public void deleteBatch(int[] userIds) {
		// 实例化服务类
		//userDao = userDao == null ? (UserDao) daoMgr.getDao(UserDao.class) : userDao;
		try {
			userDao.deleteBatch(userIds);
		} catch (Exception e) {
			log.error("批量删除用户 时出错:" + e.getMessage()+e.toString());
		}
	}
	
	// power
	public List<Power> getAllPowers() {
		// 实例化服务类
		//powerDao = powerDao == null? (PowerDao) daoMgr.getDao(PowerDao.class) : powerDao;  
		List<Power> userpowerList = new ArrayList<Power>();
		try {
			userpowerList = powerDao.findAllPower();
		} catch (Exception e) {
			log.error("获取用户权限列表 时出错:" + e.getMessage()+e.toString());
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
	 * @describe : 分页-获取一段记录
	 * @param nameSpace : 空间名. 取User.xml中，sqlMap namespace="User" 的值
	 * @param currentPage : 当前页
	 * @param pagerMethod : 翻页的方法. 有前一页,后一页,最前,最后 
	 * @param pageSize : 页面显示的最大数量
	 * @param args : 需要条件搜索是的参数. 参数需要对照,xml和page类
	 * @param limitCount : 搜索结果的可以返回的最大数值
	 * @return Pager 相关的分页信息
	 * @date:2009-11-4
	 */
	public Pager getPager(String currentPage, String pagerMethod,int pageSize,Map<String, Object> args) {
		// user为空间名 ：取User.xml中，<sqlMap namespace="User">的值
		
		// 实例化服务类
		//basePageDao = basePageDao == null ?(BasePageDao) daoMgr.getDao(BasePageDao.class) :  basePageDao;		
		
		this.basePageDao.setNameSpace("User");
		return this.basePageDao.getPager(currentPage, pagerMethod, pageSize,args);
	}

}
