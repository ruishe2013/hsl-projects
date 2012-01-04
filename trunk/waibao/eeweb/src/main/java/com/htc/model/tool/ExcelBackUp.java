package com.htc.model.tool;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.bean.BeanForJxls;
import com.htc.bean.BeanForLabel;
import com.htc.bean.BeanForRecord;
import com.htc.bean.BeanForSearchRecord;
import com.htc.common.FunctionUnit;
import com.htc.common.HtcFiles;
import com.htc.domain.*;
import com.htc.model.MainService;
import com.htc.model.SetSysService;

/**
 * @ CSVBackUp.java
 * 作用 : 把每个月的数据备份成csv格式保存
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
public class ExcelBackUp {

	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(ExcelBackUp.class);
	private static int BATCHSIZE = 10; // 一个excel最多容纳的sheet数量
	
	/**
	 * @describe: 获取xls模板路径
	 * @return 成功:返回xls模板路径	失败:返回null
	 * @date:2010-1-20
	 */
	@SuppressWarnings("unused")
//	private static String getTemplateFileName() throws IOException{
//		// 模板xls文件路径
//        String templateFileName = ExcelBackUp.class.getResource("/").getPath();
//        templateFileName = templateFileName.substring(1,templateFileName.indexOf("/WEB-INF")+1)+"WEB-INF/templates/record.xls";
//        boolean tempFileExist = HtcFiles.CheckFile(templateFileName); // 检查模板文件是否存在
//        if (!tempFileExist){
//        	templateFileName = null;
//        	MainService mainService = new MainService(); 
//        	mainService.packTlog(TLog.ERROR_LOG, "找不到模板excel,不能备份历史列表...");
//        }
//		return templateFileName;
//	}
	
	
	/**
	 * @describe: 获取备份xls保存的路径的一部分.并检查文件夹是否存在,没有就创建<br>
	 * 【格式:保存路径+y年/m月/d日/+(区域名)+(N/M).xls.】<br>
	 * 【如:c:/htc/backup/2010年/1月/20日/2010年1月20日(测试区)(2/5).xls】<br>
	 * @param perPath: 保存的文件名中的路径
	 * @param mainService: 写日志需要的服务
	 * @return 成功:返回备份xls路径的一部分	失败:返回null<br>
	 * @date:2010-1-20
	 */
	private static String getJxlsFileNamePer(Date date, String perPath, MainService mainService) throws Exception{
		//String destFileName_header = CommonDataUnit.getSysArgsByKey(BeanForSysArgs.BACKUP_PATH) +
        String destFileName_header = perPath + "/" +  FunctionUnit.getTypeTime(date);
        
        // 建或者检查文件夹
        if (!HtcFiles.createOrCheckFolder(destFileName_header)){
        	destFileName_header = null;
        	mainService.packTlog(TLog.ERROR_LOG, "获取备份列表路径出错");
        }; 
        return destFileName_header;
	}
	
	/**
	 * @describe: 根据遍历仪器ID,获取数据,并保存在excel中,文件名格式(2010年02月19日(测试区)(1_5).xls)
	 * 格式:保存路径+y年/m月/d日/+(区域名)+(N/M).xls.】
	 * @param startTime: 开始收索的时间
	 * @param endTime ：结束收索的时间
	 * @param perPath: 保存的文件名中的路径
	 * @param area2equi: 获取全部对应[区域:仪器]
	 * @param tempShow:  温度显示类型(温度显示格式(1:摄氏 2:华氏))
	 * @param equiMap:  所有仪器的信息
	 * @param normalCol:  正常的值
	 * @param highCol:  高颜色的值
	 * @param lowCol:  底颜色的值 
	 * @param setSysService:  把记录写到数据库中时,需要的服务
	 * @param setSysService:  把记录写到数据库中时,需要的服务
	 * @date:2010-1-17
	 */
	public static void storeToXls(Date startTime,Date endTime, String perPath,
			Map<String, Map<Integer, BeanForLabel>> area2equi,
			int tempShow, Map<Integer, EquipData> equiMap,
			String normalCol,String highCol, String lowCol,
			SetSysService setSysService, MainService mainService) throws Exception {
		Date tempDate = null;
		do {
			// 下一天
			tempDate = FunctionUnit.nextTime(startTime, 1, FunctionUnit.Calendar_END_DAY).getTime();
			tempDate = tempDate.after(endTime)? endTime : tempDate;
			// 减少一秒钟,因为时间段要在[0:0:0到23:59:59]之间
			store2Xls(startTime, FunctionUnit.nextTime(tempDate, -1, FunctionUnit.Calendar_END_SECOND).getTime(),
					perPath ,area2equi, tempShow, equiMap, normalCol, highCol, lowCol, setSysService, mainService);
			startTime= tempDate;
		} while (startTime.before(endTime));
	}

	/**
	 * @describe: 根据遍历仪器ID,获取数据,并保存在excel中,文件名格式(2010年02月19日(测试区)(1_5).xls)
	 * @param startTime: 开始收索的时间
	 * @param endTime ：结束收索的时间
	 * @param perPath: 保存的文件名中的路径
	 * @param area2equi: 获取全部对应[区域:仪器]
	 * @param tempShow:  温度显示类型(温度显示格式(1:摄氏 2:华氏))
	 * @param equiMap:  所有仪器的信息
	 * @param normalCol:  正常的值
	 * @param highCol:  高颜色的值
	 * @param lowCol:  底颜色的值 
	 * @param setSysService:  把记录写到数据库中时,需要的服务
	 * @param mainService:  写日志时,需要的服务
	 * @date:2010-1-17
	 */
	private static void store2Xls(Date startTime,Date endTime, String perPath,
			Map<String, Map<Integer, BeanForLabel>> area2equi,
			int tempShow, Map<Integer, EquipData> equiMap,
			String normalCol,String highCol, String lowCol,
			SetSysService setSysService, MainService mainService) throws Exception {
		// 模板xls文件路径 -- jxl中不需要
        //String templateFileName = getTemplateFileName();
        //if (templateFileName == null){return;} // 模板不存在,退出
        
		// 产生xls备份的文件路径的一部分
        String destFileName_header = getJxlsFileNamePer(startTime, perPath, mainService);
        if (destFileName_header == null){return;}
        String currentTime = FunctionUnit.getDateToStr(startTime, 
        		FunctionUnit.Calendar_END_DAY, FunctionUnit.SHOW_CHINESE);//时间
        destFileName_header = destFileName_header + "/" + currentTime;
        
        String destFileName = "";														// xls备份的文件路径-临时变量
        //Map<String, Map<Integer, BeanForLabel>> area2equi = CommonDataUnit.area2Equipment();	// 获取全部对应[区域:仪器]
        Map<Integer, BeanForLabel> placeMaps = null;								        	// 获取map(eqid,仪器标签)
        String areaStr = "";															// 区域名
        String place = "";																// 仪器主键,查询Sql用
        List<String> label = null;														// 仪器标签List,导入xls用
        List<BeanForJxls> datas = null;													// 数据列表List,导入xls用
        int count ; 																	// 计数器,结合BATCHSIZE,做分块用
        int beanSize ; 																	// 单个区域内的仪器个数,结合BATCHSIZE,标识文件名用
        int pageSize ; 																	// excel 总页数
        int pageCount ; 																// excel 页数索引
        BeanForLabel bean = null;
        
        for (Entry<String, Map<Integer, BeanForLabel>> entry_ : area2equi.entrySet()) {
        	areaStr = entry_.getKey();
        	placeMaps = null;	placeMaps = entry_.getValue();
        	
        	place = "";																	// eqid
        	label = null;	label = new ArrayList<String>();							// 仪器标签
        	datas = null;	datas = new ArrayList<BeanForJxls>();						// 数据列表
        	count = 1; 																	// 计数器
        	beanSize = placeMaps.size(); 												// 仪器个数
        	pageSize = beanSize / BATCHSIZE + 1;
        	pageCount = 1;
        	
        	for (Entry<Integer, BeanForLabel> entry : placeMaps.entrySet()) {
        		bean = entry.getValue();
        		label.add(bean.getAreaName()+"-"+bean.getMark());//sheet名
        		place = String.valueOf(entry.getKey()); // 遍历得到的仪器ID
        		// 根据条件从数据库获取结果集
        		datas.add(fillBeanForJxls(entry.getKey(), bean, currentTime, 
        				fillRecordsForJxls(place,startTime,endTime,mainService) ));
        		
        		// 存入xls中-批量操作
        		if (count == BATCHSIZE){
        			if (beanSize == BATCHSIZE){break;}
        			destFileName = destFileName_header + "(" + areaStr + ")" + "("+ pageCount +"_" + pageSize + ").xls";
        			//JxlsUnit.transformXLSFile(areaStr, datas, label, templateFileName, destFileName);
        			JxlUnit.transformXLSFile(areaStr, datas, destFileName, tempShow, equiMap, normalCol, highCol, lowCol);
        	        // 在数据库中插入记录日志
        	        //SetSysService setSysService = new SetSysService();
        	        setSysService.packTBackUp(destFileName, areaStr);
        	        
        			label = null;	label = new ArrayList<String>();
        			datas = null;	datas = new ArrayList<BeanForJxls>();
        			pageCount++;	count = 0; 
        		}
        		count ++;
        	}// end for		
		
			// 收尾-添加最后一个文件
			if (beanSize <= BATCHSIZE){
				destFileName = destFileName_header + "(" + areaStr + ")" + ".xls";
			}else{
				destFileName = destFileName_header + "(" + areaStr + ")" + "("+ pageSize +"_" + pageSize + ").xls";
			}
			if (beanSize != 0){ // 过滤没有仪器的区域
				//JxlsUnit.transformXLSFile(areaStr, datas, label, templateFileName, destFileName);
				if (datas.size()> 0){
					JxlUnit.transformXLSFile(areaStr, datas, destFileName, tempShow, equiMap, normalCol, highCol, lowCol);
        	        // 在数据库中插入记录日志
        	        //SetSysService setSysService = new SetSysService();
        	        setSysService.packTBackUp(destFileName, areaStr);
				}
			}
        }// end for
		
	}
	
	/**
	 * @describe: 获取BeanForJxls对象	
	 * @param eqid BeanForJxls对象:eqid
	 * @param bean BeanForJxls对象:title--bean.getAreaName()+"-"+bean.getMark()
	 * @param currentTime BeanForJxls对象:rtime
	 * @param beanList BeanForJxls对象:beanList
	 * @date:2010-1-18
	 */
	private static BeanForJxls fillBeanForJxls(int eqid, BeanForLabel bean, String rtime, List<BeanForRecord> beanList){
		BeanForJxls jxlsBean = new BeanForJxls();
		jxlsBean.setEqid(eqid);
		jxlsBean.setTitle(bean.getAreaName()+"-"+bean.getMark());
		jxlsBean.setDsrsn(bean.getDsrsn());
		jxlsBean.setRtime(rtime);
		jxlsBean.setBeanList(beanList);
		return jxlsBean;
	}
	
	/**
	 * @describe: 根据条件获取结果集
	 * @param places 仪器ID
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param mainService 查询数据需要的服务
	 * @date:2010-1-17
	 */
	private static List<BeanForRecord> fillRecordsForJxls(String places, Date startTime,Date endTime,MainService mainService) throws Exception{
		List<BeanForRecord> rsRecordBeanListRs = new ArrayList<BeanForRecord>(); // 返回的结果集
		// startTime退一秒:经过调试:ibatis会把['2009-12-18 00:00:00'变成2009-12-18 00:00:00.0'],
		// 而mysql中前面的值比后面的值小,因而搜索结果总是会少第一条数据
		//MainService mainService = new MainService();
		List<BeanForRecord> recordBeanListRs = mainService.		 // 数据库返回的结果集
			searchRecords(fillRecordSearchBean(places,
						FunctionUnit.nextTime(startTime, -1, FunctionUnit.Calendar_END_SECOND).getTime(),
						endTime));		 
		
		Date curtTime = null;		// 遍历当前的时间
		Date perCurTime = null;		// 遍历当前的前一个时间
		
		for (BeanForRecord beanForRecord : recordBeanListRs) {
			curtTime = beanForRecord.getRecTime();
			while (curtTime.after(startTime)){
				if (perCurTime == null){break;}//第一次进入时跳出循环
				if (startTime.after(perCurTime)){// 如果当前的前一个时间比startTime大,
					rsRecordBeanListRs.add(getEmptyBean(startTime));
				}
				//下一个临时时间(下一分钟)
				startTime = FunctionUnit.nextTime(startTime, 1, FunctionUnit.Calendar_END_MINUTE).getTime();
			}// end while
			perCurTime = curtTime;
			// 这个只有在jxls中才用到
			// beanForRecord.setRecTimeStr(FunctionUnit.getTime_Long_Str(beanForRecord.getRecTime()));
			rsRecordBeanListRs.add(beanForRecord);
		}
		return rsRecordBeanListRs;
	}
	
	/**
	 * @describe:	生成一个空的BeanForRecord对象,给予一个时间的属性,用来填充空的数据
	 * @date:2010-1-18
	 */
	private static BeanForRecord getEmptyBean(Date tempTime){
		BeanForRecord tempBean = new BeanForRecord();
		tempBean.setRecTime(tempTime);
		tempBean.setRecTimeStr(FunctionUnit.getTime_Long_Str(tempTime));
		tempBean.setTemperature("-");
		tempBean.setHumidity("-");
		
		return tempBean;
	}
	
	/**
	 * @describe: 获取查询数据的bean
	 * @param places 地址列表
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @date:2010-1-17
	 */
	private static BeanForSearchRecord fillRecordSearchBean(String places, Date startTime,
			Date endTime) throws Exception {
		BeanForSearchRecord beanForSearchRecord = new BeanForSearchRecord();
		
		// 地址列表 
		beanForSearchRecord.setPlaceList(places);
		// 排序类型  1:order by recTime, equipmentId 2:order by equipmentId, recTime(现在都用1)
		beanForSearchRecord.setOrderByType(1);
		//设置搜索数据类型:即时数据
		beanForSearchRecord.setStattype(0);			
		// 开始时间 eg:2009-09-01 18:10:02
		beanForSearchRecord.setAlarmStartFrom(startTime);
		// 结束时间
		beanForSearchRecord.setAlarmStartTo(endTime);
		return beanForSearchRecord;
	}
	
}
