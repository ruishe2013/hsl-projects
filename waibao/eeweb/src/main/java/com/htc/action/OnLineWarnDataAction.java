package com.htc.action;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.googlecode.jsonplugin.annotations.JSON;
import com.htc.bean.BeanForOnlineWarn;
import com.htc.bean.BeanForSysArgs;
import com.htc.common.FunctionUnit;
import com.htc.domain.*;
import com.htc.model.*;

/**
 * @ OnLineWarnDataAction.java
 * ���� : ���߱������ݵ�Ԫaction.
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-5     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class OnLineWarnDataAction extends AbstractAction {

	// ������
	private MainService mainService;
	
	// ҳ����ʾ
	//private Map<Integer, Record> warnMap = new ConcurrentHashMap<Integer, Record>();// ��ǰ�����б�-δ����
	//private List<BeanForOnlineWarn> warnList = new ArrayList<BeanForOnlineWarn>(); // ��ǰ�����б�-�����
	private List<AlarmRec> warnList = null; // ��ǰ�����б�-�����
	
	//private int tempshowType;						// �¶���ʾ��ʽ(1:���� 2:����)	
	private int palyFalg;							// ������������״̬������������״̬ (1:������  2:���� 3:ϵͳ�رղ���)
	private String alarmPlayFile = ""; 				// ���������ļ�
	private int warnItemCount ; 					// �����б�Ԫ�ظ���
	private int falshTime;							// ҳ��ˢ��ʱ��-ͬ���ڻ�ȡʱ��	
	
	// json ����
	private int alarmId;							// json �������: ������������
	private int doWarnFalg; 						// json �������: ����������(0:�û�δ��½   1:��λ�����ɹ�  2:�Ѿ����˸�λ����)
	
	// ͳ��ϵͳ�����������������: 1:ϵͳ�м����¶�����ʪ������		2:ϵͳ��ȫ�����¶�����	3:ϵͳ��ȫ����ʪ������
	@SuppressWarnings("unused")
	private int systemEqType;		
	
	// ���췽��
	public OnLineWarnDataAction() {
	}
	//ע��service -- spring ioc
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	
	@Override
	public String execute() {
		warnList = mainService.selectUndoRec();
		if (warnList == null){warnList = new ArrayList<AlarmRec>();}
		// �����б�Ԫ�ظ���
		warnItemCount = warnList.size();
		//������������״̬ (1:������  2:���� 3:ϵͳ�رղ���)
		palyFalg = showpalyFalg();
		// ���������ļ�
		alarmPlayFile = commonDataUnit.getWarnPlayFile();
		// ҳ��ˢ��ʱ��-ͬ���ڻ�ȡʱ��
		falshTime = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.DATA_FLASHTIME));
		
		return SUCCESS;
	}
	
	/**
	 * @describe: ��ȡ�¶Ȼ���ʪ�ȵ���ʾ�ַ�	
	 * @param value ���¶Ȼ���ʪ��ֵ
	 * @param alarmtype ����������
	 * @param type :[0:ʪ�� 1:���� 2:����] 
	 * 		��Ҫ�����¶���ʾΪ���ϵ�ʱ����(1:���� 2:����),ʪ��ʱ��Ϊ0
	 * @return:
	 * @date:2010-1-7
	 */
	public String catchTempHuimStr(String value,int alarmtype, int type){
		//eg:�¶�[�¶�����+��/F+��������]:�磺15.58����±��� 
		//eg:ʪ��[ʪ������+%+��������]�磺100.58%��ʪ����
		StringBuffer strbuf = new StringBuffer();
		// ��ʪ��ֵ
		if (type == 2){
			strbuf.append(getRealTemperature(value,type)); 
		}else{
			strbuf.append(value);
		}
		// ��λ
		strbuf.append(type==0?"%RH":type==1?"��":"F");
		type = type == 0? 2: 1;
		// ��������
		strbuf.append(getAlarmType(alarmtype,type));
		return strbuf.toString();
	}
	
	/**
	 * @describe: ��ȡ��������״̬ 	
	 * @return: (1:������  2:����)
	 * @date:2010-1-6
	 */
	public int showpalyFalg(){
		int rsInt = 1;
		// ���ж�ϵͳ��û�п�����������
		rsInt = commonDataUnit.getWarnOpenFlag();
		// ��Щ���벻��,��Ϊҳ����û�и��˲��ű�־
//		if (rsInt == 2){
//			// ���жϸ�����û�п�����������
//			ActionContext ctx = ActionContext.getContext();
//			// û�е�¼������
//			if (ctx.getSession().get(paly_SESSION_TAG)==null){
//				rsInt = 1;
//			}else{
//				rsInt = (Integer)ctx.getSession().get(paly_SESSION_TAG);
//			}			
//		}else{
//			rsInt = 3;
//		}		
		rsInt = rsInt == 2? 2:1;
		return rsInt;
	}
	
	/**
	 * @describe: �������˲��ű�־ -- ���ڴ���
	 * @date:2009-11-5
	 */
	public String openBackSound(){
		palyFalg = 2;		// (1:������ 2:����)
//		ActionContext ctx = ActionContext.getContext();
//		ctx.getSession().put(paly_SESSION_TAG, palyFalg);
		// ��ȡ��ʱ��������
		execute();
		return SUCCESS;
	}
	
	/**
	 * @describe: �رո��˲��ű�־ (1:������ 2:����) -- ���ڴ���
	 * @date:2009-11-5
	 */
	public String closeBackSound(){ 
		palyFalg = 1;		// (1:������ 2:����)		
//		ActionContext ctx = ActionContext.getContext();
//		ctx.getSession().put(paly_SESSION_TAG, palyFalg);
		// ��ȡ��ʱ��������
		execute();
		return SUCCESS;
	}
	
	/**
	 * @describe: ������,��λ���޸����ݿ���Ӧ��¼ 
	 * ��Ӧ: Action : doWarnJson 
	 * 		 jsp : doWarn
	 * @date:2009-11-5
	 */
	public String doWarnJson() {
		String userName = getSysUserName();
		if (userName != null){
			// effectRow��ʾ���ݿ�Ӱ��ĺ���(1:��ʾ�޸ĳɹ� 0:��ʾ�Ѿ��޸Ĺ���)
			int effectRow = mainService.updateAlarmRec(getUpdateAlarmBean(alarmId,userName));
			if (effectRow == 0){
				doWarnFalg = 2;//(0:�û�δ��½   1:��λ�����ɹ�  2:�Ѿ����˸�λ����)
			}else if (effectRow == 1){
				doWarnFalg = 1;//(0:�û�δ��½   1:��λ�����ɹ�  2:�Ѿ����˸�λ����)
			}
		}else{
			doWarnFalg = 0;//(0:�û�δ��½   1:��λ�����ɹ�  2:�Ѿ����˸�λ����)
		}
		return SUCCESS;
	}
	
	/**
	 * @describe: ��ȡ��Ҫ�����AlarmBean		
	 * @param alarmId �������
	 * @param username	�����û�
	 * @date:2009-12-12
	 */
	public AlarmRec getUpdateAlarmBean(int alarmId,  String username){
		AlarmRec alarm = new AlarmRec();
		alarm.setAlarmEnd(new Date().getTime());// ����ʱ��201645
		alarm.setAlarmmode(AlarmRec.PC_MODE);	// ����ģʽ
		alarm.setState(1);						// ����״̬:1:�Ѹ�λ	2��δ��λ		
		alarm.setUsername(username);			// �����û�
		
		alarm.setAlarmId(alarmId);				// �������(���ݿ�����)
		return alarm;
	}
	
	/**
	 * @describe: �����¶���ʾ����,��ʾ�¶�ֵ
	 * @param temp: ���ϵ��¶�ֵ
	 * @param tempType: �¶���ʾ��ʽ(0:ʪ�� 1:���� 2:����)
	 * @return
	 * @date:2009-12-17
	 */
	public String getRealTemperature(String temp, int tempType){
		return WarnDataAction.getRealTemperature(temp, tempType);
	}	
	
	/**
	 * @describe:	��ȡ ���� �ַ�
	 * s:property	value="@com.htc.action.OnLineWarnDataAction@getAlarmType(value.alarmtype,1)"
	 * ���ñ�action��ҳ��Ҳ����:property	value="getAlarmType(value.alarmtype,1)"
	 * @param alarmtype  ��������
	 * @param type (1:�¶�  2:ʪ��)
	 * @date:2009-11-5
	 */
	public String getAlarmType(int  alarmtype,int type){
		return WarnDataAction.getAlarmType(alarmtype, type);
	}
	
	/**
	 * @describe: ��ȡ �����������¶�,ʪ�ȷ���
	 * s:property	value="@com.htc.action.OnLineWarnDataAction@getAreaString(value.equipmentId)"
	 * @param equipmentId ����ID
	 * @date:2009-11-5
	 */
	public String getAreaString(int  equipmentId){
		//return WarnDataAction.getAreaString(equipmentId);
		EquipData eq = commonDataUnit.getEquipByID(equipmentId);
		StringBuffer rsStrBuf = new StringBuffer();
		
		if (eq == null){
			rsStrBuf.append("-");
		}else{
			// �¶���ʾ��ʽ(1:���� 2:����)
			String str = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE))==1?"��":"F";
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
	 * @describe:
	 * ͳ��ϵͳ�����������������: 
	 * 		1:ϵͳ�м����¶�����ʪ������		
	 * 		2:ϵͳ��ȫ�����¶�����	
	 * 		3:ϵͳ��ȫ����ʪ������
	 * @return
	 * @date:2010-1-23
	 */
	@JSON(deserialize=false,serialize=false)
	public int getSystemEqType() {
		// ���� systemEqType
		return commonDataUnit.getSystemEqType();
	}
	// ----------------����Ϊget��set����----------------
	public int getPalyFalg() {
		return palyFalg;
	}
	
	public void setPalyFalg(int palyFalg) {
		this.palyFalg = palyFalg;
	}
	
	public List<AlarmRec> getWarnList() {
		return warnList;
	}

	@JSON(deserialize=false,serialize=false)
	public int getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}

	public int getDoWarnFalg() {
		return doWarnFalg;
	}

	public void setDoWarnFalg(int doWarnFalg) {
		this.doWarnFalg = doWarnFalg;
	}

	@JSON(deserialize=false,serialize=false)
	public String getAlarmPlayFile() {
		return alarmPlayFile;
	}	
	
	public void setAlarmPlayFile(String alarmPlayFile) {
		this.alarmPlayFile = alarmPlayFile;
	}

	public int getWarnItemCount() {
		return warnItemCount;
	}

	public void setWarnItemCount(int warnItemCount) {
		this.warnItemCount = warnItemCount;
	}
	public int getFalshTime() {
		return falshTime;
	}
	
}
