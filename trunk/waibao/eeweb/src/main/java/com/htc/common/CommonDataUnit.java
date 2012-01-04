package com.htc.common;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.htc.bean.*;
import com.htc.domain.*;
import com.htc.model.*;
import com.htc.model.seriaPort.SimCard_Unit;

/**
 * @ WarnDataUnit.java
 * ���� : �������ݵ�Ԫ<br>
 * 	1: ������Ϣ(һ�μ���,���������) Map(equipmentId, EquipData)<br>
 * 	2: ����������Ϣ�б� Map(equipmentId, Record)<br>
 * 	3: ��ַ�ַ����б�(��ʽ�����) Map(equipmentId, String)<br>
 * 	4: �����������б� List(equipmentId)<br>
 * 	5: �������ŵ��ļ�<br>
 *  6: ����ַ��Ӧ�ĵ�ѹֵ Map(equipmentId, power)<br>
 *  7: ����յ������Ƿ��Ǿ�������<br>
 *  8: ��ȡ��һ��������ID<br>
 * ע������ : ��<br>
 * VERSION       DATE            BY       CHANGE/COMMENT<br>
 * 1.0          2009-11-4     YANGZHONLI  create<br>
 */
public class CommonDataUnit {
	//����Щ���Ͻ��в����޸��ǰ�ȫ��(ConcurrentHashMap��CopyOnWriteArrayList)
	/**
	 * ������Ϣ(һ�μ���,���������)Map(Integer, EquipData)
	 */
	public Map<Integer, EquipData> equiMap = new ConcurrentHashMap<Integer, EquipData>();
	/**
	 * ������Ϣ(һ�μ���,���������)List(EquipData)
	 */
	public List<EquipData> equiList = new CopyOnWriteArrayList<EquipData>();
	/**
	 * ��ַ�ַ����б�(��ʽ�����)
	 */
	public Map<Integer, String> equiMapStr = new ConcurrentHashMap<Integer, String>();
	/**
	 *���е绰�����б� 
	 */
	public List<PhoneList> phonelists = new ArrayList<PhoneList>();
	/**
	 * �������ŵ��ļ�
	 */
	public String warnPlayFile = null;
	/**
	 * �� �񲥷ű����ļ�(0:δ��ʼ�� 1:���� 2����)(ȫ��)
	 */
	public int warnOpenFlag = 0;
	/**
	 * ϵͳ��Ϣmap(String,String)
	 */
	public Map<String, String> sysArgs = new ConcurrentHashMap<String, String>();

	/**
	 * ��ȡȫ����������Ϣ
	 */
	public List<Workplace> allWorkPlaceList = new CopyOnWriteArrayList<Workplace>();
	/**
	 * ͳ��ϵͳ�����������������: 1:ϵͳ�м����¶�����ʪ������		2:ϵͳ��ȫ�����¶�����	3:ϵͳ��ȫ����ʪ������
	 */
	public int systemEqType = 0;
	/**
	 * access�������Ƿ�������մ���, 
	 * accessDataEmpty=true ���,������
	 * accessDataEmpty=false ������
	 */
	public boolean accessDataEmpty = false;
	/**
	 * access����������״̬
	 * accessLinkState=true ��������
	 * accessLinkState=false ����ʧ��
	 * Ĭ����TRUE
	 */
	public boolean accessLinkState = true;
	/**
	 * ÿ10����,��access���ݿ��в�������޸����ݱ��:
	 * ���1000 * 60 * 10
	 */
	public long accessTimeFalg = -1;
	/**
	 * �����Ƿ������У��������ڲɼ�����ʱeqDoingDataΪtrue,�ɼ����eqDoingDataΪfalse
	 */
	public boolean eqDoingData = false;
	/**
	 * ��Ҫ���ӵ�access�ĵ���
	 */
	public int accessLinkCount;
	/**
	 * ����ģ���Ƿ��ڴ�������. ��������ʱsmsDoingDataΪtrue,�������smsDoingDataΪfalse 
	 */
	public boolean smsDoingData = false;
	
	/**
	 *  ����ģ�������������
	 */
	private Queue<SmsRecord> queue4SimCard = new LinkedList<SmsRecord>();
	
	private ManaService manaService;
	private SetSysService setSysService;
	private MainService mainService;
	private ServiceAccess serviceAccess;
	private SimCard_Unit simCard_Unit;
	
	//ע��service -- spring ioc
	public void setManaService(ManaService manaService) {
		this.manaService = manaService;
	}
	public void setSetSysService(SetSysService setSysService) {
		this.setSysService = setSysService;
	}
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	public void setServiceAccess(ServiceAccess serviceAccess) {
		this.serviceAccess = serviceAccess;
	}
	public void setSimCard_Unit(SimCard_Unit simCard_Unit) {
		this.simCard_Unit = simCard_Unit;
	}
	
	/**
	 * @describe:ͳ��ϵͳ�����������������:<br> 
	 * @return	0:ϵͳ��û���κ�����<br>
	 * 			1:ϵͳ�м����¶�����ʪ������<br>		
	 * 			2:ϵͳ��ȫ�����¶�����<br>	
	 * 			3:ϵͳ��ȫ����ʪ������<br>
	 * @date:2010-1-23
	 */
	public synchronized int getSystemEqType(){
		if (systemEqType == 0){
			int tempInt = 0;
			List<BeanForEqTypeCount> eqCount = manaService.getEqCountByType();
			for (BeanForEqTypeCount bean : eqCount) {
				if (bean.getEqType() == 1){// 1:��ʪ��
					tempInt += 100; 
				}else if (bean.getEqType() == 2){// 2:���¶�
					tempInt += 10;
				}else if (bean.getEqType() == 3){//	3:��ʪ��
					tempInt += 1;
				}
			}
			systemEqType = tempInt==0?0:tempInt==1?3:tempInt==10?2:1;
		}
		return systemEqType;
	}
	
	public void reSetSystemEqType(){
		systemEqType = 0;
	}
	
	/**
	 * ���绰�����б�
	 */
	public void fillPhoneList(){
		phonelists = setSysService.getAllPhoneLists();	//���е绰�����б�
	}
	
	/**
	 * ��ȡ�绰�����б�
	 */
	public List<PhoneList> getPhoneList(){
		if (phonelists.size() <= 0){
			fillPhoneList();
		}
		return phonelists;			
	}
	
	/**
	 * @describe :��� ѡ��������Ϣ (һ��ת��,���������) equiMap:Map(equipmentId, EquipData) ��
	 *           equiAddrList:List(equipmentId)
	 * @date:2009-11-2
     */
	public void fillEquiMap() {
		BeanForEqOrder eqorderBean = new BeanForEqOrder();
		eqorderBean.setEqorderType(BeanForEqOrder.SELECT_WITH_ORDERID);

		equiList = manaService.getAllEquiObject(eqorderBean);
		int equipmentId;
		if (equiList == null){ 
			equiList =  new CopyOnWriteArrayList<EquipData>();
		}
		equiMap.clear();
		accessLinkCount = 0; // ��ʼ��,��Ҫ��ʾ��access�ĸ��� 
		for (int j = 0; j < equiList.size(); j++) {
			equipmentId = equiList.get(j).getEquipmentId();
			if (equiList.get(j).getShowAccess()==1){ // ��Ҫ��ʾ��access�ĸ���
				accessLinkCount++;
			}
			equiMap.put(equipmentId, equiList.get(j));
		}
	}
	
	/**
	 * @describe: ��ȡ��һ��������ID		
	 * @return: ����0,��û����������
	 * @date:2009-11-4
	 */
	public int getFirstEquipmentId(){
		int equipmentId = 0;
		if (equiMap.size() <= 0){
			fillEquiMap();
		};
		equipmentId = equiMap.size() > 0 ? equiMap.keySet().iterator().next() : 0;
		return equipmentId;
	}
	
	/**
	 * @describe :��ȡȫ����������ϢList(EquipData)
	 * @date:2009-11-2
     */
	public List<EquipData> getEquiList() {
		if (equiList.size() <= 0){
			fillEquiMap();
		}
		return equiList;
	}

	/**
	 * @describe: ɾ����Ӧ��������Ӧ��ϢList(EquipData)
	 * @param equipmentId : ����ID
	 * @date:2009-11-2
	 */
	public void removeEquiList(int equipmentId) {
		for (EquipData equipData : equiList) {
			if (equipData.getEquipmentId() == equipmentId){
				equiList.remove(equipData);
				break;
			}
		}
	}
	
	/**
	 * @describe: ��ȡȫ������ �������б�(List: equipmentId)
	 * @date:2009-11-2
     */
	public Set<Integer> getEquipmentList() {
		if (equiMap.size() <= 0 ){
			fillEquiMap();
		}
		return equiMap.keySet();
	}

	/**
	 * @describe :��ȡȫ����������Ϣ(Map:equipmentId, EquipData)
	 * @date:2009-11-2
     */
	public Map<Integer, EquipData> getEquiMap() {
		if (equiMap.size() <= 0){
			fillEquiMap();
		}
		return equiMap;
	}

	/**
	 * @describe :��ȡ ѡ�ж�Ӧ��������Ϣ���ݶ��� EquipData
	 * @param equipmentId : ����Id
	 * @date:2009-11-2
     */
	public EquipData getEquipByID(int equipmentId) {
		if ( (equiMap.size() == 0) || (!equiMap.containsKey(equipmentId)) ){
			fillEquiMap();
		}
		return equiMap.get(equipmentId);
	}

	/**
	 * @describe: ɾ����Ӧ��������Ӧ��Ϣ
	 * @param equipmentId : ����ID
	 * @date:2009-11-2
	 */
	public void removeEquiMap(int equipmentId) {
		if (equiMap.containsKey(equipmentId)) {
			equiMap.remove(equipmentId);
		}
	}
	
	/**
	 * @describe: ���ȫ��������ʶ�� Map(equipmentId : �ص� + ��� + ������ַ)
	 * @date:2009-11-2
     */
	public void fillEquiStr() {
		BeanForEqOrder eqorderBean = new BeanForEqOrder();
		eqorderBean.setEqorderType(BeanForEqOrder.SELECT_WITH_EQUIPID);
		equiMapStr = manaService.getAllEquiString(eqorderBean);
	}		

	/**
	 * @describe: ��ȡȫ��������ʶ��.Map(equipmentId : �ص� + ��� + ������ַ)
	 * @date:2009-11-2
	 */
	public Map<Integer, String> getEquiMapStr() {
		if (equiMapStr.size() <= 0){
			fillEquiStr();
		}
		return equiMapStr;
	}
	
	/**
	 * @describe: ��ȡ����������ʶ��.Map(equipmentId : �ص� + ��� + ������ַ)
	 * @date:2009-11-2
     */
	public String getEquiMapStrById(int  equipmentId) {
		if ( (equiMapStr.size() == 0) || (!equiMapStr.containsKey(equipmentId)) ){
			fillEquiStr();
		}
		return equiMapStr.get(equipmentId);
	}
	
	/**
	 * @describe: ɾ����Ӧ��������ʶ��
	 * @param equipmentId : ����ID
	 * @date:2009-11-2
	 */
	public void removeEquiMapStr(int equipmentId) {
		if (equiMapStr.containsKey(equipmentId)) {
			equiMapStr.remove(equipmentId);
		}
	}

	/**
	 * @describe: ��ȡ�������ŵ��ļ�
	 * @date:2009-11-2
     */
	public  String getWarnPlayFile() {
		if ((warnPlayFile == null) || (warnPlayFile.length() <= 0)) {
			warnPlayFile = getSysArgsByKey(BeanForSysArgs.ALARM_PLAYFILE);
		}
		return warnPlayFile;
	}

	/**
	 * @describe: ���ñ������ŵ��ļ�
	 * @date:2009-11-2
     */
	public void setWarnPlayFile(String warnPlayFile) {
		this.warnPlayFile = warnPlayFile;
	}

	/**
	 * @describe: ��ȡϵͳ���ű�����־ (0:δ��ʼ�� 1:�ر� 2����)
	 * @date:2009-11-2
     */
	public  int getWarnOpenFlag() {
		String open_pcSound = "";
		if (warnOpenFlag == 0) {
			open_pcSound = getSysArgsByKey(BeanForSysArgs.OPEN_PCSOUND);
			warnOpenFlag = Integer.parseInt(open_pcSound);
		}
		return warnOpenFlag;
	}

	/**
	 * @describe: ͨ����ֵ��ȡϵͳ����	
	 * @param argsKey ϵͳ��ֵ
	 * @return: ��Ӧ��ϵͳ����
	 * @date:2009-11-29
	 */
	public String getSysArgsByKey(String argsKey){
		String rsStr= null;
		
		try {
			if ( sysArgs.containsKey(argsKey) ) {
				rsStr = sysArgs.get(argsKey);
			}else{// ��һ�μ��û�����¼������ݿ� 
				sysArgs = getAllSysArgs();
				if (sysArgs.containsKey(argsKey)){
					rsStr = sysArgs.get(argsKey);
				}
			}
		} catch (Exception e) {
			 // ���غ���û�о�ʹ��Ĭ��ֵ
			rsStr = getDefSysArgsByKey(argsKey);
		}
		return rsStr;
	}
	
	/**
	 * @describe:	��ȡϵͳĬ�ϵĲ���
	 * @param argsKey
	 * @date:2009-11-29
	 */
	public String getDefSysArgsByKey(String argsKey){
		return BeanForSysArgs.getDefaultSysArgsMap().get(argsKey);
	}
	
	/**
	 * @describe: ��ȡϵͳ��ȫ������
	 * @return:
	 * @date:2009-11-29
	 */
	public Map<String, String> getAllSysArgs(){
		return setSysService.getSysParamMap();
	} 
	
	/**
	 * @describe:	����ϵͳ����
	 * @date:2009-11-29
	 */
	public void reSetSysArgs(){
		warnOpenFlag = 0;
		warnPlayFile = null;
		sysArgs.clear();
	}
	
	
	/**
	 * @describe: �����Ƿ񲥷ű����ļ�(0:δ��ʼ�� 1:���� 2����)
	 * @date:2009-11-2
     */
	public void setWarnOpenFlag(int warnOpenFlag) {
		this.warnOpenFlag = warnOpenFlag;
	}

	/**
	 * @describe : ���ĸյõ������Ƿ��Ǿ�������,�����,���뾯���б�warnList(Map:Integer, Record)
	 * @param record : ����Record��������
	 * @date:2009-11-2
     */
	public void checkDataWarn(Record record, EquipData equipData) {
		if (equipData == null) {return;}
		
		//���������-[�¶�-ʪ��-eqID-��¼ʱ��]---������Ҫ������-[��������,��������,������ǩ]
		int hasRang = 0; // �����Ƿ�Ϊ������¼(�¶�,ʪ������֮��)
		String tempInfoMsg ="-"; // �¶���ص�����
		String humiInfoMsg ="-"; // ʪ����ص�����

		int alarmtype = 0;// ��������(�¶ȳ�����Χ 1:����������Χ 2:����������Χ) + (ʪ�ȳ�����Χ 10:����������Χ+20:����������Χ)
		// ͳ��ϵͳ�����������������: 1:ϵͳ�м����¶�����ʪ������		2:ϵͳ��ȫ�����¶�����	3:ϵͳ��ȫ����ʪ������
		int sysEqType = getSystemEqType(); 
		
		float tempFloat = 0;
		// �Ƚ��¶��Ƿ񳬳�����
		if (sysEqType != 3){
			if (equipData.getEquitype() != EquipData.TYPE_HUMI_ONLY){// ���¶Ⱥ���ʪ���ܼ���¶�
				tempFloat = Float.parseFloat(record.getTemperature());	// �¶�
				int tempType = Integer.parseInt(
						getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)); // �¶���ʾ����(�¶���ʾ��ʽ(1:���ϡ� 2:����F))
				if (tempType == 2){
					tempFloat = tempFloat*9/5 + 32;// ��ʾΪ���ϵ�ʱ��,ת����ʽ
				}
				
				tempInfoMsg = tempFloat + (tempType==1?"��":"F") + ",";
				if (tempFloat < (equipData.getTempDown())) {// ����
					hasRang += 1;		alarmtype = 1;
					tempInfoMsg = tempInfoMsg + "���±���";
				} else if (tempFloat > (equipData.getTempUp())) {// ����
					hasRang += 1;		alarmtype = 2;
					tempFloat = tempFloat - equipData.getTempUp();
					tempInfoMsg = tempInfoMsg + "���±���";
				} else {// ����
					tempInfoMsg = tempInfoMsg + "����";
				}
			}
		}// end if (sysEqType != 3)
		if (sysEqType != 2){
			if (equipData.getEquitype() != EquipData.TYPE_TEMP_ONLY){// ��ʪ�Ⱥ���ʪ���ܼ���¶�
				tempFloat = Float.parseFloat(record.getHumidity());			// ʪ��
				// �Ƚ�ʪ���Ƿ񳬳�����
				humiInfoMsg = tempFloat + "%RH,";
				if (tempFloat < (equipData.getHumiDown())) {// ����
					hasRang += 1;		alarmtype += 10;
					humiInfoMsg = humiInfoMsg + "��ʪ����";
				} else if (tempFloat > (equipData.getHumiUp())) {// ����
					hasRang += 1;		alarmtype += 20;
					humiInfoMsg = humiInfoMsg + "��ʪ����";
				} else {// ����
					humiInfoMsg = humiInfoMsg + "����";
				}
			}			
		}
		// ���� �������޵����ݼ�¼,�������ļ�
		if (hasRang > 0) {
			// �Ե�ǰ�ı�������,Ӧ��ִ�����ֲ���:��������޸�
			int equipmentId = record.getEquipmentId();
			BeanForAlarmRs rsBean = mainService.getDoWhich(equipmentId);			
			AlarmRec alarmRec = new AlarmRec();
			alarmRec.setAlarmEnd(record.getRecTime().getTime());
			
			if (rsBean.getRsType() == BeanForAlarmRs.ADD_ALARM){
				alarmRec.setEquipmentId(equipmentId);
				alarmRec.setTemperature(tempInfoMsg);
				alarmRec.setHumidity(humiInfoMsg);
				alarmRec.setAlarmStart(record.getRecTime().getTime());
				alarmRec.setAlarmtype(alarmtype);// ��������(�¶ȳ�����Χ 1:����������Χ 2:����������Χ) + (ʪ�ȳ�����Χ 10:����������Χ+20:����������Χ)
				// ���ô���״̬Ϊ[Ϊ��λ]
				alarmRec.setState(2);//����״̬:1:�Ѹ�λ	2��δ��λ
				alarmRec.setUsername("����");// �����û���Ϊ:
				alarmRec.setAlarmmode(AlarmRec.NO_ACTION_MODE);// ���ô���ģʽ[δ����:0 PC:1 GPRS:2]
				alarmRec.setPlaceName(equipData.getPlaceStr() + "-" + equipData.getMark());// ������
				alarmRec.setNormalArea(getAreaString(equipData));// ����������Χ
				alarmRec.setEquitype(equipData.getEquitype());// ��ʪ�����ͣ�1,'��ʪ��';2,'���¶�';3,'��ʪ��'
				
				mainService.insertAlarmRec(alarmRec);
				// ������ű���
				// �ж� -- ���ű����Ƿ��(1:�ر� 2:��) �� ����ͨ�Ŵ����Ƿ��  
				if ((Integer.parseInt(getSysArgsByKey(BeanForSysArgs.OPEN_SHORT_MESSAGE))==2)&&(simCard_Unit.isRunFlag())){
					// ��ȡ���Ͷ��ŵ��ֻ��б�
					BeanForSms smsBean = manaService.getPlacePhones(equipmentId);  // ��ȡ����������ַ������
					List<String> phoneList = setSysService.getPhones(smsBean.getPhonelist());// ��ȡ����������ַ���ֻ��б�
					String msg = "������:"+ alarmRec.getPlaceName()+ "\n" +
								"�¶�:" + tempInfoMsg + "\n" +
								"ʪ��:"+humiInfoMsg + "\n" + 
								//"������Χ:" + alarmRec.getNormalArea()+ "\n" +
								"ʱ��:" + FunctionUnit.getDateToStr(record.getRecTime(),	FunctionUnit.Calendar_END_MINUTE, FunctionUnit.UN_SHOW_CHINESE);
					SmsRecord smsRec = null;
					for (String phoneNo : phoneList) {
						// simCard_Unit.sendMessage(getSysArgsByKey(BeanForSysArgs.SMS_CENTER_NUMBER), phoneNo, msg); // ����ģ���ʱ���д���
						// �Ѷ��ŷ�����Ϣ���浽���ݿ�
						smsRec = new SmsRecord();
						smsRec.setSmsphone(phoneNo);				// ���Ž��պ���
						smsRec.setSmscontent(msg);					// ��������
						smsRec.setSmstype(1); 						// �������ŵ�����
						smsRec.setSmsrectime(record.getRecTime());	// ���ŷ���ʱ��
						// mainService.insertSmsRec(smsRec);	 	// ����ģ���ʱ���д���
						queue4SimCard.offer(smsRec);				// ����Ҫ���͵Ķ��ż����ж�
					}
				}
			}else if (rsBean.getRsType() == BeanForAlarmRs.UPDATE_ALARM){
				alarmRec.setAlarmId(rsBean.getAlarmId());// ���ñ������,����ִ����� (update talarmrec set alarmEnd = #alarmEnd# where alarmId = #alarmId#)
				mainService.updateAlarmRecing(alarmRec);
			}
		}// end (hasRang > 0)
			
	}// end public
	
	/**
	 * �õ�����ǰ���һ��SmsRecordԪ��
	 */
	public SmsRecord pollSimCard(){
		return queue4SimCard.poll();
	}
	
	/**
	 * @describe: ��ȡ ����������Χ. ��:1.0~10.0��/0.0~100.0%RH
	 * @param equipmentId ����ID
	 * @date:2009-11-5
	 */	
	public String getAreaString(int  equipmentId){
		EquipData eq = getEquipByID(equipmentId);
		return getAreaString(eq);
	}	
	
	/**
	 * @describe: ��ȡ ����������Χ. ��:1.0~10.0��/0.0~100.0%RH
	 * @param eq ������Ϣ
	 * @date:2009-11-5
	 */	
	public String getAreaString(EquipData eq){
		StringBuffer rsStrBuf = new StringBuffer();
		if (eq == null){
			rsStrBuf.append("-");
		}else{
			// �¶���ʾ��ʽ(1:���� 2:����)
			String str = Integer.parseInt(getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE))==1?"��":"F";
			if (eq.getEquitype() != EquipData.TYPE_HUMI_ONLY){
				rsStrBuf.append(eq.getTempDown());
				rsStrBuf.append("~");
				rsStrBuf.append(eq.getTempUp());
				rsStrBuf.append(str);
			}
			if (eq.getEquitype() == EquipData.TYPE_TEMP_HUMI){
				rsStrBuf.append("/");
			}
			if (eq.getEquitype() != EquipData.TYPE_TEMP_ONLY){
				rsStrBuf.append(eq.getHumiDown());
				rsStrBuf.append("~");
				rsStrBuf.append(eq.getHumiUp());
				rsStrBuf.append("%RH");
			}
		}
		eq = null;
		return rsStrBuf.toString();
	}	

	/**
	 * @describe: ��ȡȫ����������Ϣ	 
	 * @date:2009-11-30
	 */
	public  List<Workplace> getAllWorkPlaceList() {
		if (allWorkPlaceList.size() <= 0){
			reSetAllWorkPlaceList();
		}
		return allWorkPlaceList;
	}

	/**
	 * @describe: ���� ��ȡȫ������
	 * @param allWorkPlaceList:
	 * @date:2009-11-30
	 */
	public  void reSetAllWorkPlaceList() {
		this.allWorkPlaceList = manaService.getAllWorkplace(1); 
	}

	/**
	 * @describe: û��Ĭ��ѡ������ʱ,���������б���ҳ���ϵ���ʾ����
	 * ��ʽ:map(������,�����е������б�)
	 * @param selEquipId : ��ҪĬ��ѡ�е�����Id����
	 * @return:
	 * @date:2009-11-30
	 */
	public  Map<String, String> createEqAreaWithNOSel(List<Integer> selEquipIds){
		Map<String, String> showArea = new HashMap<String, String>();  // ���ص�map����
		StringBuffer strBuf = new StringBuffer(); // ��ʱ����
		boolean isCheck = false;				  // ��ʱ����--�Ƿ�Ҫѡ��	
		String areaName ;						  // ��ʱ���� --������
		Map<Integer, EquipData> allEquip = getEquiMap(); 	// ��ȡȫ������
		if (allEquip.size() <= 0){return showArea;}
		
		List<Workplace> workPlace = getAllWorkPlaceList();  // ��ȡȫ������
		EquipData tempEquip = null;					  		// ��ʱ����--��������
		int equipId ;
		for (int i = 0; i < workPlace.size(); i++) {
			areaName = workPlace.get(i).getPlaceName();		// ������
			strBuf.delete(0, strBuf.length());
			
			for (Entry<Integer, EquipData> entry : allEquip.entrySet()) {
				equipId = entry.getKey();
				tempEquip = null;	tempEquip = entry.getValue();
				if ( areaName.equals(tempEquip.getPlaceStr()) ){
					isCheck = selEquipIds == null ? false : selEquipIds.contains(equipId); // �鿴�Ƿ���Ҫѡ��
					strBuf.append(create_Html_P(tempEquip.getEquipmentId(),tempEquip.getMark(),isCheck));
				}				
			}// end for
			
			// ��װ��ӦhtmlԪ��
			if (strBuf.length() > 0) {
				showArea.put(areaName, strBuf.toString());
			}
			
		}// end fof
		return showArea;
	}
	
	/**
	 * @describe: ����Html��tdԪ����Ϣ	
	 * @param equipmentId : ��������
	 * @param label ��ʾ�ı�ǩ
	 * @param isCheck �Ƿ�ѡ��(true:ѡ�� false:��ѡ��)
	 * @return:  �����pԪ����Ϣ
	 * @date:2009-11-30
	 */
	public static String create_Html_P(int equipmentId, String label, boolean isCheck){
		// ��ʽ����://��������ʾ12����(12���ֽ�)
//		 <li>		
//			<label for="undo_reset" title="ttttt">						
//				<input type="checkbox"	name="resetBox" id="undo_reset" value="2" />δ��λ
//			</label>
//		</li>
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("<li>");
		strBuf.append("<label for=\"eq"+ equipmentId + "\" title=\"" +label+ "\">");//��ʾ��Ϣ
		strBuf.append("<input type=\"checkbox\" id=\"eq" + equipmentId + "\" value=\"" +equipmentId+ "\"");
		strBuf.append(isCheck?" checked=\"checked\"":"");
		strBuf.append(" />");//checkbox 
		//strBuf.append(FunctionUnit.substringByByte(label, 24, "..."));//����ȡ12������  
		strBuf.append(label);//����ȡ12������  
		strBuf.append("</label>");
		strBuf.append("</li>");
		
		return strBuf.toString();
	}
	
	/**
	 * @describe: ����Html��tdԪ����Ϣ	
	 * @param equipmentId : ��������
	 * @param label ��ʾ�ı�ǩ
	 * @param isCheck �Ƿ�ѡ��(true:ѡ�� false:��ѡ��)
	 * @return:  �����pԪ����Ϣ
	 * @date:2009-11-30
	 */
	public static String create_Html_P(String equipmentId, String label, boolean isCheck){
		// ��ʽ����://��������ʾ12����(12���ֽ�)
//		 <li>		
//			<label for="undo_reset" title="ttttt">						
//				<input type="checkbox"	name="resetBox" id="undo_reset" value="2" />δ��λ
//			</label>
//		</li>
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("<li>");
		strBuf.append("<label for=\"eq"+ equipmentId + "\" title=\"" +label+ "\">");//��ʾ��Ϣ
		strBuf.append("<input type=\"checkbox\" id=\"eq" + equipmentId + "\" value=\"" +equipmentId+ "\"");
		strBuf.append(isCheck?" checked=\"checked\"":"");
		strBuf.append(" />");//checkbox 
		//strBuf.append(FunctionUnit.substringByByte(label, 24, "..."));//����ȡ12������  
		strBuf.append(label);//����ȡ12������  
		strBuf.append("</label>");
		strBuf.append("</li>");
		
		return strBuf.toString();
	}
	
	/**
	 * @describe: ��ȡ����������Ķ�Ӧ�б�	
	 * @date:2010-1-19
	 */
	public  Map<String,Map<Integer,BeanForLabel>> area2Equipment(){
		Map<String,Map<Integer,BeanForLabel>> rsMap = new ConcurrentHashMap<String,Map<Integer,BeanForLabel>>();
		List<Workplace> workPlace = getAllWorkPlaceList();	// ��ȡȫ������
		Map<Integer, EquipData> allEquip = getEquiMap();	// ��ȡȫ������
		
		String areaName ;									// ��ʱ���� --������
		EquipData tempEquip = null;					  		// ��ʱ����--��������
		int equipId ;										// ��ʱ����--����ID
		Map<Integer,BeanForLabel> tempArea = null;				// ��ʱ����--�������
		BeanForLabel bean;
		
		for (int i = 0; i < workPlace.size(); i++) {
			areaName = workPlace.get(i).getPlaceName();		// ������
			tempArea = null;	tempArea = new ConcurrentHashMap<Integer, BeanForLabel>();
			for (Entry<Integer, EquipData> entry : allEquip.entrySet()) {
				equipId = entry.getKey();
				tempEquip = null;	tempEquip = entry.getValue();	
				if ( areaName.equals(tempEquip.getPlaceStr()) ){
					bean = new BeanForLabel();
					bean.setAreaName(areaName);
					bean.setDsrsn(tempEquip.getDsrsn());
					bean.setMark(tempEquip.getMark());
					tempArea.put(equipId, bean);
				}
			}
			rsMap.put(areaName, tempArea);
		}
		return rsMap;
	}
	
	public String comName(){
		String str = getSysArgsByKey(BeanForSysArgs.COMPANY_NAME);
		return str;
	}

	/**
	 * �ж� access�������Ƿ�������մ���<br>
	 * accessDataEmpty=true ���,������<br>
	 * accessDataEmpty=false ������<br>
	 */
	public  boolean isAccessDataEmpty() {
		return accessDataEmpty;
	}

	/**
	 * ���� access�������Ƿ�������մ���<br> 
	 * accessDataEmpty=true ���,������<br>
	 * accessDataEmpty=false ������<br>
	 */
	public  void setAccessDataEmpty(boolean accessDataEmpty) {
		this.accessDataEmpty = accessDataEmpty;
	}

	/**
	 * ÿ10����,��access���ݿ��в�������޸����ݱ��:
	 * ���1000 * 60 * 10
	 */
	public  long getAccessTimeFalg() {
		return accessTimeFalg;
	}

	/**
	 * ÿ10����,��access���ݿ��в�������޸����ݱ��:
	 * ���1000 * 60 * 10
	 */
	public  void setAccessTimeFalg(long accessTimeFalg) {
		this.accessTimeFalg = accessTimeFalg;
	}
	
	/**
	 * access����������״̬<br/>
	 * accessLinkState=true ��������<br/>
	 * accessLinkState=false ����ʧ��<br/>
	 * Ĭ����TRUE
	 */
	public  boolean isAccessLinkState() {
		return accessLinkState;
	}

	/**
	 * access����������״̬<br/>
	 * accessLinkState=true ��������<br/>
	 * accessLinkState=false ����ʧ��<br/>
	 * Ĭ����TRUE
	 */
	public  void setAccessLinkState(boolean accessLinkState) {
		this.accessLinkState = accessLinkState;
	}
	
	/**
	 * @describe:	ϵͳ�����仯ʱ,����ϵͳ�����Ϣ<br>
	 * @param reArea: �Ƿ����� ������Ϣ<br>
	 * @param reEq:   �Ƿ����� ������Ϣ<br>
	 * @param reAccess: �Ƿ����� Access��Ϣ<br>
	 * @param rsSys: �Ƿ����� ϵͳ��Ϣ<br>
	 * @param rsMinRec: �Ƿ����� ������ʵʱ������ص����ݿ�<br>
	 * @date:2010-3-22
	 */
	public synchronized  void resetSystem(boolean reArea, 
			boolean reEq, boolean reAccess, boolean rsSys, boolean rsMinRec){
		if (reArea){ // �Ƿ����� ������Ϣ
			reSetAllWorkPlaceList();
		}
		if (reEq){ // �Ƿ����� ������Ϣ
			fillEquiMap();
			fillEquiStr();
			reSetSystemEqType();
		}
		if (reAccess){ // �Ƿ����� Access��Ϣ
			if (serviceAccess.deleteAll()){
				setAccessDataEmpty(true);
			};			
		}
		if (rsSys){ // �Ƿ����� ϵͳ��Ϣ
			reSetSysArgs();
		}
		if (rsMinRec){//�Ƿ����� ������ʵʱ������ص����ݿ�
			// ��ɾ��ȫ������
			mainService.truncateMinRecord();
			//MainService.getInstance().deleteMinAll();
			// ���"��ʱ���� ������������", �������ʱ�Ŀ�����
			mainService.insertMinRecord(getEquiList(), 
					Integer.parseInt(getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_LINE)));
		}
	}
	
	/**
	 * �����Ƿ������У��������ڲɼ�����ʱeqDoingDataΪtrue,�ɼ����eqDoingDataΪfalse
	 */
	public synchronized  boolean isEqDoingData() {
		return eqDoingData;
	}
	/**
	 * �����Ƿ������У��������ڲɼ�����ʱeqDoingDataΪtrue,�ɼ����eqDoingDataΪfalse
	 */
	public synchronized  void setEqDoingData(boolean eqDoingData) {
		this.eqDoingData = eqDoingData;
	}

	/**
	 * @describe:	��Ҫ���ӵ�access�ĵ���
	 * @date:2010-3-26
	 */
	public  int getAccessLinkCount() {
		return accessLinkCount;
	}
	
	/**
	 * ����ģ���Ƿ��ڴ�������. ��������ʱsmsDoingDataΪtrue,�������smsDoingDataΪfalse 
	 */
	public synchronized boolean isSmsDoingData() {
		return smsDoingData;
	}
	/**
	 * ����ģ���Ƿ��ڴ�������. ��������ʱsmsDoingDataΪtrue,�������smsDoingDataΪfalse 
	 */	
	public synchronized void setSmsDoingData(boolean smsDoingData) {
		this.smsDoingData = smsDoingData;
	}
	
}