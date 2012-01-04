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
 * @ UserAction.java 作用 : 导出excel模块action. 注意事项 : 无 VERSION DATE BY
 * CHANGE/COMMENT 1.0 2009-11-4 YANGZHONLI create
 */
public class exportxlsAction extends AbstractAction {

	private static final long serialVersionUID = -6116136871830584200L;
	
	// 导出历史备份xls用 或者 导出系统配置
	private InputStream excelStream; 		// 需要生成getter和setter
	private String  filename;				// 从页面传人的文件名
	private String  contentDisposition;		// 导出的文件名
	
	// 导入系统配置
	@SuppressWarnings("unused")
	private static final int BUFFER_SIZE = 16 * 1024;
	private File configFile;
	private String configFileContentType;
	private String configFileFileName;	
	
	// table导出xls用
	private String tablecontent;			// 页面table数据导出excel时的临时存放区域
	
	// 下载错误信息
	private String headTitle;				// 标题
	private String headDetail;				// 细节	
	private int showTipMsg=0;				// 是否显示错误标记(0:不显示 1:显示)
	
	private Level_First_Serial first_Level;
	private SetSysService setSysService;
	private ManaService manaService;
	private SimCard_Unit simCard_Unit;	
	
	//注册service -- spring ioc
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
	
	// 构造方法
	public exportxlsAction() {
	}

	@Override
	public String execute() {
		return SUCCESS;
	}

	public String testExport() {
		// 直接导出excel--功能测试代码
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
	
	// 导出历史备份excel
	public String exportFile(){
		boolean fileExist = true;	// 判断文件是否存在
		if (fileExist){ // 导出文件
			try {
				excelStream = new FileInputStream(filename);
				filename = filename.substring(filename.lastIndexOf("/")+1);
				contentDisposition = "filename=\"" + new String(filename.getBytes(), "ISO8859-1") + "\"";
			} catch (Exception e) {
				nodataInfo(1,3);
				return "noDataReturn";				
			}
			return SUCCESS;
		}else{			// 下载错误页面
			nodataInfo(1,3);
			return "noDataReturn";
		}
		
	}
	
	// 导出配置文件
	public String exportSysConfig(){
		boolean eqExist = eqCount() != 0;	// 判断仪器列表是否不为空
		if (eqExist){ // 导出文件
			try {
				// 文件格式:公司名-2010年2月1日-配置文件.bin
				//filename = CommonDataUnit.getSysArgsByKey(BeanForSysArgs.COMPANY_NAME) + "-" + 
				filename = commonDataUnit.getSysArgsByKey(BeanForSysArgs.COMPANY_NAME) + "-" + 
					FunctionUnit.getDateToStr(new Date(), FunctionUnit.Calendar_END_DAY, FunctionUnit.SHOW_CHINESE) +
					"-配置文件.bin";
				contentDisposition = "filename=\"" + new String(filename.getBytes(), "ISO8859-1") + "\"";
				excelStream = HtcFiles.writeBuffer(findStoreArgs());
				if (excelStream == null){nodataInfo(3, 1);		return "noDataReturn";}
			} catch (Exception e) {
				nodataInfo(3, 1);
				return "noDataReturn";				
			}
			return SUCCESS;
		}else{			// 下载错误页面
			nodataInfo(2, 1);
			return "noDataReturn";
		}
	}
	
	//  ①导入配置系统配置信息->②关闭串口->③解析数据并导入数据库->④重启串口
	public String configUpload(){
		// ①导入配置系统配置信息
		BeanForEnDeJson beanJson = HtcFiles.deCryptFile(configFile);
		if (beanJson != null){// 读取文件,解密正确
			boolean bool = false;
			// ②关闭串口
			//Level_First_Serial.getInstance().endRunSerial();
			if (first_Level.isRunningFlag()){first_Level.endRunSerial();} 	// 停止数据采集串口
			if (simCard_Unit.isRunFlag()){simCard_Unit.closePort();}		// 停止短信模块
			
			// ③解析数据并导入数据库
			// ③-1导入区域信息(③-1一定要在③-2前面)
			bool = beanJson.getWorkplaceList().size() == beanJson.getWorkplaceCount();
			if (bool){bool = manaService.insertWorkplaceBatch(beanJson.getWorkplaceList());}
			
			// ③-2导入仪器信息
			if (bool){bool = beanJson.getEquipDataList().size() == beanJson.getEquipCount();}
			if (bool){bool = manaService.insertEquipDataBatch(beanJson.getEquipDataList());}
			
			// ③-3导入系统信息
			if (bool){bool = beanJson.getSysParamList().size() == beanJson.getSystemCount();}
			if (bool){bool = setSysService.updateSysParam(beanJson.getSysParamList());}
			
			// ③-4导入短信发放格式信息
			if (bool){bool = beanJson.getGprsSetList().size() == beanJson.getGprsCount();}
			if (bool){bool = setSysService.insertGprsSetBatch(beanJson.getGprsSetList());}
			
			// 显示配置错误信息
			if (!bool){
				nodataInfo(6,2);// 导入配置成功. 但数据通讯连接出错
				return "noDataReturn";
			}else{
				// 重置系统相关信息
				//CommonDataUnit.resetSystem(true,true,true,true,true);	
				commonDataUnit.resetSystem(true,true,true,true,true);	
				// 重置总览和实时曲线涉及的数据库--先删除所用数据,然后填充空数据
			}
			
			// 测试代码 
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
//				nodataInfo(6,2);// 导入配置成功. 但数据通讯连接出错
//			}			
			
			// ④重启串口
			//int rsint = Level_First_Serial.getInstance().startRunSerial();
			int rsint = first_Level.startRunSerial();
			// ⑤重启短信模块
			if (Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_SHORT_MESSAGE))==2){
				String passport = "";
				if (first_Level.isRunningFlag() == true){
					passport = first_Level.getPortStr();
				}			
				simCard_Unit.openPort(passport, 9600);				
			}
			if (rsint == 1){
				nodataInfo(2,2);// 没有仪器添加
				return "noDataReturn";
			}else if (rsint == 2){
				nodataInfo(5,4);// 数据库链接出错
				return "noDataReturn";
			}else {// rsint == 0
				showTipMsg = 1;
				addFieldError("showsucc", "导入配置成功");
				return SUCCESS;
			}
			
		}else{ // 读取文件,解密错误
			nodataInfo(4,2);
			return "noDataReturn";
		}
	}
	
	/**
	 * @describe: 获取仪器个数	
	 * @date:2010-1-29
	 */
	public int eqCount(){
		int rsint = commonDataUnit.equiList.size();
		return rsint;
	} 
	
	// 把系统需要到场的信息传入Bean,以便转换成数据流
	public BeanForEnDeJson findStoreArgs(){
		BeanForEnDeJson beanJson = new BeanForEnDeJson();
		// 区域信息	- Workplace
		List<Workplace> worklist = manaService.getAllWorkplace(0);
		beanJson.setWorkplaceList(worklist);
		beanJson.setWorkplaceCount(worklist.size());
		// 仪器信息 - EquipData
		BeanForEqOrder eqorderBean = new BeanForEqOrder();
		eqorderBean.setEqorderType(BeanForEqOrder.SELECT_WITH_NOTHING);
		eqorderBean.setUseless(0);
		List<EquipData> equiplist = manaService.selectEquiOrderStr(eqorderBean);
		beanJson.setEquipDataList(equiplist);
		beanJson.setEquipCount(equiplist.size());
		// 系统信息 - SysParam
		List<SysParam> syslist = setSysService.selectAllParam();
		beanJson.setSysParamList(syslist);
		beanJson.setSystemCount(syslist.size());
		// 短信信息 - GprsSet
		List<GprsSet> gprslist = setSysService.getAllSetLists();
		beanJson.setGprsSetList(gprslist);
		beanJson.setGprsCount(gprslist.size());
		
		return beanJson;
	}
	
	/**
	 * @describe: 没有数据时显示的信息
	 * @param type: 
	 * 		1:下载备份文件用 -- 找不到要下载的文件<br>
	 * 		2:下载配置文件用,仪器列表为空,无法配置	<br>	
	 * 		3:文件加密出错<br>	
	 * 		4:文件读取出错,请选择正确的配置文件<br>
	 * 		5:导入配置成功. 但数据通讯连接出错<br>
	 * 		6:数据库链接出错<br>
	 * @param inOrOut: 1:导出 2:导入	3:下载 4:导入成功,但是连接出错<br>
	 * @date:2010-1-29
	 */
	private void nodataInfo(int type, int inOrOut) {
		StringBuffer strbuf = new StringBuffer();
		headTitle = inOrOut==1?"无法导出配置信息...":inOrOut==2?	"无法导入配置信息...": 
			inOrOut==3?"无法下载该文件...":"配置导入成功. 但数据通讯出错...";
		strbuf.append("提示:");
		strbuf.append("<br/>");
		strbuf.append("<label>");
		if (type ==1){
			strbuf.append("    ●找不到要下载的文件,可能已经被删除...");
		}else if (type == 2){
			strbuf.append("    ●仪器列表为空,无法配置,请先添加仪器...");
		}else if (type == 3){
			strbuf.append("    ●文件加密出错...");
		}else if (type == 4){
			strbuf.append("    ●文件读取出错, 配置文件被破坏, 请选择正确的配置文件...");
		}else if (type == 5){
			strbuf.append("    请检查仪器是否连成正确. 在确保连接正确的前提下,做以下尝试:");
			strbuf.append("</label>");
			strbuf.append("<br/>");
			
			strbuf.append("<label>");
			strbuf.append("     ①再导入一次配置");
			strbuf.append("</label>");
			strbuf.append("<br/>");			
			strbuf.append("<label>");
			strbuf.append("     ②重启电脑后,再导入配置");
			strbuf.append("</label>");
			strbuf.append("<br/>");			
			strbuf.append("<label>");
			strbuf.append("     ③前面两种方法都无法解决,请及时联系厂家");
		}else if (type == 6){
			strbuf.append("    ●数据库链接出错");
		} 
		
		strbuf.append("</label>");
		headDetail = strbuf.toString();
	}
	
	// 导出页面excel,主要是table转excel
	public String exportExcel(){
		// 这里主要是接受页面的传递过来的tablecontent参数, 以便在之后导出的页面中使用
		//System.out.println(tablecontent);
		return SUCCESS;
	}

	// ---------------- get，set方法----------------//
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
