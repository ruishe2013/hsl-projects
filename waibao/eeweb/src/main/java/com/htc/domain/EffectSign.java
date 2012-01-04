package com.htc.domain;

/**
 * @ EffectSign.java
 * 作用 : Gprs 有效的代替符号单元类
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class EffectSign {

	private int signId;			// 数字符号
	private String signStr;		// 字符符号
	private String format;		// 格式化
	private String mean;		// 含义
	private String sample;		// 举例
	/**
	 * @return the signId
	 */
	public int getSignId() {
		return signId;
	}
	/**
	 * @param signId the signId to set
	 */
	public void setSignId(int signId) {
		this.signId = signId;
	}
	/**
	 * @return the signStr
	 */
	public String getSignStr() {
		return signStr;
	}
	/**
	 * @param signStr the signStr to set
	 */
	public void setSignStr(String signStr) {
		this.signStr = signStr;
	}
	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}
	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	/**
	 * @return the mean
	 */
	public String getMean() {
		return mean;
	}
	/**
	 * @param mean the mean to set
	 */
	public void setMean(String mean) {
		this.mean = mean;
	}
	/**
	 * @return the sample
	 */
	public String getSample() {
		return sample;
	}
	/**
	 * @param sample the sample to set
	 */
	public void setSample(String sample) {
		this.sample = sample;
	}
	
	/**
	 * @describe: 创建 有效的代替符号单元类	
	 * @param signId 数字符号
	 * @param signStr 字符符号
	 * @param format 格式化
	 * @param mean 含义
	 * @param sample 举例
	 * @date:2009-11-4
	 */
	public static EffectSign createEle(int signId,String signStr,String format,String mean,String sample){
		EffectSign sign = new EffectSign();
		sign.setSignId(signId);
		sign.setSignStr(signStr);
		sign.setFormat(format);
		sign.setMean(mean);
		sign.setSample(sample);
		return sign;
	}
	
}
