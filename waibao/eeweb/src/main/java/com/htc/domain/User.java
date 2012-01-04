package com.htc.domain;

import java.io.Serializable;

public class User implements Serializable {
	
	private static final long serialVersionUID = -1853419149995204092L;
	
	//���ݿ���б��Ա
	private int userId;
	private String name;
	private String password;
	private int power;			// 0:������(ֻ��htc_htc_htc��һ���˺�) 1: ����Ա 2: �߼�����Ա
	private int useless;		// 1:��ʾ����û�����ʹ�� 0:��ʾ����˻��Ѿ���ɾ����
	private String placeAStr;	// ���������û��б�
	private String placeBStr;	// ʵʱ�����û��б�
	private String personStr;	// �û�������������(��ʱû���õ�)
	private int updateFlag =0;	// 0:�޸�һ����Ϣ(����,����,Ȩ��) 1:�޸�placeAStr	2:�޸�placeBStr 
	
	//--������Ա--Ȩ����
	private String powerStr;   
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public int getUseless() {
		return useless;
	}
	public void setUseless(int useless) {
		this.useless = useless;
	}
	public String getPlaceAStr() {
		return placeAStr;
	}
	public void setPlaceAStr(String placeAStr) {
		this.placeAStr = placeAStr;
	}
	public String getPlaceBStr() {
		return placeBStr;
	}
	public void setPlaceBStr(String placeBStr) {
		this.placeBStr = placeBStr;
	}
	public String getPersonStr() {
		return personStr;
	}
	public void setPersonStr(String personStr) {
		this.personStr = personStr;
	}
	public String getPowerStr() {
		return powerStr;
	}
	public void setPowerStr(String powerStr) {
		this.powerStr = powerStr;
	}
	public int getUpdateFlag() {
		return updateFlag;
	}
	public void setUpdateFlag(int updateFlag) {
		this.updateFlag = updateFlag;
	}
}
