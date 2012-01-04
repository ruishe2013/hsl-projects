package com.htc.domain;

/**
 * @ EffectSign.java
 * ���� : Gprs ��Ч�Ĵ�����ŵ�Ԫ��
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class EffectSign {

	private int signId;			// ���ַ���
	private String signStr;		// �ַ�����
	private String format;		// ��ʽ��
	private String mean;		// ����
	private String sample;		// ����
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
	 * @describe: ���� ��Ч�Ĵ�����ŵ�Ԫ��	
	 * @param signId ���ַ���
	 * @param signStr �ַ�����
	 * @param format ��ʽ��
	 * @param mean ����
	 * @param sample ����
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
