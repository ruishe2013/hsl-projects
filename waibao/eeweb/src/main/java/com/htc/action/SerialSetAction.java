package com.htc.action;

import java.util.Map;

import com.htc.bean.BeanForSetData;
import com.htc.common.FunctionUnit;
import com.htc.model.seriaPort.Level_First_Serial;

/**
 * @ SerialSetAction.java
 * ���� : ���� - ��ȡ������Ӳ�������Ϣaction.
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class SerialSetAction extends AbstractAction {
	
	// ҳ����ʾ
	private BeanForSetData serialData;				// ҳ����ʾBean
	private int baudRateNo = 9600;					// ������  Ĭ��: 9600 
	private Map<Integer, String>  baudRatelList;	// ������		-- ������ 
	
	// ҳ�� <s:checkbox/>
	private boolean agreeAddrsFalg;					// �Ƿ��޸ĵ�ַ
	private boolean addFlag;						// �Ƿ� ������ˮ�ź͵�ַ
	private boolean showPowerFlag;					// �Ƿ���ʾ������Ϣ
	private boolean showVision;						// �Ƿ���ʾ�汾��Ϣ
	
	private int configId;							// ���������������� ʱ�õ�  - ��Ҫ�޸ĵ�ID
	private int configvalue;						// ����������������  ʱ�õ� - ��Ҫ�޸ĵ�ֵ
	private int configaddress;						// ����������������  ʱ�õ� - ��Ҫ�޸ĵĵ�ַ
	
	// ҳ���Ƿ���ʾ��ʾ��Ϣ
	private int showTipMsg = 0;					// 0:����ʾ  1:��ʾ
	
	private Level_First_Serial first_Level;
	//ע��service -- spring ioc
	public void setFirst_Level(Level_First_Serial first_Level) {
		this.first_Level = first_Level;
	}

	//���캯��
	public SerialSetAction() {
	}

	//��һ����ʾ�õ�
	public String execute(){
		showTipMsg = 0;
		return SUCCESS;
	}
	
	// ����0����������
	public String portSet(){
		showTipMsg = 1; // jspҳ��-��ʾ��ʾ��Ϣ
		String messageInfo;
		//�ж� ���ڵ���  �Ƿ�������
		//if (Level_First_Serial.getInstance().isRunningFlag() == true){
		if (first_Level.isRunningFlag() == true){
			messageInfo = getText("serialport.set.stop.running");
			addActionMessage(messageInfo);
			return SUCCESS;
		}else{
			// ���Դ����Ƿ���� -- �Զ�����
			//if (Level_First_Serial.getInstance().initConfig(baudRateNo)){		// �������� 
			if (first_Level.initConfig(baudRateNo)){		// �������� 
				messageInfo = getText("serialport.port.is.usefull");
				addActionMessage(messageInfo);
			}else{														// ���Ӵ���
				messageInfo = getText("serialport.port.not.usefull");
				addFieldError("configError", messageInfo);
			}
			return SUCCESS;
		}
	}
	
	/**
	 * @describe: ǰ�ô���. �򿪴���,��ʼ���˿ںźͲ�����
	 * @return true: ׼������		false: ���������� 
	 * @date:2009-11-6
	 */
	public boolean preAction(){
		showTipMsg = 1; // jspҳ��-��ʾ��ʾ��Ϣ
		
		boolean rsBool = false;
		String messageInfo;
		//if (Level_First_Serial.getInstance().isRunningFlag() == true){
		if (first_Level.isRunningFlag() == true){
			messageInfo = getText("serialport.set.stop.running");
			addActionMessage(messageInfo);			
		}else{
			//Level_First_Serial.getInstance().initConfigVoid(baudRateNo);
			first_Level.initConfigVoid(baudRateNo);
			//rsBool = Level_First_Serial.getInstance().openPort();
			rsBool = first_Level.openPort();
			if (!rsBool){
				messageInfo = getText("serialport.set.port.error");
				addFieldError("configError", messageInfo);
			}
		}
		return rsBool;
	}
	
	//����1�����û�����,(ҳ��������ʾ)��ˮ�ż�1, ���õ�ַ,(ҳ��������)��ַ��1
	public String setMacAddr(){
		boolean rsBool = false;
		String messageInfo;
		
		if (preAction()){// ���׼�����
			//Level_First_Serial first_Level = Level_First_Serial.getInstance();
			
			//1:�����޸Ļ��� ����
			rsBool = first_Level.setMac(serialData);
			if (rsBool){
				messageInfo = getText("serialport.set.mac.succes");
				addActionMessage(messageInfo);
				
				//2:�����޸ĵ�ַ ����
				if ( agreeAddrsFalg == true ){
					rsBool =  first_Level.setAddr(serialData, Integer.parseInt(serialData.getAddress()) );
					if (rsBool){
						messageInfo = getText("serialport.set.addr.succes");
						addActionMessage(messageInfo);
					}else{
						messageInfo = getText("serialport.set.addr.failure");
						addFieldError("addressError", messageInfo);
					}//end if rsBool
				}//end if 2:agreeAddrs
				
				// ��ʾ����Ϣ
				if ( rsBool ) { 
					// ͨ�����ţ���ȡ����汾��,ͨѶ��ַ(������ȡ����)
					serialData = first_Level.readInfo(serialData);
					showMessInfo(first_Level);
				}
				
				//��ַ��1	��		��ˮ�ż�1
				if ( rsBool && addFlag){
					// ��ַ
					serialData.setAddress(String.valueOf( Integer.parseInt(serialData.getAddress()) + 1) );
					// ��ˮ��
					serialData.setMCNo(fillStringTo4(serialData.getMCNo()));
				}
			}else{
				messageInfo = getText("serialport.set.mac.failure");
				addFieldError("mcError", messageInfo);
			}//end if 1:rsBool
			
		}
		
		return SUCCESS;
	}
	
	public String fillStringTo4(String str){
		StringBuffer strbuf = new StringBuffer();
		str = String.valueOf((Integer.parseInt(str)+1)%10000);
		int len = str.length();
		for (int i = len; i < 4; i++) {
			strbuf.append("0");
		}
		strbuf.append(str);
		return strbuf.toString();
	}	
	
	//����2��ͨ�����ţ���ͨѶ��ַ������汾��
	public String readInfo(){
		String messageInfo;
		
		if (preAction()){// ���׼�����
			//Level_First_Serial first_Level = Level_First_Serial.getInstance();
			BeanForSetData tempBean = new BeanForSetData();
			serialData = first_Level.readInfo(tempBean);	// ��ȡ��Ϣ
			if (serialData != null){
				showMessInfo(first_Level);
				messageInfo = getText("serialport.set.read.succes");
				addActionMessage(messageInfo);
			}else{
				messageInfo = getText("serialport.set.read.failure");
				addFieldError("readError", messageInfo);
			}//end if
		}//end preAction
		return SUCCESS;
	}
	
	//����3���·�У��ʱ��
	public String checkTime(){
		boolean rsBool = false;
		String messageInfo;
		
		if (preAction()){// ���׼�����
			//Level_First_Serial first_Level = Level_First_Serial.getInstance();
			rsBool =  first_Level.writeTime(1);	// �·�У��ʱ��
			if (rsBool){
				messageInfo = getText("serialport.set.time.succes");
				addActionMessage(messageInfo);
			}else{
				messageInfo = getText("serialport.set.time.failure");
				addFieldError("timeError", messageInfo);
			}
		}
		return SUCCESS;
	}
	
	//����4����������Ĭ������(�¶�,ʪ��,¶���...)
	public String updateEqConfig(){
		boolean rsBool = false;
		String messageInfo = "";
		// System.out.println(configaddress + ":" + configId + ":" + configvalue);
		
		if (preAction()){// ���׼�����
			//Level_First_Serial first_Level = Level_First_Serial.getInstance();
			
			if (configId == Level_First_Serial.SET_DEFAULT_TEMP){
				messageInfo = "Ĭ���¶�";
				rsBool = first_Level.setDefaultTemp(configaddress, String.valueOf(configvalue));
			}else if (configId == Level_First_Serial.SET_DEFAULT_HUMI){
				messageInfo = "Ĭ��ʪ��";
				rsBool = first_Level.setDefaultHumi(configaddress, String.valueOf(configvalue));
			}else if (configId == Level_First_Serial.SET_DEFAULT_DEWPOINT){
				messageInfo = "Ĭ��¶��";
				rsBool = first_Level.setDefaultDewPoint(configaddress, String.valueOf(configvalue));
			}else if (configId == Level_First_Serial.SET_REC_INTERVAL){
				messageInfo = "��¼���";
				rsBool = first_Level.setRecInterval(configaddress, configvalue);
			}else if (configId == Level_First_Serial.SET_MAX_STORE_NUMBER){
				messageInfo = "��ʷ��¼����";
				rsBool = first_Level.setMaxStoreNumber(configaddress, configvalue);
			}else if (configId == Level_First_Serial.SET_HUMI_MAX_VALUE){
				messageInfo = "��ʪ����";
				rsBool = first_Level.setHumiMaxValue(configaddress, configvalue);
			}else if (configId == Level_First_Serial.SET_HUMI_MIN_VALUE){
				messageInfo = "��ʪ����";
				rsBool = first_Level.setHumiMinValue(configaddress, configvalue);
			}else if (configId == Level_First_Serial.SET_TEMP_MAX_VALUE){
				messageInfo = "���±���";
				rsBool = first_Level.setTempMaxValue(configaddress, configvalue);
			}else if (configId == Level_First_Serial.SET_TEMP_MIN_VALUE){
				messageInfo = "���±���";
				rsBool = first_Level.setTempMinValue(configaddress, configvalue);
			}else if (configId == Level_First_Serial.SET_DEWPOINT_MAX_VALUE){
				messageInfo = "��¶�㱨�� ";
				rsBool = first_Level.setDewPointMaxValue(configaddress, configvalue);
			}else if (configId == Level_First_Serial.SET_DEWPOINT_MIN_VALUE){
				messageInfo = "��¶�㱨��";
				rsBool = first_Level.setDewPointMinValue(configaddress, configvalue);
			}
			
			if (rsBool){
				messageInfo = "����" + messageInfo + "�ɹ�.";
				addActionMessage(messageInfo);
			}else{
				messageInfo = "����" + messageInfo + "ʧ��.";
				addFieldError("timeError", messageInfo);
				messageInfo = "�ȼ��: ��������ںŲ��ԡ��鿴��������״̬";
				addActionMessage(messageInfo);
				messageInfo = "�ټ��: �����ַ����Ӧ�������Ƿ���������";
				addActionMessage(messageInfo);
			}
		}
		return SUCCESS;
	}
	
	
	
	/**
	 * @describe: ��ʾ����Ϣ -- ��Ҫ������ �����Ͱ汾��
	 * @param first_Level Ӳ����Ϣ������
 	 * @date:2009-11-6
	 */
	public void showMessInfo(Level_First_Serial first_Level) {
		
		// ��ȡ���� -- ��ͨ��ͨѶ��ַ��ȡ������,���֮ǰ����Ҫ��ȡͨѶ��ַ
		if (serialData != null){
			
			// ����
			if (showPowerFlag == true){
				int tempInt;
				tempInt = first_Level.readPower( Integer.parseInt( serialData.getAddress() ) );
				if (tempInt == -1){//������ȡʧ��
					serialData.setPowerVal("��ȡʧ��");
				}else{
					serialData.setPowerVal(FunctionUnit.FLOAT_DATA_FORMAT.format
							(tempInt / (float) 1000) + "v");
				}
			}
			
			// �汾��  -- serialData�������Ѿ��а汾��Ϣ,����ֻ����û��ѡ�и�ѡ��ʱ,��ֵ����Ϊnullֵ,
			if (showVision == false){
				serialData.setVersion(null);
			}
			
		}
		
	}
	
	// ----------------����Ϊget��set����----------------	
	public Map<Integer, String> getBaudRatelList() {
		baudRatelList = getMapSel("gprsbaudRate");
		return baudRatelList;
	}
	
	// ----------------��ͨ get��set����----------------
	public BeanForSetData getSerialData() {
		return serialData;
	}

	public void setSerialData(BeanForSetData serialData) {
		this.serialData = serialData;
	}

	public int getBaudRateNo() {
		return baudRateNo;
	}

	public void setBaudRateNo(int baudRateNo) {
		this.baudRateNo = baudRateNo;
	}

	public boolean isAgreeAddrsFalg() {
		return agreeAddrsFalg;
	}

	public void setAgreeAddrsFalg(boolean agreeAddrsFalg) {
		this.agreeAddrsFalg = agreeAddrsFalg;
	}

	public boolean isAddFlag() {
		return addFlag;
	}

	public void setAddFlag(boolean addFlag) {
		this.addFlag = addFlag;
	}

	public boolean isShowPowerFlag() {
		return showPowerFlag;
	}

	public void setShowPowerFlag(boolean showPowerFlag) {
		this.showPowerFlag = showPowerFlag;
	}

	public boolean isShowVision() {
		return showVision;
	}

	public void setShowVision(boolean showVision) {
		this.showVision = showVision;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public int getConfigvalue() {
		return configvalue;
	}

	public void setConfigvalue(int configvalue) {
		this.configvalue = configvalue;
	}

	public int getConfigaddress() {
		return configaddress;
	}

	public void setConfigaddress(int configaddress) {
		this.configaddress = configaddress;
	}

	public int getShowTipMsg() {
		return showTipMsg;
	}

	public void setShowTipMsg(int showTipMsg) {
		this.showTipMsg = showTipMsg;
	}
	
}
