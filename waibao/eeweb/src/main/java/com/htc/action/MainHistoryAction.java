package com.htc.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import com.googlecode.jsonplugin.annotations.JSON;
import com.htc.bean.BeanForFlashSetting;
import com.htc.bean.BeanForRecord;
import com.htc.bean.BeanForSearchRecord;
import com.htc.bean.BeanForSysArgs;
import com.htc.bean.BeanForZjHisRec;
import com.htc.common.FunctionUnit;
import com.htc.domain.EquipData;
import com.htc.domain.TLog;
import com.htc.domain.ZjHistory;
import com.htc.model.MainService;
import com.htc.model.ServiceSqlServer;

/**
 * @ MainHistoryAction.java
 * ���� : ��ѯ��ʷ����action. 
 * 		��ʾ����: �Ա���flash����ʽ��.
 * 		��ѯ���ͣ���ʱ����, �ձ���, �±���
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class MainHistoryAction extends AbstractAction {

	// ������
	private MainService mainService;
	private ServiceSqlServer serviceSqlServer;

	// �Բ�ѯ���ݸ���Ԥ����Χ��ʾ��ͬ��CSS class��ʽ;
	private String LOWDATACSS 	 = "lowdata";
	private String NORMALDATACSS = "normaldata";
	private String HIGHDATACSS 	 = "highdata";

	// json��table����
	private int[] statBox;						// ͳ������ -- ��ѡ�� �ձ�,�±�ҳ���ͳ������
	
	// flash������
	private String tempDataStr;					// �¶������ַ�				
	private String tempConfigStr;				// �¶������ַ�				
	private String humiDataStr;					// ʪ�������ַ�			
	private String humiConfigStr;				// �¶������ַ�
	private StringBuffer tempDataBuf =
		new StringBuffer();						// ����flash�¶�����
	private StringBuffer humiDataBuf = 
		new StringBuffer();						// ����flashʪ������
	
	// table������
	private String strJspTableHeader; 			// table��������
	private String strJspTable; 				// table���� -- ����
	private List<String> jsptableList;			// table���� -- ��list
	private Date firstDate;						// ������ʱ��,����ĵ�һ������
	private Date lastDate;						// ������ʱ��,�������������

	// ҳ������
	private Map<Integer,String> searchInterval;	// �������
	private Map<String, String> areaToEqList;	// ҳ���ַѡ���õ�map(������,�����е������б�)	
	private int showType;						// ҳ����ʾ����(1:���� 2:����)
	private int maxSelector;					// ҳ���������ʾ�ĵ�ַ����	
	private int max_rs_now;						// ҳ���������ʾ��ʱ������-��ʱ
	private int max_rs_day;						// ҳ���������ʾ��ʱ������-�ձ�	
	private int max_rs_month;					// ҳ���������ʾ��ʱ������-�±�	

	// ҳ�洫�ݵĲ���
	private String placeListids="";				// ѡ�е��б�
	private String timeFrom="";					// ��ʼʱ��
	private String timeTo="";					// ����ʱ��
	private int interval;						// ʱ���� 
	
	// ��ѯҩ��������
	private	String tempDown = "0";				// �¶�����
	private	String tempUp = "40"; 				// �¶�����
	private	String humiDown = "0";				// ʪ������
	private	String humiUp = "100";				// ʪ������
	
	// ��������-ϸ��
	private String headTitle;					// ��ʽ:��˾�� -[��ʷ����]��ѯ��� 
	private String headDetail;					// ��ʽ:��ѯ����: �ձ��� ��ѯʱ�䷶Χ: 2009��11��06�� 12ʱ~ 2009��11��06�� 23ʱ
	
	// ͳ��ϵͳ�����������������: 1:ϵͳ�м����¶�����ʪ������		2:ϵͳ��ȫ�����¶�����	3:ϵͳ��ȫ����ʪ������
	@SuppressWarnings("unused")
	private int systemEqType;		
	
	
	// ���췽��
	public MainHistoryAction() {
	}
	//ע��service -- spring ioc
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}	
	public void setServiceSqlServer(ServiceSqlServer serviceSqlServer) {
		this.serviceSqlServer = serviceSqlServer;
	}
	
	@Override
	public String execute() {
		//ҳ���ַѡ���õ�map(������,�����е������б�)
		//areaToEqList = CommonDataUnit.createEqAreaWithNOSel(null);	
		areaToEqList = commonDataUnit.createEqAreaWithNOSel(null);	
		// ҳ���������ʾ�ĵ�ַ����
		if (showType == 1){	// ����
			maxSelector = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_SELECT_COUNT_TABLE));
			max_rs_now = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_TABLE_NOW));
			max_rs_day = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_TABLE_DAY));
			max_rs_month = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_TABLE_MONTH));
		}else if (showType == 2){	// ����
			maxSelector = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_SELECT_COUNT_FLASH));
			max_rs_now = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_FLASH_NOW));
			max_rs_day = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_FLASH_DAY));
			max_rs_month = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_FLASH_MONTH));
		}
		return SUCCESS;
	}
	
	/**
	 * @describe: ���ӵ���־
	 * @param typeStr 1:��ʱ���� 2:�ձ� 3:�±� 4:��ʱ���� 5:������ 6:������
	 * @date:2009-12-22
	 */
	public void insertToLog(String typeStr, long runtime){
		StringBuffer strbuf = new StringBuffer();
		strbuf.append(getSysUserName()+":��ѯ");
		strbuf.append(typeStr);
		strbuf.append("  ");
		strbuf.append("ʱ���:");
		strbuf.append(timeFrom);
		strbuf.append("~");
		strbuf.append(timeTo);
		strbuf.append("-->��̨��ʱ:" + runtime + "����");
		//MainService mainService = new MainService();
		mainService.packTlog(TLog.SEARCH_LOG, strbuf.toString());
	}
	
	// ��ˮ���� -json
	public String freeTimeJson() {
		long start = System.currentTimeMillis();
		int statInt = 0; // ��ҳ���ȡmax(1000),avg(100),min(10)��ֵ���ܺ�	
		for (int i = 0; i < statBox.length; i++) {
			statInt += statBox[i];
		}
		boolean haveData = false;
		try {
			haveData = searchForJson(statInt, MainService.TYPE_RECENT, interval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (haveData){
			start = System.currentTimeMillis() - start ;
			insertToLog("��ˮ����",start);
			return PRINTOUT;
		}else{
			nodataInfo(1);
			return NODATARETURN;
		}	
	}

	// ������-  -json
	public String dailyJson() {
		long start = System.currentTimeMillis();
		int statInt = 0; // ��ҳ���ȡmax(1000),avg(100),min(10)��ֵ���ܺ�	
		for (int i = 0; i < statBox.length; i++) {
			statInt += statBox[i];
		}
		boolean haveData = false;
		try {
			interval = 1; // �ձ����߼��Ϊ1
			haveData = searchForJson(statInt, MainService.TYPE_DAILY, interval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (haveData){
			start = System.currentTimeMillis() - start ;
			insertToLog("������",start);
			return PRINTOUT;
		}else{
			nodataInfo(1);
			return NODATARETURN;
		}	
	}

	// �±����� -json
	public String monthJson() {
		long start = System.currentTimeMillis();
		int statInt = 0; // ��ҳ���ȡmax(1000),avg(100),min(10)��ֵ���ܺ�	
		for (int i = 0; i < statBox.length; i++) {
			statInt += statBox[i];
		}
		boolean haveData = false;
		try {
			interval = 1; // �±����߼��Ϊ1
			haveData = searchForJson(statInt, MainService.TYPE_MONTH, interval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (haveData){
			start = System.currentTimeMillis() - start ;
			insertToLog("������", start);
			return PRINTOUT;
		}else{
			nodataInfo(1);
			return NODATARETURN;
		}	
	}

	/**
	 * @describe: û������ʱ��ʾ����Ϣ
	 * @param type: 1:���� 2:����
	 * @date:2009-12-10
	 */
	private void nodataInfo(int type) {
		StringBuffer strbuf = new StringBuffer();
		headTitle = "����������ʷ" + (type==1?"����":"����")+"...";
		strbuf.append("����:");
		strbuf.append("<br/>");
		strbuf.append("<label>");
		strbuf.append("    �������ʼʱ��ͽ���ʱ��");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<label>");
		strbuf.append("    ��ѡ���ʵ�������");
		strbuf.append("</label>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("*���������,��Ȼû�в�ѯ���,��ô���ʱ����ȷʵû�����ݼ�¼����...");		
		headDetail = strbuf.toString();
	}

	// ��ˮ��¼ - table
	public String freeTime() {
		long start = System.currentTimeMillis();
		int statInt = 0; // ��ҳ���ȡmax(1000),avg(100),min(10)��ֵ���ܺ�	
		for (int i = 0; i < statBox.length; i++) {
			statInt += statBox[i];
		}
		boolean haveData = false;
		try {
			haveData = searchForTable(statInt, MainService.TYPE_RECENT, interval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (haveData){
			setInfoForTable(MainService.TYPE_RECENT );
			start = System.currentTimeMillis() - start ;
			insertToLog("��ˮ��¼",start);
			return PRINTOUT;
		}else{
			nodataInfo(2);
			return NODATARETURN;
		}	
	}

	// �ձ��� - table
	public String daily() {
		long start = System.currentTimeMillis();
		int statInt = 0; // ��ҳ���ȡmax(1000),avg(100),min(10)��ֵ���ܺ�	
		for (int i = 0; i < statBox.length; i++) {
			statInt += statBox[i];
		}
		interval = 1; // �ձ�����Ϊ1
		boolean haveData = false;
		try {
			haveData = searchForTable(statInt, MainService.TYPE_DAILY, interval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (haveData){
			setInfoForTable(MainService.TYPE_DAILY );
			start = System.currentTimeMillis() - start ;
			insertToLog("�ձ���",start);
			return PRINTOUT;
		}else{
			nodataInfo(2);
			return NODATARETURN;
		}
	}

	// �±��� - table
	public String month() {
		long start = System.currentTimeMillis();
		int statInt = 0; // ��ҳ���ȡmax(1000),avg(100),min(10)��ֵ���ܺ�	
		for (int i = 0; i < statBox.length; i++) {
			statInt += statBox[i];
		}
		interval = 1; // �±�����Ϊ1
		boolean haveData = false;
		try {
			haveData = searchForTable(statInt, MainService.TYPE_MONTH, interval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (haveData){
			start = System.currentTimeMillis() - start ;
			insertToLog("�±���",start);
			setInfoForTable(MainService.TYPE_MONTH );
			return PRINTOUT;
		}else{
			nodataInfo(2);
			return NODATARETURN;
		}
	}
	
	/**
	 * @describe: ����ͳ�����ͺ��������,����ҳ����ʾ��Ϣ	
	 * @param selectType ͳ������(MainService.TYPE_RECENT:��ʱ���� 
	 * 							   MainService.TYPE_DAILY:�����ձ�����
	 * 							   MainService.TYPE_MONTH:�����±�����) 
	 * @return:
	 * @date:2009-12-10
	 */
	public void setInfoForTable(int statType){
		String tempStr = "";
		StringBuffer strbuf = new StringBuffer();
		
		// headTitle��ʽ:��˾�� -[��ʷ����]��ѯ��� 
		// headDetail��ʽ:��ѯ����: �ձ��� 	ʱ�䷶Χ: 2009��11��06�� 12ʱ~ 2009��11��06�� 23ʱ		
		//strbuf.append(CommonDataUnit.getSysArgsByKey(BeanForSysArgs.COMPANY_NAME));//��˾��
		strbuf.append(commonDataUnit.getSysArgsByKey(BeanForSysArgs.COMPANY_NAME));//��˾��
		strbuf.append("-��ʷ���ݲ�ѯ��� ");
		headTitle = strbuf.toString();
		
		strbuf.delete(0, strbuf.toString().length() - 1);
		tempStr = statType==MainService.TYPE_RECENT?"��ˮ����":
				statType==MainService.TYPE_DAILY?"�ձ���":"�±���";
		strbuf.append("<label>��ѯ����:" + tempStr +"</label>");
		// ͳ������
		strbuf.append("<label>ͳ������: ");
		for (int i = 0; i < statBox.length; i++) {
			if (statBox[i] == MainService.TYPE_MIN_STAT){
				strbuf.append(" ��Сֵ");		
			}else if (statBox[i] == MainService.TYPE_AVG_STAT){
				strbuf.append(" ƽ��ֵ");	
			}else if (statBox[i] == MainService.TYPE_MAX_STAT){
				strbuf.append(" ���ֵ");	
			}
		}			
		strbuf.append("</label>");
		tempStr = statType==MainService.TYPE_RECENT?"<label>���:"+interval+"����</label>":"";
		strbuf.append(tempStr);
		strbuf.append("<br/>");
		tempStr = getDateStringByStat(firstDate, statType);
		strbuf.append("<label>ʱ�䷶Χ:" + tempStr);
		tempStr = getDateStringByStat(lastDate, statType);
		strbuf.append("~" + tempStr);
		strbuf.append("</label>");
		
		headDetail = strbuf.toString();
	}

	/**
	 * @describe: ����ͳ�����͸�ʽ��ʱ��	
	 * @param date Ҫ��ʽ����ʱ��
	 * @param statType ͳ������(MainService.TYPE_RECENT:��ʱ���� 
	 * 							   MainService.TYPE_DAILY:�����ձ�����
	 * 							   MainService.TYPE_MONTH:�����±�����) 
	 * @return:
	 * @date:2009-12-10
	 */
	private String getDateStringByStat(Date date, int statType) {
		String dateStr = "";
		if(statType == MainService.TYPE_RECENT){//��: 2009��05��01�� 05ʱ12��56��
			dateStr = FunctionUnit.getDateToStr(date,
					FunctionUnit.Calendar_END_SECOND, FunctionUnit.SHOW_CHINESE);
		}else if(statType == MainService.TYPE_DAILY){//��:2009��05��01��
			dateStr = FunctionUnit.getDateToStr(date,
					FunctionUnit.Calendar_END_HOUR, FunctionUnit.SHOW_CHINESE);
		}else if(statType == MainService.TYPE_MONTH){//��:2009��05��
			dateStr = FunctionUnit.getDateToStr(date,
					FunctionUnit.Calendar_END_DAY, FunctionUnit.SHOW_CHINESE);
		}
		return dateStr;
	}

	/**
	 * @describe :����type(ͳ������),����flash����ԭ��
	 * @param type :ͳ������.(TYPE_NO_STAT:û��ͳ��,TYPE_MAX_STAT:ͳ�����ֵ,
	 *            ����TYPE_MIN_STAT��TYPE_AVG_STAT)
	 * @param selectType : ����������������(	MainService.TYPE_RECENT:��ʱ���� 
	 * 										MainService.TYPE_DAILY:�����ձ�����
	 * 										MainService.TYPE_MONTH:�����±�����)
	 * @param interval : ���ݲ�ѯ�ļ��.Ŀǰֻ����ˮ������ʹ��������� 
	 * @throws Exception:���ݿ��쳣,�Լ������쳣
	 * @date:2009-11-2
     */	
	public boolean searchForJson(int type, int selectType,int interval) throws Exception {
		boolean  rsBool = false;
		
		// ʵ����������
//		if (mainService == null) {
//			mainService = new MainService();
//		}
		// ȥ�����һ������,js���Ѿ�����
		// placeListids = placeListids.equals("")?"0":placeListids.substring(0, placeListids.length()-1);		
		// ������¼ 
		List<BeanForRecord> recordBeanListRs = mainService.searchRecords(fillRecordSearchBean(placeListids,
				timeFrom, timeTo, selectType, 1));

		// ��������������з���,����װ��ҳ����ʾ����
		// tableԪ��
		if (recordBeanListRs.size() > 0) {
			rsBool = true;
			// ͳ��ϵͳ�����������������: 1:ϵͳ�м����¶�����ʪ������		2:ϵͳ��ȫ�����¶�����	3:ϵͳ��ȫ����ʪ������
			int sysEqType = getSystemEqType();					
			
			// :����,����װflash����
			package_Data(sysEqType, filldataWithDate(recordBeanListRs), placeListids, type, selectType, interval, 2);
			// �����ʪ�������ַ�
			tempDataStr = tempDataBuf.toString();		// �¶������ַ�
			humiDataStr = humiDataBuf.toString();		//  ʪ�������ַ�
			// �����ʪ�������ַ�
			flashConfig(placeListids, getFlashTitle(type, selectType));
		}
		
		// ������
		recordBeanListRs = null;
		return rsBool;
	}
	
	/**
	 * @describe:	��ȡflash���� -- û������������, �зŴ���(�����ʪ�������ַ�)
	 * @param userPlaceList ѡ��ĵ�ַ�б�
	 * @param headerStr ��ʪ�����߱���
	 * @return:
	 * @date:2009-12-8
	 */
	public void flashConfig(String userPlaceList, String headerStr){
		List<String> equipLabels = new ArrayList<String>();		// ������ǩ�б�				
		
		// ��ȡѡ���б��������������	-- ׼������
		String[] strArray = userPlaceList.split(",");
		int[] equipmentIds = new int[strArray.length];		
		for (int i = 0; i < strArray.length; i++) {
			equipmentIds[i] = Integer.parseInt(strArray[i].trim());
		}
		// ��ȡ��ʾ���� -- ׼������
		//int tempType = Integer.parseInt(CommonDataUnit.
		int tempType = Integer.parseInt(commonDataUnit.
				getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)); // �¶���ʾ����(�¶���ʾ��ʽ(1:���� 2:����))
		// ��ȡ������ǩ�б� -- ׼������
		for (Integer equipId: equipmentIds) {
			//equipLabels.add(CommonDataUnit.getEquiMapStrById(equipId));		
			equipLabels.add(commonDataUnit.getEquiMapStrById(equipId));		
		}
		
		// ��ʪ�� -- ������������
		BeanForFlashSetting configbean = new BeanForFlashSetting();
		configbean.redraw = true;									// ��������Ӧ��С(Ĭ���ǹرյ�)
		configbean.zoomable_use = true;								// ʹ�÷Ŵ���
		configbean.grid_x_show_count = 10 ;							// X����ʾ10��ǩ
		configbean.bullet_size = 4 ;								// ����Բ���С��Ϊ4
		configbean.flashdata = equipLabels ;						// flsh����ͼ����List�б�
		configbean.label_name = headerStr;							// flash��ǩͷ
		
		// �¶�����
		configbean.y_value_append = tempType == 1 ? "��" : "F";	// Y�ḽ�ӱ�ǩ
		configbean.legend_value_append = tempType == 1 ? "��" : "F";	
		
		// �õ��¶����������ַ�
		tempConfigStr = configbean.toString();		
		
		// ʪ������
		configbean.y_value_append = "%";// Y�ḽ�ӱ�ǩ
		configbean.legend_value_append = "%RH";
		// �õ�ʪ�����������ַ�
		humiConfigStr = configbean.toString();										
	}		
	
	/**
	 * @describe:	�õ�flash����ʱ��ͷ��<br>
	 * ��ʽ:��ѯ����: ��ˮ��¼/�ձ���/�±���   		ͳ������: ��Сֵ/ƽ��ֵ/���ֵ		��ѯʱ�䷶Χ: fromtime ~ endtime<br>
	 * @param type :ͳ������.(TYPE_NO_STAT:û��ͳ��,TYPE_MAX_STAT:ͳ�����ֵ,
	 *            ����TYPE_MIN_STAT��TYPE_AVG_STAT)
	 * @param selectType : ����������������(	MainService.TYPE_RECENT:��ʱ���� 
	 * 										MainService.TYPE_DAILY:�����ձ�����
	 * 										MainService.TYPE_MONTH:�����±�����)
	 * @date:2009-11-12
	 */
	private String getFlashTitle(int type, int selectType) {
		// ��ʽ:��ѯ����: /�ձ���/�±���   		ͳ������: ��Сֵ/ƽ��ֵ/���ֵ		��ѯʱ�䷶Χ: fromtime ~ endtime
		StringBuffer strbuf = new StringBuffer();
		String startTimeStr = "", endTimeStr = "", another = "";
		
		strbuf.append("��ѯ����: ");
		if (selectType == MainService.TYPE_RECENT){
			strbuf.append("��ˮ����		");
			another = "		���" + interval + "����";
		}else if(selectType == MainService.TYPE_DAILY){
			strbuf.append("�ձ�����		");			
		}else if(selectType == MainService.TYPE_MONTH){
			strbuf.append("�±�����		");			
		}
		startTimeStr = getDateStringByStat(firstDate,selectType);
		endTimeStr = getDateStringByStat(lastDate,selectType);
		
		// ͳ������
		if (type == MainService.TYPE_NO_STAT){	// ����
		}else if (type == MainService.TYPE_MIN_STAT){
			strbuf.append("ͳ������: ");
			strbuf.append("��Сֵ	");			
		}else if (type == MainService.TYPE_AVG_STAT){
			strbuf.append("ͳ������: ");
			strbuf.append("ƽ��ֵ	");			
		}else if (type == MainService.TYPE_MAX_STAT){
			strbuf.append("ͳ������: ");
			strbuf.append("���ֵ	");			
		}
		
		strbuf.append("ʵ�ʲ�ѯʱ�䷶Χ: ");
		strbuf.append(startTimeStr + " ~ " + endTimeStr + another);
		
		return strbuf.toString();
	}
	
	/**
	 * @describe :����type(ͳ������),����tableԭ��
	 * @param type :ͳ������.(TYPE_NO_STAT:û��ͳ��,TYPE_MAX_STAT:ͳ�����ֵ,
	 *            ����TYPE_MIN_STAT��TYPE_AVG_STAT)
	 * @param selectType : ����������������(	MainService.TYPE_RECENT:��ʱ���� 
	 * 										MainService.TYPE_DAILY:�����ձ�����
	 * 										MainService.TYPE_MONTH:�����±�����)
	 * @param interval : ���ݲ�ѯ�ļ��.Ŀǰֻ����ˮ������ʹ��������� 
	 * @return true:�����ݷ���  false:�����ݷ��� 
	 * @throws Exception:���ݿ��쳣,�����쳣
	 * @date:2009-11-2
     */
	public boolean searchForTable(int type, int selectType, int interval) throws Exception {
		boolean  rsBool = false;
		// ʵ����������
//		if (mainService == null) {
//			mainService = new MainService();
//		}
		
		// ȥ�����һ������,js���Ѿ�����
		//placeListids = placeListids.equals("")?"0":placeListids.substring(0, placeListids.length()-1);		
		// ������¼ 
		List<BeanForRecord> recordBeanListRs = mainService.searchRecords(fillRecordSearchBean(placeListids,
				timeFrom, timeTo, selectType, 1));
		
		// ��������������з���,����װ��ҳ����ʾ����
		// tableԪ��
		if (recordBeanListRs.size() > 0) {
			// ͳ��ϵͳ�����������������: 1:ϵͳ�м����¶�����ʪ������		2:ϵͳ��ȫ�����¶�����	3:ϵͳ��ȫ����ʪ������
			int sysEqType = getSystemEqType();	
			
			rsBool = true;	// �����ݷ���
			// 1:����,����װ����
			jsptableList = package_Data(sysEqType, filldataWithDate(recordBeanListRs), placeListids, type, selectType, interval,1);
			// 2:ҳ����ʾ�ĵ�ַ�б�--��ʾ�ڵ�һ��
			strJspTableHeader = packageToTableHeader(sysEqType, placeListids, type);
		}
		// ������
		recordBeanListRs = null;
		return rsBool;
	}

	/**
	 * @describe: ����������,�������ݵı���
	 * @param sysEqType : ͳ��ϵͳ�����������������: 1:ϵͳ�м����¶�����ʪ������		2:ϵͳ��ȫ�����¶�����	3:ϵͳ��ȫ����ʪ������
	 * @param placeListids : Ҫ��ʾ�ĵ�ַ�б�,���Ÿ����������ַ���.�磺1,2,3,5
	 * @param type :ͳ������.(TYPE_NO_STAT:û��ͳ��,TYPE_MAX_STAT:ͳ�����ֵ,
	 *            ����TYPE_MIN_STAT��TYPE_AVG_STAT) TYPE_RECENTʱ<td colspan="2">
	 *            ,�����<td colspan="2��4��6">
	 * @return: jspҳ��ɵ�һ��<tr></tr>���ϵ��ַ������
	 * @date:2009-11-2
     */
	private String packageToTableHeader(int sysEqType, String placeListids, int type) {
		StringBuffer strBufA = new StringBuffer();
		StringBuffer strBufB = new StringBuffer();
		String[] strArray = placeListids.split(",");
		int intTemp;
		String strTemp = null;
		String str_Temp = "";
		String str_Humi = "";
		
		// ��ȡ��ʾ���� -- ׼������
		//int tempType = Integer.parseInt(CommonDataUnit.
		int tempType = Integer.parseInt(commonDataUnit.
				getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)); // �¶���ʾ����(�¶���ʾ��ʽ(1:���� 2:����))
		String tempTypeShowStr = tempType==1?"��":"F";

		// ��������type,�趨tdԪ����Ҫռ������
		intTemp = 0;
		int colspan = sysEqType == 1 ? 2 : 1;
		if (type >= MainService.TYPE_MAX_STAT) { // ���ֵ,�ձ����±���
			type = type - MainService.TYPE_MAX_STAT;
			intTemp += colspan;
			str_Temp += sysEqType != 3?"<td>����¶�(" + tempTypeShowStr + ")</td>":"";
			str_Humi += sysEqType != 2?"<td>���ʪ��(%RH)</td>":"";
		}
		if (type >= MainService.TYPE_AVG_STAT) { // ƽ��ֵ,�ձ����±���
			type = type - MainService.TYPE_AVG_STAT;
			intTemp += colspan;
			str_Temp += sysEqType != 3?"<td>ƽ���¶�(" + tempTypeShowStr + ")</td>":"";
			str_Humi += sysEqType != 2?"<td>ƽ��ʪ��(%RH)</td>":"";
		}
		if (type >= MainService.TYPE_MIN_STAT) { // ��Сֵ,�ձ����±���
			type = type - MainService.TYPE_MIN_STAT;
			intTemp += colspan;
			str_Temp += sysEqType != 3?"<td>��С�¶�(" + tempTypeShowStr + ")</td>":"";
			str_Humi += sysEqType != 2?"<td>��Сʪ��(%RH)</td>":"";
			type = 0;		//��֤��������һ��if
		}
		if (type >= MainService.TYPE_NO_STAT) { // ��ͳ��ֻ��ʾ����,��ʱ������
			type = type - MainService.TYPE_NO_STAT;
			intTemp += colspan;
			str_Temp += sysEqType != 3?"<td>�¶�(" + tempTypeShowStr + ")</td>":"";
			str_Humi += sysEqType != 2?"<td>ʪ��(%RH)</td>":"";
		}

		// �о� ������ʶ��
		strBufA.append("<td rowspan=2>NO</td><td rowspan=2>��¼ʱ��</td>");
		Map<Integer, String> placeList = getPlaceList();
		for (int i = 0; i < strArray.length; i++) {
			// ������ʶ��
			strTemp = placeList.get(Integer.parseInt(strArray[i].trim()));
			strBufA.append("<td colspan=\"" + intTemp + "\">");
			strBufA.append(strTemp + "</td>");
			strBufB.append(str_Temp + str_Humi);
		}

		return strBufA.toString() + "</tr><tr class=\"top\">"+ strBufB.toString();
	}
	
	/**
	 * @describe: ���ذ�ʱ�����еĽ����
	 * @param recordBeanListRs : Ҫ���������ݼ�
	 * @date:2009-12-7
	 */
	private Map<Date, Map<Integer, BeanForRecord>> filldataWithDate(List<BeanForRecord> recordBeanListRs){
		
		Map<Date, Map<Integer, BeanForRecord>> sortStore = 
			new LinkedHashMap<Date, Map<Integer,BeanForRecord>>();					// ��ʱ������Ľ����
		Map<Integer, BeanForRecord> tempStore = new HashMap<Integer, BeanForRecord>();	// ��ʱ���ݼ�(sortStore�е�Ԫ��)
		
		// recordBeanListRs����ǰ���ʱ�����������е�,��˿��Ը���ʱ��������
		int equipmentId = 0 ;
		Date currentDate = null; // ���ݼ�������ǰʱ��
		Date preDate = null; // ���ݼ�����ǰһ��ʱ��
		for (BeanForRecord recordBean : recordBeanListRs) {
			equipmentId = recordBean.getEquipmentId();
			currentDate = recordBean.getRecTime();	// ������ǰʱ��
			
			// �ظ�Idʱ,��ʱ�䲻ͬʱ,���� �ڴ�ת��,������Ŀ������ 
			// ��������޸�ϵͳʱ��,����ͬһʱ����������¼.���������,ȡ���������,����ǰ�������
			if (tempStore.containsKey(equipmentId)){
				if (currentDate.compareTo(preDate)!=0){
					Map<Integer, BeanForRecord> tempStore_ = new HashMap<Integer, BeanForRecord>();
					for (int equipId : tempStore.keySet()) {	// ת���ռ�
						tempStore_.put(equipId, tempStore.get(equipId));
					}// end for
					sortStore.put(preDate, tempStore_);
					tempStore.clear();
				}
			}	
			
			tempStore.put(equipmentId, recordBean);
			preDate = recordBean.getRecTime();	// ����ǰһ��ʱ��
		}
		
		// �������һ�����ݼ�
		Map<Integer, BeanForRecord> tempStore_ = new HashMap<Integer, BeanForRecord>();
		for (int equipId : tempStore.keySet()) {	// ת���ռ�
			tempStore_.put(equipId, tempStore.get(equipId));
		}// end for
		sortStore.put(currentDate, tempStore_);		
		tempStore = null;
		
		return sortStore;
	}
	
	/**
	 * @describe: ������(��ʱ����)���з���,����װ��jspҳ�������ʾ��<tr>...</tr>����
	 * @param sysEqType : ͳ��ϵͳ�����������������: 1:ϵͳ�м����¶�����ʪ������		2:ϵͳ��ȫ�����¶�����	3:ϵͳ��ȫ����ʪ������
	 * @param sortStore : ��ʱ�����д�������ݼ�
	 * @param placeListids : Ҫ��ʾ�ĵ�ַ�б�,���Ÿ����������ַ���.�磺1,2,3,5
	 * @param type :ͳ������.(TYPE_NO_STAT:tû��ͳ��,TYPE_MAX_STAT:ͳ�����ֵ,
	 *            ����TYPE_MIN_STAT��TYPE_AVG_STAT)
	 * @param selectType : ����������������( MainService.TYPE_RECENT:��ʱ����	
	 * 			MainService.TYPE_DAILY:�����ձ�����	MainService.TYPE_MONTH:�����±�����)             
	 * @param interval : ���ݲ�ѯ�ļ��.Ŀǰֻ����ˮ������ʹ��������� 
	 * @param tableOrflash : 1: table����,2: flash����
	 * @return jspҳ�������ʾ��<tr></tr>���ϵ��ַ������
	 * @throws Exception 
	 * @date:2009-10-31
	 */
	private List<String> package_Data(int sysEqType, Map<Date, Map<Integer, BeanForRecord>> sortStore, String placeListids, 
			int type,  int selectType, int interval, int tableOrflash) throws Exception {
		
		//StringBuffer strBuf = new StringBuffer();					// ���ؽ����buffer -- ����
		List<String> rsList = new CopyOnWriteArrayList<String>();	// ���ؽ����List
		
		// ��ȡʱ��ĸ�ʽ			-- ׼������
		int calendar_type = 0;
		if(selectType == MainService.TYPE_RECENT){		//	��ʱ���� 
			calendar_type = FunctionUnit.Calendar_END_SECOND ;
			interval = interval *60;
		}else if(selectType == MainService.TYPE_DAILY){	//	�ձ�����
			calendar_type = FunctionUnit.Calendar_END_HOUR ;
		}else if(selectType == MainService.TYPE_MONTH){	//	�±�����
			calendar_type = FunctionUnit.Calendar_END_DAY ;
		}
		
		// ��ȡѡ���б��������������	-- ׼������
		String[] strArray = placeListids.split(",");
		int[] equipmentIds = new int[strArray.length];		
		for (int i = 0; i < strArray.length; i++) {
			equipmentIds[i] = Integer.parseInt(strArray[i].trim());
		}
		
//		for (Date temp_Date : sortStore.keySet()) {	// �������
//			for (Integer equipId : sortStore.get(temp_Date).keySet()) { // �ڲ�����
//				System.out.println(equipId+":"+temp_Date.toLocaleString()+":"+sortStore.get(temp_Date).get(equipId).getHumiAvg());
//			}
//		}
		
		// ��ȡ��ʾ���� -- ׼������
		//int tempType = Integer.parseInt(CommonDataUnit.
		int tempType = Integer.parseInt(commonDataUnit.
				getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)); // �¶���ʾ����(�¶���ʾ��ʽ(1:���� 2:����))
		
		// ��ʼˢѡ����
		String tempStr = null;	// ��ʱ��Ž����
		Date nextDate = null;	// ��ʱʱ�� - �����¸�ʱ��
		Date currentDate = null;		// ��ʱʱ�� - ���浱ǰʱ��
		int rowCount = 1;		// ������ʾ�кŵ�
		Map<Integer, BeanForRecord> tempBean = new HashMap<Integer, BeanForRecord>();
		boolean tempBool = selectType == MainService.TYPE_RECENT;// �����Ƿ��Ǽ�ʹ����
		
		Set<Entry<Date, Map<Integer, BeanForRecord>>> entrys = sortStore.entrySet();// ��ֵ��(���)
		Date temp_Date; // ��-ʱ��(���)
		Map<Integer, BeanForRecord> value; // ֵ(���)
		int equipId ;// ��-����ID(�ڲ�)
		BeanForRecord bean = null; // ֵ(�ڲ�)
        for(Entry<Date, Map<Integer, BeanForRecord>> entry: entrys){
        	temp_Date = null; temp_Date = entry.getKey();
        	value = null; 	value = entry.getValue();
        	lastDate = temp_Date;	// ��ȡ���һ������
        	if (nextDate == null){		// ��ȡ�Ƚϵ�ʱ���
        		firstDate = temp_Date;	// ������ʱ��,��ȡ��һ������,���������Ƚ�	
        		currentDate = temp_Date;
        		nextDate = FunctionUnit.nextTime(temp_Date, interval, calendar_type).getTime();
        	}else{
        		while (nextDate.after(temp_Date) == false) {
        			// ��������ͬ���뻺��,ֱ����һ������ȵ�ʱ��,Ȼ�����Щ���洦����<tr></tr>Ԫ�� ����flashԪ��
        			tempStr = FunctionUnit.getDateToStr(currentDate, 
        					(calendar_type == FunctionUnit.Calendar_END_SECOND?FunctionUnit.Calendar_END_MINUTE : calendar_type)
        					,FunctionUnit.UN_SHOW_CHINESE);	// ʱ���ַ���
        			
        			if (tableOrflash == 1){	// table ʱ����
        				tempStr = makeTrElement(sysEqType, rowCount, tempBean, equipmentIds, type, tempStr);
        				rsList.add(tempStr);
        				//strBuf.append(tempStr);
        			}else if (tableOrflash == 2) { //flash ʱ����
        				makeFlashBuf(tempBean, equipmentIds, type, tempStr);
        			}
        			tempBean.clear(); tempBean = null;
        			tempBean = new HashMap<Integer, BeanForRecord>();
        			currentDate = nextDate;
        			nextDate = FunctionUnit.nextTime(nextDate, interval, calendar_type).getTime();
        			rowCount ++;
				}
        	}
	        for(Entry<Integer, BeanForRecord> entry_: value.entrySet()){
	            equipId= entry_.getKey();
	            bean = null;	bean = entry_.getValue();
				if (tempBean.containsKey(equipId)){		// ���µ������������,��С,ƽ����ֵ
					// ����¶�
					tempStr = FunctionUnit.getStatFloat((tempBool?bean.getTemperature():bean.getTempMax()),
							tempBean.get(equipId).getTempMax(),FunctionUnit.STAT_MAX, tempType);
					tempBean.get(equipId).setTempMax(tempStr);
					// ��С�¶�
					tempStr = FunctionUnit.getStatFloat((tempBool?bean.getTemperature():bean.getTempMin()),
							tempBean.get(equipId).getTempMin(),FunctionUnit.STAT_MIN, tempType);					
					tempBean.get(equipId).setTempMin(tempStr);
					// ƽ���¶�
					tempStr = FunctionUnit.getStatFloat((tempBool?bean.getTemperature():bean.getTempAvg()),
							tempBean.get(equipId).getTempAvg(),FunctionUnit.STAT_AVG, tempType);					
					tempBean.get(equipId).setTempAvg(tempStr);
					
					// ���ʪ��
					tempStr = FunctionUnit.getStatFloat((tempBool?bean.getHumidity():bean.getHumiMax()),
							tempBean.get(equipId).getHumiMax(),FunctionUnit.STAT_MAX, 0);					
					tempBean.get(equipId).setHumiMax(tempStr);					
					// ��Сʪ��
					tempStr = FunctionUnit.getStatFloat((tempBool?bean.getHumidity():bean.getHumiMin()),
							tempBean.get(equipId).getHumiMin(),FunctionUnit.STAT_MIN, 0);					
					tempBean.get(equipId).setHumiMin(tempStr);
					// ƽ��ʪ��
					tempStr = FunctionUnit.getStatFloat((tempBool?bean.getHumidity():bean.getHumiAvg()),
							tempBean.get(equipId).getHumiAvg(),FunctionUnit.STAT_AVG, 0);					
					tempBean.get(equipId).setHumiAvg(tempStr);					
				}else{
					if(tempBool){// ��ʱ���� 
						tempStr = bean.getTemperature(); // ����ʱ�������,��С,ƽ����ֵ
						bean.setTempMax(tempStr);	bean.setTempMin(tempStr);	bean.setTempAvg(tempStr);
						tempStr = bean.getHumidity();
						bean.setHumiMax(tempStr);	bean.setHumiMin(tempStr);	bean.setHumiAvg(tempStr);
					}
					if (tempType == 2){// �¶���ʾ��ʽ(1:���� 2:����)
						tempStr = FunctionUnit.FLOAT_DATA_FORMAT.format( Float.parseFloat(bean.getTempAvg()) *9/5 + 32);
						bean.setTempAvg(tempStr);			
						tempStr = FunctionUnit.FLOAT_DATA_FORMAT.format( Float.parseFloat(bean.getTempMax()) *9/5 + 32);
						bean.setTempMax(tempStr);
						tempStr = FunctionUnit.FLOAT_DATA_FORMAT.format( Float.parseFloat(bean.getTempMin()) *9/5 + 32);
						bean.setTempMin(tempStr);
					}
					tempBean.put(equipId, bean);
					
				}// if (tempBean.containsKey(equipId))
	        }// for �ڲ�����
        }// for �������
		
		// forѭ��������,�������һ��û����ȥ,���ﲹ��
		tempStr = FunctionUnit.getDateToStr(lastDate, 
				(calendar_type == FunctionUnit.Calendar_END_SECOND?FunctionUnit.Calendar_END_MINUTE : calendar_type), 
				FunctionUnit.UN_SHOW_CHINESE);	// ʱ���ַ���
		
		if (tableOrflash == 1){	// table ʱ����
			tempStr = makeTrElement(sysEqType, rowCount, tempBean, equipmentIds, type, tempStr);
			rsList.add(tempStr);
			//strBuf.append(tempStr);
		}else if (tableOrflash == 2) { //flash ʱ����
			makeFlashBuf(tempBean, equipmentIds, type, tempStr);
		}
		sortStore.clear();	sortStore = null; tempBean.clear();	tempBean = null; 
		//return strBuf.toString();
		return rsList;
	}
	
	/**
	 * @describe: ���flash�¶���Ҫ������	
	 * @param store Ҫ��������ݼ��� 
	 * @param equipmentIds Ҫ��ʾ�ĵ�ַ�б�
	 * @param type :ͳ������.(TYPE_NO_STAT:û��ͳ��,TYPE_MAX_STAT:ͳ�����ֵ,
	 *            ����TYPE_MIN_STAT��TYPE_AVG_STAT)
	 * @param dateStr:ʱ���ַ���
	 * @date:2009-12-8
	 */
	private void makeFlashBuf(Map<Integer, BeanForRecord> store, int[] equipmentIds, 
			int type, String dateStr){
		BeanForRecord recordBean;
		StringBuffer temp_buf = new StringBuffer();
		temp_buf.append(dateStr);
		temp_buf.append(";");
		
		StringBuffer humi_buf = new StringBuffer();
		humi_buf.append(dateStr);
		humi_buf.append(";");
		
		// ���ݵ�ַ�б�,ѡ����ص���ʪ������
		for (int i = 0; i < equipmentIds.length; i++) {
			if (store.containsKey(equipmentIds[i])) {
				recordBean = null;
				recordBean = store.get(equipmentIds[i]);
				//���˲���������
				if (recordBean.getEquipmentId() <= 0) {
					temp_buf.append(";");
					humi_buf.append(";");
				}else { // �������ݽ���
					temp_buf.append(makeFlashPoint(recordBean, type, 1));
					temp_buf.append(";");
					humi_buf.append(makeFlashPoint(recordBean, type, 2));
					humi_buf.append(";");
				}
			} else {
				temp_buf.append(";");
				humi_buf.append(";");
			}
		}// end for
		
		// һ��ʱ�����,�ӻ��з�.ȥ�����һ���ֺ�
		tempDataBuf.append(temp_buf.substring(0, temp_buf.length()-1));
		tempDataBuf.append("\n");
		humiDataBuf.append(humi_buf.substring(0, humi_buf.length()-1));
		humiDataBuf.append("\n");
	}
	

	/**
	 * @describe: ����ͳ������,��ȡ�¶Ȼ���ʪ�ȵ�ͳ��ֵ	
	 * @param recordBean ���������ݼ�
	 * @param type :ͳ������.(TYPE_NO_STAT:û��ͳ��,TYPE_MAX_STAT:ͳ�����ֵ,
	 *            ����TYPE_MIN_STAT��TYPE_AVG_STAT)
	 * @param tempOrhumi: 1: �¶�	2: ʪ��
	 * @date:2009-12-8
	 */
	private String makeFlashPoint(BeanForRecord recordBean, int type, int tempOrhumi) {
		//EquipData equip = CommonDataUnit.getEquipByID(recordBean.getEquipmentId());
		EquipData equip = commonDataUnit.getEquipByID(recordBean.getEquipmentId());
		String tempStr="" ,humiStr="";
		if (equip == null) {
			return tempStr;
		}
		// eqType(1,'��ʪ��';2,'���¶�';3,'��ʪ��')
		int eqType = equip.getEquitype();
		
		// ��ȡ �� ʪ��
		if (type == MainService.TYPE_MAX_STAT){			//���ֵ
			tempStr = recordBean.getTempMax();
			humiStr = recordBean.getHumiMax();
		}
		if (type == MainService.TYPE_AVG_STAT){			//ƽ��ֵ
			tempStr = recordBean.getTempAvg();
			humiStr = recordBean.getHumiAvg();
		}
		if (type == MainService.TYPE_MIN_STAT){			//��Сֵ
			tempStr = recordBean.getTempMin();
			humiStr = recordBean.getHumiMin();
		}
		if (tempOrhumi == 1){ // �¶�
			tempStr = eqType == EquipData.TYPE_HUMI_ONLY? "": tempStr;
		}else if(tempOrhumi == 2){ // ʪ��
			tempStr = eqType == EquipData.TYPE_TEMP_ONLY? "": humiStr;
		}
		return tempStr;
	}

	/**
	 * @describe: ����Щ���洦����<tr>...</tr>Ԫ��	
	 * @param sysEqType : ͳ��ϵͳ�����������������: 1:ϵͳ�м����¶�����ʪ������		2:ϵͳ��ȫ�����¶�����	3:ϵͳ��ȫ����ʪ������
	 * @param rowCount ��ʾ������
	 * @param store Ҫ��������ݼ���
	 * @param equipmentIds Ҫ��ʾ�ĵ�ַ�б�
	 * @param type :ͳ������.(TYPE_NO_STAT:û��ͳ��,TYPE_MAX_STAT:ͳ�����ֵ,
	 *            ����TYPE_MIN_STAT��TYPE_AVG_STAT)
	 * @param dateStr ʱ���ַ���
	 * @return:
	 * @date:2009-12-8
	 */
	private String makeTrElement(int sysEqType, int rowCount, Map<Integer, BeanForRecord> store, int[] equipmentIds, 
			int type, String dateStr) {
		StringBuffer strBuf = new StringBuffer();
		BeanForRecord recordBean;
		String tempStr = "";
		
		strBuf.append("<tr>");
		strBuf.append("<td>" + rowCount + "</td>");
		strBuf.append("<td>" + dateStr + "</td>");
		// ���ݵ�ַ�б�,ѡ����ص���ʪ������
		for (int i = 0; i < equipmentIds.length; i++) {
			if (store.containsKey(equipmentIds[i])) {
				recordBean = null;
				recordBean = store.get(equipmentIds[i]);
				//���˲���������
				if (recordBean.getEquipmentId() <= 0) {
					tempStr = getEmptyTd(sysEqType, type);
				}else {
					tempStr = makeTdElement(sysEqType, recordBean, type);
				}
			} else {
				tempStr = getEmptyTd(sysEqType, type);
			}
			strBuf.append(tempStr);
		}// end for
		strBuf.append("</tr>");
		return strBuf.toString();
	}
	
	/**
	 * @describe: ��ѡ��ĵ�ַû������,��ʱ��ʾ��<td>Ԫ�ؾ�Ϊ"-",�����<td>Ԫ�ظ�������Ҫ����type��̬����
	 * @param sysEqType ϵͳ��ʪ������
	 * @param type ͳ������.(TYPE_NO_STAT:û��ͳ��,TYPE_MAX_STAT:ͳ�����ֵ,����TYPE_MIN_STAT��TYPE_AVG_STAT)
	 * @return: ����type,��̬����<td>����
	 * @date:2009-11-3
	 */
	private String getEmptyTd(int sysEqType,int type) {
		StringBuffer strBuf = new StringBuffer();
		if (type >= MainService.TYPE_MAX_STAT){
			type = type - MainService.TYPE_MAX_STAT;
			strBuf.append("<td>-</td>");
		}
		if (type >= MainService.TYPE_AVG_STAT){
			type = type - MainService.TYPE_AVG_STAT;
			strBuf.append("<td>-</td>");
		}
		if (type >= MainService.TYPE_MIN_STAT){
			type = type - MainService.TYPE_MIN_STAT;
			strBuf.append("<td>-</td>");
			type = 0;	//��֤��������һ��if
		}
		if (type >= MainService.TYPE_NO_STAT){
			type = type - MainService.TYPE_NO_STAT;
			strBuf.append("<td>-</td>");
		}
		//����ϵͳ��ʪ������,������
		String rsStr = sysEqType ==1 ? strBuf.toString() + strBuf.toString():strBuf.toString(); 
		return rsStr;
	}

	/**
	 * @describe :��������,���������������,Խ����ò�ͬ��css����ʾ,�õ��¶Ⱥ�ʪ��<td></td>Ԫ��
	 *           ע���:css�ǹ̶���.���ڽ���LOWDATACSS,��������NORMALDATACSS,���ڽ���HIGHDATACSS
	 * @param sysEqType :ϵͳ��ʪ������
	 * @param recordBean :��Ҫ����������,��Ҫ�漰:��ʪ�Ⱥ͵�ַ����
	 * @param type :ͳ������.(TYPE_NO_STAT:û��ͳ��,TYPE_MAX_STAT:ͳ�����ֵ,����TYPE_MIN_STAT��TYPE_AVG_STAT)
	 * @return:<td></td>Ԫ�������ַ���
	 * @date:2009-10-31
	 */
	private String makeTdElement(int sysEqType, BeanForRecord recordBean, int type) {
		//EquipData equip = CommonDataUnit.getEquipByID(recordBean.getEquipmentId());
		EquipData equip = commonDataUnit.getEquipByID(recordBean.getEquipmentId());
		StringBuffer strBuf = new StringBuffer();
		int typeTemp = type;
		//float tempFloat;
		String tempStr = "";

		if (equip == null) {
			tempStr = getEmptyTd(sysEqType, typeTemp);
			return tempStr;
		}
		
		// eqType(1,'��ʪ��';2,'���¶�';3,'��ʪ��')
		int eqType = equip.getEquitype();
		if (sysEqType != 3){
			// �¶�
			if (typeTemp >= MainService.TYPE_MAX_STAT){			//���ֵ
				typeTemp = typeTemp - MainService.TYPE_MAX_STAT;
				tempStr = eqType == EquipData.TYPE_HUMI_ONLY?"<td>-</td>": 
					fillTdContent(recordBean.getTempMax(), equip.getTempDown(), equip.getTempUp(), "��");
				strBuf.append(tempStr);
			}
			if (typeTemp >= MainService.TYPE_AVG_STAT){			//ƽ��ֵ
				typeTemp = typeTemp - MainService.TYPE_AVG_STAT;
				tempStr = eqType == EquipData.TYPE_HUMI_ONLY?"<td>-</td>": 
					fillTdContent(recordBean.getTempAvg(), equip.getTempDown(), equip.getTempUp(), "��");
				strBuf.append(tempStr);
			}
			if (typeTemp >= MainService.TYPE_MIN_STAT){			//��Сֵ
				typeTemp = typeTemp - MainService.TYPE_MIN_STAT;
				tempStr = eqType == EquipData.TYPE_HUMI_ONLY?"<td>-</td>": 
					fillTdContent(recordBean.getTempMin(), equip.getTempDown(), equip.getTempUp(), "��");
				strBuf.append(tempStr);
				typeTemp = 0;	//��֤��������һ��if
			}
			if (typeTemp >= MainService.TYPE_NO_STAT){			 //��ͳ��ֻ��ʾ����,��ʱ������(Ŀǰû���õ�)
				typeTemp = typeTemp - MainService.TYPE_NO_STAT;
				tempStr = eqType == EquipData.TYPE_HUMI_ONLY?"<td>-</td>": 
					fillTdContent(recordBean.getTemperature(), equip.getTempDown(), equip.getTempUp(), "��");
				strBuf.append(tempStr);
			}		
		}
		
		// ʪ��
		typeTemp = type;
		if (sysEqType != 2){
			if (typeTemp >= MainService.TYPE_MAX_STAT){			//���ֵ
				typeTemp = typeTemp - MainService.TYPE_MAX_STAT;
				tempStr = eqType == EquipData.TYPE_TEMP_ONLY?"<td>-</td>":
					fillTdContent(recordBean.getHumiMax(), equip.getHumiDown(), equip.getHumiUp(), "%");
				strBuf.append(tempStr);
			}
			if (typeTemp >= MainService.TYPE_AVG_STAT){			//ƽ��ֵ
				typeTemp = typeTemp - MainService.TYPE_AVG_STAT;
				tempStr = eqType == EquipData.TYPE_TEMP_ONLY?"<td>-</td>":
					fillTdContent(recordBean.getHumiAvg(), equip.getHumiDown(), equip.getHumiUp(), "%");
				strBuf.append(tempStr);
			}
			if (typeTemp >= MainService.TYPE_MIN_STAT){			//��Сֵ
				typeTemp = typeTemp - MainService.TYPE_MIN_STAT;
				tempStr = eqType == EquipData.TYPE_TEMP_ONLY?"<td>-</td>":
					fillTdContent(recordBean.getHumiMin(), equip.getHumiDown(), equip.getHumiUp(), "%");
				strBuf.append(tempStr);
				typeTemp = 0;	//��֤��������һ��if
			}
			if (typeTemp >= MainService.TYPE_NO_STAT){			 //��ͳ��ֻ��ʾ����,��ʱ������(Ŀǰû���õ�)
				typeTemp = typeTemp - MainService.TYPE_NO_STAT;
				tempStr = eqType == EquipData.TYPE_TEMP_ONLY?"<td>-</td>":
					fillTdContent(recordBean.getHumidity(),	equip.getHumiDown(), equip.getHumiUp(), "%");
				strBuf.append(tempStr);
			}			
		}			

		return strBuf.toString();
	}

	/**
	 * @describe: data����down(����),up(����),���<td>Ԫ��,��ʾ��ͬ��css��ʽ
	 * 			(LOWDATACSS,NORMALDATACSS,HIGHDATACSS)
	 * @param data :��Ҫ����������
	 * @param down :����
	 * @param up :����
	 * @param endFlag :������ĺ�׺. �¶�:��   ʪ��:% 
	 * @return :���<td>Ԫ��,��ʾ��ͬ��css��ʽ
	 * @date:2009-11-3
	 */
	private String fillTdContent(String data, float down, float up, String endFlag) {
		StringBuffer strBuf = new StringBuffer();
		float tempFloat;
		String tempStr = "";
		if (data.equals("")){
			strBuf.append("<td></td>");	
		}else{
			tempFloat = Float.parseFloat(data);
			tempStr = tempFloat < down ? LOWDATACSS	: tempFloat > up ? HIGHDATACSS: NORMALDATACSS;
			strBuf.append("<td class = \"" + tempStr + "\">");
			//strBuf.append(data + endFlag );
			strBuf.append(data);
			strBuf.append("</td>");		
		}
		return strBuf.toString();
	}

	/**
	 * @describe: ���� ������ַ�б�, ��ʼʱ��, ����ʱ���ѡ�������,���ɲ�ѯ����	
	 * @param places : ������ַ�б�
	 * @param startTime : ��ʼʱ��
	 * @param endTime ����ʱ��
	 * @param selectType : ����������������(	MainService.TYPE_RECENT:��ʱ���� 
	 * 										MainService.TYPE_DAILY:�����ձ�����
	 * 										MainService.TYPE_MONTH:�����±�����)
	 * @param orderByType : ��������: 1:order by recTime, equipmentId 2:order by equipmentId, recTime(���ڶ���1)
	 * @return ���� ������� ��ѯ����
	 * @throws Exception: �쳣
	 * @date:2009-11-3
	 */	
	public BeanForSearchRecord fillRecordSearchBean(String places, String startTime,
			String endTime, int selectType, int orderByType) throws Exception {
		BeanForSearchRecord beanForSearchRecord = new BeanForSearchRecord();
		SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);
		Date date = null;
		
		// ��ַ�б� 
		beanForSearchRecord.setPlaceList(places);
		// ��������
		beanForSearchRecord.setOrderByType(orderByType);
		if (selectType == MainService.TYPE_RECENT) {// ��ʱ����
			//����������������:��ʱ����
			beanForSearchRecord.setStattype(0);			
			// ��ʼʱ�� eg:2009-09-01 18:10:02
			date = dateFormat.parse(startTime);
			beanForSearchRecord.setAlarmStartFrom(date);
			// ����ʱ��
			date = dateFormat.parse(endTime);
			beanForSearchRecord.setAlarmStartTo(date);
		}else if (selectType == MainService.TYPE_DAILY) {// �ձ�
			//����������������:�ձ�
			beanForSearchRecord.setStattype(1);
			// ��ʼʱ�� eg:2009-08-31
			date = dateFormat.parse(startTime + " 0:00:00");
			beanForSearchRecord.setAlarmStartFrom(date);
			// ����ʱ��
			date = dateFormat.parse(endTime +  " 23:59:59");
			beanForSearchRecord.setAlarmStartTo(date);
		} else if (selectType == MainService.TYPE_MONTH) {// �±�
			//����������������:�±�
			beanForSearchRecord.setStattype(2);			
			// ��ʼʱ�� eg:2009-05-dd
			date = dateFormat.parse(startTime + " 0:00:00");
			beanForSearchRecord.setAlarmStartFrom(date);
			// ����ʱ��--������ʱ����¼�һ
			date = dateFormat.parse(endTime + " 0:00:00");
			date = FunctionUnit.nextTime(date, 1, FunctionUnit.Calendar_END_MONTH).getTime();
			date = FunctionUnit.nextTime(date, -1, FunctionUnit.Calendar_END_SECOND).getTime();
			beanForSearchRecord.setAlarmStartTo(date);
		}

		return beanForSearchRecord;
	}
	
	public String hisZj(){
		areaToEqList = commonDataUnit.createEqAreaWithNOSel(null);
		return SUCCESS;
	}
	
	public String hisZjRec(){
		BeanForZjHisRec zjBean;
		try {
			zjBean = fillBean4ZjHisRec();
			List<ZjHistory> zjHis = serviceSqlServer.selectZjHisRec(zjBean);
			if (zjHis != null && zjHis.size() > 0){
				doHisZjRecHtml(zjHis);// �����ݰ�װ��ҳ��Ԫ�� -- ��ʾ��html��񼯺�	
				return PRINTOUT;
			}			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		nodataInfo();
		return NODATARETURN;
	}
	
	/**
	 * @describe: ��ҩ���������ʾ��html��񼯺�	
	 * @date:2010-4-15
	 */
	public void doHisZjRecHtml(List<ZjHistory> zjHis) throws ParseException{
		StringBuffer strbuf = new StringBuffer();
		int tempInt = 1;
		// ��������-ϸ��
		headTitle = commonDataUnit.getSysArgsByKey(BeanForSysArgs.COMPANY_NAME) + " - ҩ����ʷ���ݲ�ѯ";// ��ʽ:��˾�� - ��ѯ�� 
		SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);
		headDetail = "ʱ�䷶Χ:" + getDateStringByStat(dateFormat.parse(timeFrom), MainService.TYPE_RECENT) +
					"~" + getDateStringByStat(dateFormat.parse(timeTo), MainService.TYPE_RECENT);	// ����ϸ��
		
		//strJspTableHeader;// ������
		
		strbuf.append("<td>NO</td>");
		strbuf.append("<td>��¼ʱ��</td>");
		strbuf.append("<td>��������</td>");
		strbuf.append("<td>ҩ��ֱ��</td>");
		strbuf.append("<td>�¶�ֵ(��)</td>");
		strbuf.append("<td>ʪ��ֵ(%RH)</td>");
		//strbuf.append("<td>BSend</td>");//δ֪����
		strJspTableHeader = strbuf.toString();
		
		jsptableList = new ArrayList<String>();
		for (ZjHistory zjBean : zjHis) {
			strbuf.delete(0, strbuf.length());
			strbuf.append("<tr>");
			strbuf.append("<td>" + tempInt + "</td>");
			strbuf.append("<td>" + FunctionUnit.getDateToStr(zjBean.getCheckTime(), FunctionUnit.Calendar_END_SECOND, FunctionUnit.SHOW_CHINESE) + "</td>");
			strbuf.append("<td>" + zjBean.getMonitorName() + "</td>");
			strbuf.append("<td>" + zjBean.getLicenseNo() + "</td>");
			strbuf.append("<td>" + zjBean.getTemperature() + "</td>");
			strbuf.append("<td>" + zjBean.getHumidity() + "</td>");
			//strbuf.append("<td>" + zjBean.getBSend() + "</td>");
			strbuf.append("</tr>");
			jsptableList.add(strbuf.toString()); // �������
			tempInt++;
		}
	}
	
	private void nodataInfo() {
		StringBuffer strbuf = new StringBuffer();
		headTitle = "��������ҩ����ʷ����...";
		strbuf.append("����:");
		strbuf.append("<br/>");
		strbuf.append("<label>");
		strbuf.append("    �������ʼʱ��ͽ���ʱ��");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<label>");
		strbuf.append("    ��ѡ���ʵ�������");
		strbuf.append("</label>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("*���������,��Ȼû�в�ѯ���,��ô���ʱ����ȷʵû�����ݼ�¼����...");		
		headDetail = strbuf.toString();
	}	
	
	private BeanForZjHisRec fillBean4ZjHisRec() throws ParseException{
		BeanForZjHisRec zjBean = new BeanForZjHisRec();
		SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);
		
		zjBean.setPlaceList(placeListids);					// �������������б������Ÿ�����
		zjBean.setTimeFrom(dateFormat.parse(timeFrom));		// ��ʼʱ��
		zjBean.setTimeTo(dateFormat.parse(timeTo));			// ����ʱ��
		zjBean.setLowTemp(Double.parseDouble(tempDown));	// �¶�����
		zjBean.setHighTemp(Double.parseDouble(tempUp));		// �¶�����
		zjBean.setLowHumi(Double.parseDouble(humiDown));	// �¶�����
		zjBean.setHighHumi(Double.parseDouble(humiUp));		// ʪ������
		
		return zjBean;
	}

	// ----------------����Ϊget��set����----------------
	/**
	 * @describe : ��ȡȫ��������ʶ��.Map(equipmentId : �ص� + ��� + ������ַ)
	 * @date:2009-11-3
	 */
	public Map<Integer, String> getPlaceList() {
		//return CommonDataUnit.getEquiMapStr();
		return commonDataUnit.getEquiMapStr();
	}
	
	//�������
	public Map<Integer, String> getSearchInterval() {
		searchInterval = getMapSel("searchInterval");
		return searchInterval;
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
		//return CommonDataUnit.getSystemEqType();
		return commonDataUnit.getSystemEqType();
	}
	// ----------------��ͨ get��set����----------------
	public Map<String, String> getAreaToEqList() {
		return areaToEqList;
	}
	
	public String getPlaceListids() {
		return placeListids;
	}

	public void setPlaceListids(String placeListids) {
		this.placeListids = placeListids;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getShowType() {
		return showType;
	}
	
	public void setShowType(int showType) {
		this.showType = showType;
	}

	public int getMaxSelector() {
		return maxSelector;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public int getMax_rs_now() {
		return max_rs_now;
	}

	public int getMax_rs_day() {
		return max_rs_day;
	}

	public int getMax_rs_month() {
		return max_rs_month;
	}

	public String getStrJspTable() {// ����
		return strJspTable;
	}
	public List<String> getJsptableList() {// ʹ��
		return jsptableList;
	}

	public String getStrJspTableHeader() {
		return strJspTableHeader;
	}

	public int[] getStatBox() {
		return statBox;
	}

	public void setStatBox(int[] statBox) {
		this.statBox = statBox;
	}

	public String getTempDataStr() {
		return tempDataStr;
	}

	public String getTempConfigStr() {
		return tempConfigStr;
	}

	public String getHumiDataStr() {
		return humiDataStr;
	}

	public String getHumiConfigStr() {
		return humiConfigStr;
	}

	public String getHeadTitle() {
		return headTitle;
	}

	public String getHeadDetail() {
		return headDetail;
	}
	public String getTempDown() {
		return tempDown;
	}
	public void setTempDown(String tempDown) {
		this.tempDown = tempDown;
	}
	public String getTempUp() {
		return tempUp;
	}
	public void setTempUp(String tempUp) {
		this.tempUp = tempUp;
	}
	public String getHumiDown() {
		return humiDown;
	}
	public void setHumiDown(String humiDown) {
		this.humiDown = humiDown;
	}
	public String getHumiUp() {
		return humiUp;
	}
	public void setHumiUp(String humiUp) {
		this.humiUp = humiUp;
	}
	
}
