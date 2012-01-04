package com.htc.bean;

import java.io.Serializable;

/**
 * @ BeanForOnlineWarn.java
 * ���� : ���߱������ݵ�Ԫ
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-23     YANGZHONLI       create
 */
public class BeanForOnlineWarn implements Serializable {

	private static final long serialVersionUID = 758868339298401504L;
	
	/**
	 * ����Id
	 */
	private int deqid;
	/**
	 * �¶�[�¶�����+��/F+��������]:�磺15.58����±��� 
	 */
	private String dtemp;
	/**
	 * ʪ��[ʪ������+%+��������]�磺100.58%��ʪ����
	 */
	private String dhumi;
	/**
	 * �������¶Ⱥ�ʪ�ȷ�Χ[20.0~40.0��/0.0~100.0%]
	 */
	private String darea;
	/**
	 * ����ʱ��:��ʱ��[2009-12-2 12:32.52]-�״α���ʱ��
	 */
	private String dtimea;
	/**
	 * ����ʱ��:��ʱ��[2009-12-2 12:32.52]-�������ʱ��
	 */
	private String dtimeb;
	/**
	 * ������ǩ
	 */
	private String dlabel;
	
	public int getDeqid() {
		return deqid;
	}
	public void setDeqid(int deqid) {
		this.deqid = deqid;
	}
	public String getDtemp() {
		return dtemp;
	}
	public void setDtemp(String dtemp) {
		this.dtemp = dtemp;
	}
	public String getDhumi() {
		return dhumi;
	}
	public void setDhumi(String dhumi) {
		this.dhumi = dhumi;
	}
	public String getDarea() {
		return darea;
	}
	public void setDarea(String darea) {
		this.darea = darea;
	}
	public String getDtimea() {
		return dtimea;
	}
	public void setDtimea(String dtimea) {
		this.dtimea = dtimea;
	}
	public String getDtimeb() {
		return dtimeb;
	}
	public void setDtimeb(String dtimeb) {
		this.dtimeb = dtimeb;
	}
	public String getDlabel() {
		return dlabel;
	}
	public void setDlabel(String dlabel) {
		this.dlabel = dlabel;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
}
