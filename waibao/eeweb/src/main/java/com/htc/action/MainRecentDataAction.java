package com.htc.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.googlecode.jsonplugin.annotations.JSON;
import com.htc.bean.BeanForFlashData;
import com.htc.bean.BeanForFlashSetting;
import com.htc.bean.BeanForPortData;
import com.htc.bean.BeanForSysArgs;
import com.htc.bean.BeanForlBarData;
import com.htc.common.FunctionUnit;
import com.htc.domain.EquipData;
import com.htc.domain.User;
import com.htc.model.MainService;
import com.htc.model.UserService;
import com.htc.model.seriaPort.Level_Second_Serial;
import com.htc.util.CollectionUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * @ MainRecentDataAction.java
 * ���� : ��ʱ���ݹ���action. ����������, ʵʱ����, ���ڵ�������ʾ��ʱ����.
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class MainRecentDataAction extends AbstractAction {

	// ������
	private UserService userService;
	private MainService mainService;
	
	// ��ʱ���� ����������   ����
	private Map<String, String> areaToEqList;		// ҳ���ַѡ���õ�map(������,�����е������б�)
	private String userPlaceList;					// �û���ʹ�õĵ�ַ�б�
	private int palyFalg;							// ������������״̬������������״̬ (1:������  2:���� 3:ϵͳ�رղ���)
	private String alarmPlayFile = ""; 				// ���������ļ�
	
	/**
	 * ����ѡ���ַ�б�ı�ǩ.(1:placeAStr(����)  2:placeBStr(ʵʱ))
	 */
	private int tagFlag;							// ����ѡ���ַ�б�ı�ǩ.(1:placeAStr(����)  2placeBStr(ʵʱ))
	private int falshTime;							// ҳ��ˢ��ʱ��-ͬ���ڻ�ȡʱ��
	private int maxSelector;						// ҳ���������ʾ�ĵ�ַ����
	
	// ��ʱ����
	private String dataStr;							// �����ַ�				
	private String configStr;						// �����ַ�				
	
	// ��������
	private Map<Integer, BeanForlBarData> barData;	// ���ݼ� 		
	private String recTimeStr = "";					// ���¸��¼�¼���ݵ�ʱ��
	
	private Map<Integer, WorkPlaceEntity> workPlaceBarDatas;	// ���ݼ� 		
	
	// ͳ��ϵͳ�����������������: 1:ϵͳ�м����¶�����ʪ������		2:ϵͳ��ȫ�����¶�����	3:ϵͳ��ȫ����ʪ������
	@SuppressWarnings("unused")
	private int systemEqType;		
	
	
	// ���췽��
	public MainRecentDataAction() {
	}
	//ע��service -- spring ioc
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}

	@Override
	public String execute() {
		initPer();	
		doSomeBefore();
		return SUCCESS;
	}
	
	/**
	 * @describe: ��ʼ����Ϣ
	 * @date:2009-12-2
	 */
	 public void initPer(){
		
		User user = getCurrrentUser();
		boolean rsFlag = user != null;
		userPlaceList = "0";
		// �û���ʹ�õĵ�ַ�б� �� ҳ���������ʾ�ĵ�ַ����
		if (tagFlag == 1){
			if (rsFlag){userPlaceList = user.getPlaceAStr();}
			maxSelector = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_SELECT_COUNT_BAR));
		}else if (tagFlag == 2){
			if (rsFlag){userPlaceList = user.getPlaceBStr();}
			maxSelector = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_SELECT_COUNT_LINE));
			
			// ����session
			ActionContext ctx = ActionContext.getContext();
			ctx.getSession().put("user_flash_list", userPlaceList); // falsh��Ҫ��ʾ�ĸ��˵�ַ�б�		
		}
		
		List<Integer> placeList = FunctionUnit.getIntListByStr(userPlaceList);
		//ҳ���ַѡ���õ�map(������,�����е������б�)
		areaToEqList = commonDataUnit.createEqAreaWithNOSel(placeList);
		userPlaceList = userPlaceList.trim().length() == 0?"0":userPlaceList;
	}
	
	/**
	 * @describe: ������������ת��ʵʱ���߻���
	 * @date:2009-12-27
	 */
	public String jumpToLine(){
		String tempStr = userPlaceList;
		initPer();
		// ����session
		ActionContext ctx = ActionContext.getContext();
		ctx.getSession().put("user_flash_list", tempStr); // falsh��Ҫ��ʾ�ĸ��˵�ַ�б�		
		return SUCCESS;
	}
	
	/**
	 * @describe: ��ȡ���ڷ��ʵ��û���	
	 * @return: null��ʾ�û�û�е�¼,�������ݻ�ȡʧ��
	 * @date:2009-11-30
	 */
	@JSON(deserialize=false,serialize=false)
	public User getCurrrentUser(){
		User user  = null;
		//1: ����Ƿ��¼,δ��¼,���˳�
		String userName = getSysUserName();
		//2: �ж��ǲ����̼�
		userName = userName.equals(FACTORY_NAME) ? OEM_NAME : userName;
		if (userName != null){
			// ʵ����������
			//userService = userService == null ? new UserService() : userService;
			user = userService.getUserByName(userName);
		}		

		return user;
	}
	
	//��״ͼ
	public String barChartJson(){
		fillBarFlashData2();		//��װflash���ݣ�������Ƿ������ݴ���
		return SUCCESS;
	}
	
	/**
	 * @describe: ǰ�ô���:  ��ȡ�û�ʹ�õĵ�ַ�б�
	 * @return �е�ַ : true  �޵�ַ : false 
	 * @throws Exception:
	 * @date:2009-11-4
	 */
	public boolean doSomeBefore(){
		// ҳ��ˢ��ʱ��
		falshTime = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.DATA_FLASHTIME));
		boolean rsbool = userPlaceList == null ? false : !userPlaceList.equals("0");
		return rsbool;
	}
	
	/**
	 * @describe: �޸ĸ��Ի�����(��ַ�б�), ���浽���ݿ�	
	 * @date:2009-11-4
	 */
	public String updateUserConfig() {
		User user = getCurrrentUser();
		if (user != null){
			if (userPlaceList == null){initPer(); return SUCCESS;} // ��ֹ����URL���ֹ��ж�
			// (1:placeAStr(����)  2placeBStr(ʵʱ))
			// userPlaceList = userPlaceList.equals("")?"0":userPlaceList.substring(0, userPlaceList.length()-1);//��js���Ѿ��ض������һ������
			if (tagFlag == 1){
				user.setUpdateFlag(1);//1: ��ʶ�޸�placeAStr
				user.setPlaceAStr(userPlaceList);
			}else if (tagFlag == 2){
				user.setUpdateFlag(2);//2: ��ʶ�޸�placeBStr
				user.setPlaceBStr(userPlaceList);
			}			
			// ʵ����������
			//userService = userService == null ? new UserService() : userService;
			userService.updateUser(user);
			// addActionMessage("�����б�ɹ�...");
		}else{
			// addFieldError("updateconfigFailure", "�����б�ʧ��...");
			// �û�������Ȩ�޼���Ϊ0,��ʾ��Ȩ����
			ActionContext ctx = ActionContext.getContext();
			ctx.getSession().put(user_POWER_TAG, 0);//����Ȩ��session 86������Ȩ��				
		}
		initPer();
		return SUCCESS;
	}
	
	/**
	 * @describe: ����Ƿ������ݴ�����,Ȼ�� ��װbar flash����.
	 * @date:2009-11-5
	 * @deprecated see fillBarFlashData2
	 */
	public void fillBarFlashData(){
		BeanForPortData serialPortDataBean;			// �Ӵ��ڲ��������ݸ�ʽ
		BeanForlBarData serialBarDataBean;			// ��װ��ҳ������ݸ�ʽ
		EquipData equipData ; 						// ������Ϣ
		int tempType;								// ��ʾ�¶�����(1:����	2:����)
		int equipmentId;
		String temp, humi, colorTemp,colorHumi ,label, state, power;
		
		barData = null;		//������
		barData = new TreeMap<Integer, BeanForlBarData>();
		
		// �õ��û�������ʹ�õĵ�ַ�б� --���������
		List<Integer> listPlaces = getAddressList(userPlaceList);
		
		//���ڷ��� -- ��ȡ���ڼ�ʱ����
		//Level_First_Serial  first_Level = Level_First_Serial.getInstance();
		//Map<Integer, BeanForPortData> tempDataBean = first_Level.getAddressData();
		// �����ݿ��ȡ ��������
		Map<Integer, BeanForPortData> tempDataBean = mainService.getNewestMinRec(userPlaceList);
		
		//	��ʾ�¶�����
		tempType = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE));
			
		// ���ݵ�ַ�б�, ��װҪ��ʾ������
		userPlaceList = "";
		recTimeStr = "";
		int len = listPlaces.size();
		int realTemp=0;//��ʵ���¶�ֵ.����ʱ���䡣����ʱҪ��ת��
		for (int i = 0; i < len; i++) {
			
			// ���˲���ȷ������ID
			equipmentId = listPlaces.get(i);//euipmentId
			if (equipmentId == 0) {continue;}
			
			// ���������Ľ����ֵ��userPlaceList,����ҳ����ܰ�����˳����ȷ��ʾ
			userPlaceList += equipmentId + ((i+1) == len? "" : ",");
			
			// �õ�ѡ��������Ϣ
			equipData = null;
			equipData = commonDataUnit.getEquipByID(equipmentId);
			
			// ��ȡ�������ݼ���
			serialPortDataBean = null;
			serialPortDataBean = tempDataBean == null ? null : 
					tempDataBean.containsKey(equipmentId) ? tempDataBean.get(equipmentId) : null;
			serialPortDataBean = serialPortDataBean == null? null : serialPortDataBean.getMark()==0? 
					null: serialPortDataBean;
					
			// ����bean
			serialBarDataBean = null ;
			serialBarDataBean = new BeanForlBarData(); 
			
			// ��װ����  �� ҳ������
			label = null; temp = null; humi = null; colorTemp = null; colorHumi = null; state = null;
			serialBarDataBean.setEquipmentId(equipmentId);										// ����--�Ѵ���bean����
			label = commonDataUnit.getEquiMapStrById(equipmentId);
//			label = FunctionUnit.substringByByte( 			
//					commonDataUnit.getEquiMapStrById(equipmentId), 34, "...");					// ��ǩ -- ������ҳ���д���
			serialBarDataBean.setLabel(label);
			temp = serialPortDataBean == null ? "-" : equipData.getEquitype() == EquipData.TYPE_HUMI_ONLY?"-" :
				getTempHumiStr(serialPortDataBean.getTemp(), tempType);	// �¶�
			realTemp = temp.equals("-")?0:(int) (Float.parseFloat(temp)*100);
			serialBarDataBean.setTemp(temp);
			serialBarDataBean.setAppt(tempType == 1 ? "��" : "F"); 		//�¶���ʾ��׺(1:���� 2:����)
			humi = serialPortDataBean == null ? "-" : equipData.getEquitype()== EquipData.TYPE_TEMP_ONLY?"-" :
				getTempHumiStr(serialPortDataBean.getHumi(), 0);		// ʪ��
			serialBarDataBean.setHumi(humi);
			serialBarDataBean.setApph("%RH");							// ʪ����ʾ��׺
			if ( serialPortDataBean == null ){	
				colorTemp = commonDataUnit.getSysArgsByKey(BeanForSysArgs.LOW_COLORDEF); 		// û����������ʾ���±�����ɫ
				colorHumi = commonDataUnit.getSysArgsByKey(BeanForSysArgs.LOW_COLORDEF); 		// û����������ʾ���±�����ɫ
				state = "��������";
				power = equipData == null? "���޵���" : equipData.getShowPower() == 1 ? "���޵���" : "";
			}else{
				colorTemp = equipData.getEquitype() == EquipData.TYPE_HUMI_ONLY?
						commonDataUnit.getSysArgsByKey(BeanForSysArgs.NORMAL_COLORDEF) :
						getColorString(realTemp, 1, equipData );
						
				colorHumi = equipData.getEquitype()== EquipData.TYPE_TEMP_ONLY ?
						commonDataUnit.getSysArgsByKey(BeanForSysArgs.NORMAL_COLORDEF):	
					getColorString(serialPortDataBean.getHumi(), 2, equipData );
						
				state = getStateStr(tempType, realTemp,serialPortDataBean.getHumi(), equipData );
				power = equipData.getShowPower() == 1 ? getPowerString
						(serialPortDataBean.getPowerV(),equipData.getPowerType()) : "";
				serialBarDataBean.setStateInt(serialPortDataBean.getState());			//�����ô�(����)
				//���¸��¼�¼���ݵ�ʱ��
				if ( recTimeStr.length() < 5 ){
					recTimeStr = FunctionUnit.getTime_Long_Str(serialPortDataBean.getRecTime());
				}
			}
			serialBarDataBean.setPower(power);												// ����
			serialBarDataBean.setColorTemp(colorTemp);										// �¶���ɫ		
			serialBarDataBean.setColorHumi(colorHumi);										// ʪ����ɫ		
			serialBarDataBean.setStateStr(state);											// ״̬
			//��������
			barData.put(equipmentId, serialBarDataBean); 
		}//end for
	}
	
	public static class WorkPlaceEntity {
		String name;
		List<BeanForlBarData> barDatas = CollectionUtil.newArrayList();
		public WorkPlaceEntity(String name) {
			super();
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<BeanForlBarData> getBarDatas() {
			return barDatas;
		}
		public void addData(BeanForlBarData beanForlBarData) {
			barDatas.add(beanForlBarData);
		}
	}
	
	public void fillBarFlashData2(){
		BeanForPortData serialPortDataBean;			// �Ӵ��ڲ��������ݸ�ʽ
		BeanForlBarData serialBarDataBean;			// ��װ��ҳ������ݸ�ʽ
		EquipData equipData ; 						// ������Ϣ
		int tempType;								// ��ʾ�¶�����(1:����	2:����)
		int equipmentId;
		String temp, humi, colorTemp,colorHumi ,label, state, power;
		
		barData = null;		//������
		barData = new TreeMap<Integer, BeanForlBarData>();
		workPlaceBarDatas = CollectionUtil.newTreeMap();
		
		// �õ��û�������ʹ�õĵ�ַ�б� --���������
		List<Integer> listPlaces = getAddressList(userPlaceList);
		
		//���ڷ��� -- ��ȡ���ڼ�ʱ����
		//Level_First_Serial  first_Level = Level_First_Serial.getInstance();
		//Map<Integer, BeanForPortData> tempDataBean = first_Level.getAddressData();
		// �����ݿ��ȡ ��������
		Map<Integer, BeanForPortData> tempDataBean = mainService.getNewestMinRec(userPlaceList);
		
		//	��ʾ�¶�����
		tempType = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE));
			
		// ���ݵ�ַ�б�, ��װҪ��ʾ������
		userPlaceList = "";
		recTimeStr = "";
		int len = listPlaces.size();
		int realTemp=0;//��ʵ���¶�ֵ.����ʱ���䡣����ʱҪ��ת��
		
		Set<Integer> workPlaceIdSet = new HashSet<Integer>();
		
		for (int i = 0; i < len; i++) {
			
			// ���˲���ȷ������ID
			equipmentId = listPlaces.get(i);//euipmentId
			if (equipmentId == 0) {continue;}
			
			// ���������Ľ����ֵ��userPlaceList,����ҳ����ܰ�����˳����ȷ��ʾ
			userPlaceList += equipmentId + ((i+1) == len? "" : ",");
			
			// �õ�ѡ��������Ϣ
			equipData = null;
			equipData = commonDataUnit.getEquipByID(equipmentId);
			if (equipData != null) {
				workPlaceIdSet.add(equipData.getPlaceId());
			}
			
			// ��ȡ�������ݼ���
			serialPortDataBean = null;
			serialPortDataBean = tempDataBean == null ? null : 
					tempDataBean.containsKey(equipmentId) ? tempDataBean.get(equipmentId) : null;
			serialPortDataBean = serialPortDataBean == null? null : serialPortDataBean.getMark()==0? 
					null: serialPortDataBean;
					
			// ����bean
			serialBarDataBean = null ;
			serialBarDataBean = new BeanForlBarData(); 
			
			// ��װ����  �� ҳ������
			label = null; temp = null; humi = null; colorTemp = null; colorHumi = null; state = null;
			serialBarDataBean.setEquipmentId(equipmentId);							// ����--�Ѵ���bean����
			if (equipData != null) {
				serialBarDataBean.setAddress(equipData.getAddress());
			}
			label = commonDataUnit.getEquiMapStrById(equipmentId);
//			label = FunctionUnit.substringByByte( 			
//					commonDataUnit.getEquiMapStrById(equipmentId), 34, "...");					// ��ǩ -- ������ҳ���д���
			serialBarDataBean.setLabel(label);
			temp = serialPortDataBean == null ? "-" : equipData.getEquitype() == EquipData.TYPE_HUMI_ONLY?"-" :
				getTempHumiStr(serialPortDataBean.getTemp(), tempType);	// �¶�
			realTemp = temp.equals("-")?0:(int) (Float.parseFloat(temp)*100);
			serialBarDataBean.setTemp(temp);
			serialBarDataBean.setAppt(tempType == 1 ? "��" : "F"); 		//�¶���ʾ��׺(1:���� 2:����)
			humi = serialPortDataBean == null ? "-" : equipData.getEquitype()== EquipData.TYPE_TEMP_ONLY?"-" :
				getTempHumiStr(serialPortDataBean.getHumi(), 0);		// ʪ��
			serialBarDataBean.setHumi(humi);
			serialBarDataBean.setApph("%RH");							// ʪ����ʾ��׺
			if ( serialPortDataBean == null ){	
				colorTemp = commonDataUnit.getSysArgsByKey(BeanForSysArgs.LOW_COLORDEF); 		// û����������ʾ���±�����ɫ
				colorHumi = commonDataUnit.getSysArgsByKey(BeanForSysArgs.LOW_COLORDEF); 		// û����������ʾ���±�����ɫ
				state = "��������";
				power = equipData == null? "���޵���" : equipData.getShowPower() == 1 ? "���޵���" : "";
			}else{
				colorTemp = equipData.getEquitype() == EquipData.TYPE_HUMI_ONLY?
						commonDataUnit.getSysArgsByKey(BeanForSysArgs.NORMAL_COLORDEF) :
						getColorString(realTemp, 1, equipData );
						
				colorHumi = equipData.getEquitype()== EquipData.TYPE_TEMP_ONLY ?
						commonDataUnit.getSysArgsByKey(BeanForSysArgs.NORMAL_COLORDEF):	
					getColorString(serialPortDataBean.getHumi(), 2, equipData );
						
				state = getStateStr(tempType, realTemp,serialPortDataBean.getHumi(), equipData );
				power = equipData.getShowPower() == 1 ? getPowerString
						(serialPortDataBean.getPowerV(),equipData.getPowerType()) : "";
				serialBarDataBean.setStateInt(serialPortDataBean.getState());			//�����ô�(����)
				//���¸��¼�¼���ݵ�ʱ��
				if ( recTimeStr.length() < 5 ){
					recTimeStr = FunctionUnit.getTime_Long_Str(serialPortDataBean.getRecTime());
				}
			}
			serialBarDataBean.setPower(power);												// ����
			serialBarDataBean.setColorTemp(colorTemp);										// �¶���ɫ		
			serialBarDataBean.setColorHumi(colorHumi);										// ʪ����ɫ		
			serialBarDataBean.setStateStr(state);											// ״̬
			//��������
			//barData.put(equipmentId, serialBarDataBean); 
			appendBarData(workPlaceBarDatas, equipData.getPlaceId(), serialBarDataBean, equipData.getPlaceStr());
		}//end for
		// sort by address
		sortDataByAddress();
	}
	
	private void sortDataByAddress() {
		for (Map.Entry<Integer, WorkPlaceEntity> entry : workPlaceBarDatas.entrySet()) {
			WorkPlaceEntity workPlaceEntity = entry.getValue();
			if (workPlaceEntity == null) {
				continue;
			}
			List<BeanForlBarData> datas = workPlaceEntity.getBarDatas();
			Collections.sort(datas, new Comparator<BeanForlBarData>(){

				@Override
				public int compare(BeanForlBarData o1, BeanForlBarData o2) {
					if (o1 == null && o1 == null) {
						return 0;
					}
					if (o1 == null) {
						return -1;
					}
					if (o2 == null) {
						return 1;
					}
					return o1.getAddress() - o2.getAddress();
				}
				
			});
		}
	}
	
	private void appendBarData(Map<Integer, WorkPlaceEntity> workPlaceBarData, int workPlace, BeanForlBarData beanForlBarData, String placeName) {
		if (!workPlaceBarData.containsKey(workPlace)) {
			workPlaceBarData.put(workPlace, new WorkPlaceEntity(placeName));
		}
		WorkPlaceEntity entity = workPlaceBarData.get(workPlace);
		entity.addData(beanForlBarData);
	}
	
	/**
	 * @describe:	��ȡ�����ַ���	
	 * @param powerV powerV ����
	 * @param type ��������(��������[1:1.5v,2:3.6v])
	 * @return:
	 * @date:2009-12-28
	 */
	private String getPowerString(float powerV,int type) {
		String rsOut= "��������";
		if (type == 1){//��������:1200<Power<2000
			if ((powerV > 1200) && (powerV < Level_Second_Serial.POWER_1_5)){
				rsOut = "��������";
			}
		}else if (type == 2){//��������:power>3000
			if (powerV > Level_Second_Serial.POWER_3_6){
				rsOut = "��������";
			}			
		}
		return rsOut;
	}

	/**
	 * @describe: �¶Ȼ���ʪ���ַ���/100 + ƫ��
	 * @param tempOrHuim int�����¶Ȼ���ʪ������ 
	 * @param type �¶�ʱ��ʾ(1:����  2:����	 0:����ʪ��)
	 * @date:2009-11-5
	 */
	public String getTempHumiStr(float tempOrHuim, float type){
		// F=(C*9/5)+32	��=�����ϱ�ʾ���¶�  F=�û��ϱ�ʾ���¶�
		String outStr ;
		float floatValue;
		if (type == 2){
			floatValue = tempOrHuim / (float) 100;
			floatValue = floatValue * 9/5 + 32;
		}else{
			floatValue = tempOrHuim / (float) 100;
		}
		outStr = FunctionUnit.FLOAT_DATA_FORMAT.format(floatValue);
		return outStr;
	}
	
	/**
	 * @describe: ��ȡ��ǩ��ɫ�ַ���	
	 * @param tempHumi : ��ʪ��ֵ
	 * @param type (����1:�¶� 2:ʪ��)
	 * @param equipData ��������Ϣ
	 * @return: ��ǩ��ɫ
	 * @date:2009-11-5
	 */
	public String getColorString(float tempHumi, int type, EquipData equipData){
		String outStr = "";
		float tmpInt=0, tmpDown=0, tmpUp = 0;

		tmpInt = tempHumi /(float) 100 ;
		tmpDown = type == 1 ? equipData.getTempDown() : equipData.getHumiDown();
		tmpUp = type == 1 ? equipData.getTempUp() : equipData.getHumiUp();
		
		if( tmpInt < tmpDown ){
			// ���ڷ�Χʱ��ʾ����ɫ
			outStr = commonDataUnit.getSysArgsByKey(BeanForSysArgs.LOW_COLORDEF);	
		}else if ( tmpInt > tmpUp ){
			// ���ڷ�Χʱ��ʾ����ɫ
			outStr=  commonDataUnit.getSysArgsByKey(BeanForSysArgs.HIGH_COLORDEF);
		}else{
			// ������ɫ
			outStr = commonDataUnit.getSysArgsByKey(BeanForSysArgs.NORMAL_COLORDEF);
		}
		return outStr;
	}
	
	/**
	 * @describe: ��ȡ״̬�ַ���. ��: �¶�, ʪ���쳣
	 * @param type �¶�ʱ��ʾ(1:����  2:����)
	 * @param temp �¶�
	 * @param humi ʪ��
	 * @param equipData ������Ϣ
	 * @return: ����״̬���
	 * @date:2009-11-5
	 */
	public String getStateStr( int type, float temp, float humi, EquipData equipData){
		float tmpFloat;
		int errorFlag = 0;
		String outStr = "";
		int equitype = equipData.getEquitype();// 1,'��ʪ��';2,'���¶�';3,'��ʪ��'
		
		if (equitype!=3){
			// �¶ȼ���ƫ�������¶Ƚ��޻���,������ǵ��±���
			tmpFloat =  equipData.getTempDown() - temp/(float) 100 ;  
			if ( tmpFloat > 0){ 	// �¶�ƫ��
				//tmpFloat = type == 1 ? tmpFloat : (tmpFloat * 9/5 + 32); 
				//outStr = "�¶�ƫ��:"+FunctionUnit.FLOAT_DATA_FORMAT.format(tmpFloat)+(type==1?"��":"F");
				outStr = "�¶�ƫ��";
				errorFlag += 1 ;
			}
			// �¶ȼ���ƫ�������¶Ƚ��޻���,������Ǹ��±���
			tmpFloat = temp/(float) 100 - equipData.getTempUp();  
			if ( tmpFloat > 0){ 	// �¶�ƫ��
				//tmpFloat = type == 1 ? tmpFloat : (tmpFloat * 9/5 + 32); 
				//outStr = "�¶�ƫ��:"+FunctionUnit.FLOAT_DATA_FORMAT.format(tmpFloat)+(type==1?"��":"F");
				outStr = "�¶�ƫ��";
				errorFlag += 1 ;
			}
		}
		
		if (equitype!=2){
			// ʪ�ȼ���ƫ������ʪ�Ƚ��޻���,������ǵ�ʪ����
			tmpFloat =  equipData.getHumiDown() - humi/(float) 100 ;  
			if (tmpFloat > 0){	// ʪ��ƫ��
				errorFlag += 1 ;
				//outStr += "ʪ��ƫ��:" + FunctionUnit.FLOAT_DATA_FORMAT.format(tmpFloat) + "%";			
				outStr += " ʪ��ƫ��";			
			}
			// ʪ�ȼ���ƫ������ʪ�Ƚ��޻���,������Ǹ�ʪ����
			tmpFloat =  (humi/(float) 100 - equipData.getHumiUp());  
			if (tmpFloat > 0){	// ʪ��ƫ��
				errorFlag += 1 ;
				// outStr += "ʪ��ƫ��:" + FunctionUnit.FLOAT_DATA_FORMAT.format(tmpFloat) + "%";			
				outStr += " ʪ��ƫ��";			
			}
		}
		
		if (errorFlag == 0){
			outStr = "��������";
		}
		return outStr;
	}
	
	// flash �¶ȶ�̬����
	public String tempData(){
		dataStr = flashData(1); // (1:�¶� 2:ʪ��)
		return SUCCESS;
	}
	
	// flash �¶ȶ�̬����	
	public String tempConfig(){
		configStr = flashConfig(1); // (1:�¶� 2:ʪ��)
		return SUCCESS;
	}
	
	// flash ʪ�ȶ�̬����	
	public String humiData(){
		dataStr = flashData(2); // (1:�¶� 2:ʪ��)
		return SUCCESS;
	}
	// flash ʪ�ȶ�̬����	
	public String humiConfig(){
		configStr = flashConfig(2); // (1:�¶� 2:ʪ��)
		return SUCCESS;
	}
	
	/**
	 * @describe: ��ȡflash����
	 * @param type (1:�¶� 2:ʪ��)
	 * @date:2009-12-5
	 */
	public String flashData(int type){
		
		List<Integer> equipmentIds = null;						// ���������б�	
		Map<Integer,EquipData> equipDataList = new HashMap<Integer, EquipData>(); // ������Ϣ�б�
		
		int tempType = Integer.parseInt(commonDataUnit.
				getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)); // �¶���ʾ����(�¶���ʾ��ʽ(1:���� 2:����))
		
		// ��ȡsession - falsh��Ҫ��ʾ�ĸ��˵�ַ�б�
		ActionContext ctx = ActionContext.getContext();
		userPlaceList = (String) ctx.getSession().get("user_flash_list");				
		// ��ȡ ҳ�����������б�
		equipmentIds = getAddressList(userPlaceList);
		
		// ������������
		List<String> headers = new ArrayList<String>();   // �������б�
		Map<Integer, List<String>> graphs = new HashMap<Integer, List<String>>();
		List<String> graph = null;		
		EquipData  equipData = null; 
		long templong = -1;
		float floatVal;
		int equipId;		
		
		// ��ѯ���ݿ�, ��ȡĿ������
		List<BeanForPortData> dataLists = mainService.selectMinAllRec(userPlaceList);
		
		for (BeanForPortData bean : dataLists) {
			equipId = bean.getEquipmentId();
			// ������ֵ
			if (templong != bean.getRecLong()){
				templong = bean.getRecLong();
				headers.add(FunctionUnit.getTime_Short_Str(bean.getRecTime()));
			}
			// ��ȡ������������--��Ҫ�õ�(�������ͺ�ƫ��ֵ)
			if (!equipDataList.containsKey(equipId)){
				equipDataList.put(equipId,commonDataUnit.getEquipByID(equipId));			
			}
			// ��������ȡ�� ���ݼ�
			if (graphs.containsKey(equipId)){
				graph = graphs.get(equipId);
			}else{
				graph = new ArrayList<String>();
			}
			equipData = equipDataList.get(equipId);
			if (type == 1){
				// ����Flash��������--�������������������
				if ((bean.getState() != 1) ||(bean.getMark() == 0) || // ���˴���֡�Ϳ�����
						(equipData.getEquitype() == EquipData.TYPE_HUMI_ONLY) ){
					graph.add("");								// �¶�ֵ-��ʪ��ʱ����ʾ�¶�
				}else{
					floatVal = bean.getTemp() / (float) 100 ;
					if (tempType == 2){floatVal = floatVal * 9/5 + 32;}// ��ʾΪ����ʱ����ֵ
					graph.add(String.valueOf(floatVal));		// �¶�ֵ
				}
			}else if (type == 2){
				if ( (bean.getState() != 1) ||(bean.getMark() == 0) || // ���˴���֡�Ϳ�����
						(equipData.getEquitype() == EquipData.TYPE_TEMP_ONLY)){
					graph.add("");								// ʪ��ֵ-���¶�ʱ����ʾʪ��
				}else{
					floatVal = bean.getHumi() / (float) 100 ;
					graph.add(String.valueOf(floatVal));		// ʪ��ֵ
				}
			}// end (type == 2)
			// �����ݱ��浽��������
			graphs.put(equipId, graph);
		}
		
		//--------------flash�����С����,����--------------------//
		Map<Integer, String> equipLabels = new HashMap<Integer, String>();	// ������ǩ�б�				
		float highdata = 10000, lowdata = 10000;
		for (int i = 0; i < equipmentIds.size(); i++) {
			equipId = equipmentIds.get(i);
			if (equipDataList.containsKey(equipId)){
				equipLabels.put(equipId, commonDataUnit.getEquiMapStrById(equipId));//������ǩ
				equipData = null; 	equipData = equipDataList.get(equipId);
				if (type == 1){
					floatVal = equipData.getTempUp();	
					highdata = highdata==10000?floatVal:highdata > floatVal ? floatVal : highdata;		// ����ֵ--ȡ��С �ĸ���ֵ
					floatVal = equipData.getTempDown();	
					lowdata = lowdata==10000?floatVal:lowdata < floatVal? floatVal : lowdata;			// ����ֵ--ȡ��� �ĵ���ֵ
				}else if (type == 2){
					floatVal = equipData.getHumiUp();	
					highdata = highdata==10000?floatVal:highdata > floatVal ? floatVal : highdata;		// ��ʪֵ--ȡ��С �ĸ�ʪֵ
					floatVal = equipData.getHumiDown();	
					lowdata = lowdata==10000?floatVal:lowdata < floatVal? floatVal : lowdata;			// ��ʪֵ--ȡ��� �ĵ�ʪֵ
				}
			}
		}
		
		// flash���ݸ�ʽ����
		BeanForFlashData bean = new BeanForFlashData();
		bean.guide_start_value = String.valueOf(lowdata);				// ����ʼֵ-��Сֵ		
		bean.guide_end_value = String.valueOf(highdata);				// �������ֵ-���ֵ
		bean.balloon_text_append = type == 2?"%RH":tempType==1?"��":"F";	// ���߽ڵ���,������ʾ����Ϣ�ĺ�׺		
		bean.titles = equipLabels;										// flash���߱��⼯��
		bean.headers = headers;											// flash��seriesԪ��(x��
		bean.graphs = graphs;											// flash�������ݼ���
		
		return bean.toString();	// ������ʪ�������ַ�
	}
	
	/**
	 * @describe: ��ȡflash���� -������������, û�зŴ���
	 * @param type (1:�¶� 2:ʪ��)
	 * @date:2009-12-5
	 */
	public String flashConfig(int type){
		int tempType = Integer.parseInt(commonDataUnit.
				getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)); // �¶���ʾ����(�¶���ʾ��ʽ(1:���� 2:����))
		
		// ��ʪ�� -- ������������
		BeanForFlashSetting configbean = new BeanForFlashSetting();
		configbean.data_type = "xml";								// flahs���ݸ�ʽ
		int reload_data_interval = Integer.parseInt(commonDataUnit.
				getSysArgsByKey(BeanForSysArgs.DATA_FLASHTIME));
		configbean.reload_data_interval = reload_data_interval;		// Falshˢ�¼��
		configbean.zoomable_use = false;							// ��ʹ�÷Ŵ���
		if (type == 1){
			// �¶�����
			configbean.label_name = "�¶�ʵʱ��������";	// ͷ��ǩ
			configbean.y_value_append = tempType == 1 ? "��" : "F";		// Y�ḽ�ӱ�ǩ
			configbean.legend_value_append = tempType == 1 ? "��" : "F";
		}else if (type == 2){
			// ʪ������
			configbean.label_name = "ʪ��ʵʱ��������";	// ͷ��ǩ
			configbean.y_value_append = "%";
			configbean.legend_value_append = "%RH";
		}
		return configbean.toString();							// ���������ַ�
	}
	
	/**
	 * @describe: �������� �õ���ַint(equipmentId) ����	
	 * @param placeListStr Ŀ���ַ���
	 * @return:
	 * @date:2009-11-4
	 */
	public int[] getAddressArray(String placeListStr){
		String [] strArray = placeListStr.split(",");
		int[] intArray = new int[strArray.length];
		for (int i = 0; i < strArray.length; i++) {
			intArray[i] = Integer.parseInt(strArray[i].trim());
		}
		return intArray;
	}
	
	/**
	 * @describe: ����������˳���. ���밲װ˳�����еĵ�ַList(equipmentId )
	 * @param placeListStr Ŀ���ַ���
	 * @date:2009-11-4
	 */
	public List<Integer> getAddressList(String placeListStr){
		List<Integer> intList = new ArrayList<Integer>();
		
		if (placeListStr == null){return intList;}
		if ( placeListStr.trim().length() == 0 ){return intList;}
		
		String [] equipmentIds = placeListStr.split(",");
		int equipmentId, runcount = 0;
		
		for (EquipData equipData : commonDataUnit.getEquiList()) {
			if( runcount == equipmentIds.length){break;}
			for (int i = 0; i < equipmentIds.length; i++) {
				equipmentId = Integer.parseInt(equipmentIds[i].trim());
				if (equipmentId == 0 ){continue;}
				if (equipData.getEquipmentId() ==  equipmentId){
					intList.add(equipmentId);
					runcount++;
					break;
				}
			}// end for��
		} // end for��
		
		return intList;
	}
	
	// ----------------����Ϊget��set����----------------
	@JSON(deserialize=false,serialize=false)
	public String getAlarmPlayFile() {
		alarmPlayFile = commonDataUnit.getWarnPlayFile();
		return alarmPlayFile;
	}
	/**
	 * @describe: ��ȡ��������״̬ 	
	 * @return: (1:������  2:����)
	 * @date:2010-1-6
	 */	
	public int getPalyFalg() {
		// ��֤�б�������
		if (mainService.selectUndoRec().size()> 0){
			// ���ж�ϵͳ��û�п�����������
			palyFalg = commonDataUnit.getWarnOpenFlag();
			palyFalg =palyFalg == 2? 2:1;
		}else{
			palyFalg = 1;
		}
		return palyFalg;
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
	
	// ----------------��ͨ get��set����----------------
	/**
	 * @describe: ����ַ�б�,�����û��ı����¼,ѡ����Ӧ����checkbox
	 * @date:2009-11-30
	 */
	@JSON(deserialize=false,serialize=false)  
	public Map<String, String> getAreaToEqList() {
		return areaToEqList;
	}
	public String getUserPlaceList() {
		return userPlaceList;
	}
	public void setUserPlaceList(String userPlaceList) {
		this.userPlaceList = userPlaceList;
	}

	@JSON(deserialize=false,serialize=false) 
	public int getTagFlag() {
		return tagFlag;
	}

	public void setTagFlag(int tagFlag) {
		this.tagFlag = tagFlag;
	}

	public int getFalshTime() {
		return falshTime;
	}

	public void setFalshTime(int falshTime) {
		this.falshTime = falshTime;
	}
	
	@JSON(deserialize=false,serialize=false) 
	public int getMaxSelector() {
		return maxSelector;
	}

	public void setMaxSelector(int maxSelector) {
		this.maxSelector = maxSelector;
	}

	public Map<Integer, BeanForlBarData> getBarData() {
		return barData;
	}

	public void setBarData(Map<Integer, BeanForlBarData> barData) {
		this.barData = barData;
	}

	public String getRecTimeStr() {
		return recTimeStr;
	}

	public void setRecTimeStr(String recTimeStr) {
		this.recTimeStr = recTimeStr;
	}

	public String getDataStr() {
		return dataStr;
	}

	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;
	}

	public String getConfigStr() {
		return configStr;
	}
	public Map<Integer, WorkPlaceEntity> getWorkPlaceBarDatas() {
		return workPlaceBarDatas;
	}
	public void setWorkPlaceBarDatas(Map<Integer, WorkPlaceEntity> workPlaceBarDatas) {
		this.workPlaceBarDatas = workPlaceBarDatas;
	}
	

}

