package com.htc.action;

import java.util.*;

import com.htc.common.CommonDataUnit;
import com.htc.common.FunctionUnit;
import com.htc.domain.*;
import com.htc.model.ManaService;
import com.htc.model.SetSysService;

/**
 * @ AreaAction.java
 * ���� : ��������Ϣaction. ��ҳ - ���� -���� - ɾ�� - ����ɾ�� - ���ֱ༭
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class AreaAction extends AbstractActionForHigh {

	// ������
	private ManaService manaService;
	private SetSysService setSysService;

	// ��ҳ
	private Pager pager; 					// ��ҳ��
	private String currentPage="1"; 		// ��ǰҳ
	private String pagerMethod; 			// ҳ�淽�����磺ǰһҳ����һҳ����һҳ�����һҳ��.
	private String[] ids; 					// ѡ��ɾ����ѡ��(��ѡ)
	private int showTipMsg=0;				// �Ƿ���ʾ������(0:����ʾ 1:��ʾ 2:��������,�����ӱ�ע.��ɾ�������ӱ�ע,�ٽ���ɾ��.)	
	
	// ҳ����ʾ
	private List<Workplace> placeList;		// �����Ϣ�б�
	private Workplace place;				// ҳ����ʾBean
	
	// ģ����ѯ
	private Map<String, Object> searchKey;	// ��ѯ�ؼ���
	private String placeName="";			// page��ӦplaceName
	
	// �޸Ķ��Ź���
	private Map<String, String> phonelist;

	// ���췽��
	public AreaAction() {
	}
	//ע��service -- spring ioc
	public void setManaService(ManaService manaService) {
		this.manaService = manaService;
	}
	public void setSetSysService(SetSysService setSysService) {
		this.setSysService = setSysService;
	}
	
	/**
	 * @describe : ��ʾ�б�, Ҳ����ͬʱ���ҳ������
	 * @param type {0�����ҳ������ ��0�������ҳ������}
	 * @date:2009-11-4
	 */
	@SuppressWarnings("unchecked")
	public void list(int type) {
		
		// ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;
		
		if (type == 0) {
			place = null;
		}
		if (searchKey == null){
			searchKey = new HashMap<String, Object>();
		}
		if (!placeName.trim().equals("") ) {
			searchKey.put("name", placeName);
		}		
		
		pager = manaService.getPager("Workplace", getCurrentPage(),	getPagerMethod(), USERPAGESIZE,searchKey);
		placeList = (ArrayList) pager.getElements();
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

	// ����
	public String create(){
		showTipMsg = 0;
		//ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;
		
		if (!manaService.insertWorkplace(place)) {
			String areaName = getText("mana.area.name.label");
			areaName = getText(("error.exist"), new String[] { areaName });
			addFieldError("addFailure", areaName);
			showTipMsg = 1; // ��ʾҳ�����div
			return PERCREATE;
		} else {
			pagerMethod = "last"; // ע��ɹ����������һ��
			
			// ����ϵͳ�����Ϣ
			//CommonDataUnit.resetSystem(true,false,false,false,false);			
			commonDataUnit.resetSystem(true,false,false,false,false);			
			list(0);
			
			return SUCCESS;
		}
	}
	
	// ����
	public String back(){
		list(0);
		return SUCCESS;
	}	

	// ���ֱ༭
	public String perupdate(){
		showTipMsg = 0;
		// ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;		
		
		place = manaService.getWorkplaceById(place.getPlaceId());
		if (place == null){
			addFieldError("showFailure", getText(("error.comm.deleted")));
			showTipMsg = 1;
			list(0);
			return SUCCESS;
		}else{
			return PERUPDATE;
		}
	}	
	
	// ����
	public String update(){
		showTipMsg = 0;
		//ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;
		
//	    update tWorkplace set
//    	<isNotEmpty prepend=" " property="placeName">
//      	placeName = #placeName#
//    	</isNotEmpty>	
//    	<isNotNull prepend=" " property="remark">
//    		remark = #remark#
//    	</isNotNull>			
		place.setRemark(null); // �����ibatis�����û�ж���(Ҳ���ܼ�),Ҫ����һ������,������������nullֵ��������

		boolean existChildFlag = manaService.updateWorkplace(place);
		if (!existChildFlag) {
			String areaName = getText("mana.area.name.label");
			areaName =getText(("error.exist"),new String[] { areaName });
			addFieldError("upadteFailure", areaName);
			showTipMsg = 1;
			return PERUPDATE;
		} else {
			// ����ϵͳ�����Ϣ
			//CommonDataUnit.resetSystem(true,true,true,false,false);		
			commonDataUnit.resetSystem(true,true,true,false,false);		
			list(0);
			
			return SUCCESS;
		}
	}
	
	// ���ֱ༭gsm
	public String pergsm(){
		showTipMsg = 0;
		// ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;		
		
		place = manaService.getWorkplaceById(place.getPlaceId());
		if (place == null){
			addFieldError("showFailure", getText(("error.comm.deleted")));
			showTipMsg = 1;
			list(0);
			return SUCCESS;
		}else{
			// ��ȡ���ź����б� 
			List<PhoneList> phonelists = setSysService.getAllPhoneLists();
			phonelist = fillGmsArea(place.getPlaceName(),place.getRemark(), phonelists);
			return "pergsm";
		}
	}
	
	private Map<String, String> fillGmsArea(String placeName, String remark, List<PhoneList> phonelists) {
		Map<String, String> phoneTemp = new HashMap<String, String>();
		StringBuffer strbuf = new StringBuffer();
		boolean isCheck = false;
		int listid = 0;
		String label = "";
		
		if (phonelists.size()>0){
			List<Integer> gsmlist = FunctionUnit.getIntListByStr(remark);
			// ͨ���ȶԶ����б�������б���ĵ�ַ�б�,��ȷ���Ƿ�Ҫѡ�и�ѡ��.
			for (int i = 0; i < phonelists.size(); i++) {
				listid = phonelists.get(i).getListId();
				label = phonelists.get(i).getName() + "[" + phonelists.get(i).getPhone()+"]";
				isCheck = false;
				
				for (int j = 0; j < gsmlist.size(); j++) {
					if(listid == gsmlist.get(j)){
						isCheck = true;
						break;
					}
				}
				// ��ӵ�������
				strbuf.append(CommonDataUnit.create_Html_P(listid, label, isCheck));
			}
			phoneTemp.put(placeName, strbuf.toString());
		}
		return phoneTemp;
	}

	// ���ֱ༭gsm
	public String upgsm(){
		showTipMsg = 0;
		//ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;
		manaService.updateWorkplaceGms(place);
		list(0);
		return SUCCESS;
	}
	
	// ɾ��
	public String delete() throws Exception {
		showTipMsg = 0;
		//ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;
		
		//String	str  =  new  String(place.getPlaceName().getBytes("ISO-8859-1"),"gbk");
		String	str  =  place.getPlaceName();
		boolean existChildFlag = manaService.deleteWorkplace(str);
		if (!existChildFlag) {
			// ��������,�����ӱ�ע.��ɾ�������ӱ�ע,�ٽ���ɾ��.
			// addFieldError("deleteFailure", getText(("error.mana.hasChild")));
			showTipMsg = 2;
		}else{
			// ����ϵͳ�����Ϣ
			//CommonDataUnit.resetSystem(true,false,false,false,false);		
			commonDataUnit.resetSystem(true,false,false,false,false);		
		}
		list(0);
		return SUCCESS;
	}	

	// ����ɾ��
	public String deleteSelect() {
		showTipMsg = 0;
		
		//ʵ����������
		//manaService = manaService == null ? new ManaService() : manaService;		
		
		if (ids != null) {
			boolean existChildFlag = manaService.deleteWorkplaceBatch(ids);
			if (!existChildFlag) {
				addFieldError("deleteFailure", getText(("error.mana.hasChild")));
				showTipMsg = 1;
				list(1);
			} else {
				// ����ϵͳ�����Ϣ
				//CommonDataUnit.resetSystem(true,false,false,false,false);	
				commonDataUnit.resetSystem(true,false,false,false,false);	
				list(0);
			}
		}
		return SUCCESS;
	}
	
	// ģ����ѯ
	public String fuzzySearch()  {
		currentPage = "1";
		list(1);
		return SUCCESS;
	}	

	// ��ҳ
	public String getList(){
		list(1);
		return SUCCESS;
	}

	// ----------------����Ϊget��set����----------------
	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public List<Workplace> getPlaceList() {
		return placeList;
	}

	public void setPlaceList(List<Workplace> placeList) {
		this.placeList = placeList;
	}

	public Workplace getPlace() {
		return place;
	}

	public void setPlace(Workplace place) {
		this.place = place;
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

	public int getShowTipMsg() {
		return showTipMsg;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getPlaceName() {
		return placeName;
	}

	public Map<String, String> getPhonelist() {
		return phonelist;
	}
	
}
