package com.htc.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.htc.bean.BeanForEqOrder;
import com.htc.bean.BeanForSysArgs;
import com.htc.domain.EquipData;
import com.htc.domain.Pager;
import com.htc.model.ManaService;
import com.htc.model.ServiceSqlServer;

/**
 * @ EquiAction.java
 * ���� : ����������Ϣaction. ��ҳ - ���� -���� - ɾ�� - ����ɾ�� - ���ֱ༭	- ģ����ѯ - ����˳��
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class EquiAction extends AbstractActionForHigh {

	// ������
	private ManaService manaService;
	private ServiceSqlServer serviceSqlServer;

	// ��ҳ
	private Pager pager; 							// ��ҳ��
	private String currentPage="1"; 				// ��ǰҳ
	private String pagerMethod; 					// ҳ�淽�����磺ǰһҳ����һҳ����һҳ�����һҳ��.
	private int[] ids; 								// ѡ��ɾ����ѡ��(��ѡ)

	// ģ����ѯ
	private int searchType;							// page��Ӧtype
	private int searchPlaceId;						// page��ӦplaceId
	private String searchMark;						// page��Ӧmark
	private Map<String, Object> searchKey;			// ��ѯ�ؼ���

	// ҳ����ʾ
	private List<EquipData> equiList;				// �����Ϣ�б�
	private EquipData equipData ;					// ҳ����ʾBean
	private	String tempUp = "40"; 
	private	String tempDown = "20";
	private	String tempDev = "0";
	private	String humiUp = "100";
	private	String humiDown = "0";
	private	String humiDev = "0";
	private int equitype = 1;						// 1:��ʪ�� 2:���¶� 3:��ʪ��
	private int showTipMsg=0;						// �Ƿ���ʾ������(0:����ʾ 1:��ʾ)	
	private int tempshowType;						// �¶���ʾ��ʽ(1:���� 2:����)
	
	// ˳�����ҳ��
	private String eqorderStr = "";					// ������˳��,������˳���ַ���,�Զ��Ÿ���.��:2,1,3
	
	// ��������
	@SuppressWarnings("unused")
	private Map<Integer, String> equiTypes;			// 1:��ʪ�� 2:���¶� 3:��ʪ��
	private Map<Integer, String> conndatas;			// ���ڷ���λ��:����ͨ�ŵ�ʱ��,��ȡ���ֽ���,Ĭ����3��(�¶�,ʪ��,¶��),4����(�¶�,ʪ��,¶��,��ѹ)
	private Map<Integer, String> searchEquiType;	// 0:ȫ�� 1:��ʪ�� 2:���¶� 3:��ʪ��
	@SuppressWarnings("unused")
	private Map<Integer, String> areaMap;			// ��ַ��ǩ�б�  key:placeId value:placeName
	private Map<Integer, String> searchAreaMap;		// 0:ȫ�� key:placeId
	private Map<Integer, String> powerShows;		// �Ƿ���ʾ������Ϣ (0: ����ʾ    1: ��ʾ) 
	private Map<Integer, String> powerValues;		// ��ʾ����ֵ (1:1.5v, 2:3.6v)
	
	// ͳ��ϵͳ�����������������: 1:ϵͳ�м����¶�����ʪ������		2:ϵͳ��ȫ�����¶�����	3:ϵͳ��ȫ����ʪ������
	@SuppressWarnings("unused")
	private int systemEqType;
	
	// ��������-ϸ��
	private String headTitle;						// ����
	private String headDetail;						// ��Ϣϸ��		

	// ���췽��
	public EquiAction() {
	}
	//ע��service -- spring ioc
	public void setManaService(ManaService manaService) {
		this.manaService = manaService;
	}
	public void setServiceSqlServer(ServiceSqlServer serviceSqlServer) {
		this.serviceSqlServer = serviceSqlServer;
	}
	
	/**
	 * @describe : ��ʾ�б�, Ҳ����ͬʱ���ҳ������	
	 * @param type {0�����ҳ������ ��0�������ҳ������}
	 * @date:2009-11-4
	 */
	@SuppressWarnings("unchecked")
	public void list(int type) {
		
		//ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;
		
		if (type == 0) {
			equipData = null;
		}
		if (searchKey == null) {
			searchKey = new HashMap<String, Object>();
		}
		if (searchType != 0) {
			searchKey.put("type", searchType);
		}
		if (searchPlaceId != 0) {
			searchKey.put("placeId", searchPlaceId);
		}
		if (searchMark != "") {
			searchKey.put("mark", searchMark);
		}
		
		pager = manaService.getPager("EquipData", getCurrentPage(),
				getPagerMethod(), USERPAGESIZE, searchKey);
		equiList = (ArrayList) pager.getElements();
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
	}

	@Override
	public String execute() {
		return SUCCESS;
	}

	// ����½���ʱ������ҳ�� -- �����κ�����
	public String percreate(){
		// jspҳ������Ҫ��  currentPage ��ҳ�� -- �㷵����
		return PERCREATE;
	}	
	
	// ���� (Ψһ���У�������,��ע, ������ַ.�������������)
	public String create(){
		String returnPage = SUCCESS;
		showTipMsg = 0;
		
		//ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;		
		if (equipData.getPlaceId()== 0) {
			String noArea = getText("error.mana.not.exist");
			addFieldError("ararNotExist", noArea);
			showTipMsg = 1;
			returnPage =  PERCREATE;
		} else {
			if (checkRange()){
				if (!manaService.insertEquipData(equipData)) {
					String equiName = getText("equi.set.label");
					equiName = getText(("error.exist"), new String[] { equiName });
					addFieldError("addFailure", equiName);
					showTipMsg = 1;
					returnPage =  PERCREATE;
				} else {
					pagerMethod = "last"; // ע��ɹ����������һ��
					list(0);
					
					// ����ϵͳ�����Ϣ
					//CommonDataUnit.resetSystem(false,true,true,false,true);
					commonDataUnit.resetSystem(false,true,true,false,true);
					
				} // end else
			}else{
				showTipMsg = 1;
				returnPage = PERCREATE;						
			}
		}
		return returnPage;
	}
	
	// ����
	public String back(){
		list(0);
		return SUCCESS;
	}
	
	// ���ֱ༭
	public String perupdate() {
		//ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;
		equipData = manaService.getEquipDataById(equipData.getEquipmentId());
		
		// ��equipData�ָ�� һ��һ����Ԫ,������ʾ��ҳ����
		equipDataToPiece();
		if (equipData == null){
			addFieldError("showFailure", getText(("error.comm.deleted")));
			showTipMsg = 1; // ��ʾҳ�����div
			return SUCCESS;
		}else{
			return PERUPDATE;
		}
	}	

	// ���� -- ��Ϣ
	public String update() {
		String returnPage = SUCCESS;
		showTipMsg = 0;
		//ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;		
		
		boolean existFlag;
		
		if (checkRange()){
			existFlag = manaService.updateEquipData(equipData);
			if (!existFlag) {
				String equiName = getText("equi.set.label");
				equiName = getText(("error.exist"), new String[] { equiName });
				addFieldError("updateFailure", equiName);
				showTipMsg = 1;
				returnPage = PERUPDATE;
			} else {
				// ����ϵͳ�����Ϣ
				//CommonDataUnit.resetSystem(false,true,true,false,false);
				commonDataUnit.resetSystem(false,true,true,false,false);
				list(0);
			}
		}else{
			showTipMsg = 1;
			returnPage = PERUPDATE;			
		}
		return returnPage;
	}
	
	/**
	 * @describe:	���ϳ� EquipData,���ж���ʪ���Ƿ�Խ��
	 * @return: false: �쳣  true : ����
	 * @date:2009-11-18
	 */
	public boolean checkRange(){
		boolean flag = true;
		int checkInt = connectToEquipData();
		if (checkInt >= 10) {
			checkInt = checkInt - 10;
			if (equipData.getEquitype()!=2){
				flag = false;
				addFieldError("updateFailure1", "ʪ�ȷ�Χ����ȷ...");
			}
		}
		if ((checkInt >= 1) && (equipData.getEquitype()!=3) ){
			flag = false;
			addFieldError("updateFailure2", "�¶ȷ�Χ����ȷ...");
		}
		return flag;
	}

	// ɾ��
	public String delete() {
		
		//ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;		
		manaService.deleteEquipData(equipData.getEquipmentId());
		// ����ϵͳ�����Ϣ
		//CommonDataUnit.resetSystem(false,true,true,false,true);
		commonDataUnit.resetSystem(false,true,true,false,true);
		list(1);
		
		return SUCCESS;
	}

	// ����ɾ��
	public String deleteSelect() {
		
		//ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;		
		
		if (ids != null) {
			manaService.deleteEquipBatch(ids);
			// ����ϵͳ�����Ϣ
			//CommonDataUnit.resetSystem(false,true,true,false,true);
			commonDataUnit.resetSystem(false,true,true,false,true);
		}
		list(0);
		return SUCCESS;
	}

	public void equipDataToPiece(){
		tempUp =  String.valueOf(equipData.getTempUp());
		tempDown = String.valueOf(equipData.getTempDown());
		tempDev = String.valueOf(equipData.getTempDev());
		humiUp = String.valueOf(equipData.getHumiUp());
		humiDown = String.valueOf(equipData.getHumiDown());
		humiDev = String.valueOf(equipData.getHumiDev());
		equitype = equipData.getEquitype();
	}
	
	/**
	 * @describe: ���ϳ�	EquipData,���ж���ʪ�Ƿ�Խ��
	 * @return: 
	 * 	1:�����¶ȷ�Χ����(����ֵ���ڵ��ڸ���ֵ)
	 * 	10:����ʪ�ȷ�Χ����(��ʪֵ���ڵ��ڸ�ʪֵ)
	 * @date:2009-11-18
	 */
	public int connectToEquipData(){
		int rsint = 0;
		float temfloat;
		
		temfloat = Float.parseFloat(tempUp);
		equipData.setTempUp(temfloat);
		// �ж��¶ȵķ�Χ
		rsint = temfloat <= Float.parseFloat(tempDown) ? 1 : 0;
		temfloat = Float.parseFloat(tempDown);
		equipData.setTempDown(temfloat);
		
		temfloat = Float.parseFloat(tempDev);
		equipData.setTempDev(temfloat);
		
		temfloat = Float.parseFloat(humiUp);
		equipData.setHumiUp(temfloat);
		// �ж�ʪ�ȵķ�Χ
		rsint += temfloat <= Float.parseFloat(humiDown) ? 10 : 0;
		temfloat = Float.parseFloat(humiDown);
		equipData.setHumiDown(temfloat);
		
		temfloat = Float.parseFloat(humiDev);
		equipData.setHumiDev(temfloat);
		
		return rsint;
	}
	
	// ��ҳ
	public String getList(){
		list(1);
		return SUCCESS;
	}

	// ģ����ѯ
	public String fuzzySearch() {
		currentPage = "1";
		list(1);
		return SUCCESS;
	}

	// �޸��б�˳��
	public String updateOrderList() {
		//System.out.println("������˳���ַ�:" + eqorderStr);
		
		List<EquipData> orderList = new ArrayList<EquipData>();
		
		// ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;
		String[] equipmentIdList = eqorderStr.split(",");
		int orderInt = 0;
		for (String equipmentId : equipmentIdList) {
			orderInt++;
			orderList.add(getEdata(equipmentId, orderInt));
		}
		
		// �޸����ݿ�
		if (orderList.size() > 0) {
			manaService.updateEquiOrder(orderList);
		}
		
		// ����ϵͳ�����Ϣ
		//CommonDataUnit.resetSystem(false,true,false,false,false);
		commonDataUnit.resetSystem(false,true,false,false,false);
		
		//return SHOWORDER;
		list(0);
		return SUCCESS;
	}
	// ��ʾ����˳���õ��б�
	public String showOrderList() {
		
		// ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;
		
		BeanForEqOrder eqorderBean = new BeanForEqOrder();
		eqorderBean.setEqorderType(BeanForEqOrder.SELECT_WITH_ORDERID);
		equiList = manaService.getAllEquiObject(eqorderBean);
		eqorderBean = null;
		return SHOWORDER;
	}
	
	/**
	 * @describe: ��� ������Ϣ. ��Ҫ�Ƿ���ʾ����	
	 * @param key ��������ID
	 * @param order ˳���.��������������.��: 1��ʾ������ǰ��,2���,Ȼ��345...
	 * @date:2009-11-4
	 */
	public EquipData getEdata(String equipmentId, int order) {
		EquipData edata = new EquipData();
		edata.setEquipmentId(Integer.parseInt(equipmentId.trim()));
		edata.setEquiorder(order);
		return edata;
	}
	
	// �������޸ĵ�Sqlserver,�㽭ҩ���Ҫ�õ���
	public String updata4ZJ(){
		// ��Access�洢���ܿ���������´���
		if (Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_ACCESS_STORE)) == 2){
			BeanForEqOrder eqorderBean = new BeanForEqOrder();
			eqorderBean.setEqorderType(BeanForEqOrder.SELECT_WITH_NOTHING);
			eqorderBean.setUseless(0);
			List<EquipData> equiplist = manaService.selectEquiOrderStr(eqorderBean);
			if (serviceSqlServer.flashAllMonitors(equiplist)){
				showInfo(1); // �ɹ�����ʾ
			}else{
				showInfo(2); // ʧ�ܵ���ʾ
			}
		}
		return "noDataReturn";
	}
	
	/**
	 * @describe: ������д��ҩ��ֵ����ݿ�
	 * @param type <br>
	 * 1:�ɹ�����ʾ<br>
	 * 2:ʧ�ܵ���ʾ<br>
	 * @date:2010-4-14
	 */
	private void showInfo(int type) {
		StringBuffer strbuf = new StringBuffer();
		headTitle = type == 1? "�ɹ�������ҩ���ʹ�õ����ݿ�..." :"������ҩ���ʹ�õ����ݿ�ʱ����:";
		
		if (type == 1){
			strbuf.append("<label>");
			strbuf.append("�����ɹ�,�����ʹ��!");
			strbuf.append("</label><br/><br/>");		
		}else if (type == 2){
			strbuf.append("����:");
			strbuf.append("<br/>");
			strbuf.append("<label>");
			strbuf.append("    �ټ��ϵͳ���Ƿ�װ��sqlserver���ݿ�");
			strbuf.append("</label><br/><br/>");
			strbuf.append("<br/>");
			strbuf.append("<label>");
			strbuf.append("    �ڼ��sqlserver���ݿ�:�û���:sa ����:sa");
			strbuf.append("</label><br/><br/>");
			strbuf.append("<br/>");
			strbuf.append("<br/>");
			strbuf.append("    �� ȷ��������Ϣ��,��Ȼ����. �п���������ϴ�������,�뾡����ϵ����...");	
		}
		headDetail = strbuf.toString();
	}
	
	// ----------------����Ϊget��set����----------------
	public Map<Integer, String> getEquiTypes() {
		//ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;	
		return manaService.getAllEquiType();
	}

	public Map<Integer, String> getSearchEquiType() {
		searchEquiType = getEquiTypes();
		searchEquiType.put(0, getText("user.all"));
		return searchEquiType;
	}
	
	public Map<Integer, String> getConndatas() {
		conndatas = new HashMap<Integer, String>();
		conndatas.put(3, "3λ");
		conndatas.put(4, "4λ");				
		return conndatas;
	}

	public Map<Integer, String> getAreaMap() {
		//ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;			
		return manaService.getAllPlace();
	}

	public Map<Integer, String> getSearchAreaMap() {
		searchAreaMap = getAreaMap();
		searchAreaMap.put(0, getText("user.all"));
		return searchAreaMap;
	}
	// �Ƿ���ʾ������Ϣ (0: ����ʾ    1: ��ʾ)
	public Map<Integer, String> getPowerShows() {
		powerShows = new HashMap<Integer, String>();
		powerShows.put(0, "����ʾ");
		powerShows.put(1, "��ʾ");		
		return powerShows;
	}

	// ��ʾ����ֵ (1:1.5v, 2:3.6v)
	public Map<Integer, String> getPowerValues() {
		powerValues = new HashMap<Integer, String>();
		powerValues.put(1, "1.5V");
		powerValues.put(2, "3.6V");
		return powerValues;
	}

	//�¶���ʾ��ʽ(1:���� 2:����)
	public int getTempshowType(){
		//tempshowType = Integer.parseInt(CommonDataUnit.getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE));		
		tempshowType = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE));		
		return tempshowType;
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
		//return CommonDataUnit.getSystemEqType();
		return commonDataUnit.getSystemEqType();
	}	
	// ----------------��ͨ get��set����----------------
	public int[] getIds() {
		return ids;
	}

	public void setIds(int[] ids) {
		this.ids = ids;
	}

	public List<EquipData> getEquiList() {
		return equiList;
	}

	public void setEquiList(List<EquipData> equiList) {
		this.equiList = equiList;
	}

	public EquipData getEquipData() {
		return equipData;
	}

	public void setEquipData(EquipData equipData) {
		this.equipData = equipData;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getPagerMethod() {
		return pagerMethod;
	}

	public void setPagerMethod(String pagerMethod) {
		this.pagerMethod = pagerMethod;
	}

	public int getSearchPlaceId() {
		return searchPlaceId;
	}

	public void setSearchPlaceId(int searchPlaceId) {
		this.searchPlaceId = searchPlaceId;
	}

	public String getSearchMark() {
		return searchMark;
	}

	public void setSearchMark(String searchMark) {
		this.searchMark = searchMark;
	}

	public Map<String, Object> getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(Map<String, Object> searchKey) {
		this.searchKey = searchKey;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public String getTempUp() {
		return tempUp;
	}

	public void setTempUp(String tempUp) {
		this.tempUp = tempUp;
	}

	public String getTempDown() {
		return tempDown;
	}

	public void setTempDown(String tempDown) {
		this.tempDown = tempDown;
	}

	public String getTempDev() {
		return tempDev;
	}

	public void setTempDev(String tempDev) {
		this.tempDev = tempDev;
	}

	public String getHumiUp() {
		return humiUp;
	}

	public void setHumiUp(String humiUp) {
		this.humiUp = humiUp;
	}

	public String getHumiDown() {
		return humiDown;
	}

	public void setHumiDown(String humiDown) {
		this.humiDown = humiDown;
	}

	public String getHumiDev() {
		return humiDev;
	}

	public void setHumiDev(String humiDev) {
		this.humiDev = humiDev;
	}

	public int getEquitype() {
		return equitype;
	}

	public void setEquitype(int equitype) {
		this.equitype = equitype;
	}

	public void setEqorderStr(String eqorderStr) {
		this.eqorderStr = eqorderStr;
	}

	public int getShowTipMsg() {
		return showTipMsg;
	}
	
	public String getHeadTitle() {
		return headTitle;
	}
	
	public String getHeadDetail() {
		return headDetail;
	}
	
}
