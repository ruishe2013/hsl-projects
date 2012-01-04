package com.htc.action;

import java.text.SimpleDateFormat;
import java.util.*;

import com.htc.bean.BeanForSysArgs;
import com.htc.common.*;
import com.htc.domain.*;
import com.htc.model.*;

/**
 * @ WarnDataAction.java
 * ���� : �������ݲ�ѯaction.
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class WarnDataAction extends AbstractAction {

	// ������
	private MainService mainService;
	
	// ҳ����ʾ
	private List<AlarmRec> alarmRecList;			// �����Ϣ�б�
	private Map<String, String> areaToEqList;		// ҳ���ַѡ���õ�map(������,�����е������б�)
	private int maxSelector;						// ҳ���������ʾ�ĵ�ַ����	
	private int max_rs_alarm;						// ҳ���������ʾ��ʱ������-��ʱ
	
	// ҳ�洫�ݵĲ���
	private AlarmRec alarmRec;						// ҳ����ʾBean
	private String placeListids="";					// ѡ�е��б�
	private String timeFrom;						// ��ʼʱ��:��ʽ��2009-5-5 12:11:12--sql��ѯ������
	private String timeTo;							// ����ʱ��
	private String fromTime;						// ��ʼʱ��:��ʽ��2009��5��5�� 12ʱ11��12��--ҳ�淵�ر�����
	private String toTime;							// ����ʱ��
	private int maxcount;							// ���ص�����¼��
	private int tempshowType;						// �¶���ʾ��ʽ(1:���� 2:����)	
	
	private String doSetPerson;						// ����������Ա
	private int doperson=0;							// �Ƿ�ѱ���������Ա������������- 0:������ 1:���� 		
	private int resetBox;							// ��λ״̬-1:�Ѹ�λ 2:δ��λ 
	private int timeBox;							// ʱ������-1:����ʱ�� 2:��λʱ�� 
	private int orderBox;							// ��������-1:���� 2:����
	private int[] warnBoxForTemp;					// ��������-2:���� 1:���� 
	private int[] warnBoxForHumi;					// ��������-20:��ʪ 10:��ʪ 
	
	// ��������-ϸ��
	private String headTitle;				 
	private String headDetail;
	
	// ͳ��ϵͳ�����������������: 1:ϵͳ�м����¶�����ʪ������		2:ϵͳ��ȫ�����¶�����	3:ϵͳ��ȫ����ʪ������
	@SuppressWarnings("unused")
	private int systemEqType;		
	
	
	// ���췽��
	public WarnDataAction() {
	}
	//ע��service -- spring ioc
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}	

	@Override
	public String execute() {
		
		//ҳ���ַѡ���õ�map(������,�����е������б�)
		areaToEqList = commonDataUnit.createEqAreaWithNOSel(null);	
		// ҳ���������ʾ�ĵ�ַ����
		maxSelector = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_SELECT_COUNT_ALARM));
		// ҳ��ʱ��ѡ�������ֵ
		max_rs_alarm = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_ALARM));
		
		return SUCCESS;
	}
	
	/**
	 * @describe: ���ӵ���־
	 * @date:2009-12-22
	 */
	public void insertToLog(){
		StringBuffer strbuf = new StringBuffer();
		strbuf.append(getSysUserName()+":��ѯ");
		strbuf.append("��������");
		strbuf.append("  ");
		strbuf.append("ʱ���:");
		strbuf.append(timeFrom);
		strbuf.append("~");
		strbuf.append(timeTo);
		//MainService mainService = new MainService();
		mainService.packTlog(TLog.SEARCH_LOG, strbuf.toString());
	}
	
	// �г��б�
	public void list() {
		insertToLog();
		
		StringBuffer alarmtypeStrbuf = new StringBuffer();
		
		try {
			// ����:��������
			if (warnBoxForTemp != null){
				for (int i = 0; i < warnBoxForTemp.length; i++) {
					alarmtypeStrbuf.append(warnBoxForTemp[i]);
					alarmtypeStrbuf.append(",");
				}
			}
			if (warnBoxForHumi != null){
				for (int j = 0; j < warnBoxForHumi.length; j++) {
					alarmtypeStrbuf.append(warnBoxForHumi[j]);
					alarmtypeStrbuf.append(",");
				}
			}
			if ((warnBoxForTemp != null) && (warnBoxForHumi != null)) {
				for (int i = 0; i < warnBoxForTemp.length; i++) {
					for (int j = 0; j < warnBoxForHumi.length; j++) {
						alarmtypeStrbuf.append(warnBoxForTemp[i] + warnBoxForHumi[j]);
						alarmtypeStrbuf.append(",");
					}
				}
			}
			
			//ʵ����������
			//mainService = mainService == null ? new MainService() : mainService;
			
			if (alarmRec == null){
				alarmRec = new AlarmRec();
			}
			// 1:Ҫ�������������� -- ȥ�� placeListids ���һ������,js���Ѿ�����
			// placeListids = placeListids.substring(0,placeListids.length() - 1);
			alarmRec.setPlaceList(placeListids);
			// 2:��ʼʱ��
			// 3:����ʱ��
			SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);
			alarmRec.setAlarmStart(dateFormat.parse(timeFrom).getTime());
			alarmRec.setAlarmEnd(dateFormat.parse(timeTo).getTime());
			// 4:�ܷ��ص�����¼�� 
			alarmRec.setPageSize(maxcount);
			// 5:������Ա
			if ( (doperson==1)&& (doSetPerson!=null)&&(doSetPerson.trim().length() > 0) ){
				alarmRec.setUsername(doSetPerson);
			}
			// 6:��λ״̬[1:�Ѹ�λ	2��δ��λ]
			alarmRec.setState(resetBox);
			// 7:ʱ������[1:(������ʱ������) 2:(����λʱ������)]
			alarmRec.setWhichToSearch(timeBox);
			// 8:��������[1:���� 2:����]
			alarmRec.setAscOrDesc(orderBox==1?"ASC":"DESC");
			// 9:��������[2:���� 1:���� 20:��ʪ 10:��ʪ]--ȥ�����һ������
			alarmRec.setAlarmtypeStr(alarmtypeStrbuf.substring(0, alarmtypeStrbuf.length()-1));
			// 10:����ģʽ[PC ,GPRS(1��2)] -- �ȹ̶�
			//alarmRec.setAlarmmode(1);
			
			// �����������,������¼
			alarmRecList = mainService.getAllAlarmRec(alarmRec);
			
			// ���ñ���
			// headTitle��ʽ:��˾�� -�������ݲ�ѯ��� 
			headTitle = commonDataUnit.getSysArgsByKey(BeanForSysArgs.COMPANY_NAME) + "-�������ݲ�ѯ��� ";
			// headDetail��ʽ:ʱ�䷶Χ: 2009��11��06�� 12ʱ~ 2009��11��06�� 23ʱ		
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("ʱ�䷶Χ:");
			strbuf.append(fromTime);
			strbuf.append("~");
			strbuf.append(toTime);
			headDetail = strbuf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ��ȡ�б�
	public String getWarnList() {
		list();
		if (alarmRecList.size() == 0){// û������������
			nodataInfo();
			return NODATARETURN;
		}else{
			// �¶���ʾ��ʽ(1:���� 2:����)	
			tempshowType = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE));	
			return PRINTOUT;
		}
	}

	/**
	 * @describe: û������ʱ��ʾ����Ϣ
	 * @date:2009-12-10
	 */
	private void nodataInfo() {
		StringBuffer strbuf = new StringBuffer();
		headTitle = "��������������¼...";
		strbuf.append("����:");
		strbuf.append("<br/>");
		strbuf.append("<label>");
		strbuf.append("    �������ʼʱ��ͽ���ʱ��");
		strbuf.append("</label><br/>");
		strbuf.append("<label>");
		strbuf.append("    ��ѡ���ʵ�������");
		strbuf.append("</label><br/>");
		strbuf.append("<label>");
		strbuf.append("    ������ı�������");
		strbuf.append("</label><br/>");
		strbuf.append("<label>");
		strbuf.append("    ��һ����λ��Ա");
		strbuf.append("</label>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("*���������,��Ȼû�в�ѯ���,��ô���ʱ����ȷʵû�����ݼ�¼����...");
		headDetail = strbuf.toString();
	}
	
	// ɾ��������¼
	public String delete() {
		//ʵ����������
		//mainService = mainService == null ? new MainService() : mainService;
		
		mainService.deleteAlarmRecById(alarmRec.getAlarmId());
		list();
		return PRINTOUT;
	}

	/**
	 * @describe: �����¶���ʾ����,��ʾ�¶�ֵ
	 * @param temp: ���ϵ��¶�ֵ
	 * @param tempType: �¶���ʾ��ʽ(0:ʪ�� 1:���� 2:����)
	 * @return
	 * @date:2009-12-17
	 */
	public static String getRealTemperature(String temp, int tempType){
		float tempfloat = 0;
		if (tempType == 2){// �¶���ʾ��ʽ(1:���� 2:����)
			tempfloat = Float.parseFloat(temp) *9/5 + 32;
			temp = FunctionUnit.FLOAT_DATA_FORMAT.format(tempfloat);
		}
		return temp;
	}
	
	/**
	 * @describe:	��ȡ ���� �ַ�
	 * @param alarmtype ��������
	 * @param type(1:�¶�  2:ʪ��)
	 * @date:2009-11-5
	 */
	public static String getAlarmType(int  alarmtype,int type){
		String rsStr = "";
		if (type == 1){//�¶�״̬(���±���,���±���)
			if (alarmtype % 10 == 1){
				rsStr = "���±���";
			}else if ( alarmtype % 10 == 2){
				rsStr = "���±���";
			}else{
				rsStr = "����";
			}
		}else if(type == 2){//ʪ��״̬ (��ʪ����,��ʪ����)
			if ( (alarmtype < 20) && (alarmtype >=10) ){
				rsStr += "��ʪ����";
			}else if ( alarmtype >= 20){
				rsStr += "��ʪ����";
			}else{
				rsStr = "����";
			}
		}
		return rsStr;
	}
	
	/**
	 * @describe: ��ȡ ����������Χ. ��:1.0~10.0��/0.0~100.0%RH
	 * @param equipmentId ����ID
	 * @date:2009-11-5
	 */
	public String getAreaString(int  equipmentId){
		return commonDataUnit.getAreaString(equipmentId);
	}

	
	/**
	 * @describe: �Ӻ�������ȡʱ�����͵��ַ���
	 * @date:2010-1-24
	 */
	public static String getDateStrFromMills(long mills){
		String dtemp = null;
		dtemp = FunctionUnit.mill2DateStr(mills, "yyyy-MM-dd HH:mm:ss");
		return dtemp;
	} 	
	
	/**
	 * @describe: �õ���ַlist
	 * @param placeListStr ���Ÿ�����ID
	 * @date:2009-11-6
	 */
	public List<Integer> getAddressList(String placeListStr){
		String [] strArray = placeListStr.split(",");
		List<Integer> intList = new ArrayList<Integer>();
		for (int i = 0; i < strArray.length; i++) {
			intList.add(Integer.parseInt(strArray[i].trim()));
		}
		return intList;
	}
	/**
	 * @describe:
	 * ͳ��ϵͳ�����������������: 
	 * 		1:ϵͳ�м����¶�����ʪ������		
	 * 		2:ϵͳ��ȫ�����¶�����	
	 * 		3:ϵͳ��ȫ����ʪ������
	 * @return
	 * @date:2010-1-23
	 */
	public int getSystemEqType() {
		// ���� systemEqType
		return commonDataUnit.getSystemEqType();
	}
	//----------------��ͨ get��set����----------------
	//�¶���ʾ��ʽ(1:���� 2:����)
	public int getTempshowType(){
		return tempshowType;
	}	
	
	public List<AlarmRec> getAlarmRecList() {
		return alarmRecList;
	}

	public Map<String, String> getAreaToEqList() {
		return areaToEqList;
	}

	public int getMaxSelector() {
		return maxSelector;
	}

	public int getMax_rs_alarm() {
		return max_rs_alarm;
	}

	public void setAlarmRec(AlarmRec alarmRec) {
		this.alarmRec = alarmRec;
	}

	public void setPlaceListids(String placeListids) {
		this.placeListids = placeListids;
	}

	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public void setMaxcount(int maxcount) {
		this.maxcount = maxcount;
	}

	public String getHeadTitle() {
		return headTitle;
	}

	public String getHeadDetail() {
		return headDetail;
	}

	public void setDoSetPerson(String doSetPerson) {
		this.doSetPerson = doSetPerson;
	}

	public void setResetBox(int resetBox) {
		this.resetBox = resetBox;
	}

	public void setTimeBox(int timeBox) {
		this.timeBox = timeBox;
	}

	public void setOrderBox(int orderBox) {
		this.orderBox = orderBox;
	}

	public void setWarnBoxForTemp(int[] warnBoxForTemp) {
		this.warnBoxForTemp = warnBoxForTemp;
	}

	public void setWarnBoxForHumi(int[] warnBoxForHumi) {
		this.warnBoxForHumi = warnBoxForHumi;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public void setDoperson(int doperson) {
		this.doperson = doperson;
	}

}
