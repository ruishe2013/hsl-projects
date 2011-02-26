package com.aifuyun.snow.world.dal.daointerface.user;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;

/**
 * @author pister
 * 2011-1-19
 */
public interface BaseUserDAO {
	
	/**
	 * 创建用户
	 * @param baseUserDO
	 * @return
	 */
	long create(BaseUserDO baseUserDO);
	
	/**
	 * 删除用户，仅未逻辑删除
	 * @param id
	 */
	void delete(long id);
	
	/**
	 * 通过id更新用户的所有字段
	 * @param baseUserDO
	 */
	void update(BaseUserDO baseUserDO);
	
	/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	BaseUserDO queryById(long id);
	
	/**
	 * 通过id查询，忽略删除标记，也就是能查出表中所有的数据
	 * @param nick
	 * @return
	 */
	BaseUserDO queryByIdIgnoreDeletedFlag(long id);
	
	/**
	 * 通过多个id查询，只返回查询到的结果
	 * @param ids
	 * @return
	 */
	List<BaseUserDO> queryByIds(List<Long> ids);
	
	/**
	 * 通过username查询
	 * @param username
	 * @return
	 */
	BaseUserDO queryByUsername(String username);
	
	/**
	 * 通过email查询
	 * @param email
	 * @return
	 */
	BaseUserDO queryByEmail(String email);
	
	/**
	 * 通过username查询，忽略删除标记，也就是能查出表中所有的数据
	 * @param username
	 * @return
	 */
	BaseUserDO queryByUsernameIgnoreDeletedFlag(String username);
	
	/**
	 * 通过email查询，忽略删除标记，也就是能查出表中所有的数据
	 * @param email
	 * @return
	 */
	BaseUserDO queryByEmailIgnoreDeletedFlag(String email);

}
