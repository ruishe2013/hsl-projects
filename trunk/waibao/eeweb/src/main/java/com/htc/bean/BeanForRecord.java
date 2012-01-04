package com.htc.bean;

import java.io.Serializable;
import java.util.Date;

public class BeanForRecord implements Serializable {

	private static final long serialVersionUID = -8734430938123373594L;
	
	private Date recTime; 		// ��¼ʱ��
	private String recTimeStr;	// ��¼ʱ��
    private int equipmentId; 	// �������
    private String temperature; // �¶�
    private String humidity; 	// ʪ��
    private String tempMax; 	// ����¶�
    private String tempMin; 	// ��С�¶�
    private String tempAvg; 	// ƽ���¶�
    private String humiMax; 	// ���ʪ��
    private String humiMin; 	// ��Сʪ��
    private String humiAvg; 	// ƽ��ʪ��

    public Date getRecTime() {
	return recTime;
    }

    public void setRecTime(Date recTime) {
	this.recTime = recTime;
    }

    public int getEquipmentId() {
	return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
	this.equipmentId = equipmentId;
    }

    public String getTemperature() {
	return temperature;
    }

    public void setTemperature(String temperature) {
	this.temperature = temperature;
    }

    public String getHumidity() {
	return humidity;
    }

    public void setHumidity(String humidity) {
	this.humidity = humidity;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempAvg() {
        return tempAvg;
    }

    public void setTempAvg(String tempAvg) {
        this.tempAvg = tempAvg;
    }

    public String getHumiMax() {
        return humiMax;
    }

    public void setHumiMax(String humiMax) {
        this.humiMax = humiMax;
    }

    public String getHumiMin() {
        return humiMin;
    }

    public void setHumiMin(String humiMin) {
        this.humiMin = humiMin;
    }

    public String getHumiAvg() {
        return humiAvg;
    }

    public void setHumiAvg(String humiAvg) {
        this.humiAvg = humiAvg;
    }

	public String getRecTimeStr() {
		return recTimeStr;
	}

	public void setRecTimeStr(String recTimeStr) {
		this.recTimeStr = recTimeStr;
	}
}
