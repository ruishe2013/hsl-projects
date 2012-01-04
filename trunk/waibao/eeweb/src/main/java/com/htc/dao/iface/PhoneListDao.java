package com.htc.dao.iface;

import java.util.List;
import com.htc.domain.PhoneList;

public interface PhoneListDao {

	public List<PhoneList> findAllPhoneList() throws Exception;

	public List<PhoneList> findPhoneList(PhoneList phoneList) throws Exception;
	
	public List<PhoneList> findPhoneList(String listIds) throws Exception;

	public void insertPhoneList(PhoneList phoneList) throws Exception;

	public void updatePhoneList(PhoneList phoneList) throws Exception;

	public void deletePhoneList(int phoneListId) throws Exception;

	public void deleteBatch(int[] phoneListIds) throws Exception;

}
