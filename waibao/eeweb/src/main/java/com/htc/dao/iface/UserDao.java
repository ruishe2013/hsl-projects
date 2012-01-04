package com.htc.dao.iface;

import com.htc.domain.User;

public interface UserDao {

  
  public User findUser(User user)throws Exception;
  
  public void insertUser(User user)throws Exception;

  public void updateUser(User user)throws Exception;
  
  public void deleteUser(int userId)throws Exception;
  
  public void deleteBatch(int[] userIds)throws Exception;

}
