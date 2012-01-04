package com.htc.action;

import java.io.*;
import java.util.Date;
import java.util.List;

import com.htc.bean.BeanForEnDeJson;
import com.htc.bean.BeanForEqOrder;
import com.htc.bean.BeanForSysArgs;
import com.htc.common.*;
import com.htc.domain.*;
import com.htc.model.ManaService;
import com.htc.model.SetSysService;
import com.htc.model.seriaPort.Level_First_Serial;
import com.htc.model.seriaPort.SimCard_Unit;

/**
 * @ UserAction.java ���� : ����excelģ��action. ע������ : �� VERSION DATE BY
 * CHANGE/COMMENT 1.0 2009-11-4 YANGZHONLI create
 */
public class exportxlsAction extends AbstractAction {

	private static final long serialVersionUID = -6116136871830584200L;
	
	// ������ʷ����xls�� ���� ����ϵͳ����
	private InputStream excelStream; 		// ��Ҫ����getter��setter
	private String  filename;				// ��ҳ�洫�˵��ļ���
	private String  contentDisposition;		// �������ļ���
	
	// ����ϵͳ����
	@SuppressWarnings("unused")
	private static final int BUFFER_SIZE = 16 * 1024;
	private File configFile;
	private String configFileContentType;
	private String configFileFileName;	
	
	// table����xls��
	private String tablecontent;			// ҳ��table���ݵ���excelʱ����ʱ�������
	
	// ���ش�����Ϣ
	private String headTitle;				// ����
	private String headDetail;				// ϸ��	
	private int showTipMsg=0;				// �Ƿ���ʾ������(0:����ʾ 1:��ʾ)
	
	private Level_First_Serial first_Level;
	private SetSysService setSysService;
	private ManaService manaService;
	private SimCard_Unit simCard_Unit;	
	
	//ע��service -- spring ioc
	public void setFirst_Level(Level_First_Serial first_Level) {
		this.first_Level = first_Level;
	}
	public void setSetSysService(SetSysService setSysService) {
		this.setSysService = setSysService;
	}
	public void setManaService(ManaService manaService) {
		this.manaService = manaService;
	}
	public void setSimCard_Unit(SimCard_Unit simCard_Unit) {
		this.simCard_Unit = simCard_Unit;
	}		
	
	// ���췽��
	public exportxlsAction() {
	}

	@Override
	public String execute() {
		return SUCCESS;
	}

	public String testExport() {
		// ֱ�ӵ���excel--���ܲ��Դ���
		contentDisposition = "filename=\"standard111111.xls\"";
		StringBuffer excelBuf = new StringBuffer();
		excelBuf.append("BookName").append("\t").
		append("Year").append("\t").append("author").append("\n");
		
		excelBuf.append("Thinking in Java").append("\t").append("2001").
		append("\t").append("Eckel").append("\n");
		
		excelBuf.append("Spring in action").append("\t").append("2005").
		append("\t").append("Rod").append("\n");
		
		excelBuf.append("Spring in action").append("\t").append("2009").
		append("\t").append("Rod").append("\n");
		
		String excelString = excelBuf.toString();
		excelStream = new ByteArrayInputStream(excelString.getBytes(),0,excelString.length());
		//excelStream = new FileInputStream("fileName");
		return "excel";
	}
	
	// ������ʷ����excel
	public String exportFile(){
		boolean fileExist = true;	// �ж��ļ��Ƿ����
		if (fileExist){ // �����ļ�
			try {
				excelStream = new FileInputStream(filename);
				filename = filename.substring(filename.lastIndexOf("/")+1);
				contentDisposition = "filename=\"" + new String(filename.getBytes(), "ISO8859-1") + "\"";
			} catch (Exception e) {
				nodataInfo(1,3);
				return "noDataReturn";				
			}
			return SUCCESS;
		}else{			// ���ش���ҳ��
			nodataInfo(1,3);
			return "noDataReturn";
		}
		
	}
	
	// ���������ļ�
	public String exportSysConfig(){
		boolean eqExist = eqCount() != 0;	// �ж������б��Ƿ�Ϊ��
		if (eqExist){ // �����ļ�
			try {
				// �ļ���ʽ:��˾��-2010��2��1��-�����ļ�.bin
				//filename = CommonDataUnit.getSysArgsByKey(BeanForSysArgs.COMPANY_NAME) + "-" + 
				filename = commonDataUnit.getSysArgsByKey(BeanForSysArgs.COMPANY_NAME) + "-" + 
					FunctionUnit.getDateToStr(new Date(), FunctionUnit.Calendar_END_DAY, FunctionUnit.SHOW_CHINESE) +
					"-�����ļ�.bin";
				contentDisposition = "filename=\"" + new String(filename.getBytes(), "ISO8859-1") + "\"";
				excelStream = HtcFiles.writeBuffer(findStoreArgs());
				if (excelStream == null){nodataInfo(3, 1);		return "noDataReturn";}
			} catch (Exception e) {
				nodataInfo(3, 1);
				return "noDataReturn";				
			}
			return SUCCESS;
		}else{			// ���ش���ҳ��
			nodataInfo(2, 1);
			return "noDataReturn";
		}
	}
	
	//  �ٵ�������ϵͳ������Ϣ->�ڹرմ���->�۽������ݲ��������ݿ�->����������
	public String configUpload(){
		// �ٵ�������ϵͳ������Ϣ
		BeanForEnDeJson beanJson = HtcFiles.deCryptFile(configFile);
		if (beanJson != null){// ��ȡ�ļ�,������ȷ
			boolean bool = false;
			// �ڹرմ���
			//Level_First_Serial.getInstance().endRunSerial();
			if (first_Level.isRunningFlag()){first_Level.endRunSerial();} 	// ֹͣ���ݲɼ�����
			if (simCard_Unit.isRunFlag()){simCard_Unit.closePort();}		// ֹͣ����ģ��
			
			// �۽������ݲ��������ݿ�
			// ��-1����������Ϣ(��-1һ��Ҫ�ڢ�-2ǰ��)
			bool = beanJson.getWorkplaceList().size() == beanJson.getWorkplaceCount();
			if (bool){bool = manaService.insertWorkplaceBatch(beanJson.getWorkplaceList());}
			
			// ��-2����������Ϣ
			if (bool){bool = beanJson.getEquipDataList().size() == beanJson.getEquipCount();}
			if (bool){bool = manaService.insertEquipDataBatch(beanJson.getEquipDataList());}
			
			// ��-3����ϵͳ��Ϣ
			if (bool){bool = beanJson.getSysParamList().size() == beanJson.getSystemCount();}
			if (bool){bool = setSysService.updateSysParam(beanJson.getSysParamList());}
			
			// ��-4������ŷ��Ÿ�ʽ��Ϣ
			if (bool){bool = beanJson.getGprsSetList().size() == beanJson.getGprsCount();}
			if (bool){bool = setSysService.insertGprsSetBatch(beanJson.getGprsSetList());}
			
			// ��ʾ���ô�����Ϣ
			if (!bool){
				nodataInfo(6,2);// �������óɹ�. ������ͨѶ���ӳ���
				return "noDataReturn";
			}else{
				// ����ϵͳ�����Ϣ
				//CommonDataUnit.resetSystem(true,true,true,true,true);	
				commonDataUnit.resetSystem(true,true,true,true,true);	
				// ����������ʵʱ�����漰�����ݿ�--��ɾ����������,Ȼ����������
			}
			
			// ���Դ��� 
//			for (Workplace work : beanJson.getWorkplaceList()) {
//				System.out.println("Workplace:"+work.getPlaceId()+":"+work.getPlaceName()+":"+work.getUseless());
//			}
//			for (EquipData eqdata : beanJson.getEquipDataList()) {
//				System.out.println("EquipData:"+eqdata.getAddress()+":"+eqdata.getEquipmentId()+":"+eqdata.getTempDown());
//			} 
//			for (SysParam sys : beanJson.getSysParamList()) {
//				System.out.println("SysParam:"+sys.getArgsKey()+":"+sys.getArgsValue());
//			}
//			for (GprsSet gprs : beanJson.getGprsSetList()) {
//				System.out.println("GprsSet:" + gprs.getAlias() + ":" + gprs.getNumId());
//			}
//			if (!bool){
//				nodataInfo(6,2);// �������óɹ�. ������ͨѶ���ӳ���
//			}			
			
			// ����������
			//int rsint = Level_First_Serial.getInstance().startRunSerial();
			int rsint = first_Level.startRunSerial();
			// ����������ģ��
			if (Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_SHORT_MESSAGE))==2){
				String passport = "";
				if (first_Level.isRunningFlag() == true){
					passport = first_Level.getPortStr();
				}			
				simCard_Unit.openPort(passport, 9600);				
			}
			if (rsint == 1){
				nodataInfo(2,2);// û���������
				return "noDataReturn";
			}else if (rsint == 2){
				nodataInfo(5,4);// ���ݿ����ӳ���
				return "noDataReturn";
			}else {// rsint == 0
				showTipMsg = 1;
				addFieldError("showsucc", "�������óɹ�");
				return SUCCESS;
			}
			
		}else{ // ��ȡ�ļ�,���ܴ���
			nodataInfo(4,2);
			return "noDataReturn";
		}
	}
	
	/**
	 * @describe: ��ȡ��������	
	 * @date:2010-1-29
	 */
	public int eqCount(){
		int rsint = commonDataUnit.equiList.size();
		return rsint;
	} 
	
	// ��ϵͳ��Ҫ��������Ϣ����Bean,�Ա�ת����������
	public BeanForEnDeJson findStoreArgs(){
		BeanForEnDeJson beanJson = new BeanForEnDeJson();
		// ������Ϣ	- Workplace
		List<Workplace> worklist = manaService.getAllWorkplace(0);
		beanJson.setWorkplaceList(worklist);
		beanJson.setWorkplaceCount(worklist.size());
		// ������Ϣ - EquipData
		BeanForEqOrder eqorderBean = new BeanForEqOrder();
		eqorderBean.setEqorderType(BeanForEqOrder.SELECT_WITH_NOTHING);
		eqorderBean.setUseless(0);
		List<EquipData> equiplist = manaService.selectEquiOrderStr(eqorderBean);
		beanJson.setEquipDataList(equiplist);
		beanJson.setEquipCount(equiplist.size());
		// ϵͳ��Ϣ - SysParam
		List<SysParam> syslist = setSysService.selectAllParam();
		beanJson.setSysParamList(syslist);
		beanJson.setSystemCount(syslist.size());
		// ������Ϣ - GprsSet
		List<GprsSet> gprslist = setSysService.getAllSetLists();
		beanJson.setGprsSetList(gprslist);
		beanJson.setGprsCount(gprslist.size());
		
		return beanJson;
	}
	
	/**
	 * @describe: û������ʱ��ʾ����Ϣ
	 * @param type: 
	 * 		1:���ر����ļ��� -- �Ҳ���Ҫ���ص��ļ�<br>
	 * 		2:���������ļ���,�����б�Ϊ��,�޷�����	<br>	
	 * 		3:�ļ����ܳ���<br>	
	 * 		4:�ļ���ȡ����,��ѡ����ȷ�������ļ�<br>
	 * 		5:�������óɹ�. ������ͨѶ���ӳ���<br>
	 * 		6:���ݿ����ӳ���<br>
	 * @param inOrOut: 1:���� 2:����	3:���� 4:����ɹ�,�������ӳ���<br>
	 * @date:2010-1-29
	 */
	private void nodataInfo(int type, int inOrOut) {
		StringBuffer strbuf = new StringBuffer();
		headTitle = inOrOut==1?"�޷�����������Ϣ...":inOrOut==2?	"�޷�����������Ϣ...": 
			inOrOut==3?"�޷����ظ��ļ�...":"���õ���ɹ�. ������ͨѶ����...";
		strbuf.append("��ʾ:");
		strbuf.append("<br/>");
		strbuf.append("<label>");
		if (type ==1){
			strbuf.append("    ���Ҳ���Ҫ���ص��ļ�,�����Ѿ���ɾ��...");
		}else if (type == 2){
			strbuf.append("    �������б�Ϊ��,�޷�����,�����������...");
		}else if (type == 3){
			strbuf.append("    ���ļ����ܳ���...");
		}else if (type == 4){
			strbuf.append("    ���ļ���ȡ����, �����ļ����ƻ�, ��ѡ����ȷ�������ļ�...");
		}else if (type == 5){
			strbuf.append("    ���������Ƿ�������ȷ. ��ȷ��������ȷ��ǰ����,�����³���:");
			strbuf.append("</label>");
			strbuf.append("<br/>");
			
			strbuf.append("<label>");
			strbuf.append("     ���ٵ���һ������");
			strbuf.append("</label>");
			strbuf.append("<br/>");			
			strbuf.append("<label>");
			strbuf.append("     ���������Ժ�,�ٵ�������");
			strbuf.append("</label>");
			strbuf.append("<br/>");			
			strbuf.append("<label>");
			strbuf.append("     ��ǰ�����ַ������޷����,�뼰ʱ��ϵ����");
		}else if (type == 6){
			strbuf.append("    �����ݿ����ӳ���");
		} 
		
		strbuf.append("</label>");
		headDetail = strbuf.toString();
	}
	
	// ����ҳ��excel,��Ҫ��tableתexcel
	public String exportExcel(){
		// ������Ҫ�ǽ���ҳ��Ĵ��ݹ�����tablecontent����, �Ա���֮�󵼳���ҳ����ʹ��
		//System.out.println(tablecontent);
		return SUCCESS;
	}

	// ---------------- get��set����----------------//
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getTablecontent() {
		return tablecontent;
	}

	public void setTablecontent(String tablecontent) {
		this.tablecontent = tablecontent;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getHeadTitle() {
		return headTitle;
	}

	public String getHeadDetail() {
		return headDetail;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}
	
	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {
		this.configFile = configFile;
	}

	public String getConfigFileContentType() {
		return configFileContentType;
	}

	public void setConfigFileContentType(String configFileContentType) {
		this.configFileContentType = configFileContentType;
	}

	public String getConfigFileFileName() {
		return configFileFileName;
	}

	public void setConfigFileFileName(String configFileFileName) {
		this.configFileFileName = configFileFileName;
	}

	public int getShowTipMsg() {
		return showTipMsg;
	}
	
}
