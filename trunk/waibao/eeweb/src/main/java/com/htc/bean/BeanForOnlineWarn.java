package com.htc.bean;

import java.io.Serializable;

/**
 * @ BeanForOnlineWarn.java
 * 作用 : 在线报警数据单元
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-23     YANGZHONLI       create
 */
public class BeanForOnlineWarn implements Serializable {

	private static final long serialVersionUID = 758868339298401504L;
	
	/**
	 * 仪器Id
	 */
	private int deqid;
	/**
	 * 温度[温度数据+℃/F+报警类型]:如：15.58℃低温报警 
	 */
	private String dtemp;
	/**
	 * 湿度[湿度数据+%+报警类型]如：100.58%高湿报警
	 */
	private String dhumi;
	/**
	 * 正常的温度和湿度范围[20.0~40.0℃/0.0~100.0%]
	 */
	private String darea;
	/**
	 * 报警时间:长时间[2009-12-2 12:32.52]-首次报警时间
	 */
	private String dtimea;
	/**
	 * 报警时间:长时间[2009-12-2 12:32.52]-最近报警时间
	 */
	private String dtimeb;
	/**
	 * 仪器标签
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
