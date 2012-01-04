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
 * ���� : ��ÿ���µ����ݱ��ݳ�csv��ʽ����
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
public class ExcelBackUp {

	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(ExcelBackUp.class);
	private static int BATCHSIZE = 10; // һ��excel������ɵ�sheet����
	
	/**
	 * @describe: ��ȡxlsģ��·��
	 * @return �ɹ�:����xlsģ��·��	ʧ��:����null
	 * @date:2010-1-20
	 */
	@SuppressWarnings("unused")
//	private static String getTemplateFileName() throws IOException{
//		// ģ��xls�ļ�·��
//        String templateFileName = ExcelBackUp.class.getResource("/").getPath();
//        templateFileName = templateFileName.substring(1,templateFileName.indexOf("/WEB-INF")+1)+"WEB-INF/templates/record.xls";
//        boolean tempFileExist = HtcFiles.CheckFile(templateFileName); // ���ģ���ļ��Ƿ����
//        if (!tempFileExist){
//        	templateFileName = null;
//        	MainService mainService = new MainService(); 
//        	mainService.packTlog(TLog.ERROR_LOG, "�Ҳ���ģ��excel,���ܱ�����ʷ�б�...");
//        }
//		return templateFileName;
//	}
	
	
	/**
	 * @describe: ��ȡ����xls�����·����һ����.������ļ����Ƿ����,û�оʹ���<br>
	 * ����ʽ:����·��+y��/m��/d��/+(������)+(N/M).xls.��<br>
	 * ����:c:/htc/backup/2010��/1��/20��/2010��1��20��(������)(2/5).xls��<br>
	 * @param perPath: ������ļ����е�·��
	 * @param mainService: д��־��Ҫ�ķ���
	 * @return �ɹ�:���ر���xls·����һ����	ʧ��:����null<br>
	 * @date:2010-1-20
	 */
	private static String getJxlsFileNamePer(Date date, String perPath, MainService mainService) throws Exception{
		//String destFileName_header = CommonDataUnit.getSysArgsByKey(BeanForSysArgs.BACKUP_PATH) +
        String destFileName_header = perPath + "/" +  FunctionUnit.getTypeTime(date);
        
        // �����߼���ļ���
        if (!HtcFiles.createOrCheckFolder(destFileName_header)){
        	destFileName_header = null;
        	mainService.packTlog(TLog.ERROR_LOG, "��ȡ�����б�·������");
        }; 
        return destFileName_header;
	}
	
	/**
	 * @describe: ���ݱ�������ID,��ȡ����,��������excel��,�ļ�����ʽ(2010��02��19��(������)(1_5).xls)
	 * ��ʽ:����·��+y��/m��/d��/+(������)+(N/M).xls.��
	 * @param startTime: ��ʼ������ʱ��
	 * @param endTime ������������ʱ��
	 * @param perPath: ������ļ����е�·��
	 * @param area2equi: ��ȡȫ����Ӧ[����:����]
	 * @param tempShow:  �¶���ʾ����(�¶���ʾ��ʽ(1:���� 2:����))
	 * @param equiMap:  ������������Ϣ
	 * @param normalCol:  ������ֵ
	 * @param highCol:  ����ɫ��ֵ
	 * @param lowCol:  ����ɫ��ֵ 
	 * @param setSysService:  �Ѽ�¼д�����ݿ���ʱ,��Ҫ�ķ���
	 * @param setSysService:  �Ѽ�¼д�����ݿ���ʱ,��Ҫ�ķ���
	 * @date:2010-1-17
	 */
	public static void storeToXls(Date startTime,Date endTime, String perPath,
			Map<String, Map<Integer, BeanForLabel>> area2equi,
			int tempShow, Map<Integer, EquipData> equiMap,
			String normalCol,String highCol, String lowCol,
			SetSysService setSysService, MainService mainService) throws Exception {
		Date tempDate = null;
		do {
			// ��һ��
			tempDate = FunctionUnit.nextTime(startTime, 1, FunctionUnit.Calendar_END_DAY).getTime();
			tempDate = tempDate.after(endTime)? endTime : tempDate;
			// ����һ����,��Ϊʱ���Ҫ��[0:0:0��23:59:59]֮��
			store2Xls(startTime, FunctionUnit.nextTime(tempDate, -1, FunctionUnit.Calendar_END_SECOND).getTime(),
					perPath ,area2equi, tempShow, equiMap, normalCol, highCol, lowCol, setSysService, mainService);
			startTime= tempDate;
		} while (startTime.before(endTime));
	}

	/**
	 * @describe: ���ݱ�������ID,��ȡ����,��������excel��,�ļ�����ʽ(2010��02��19��(������)(1_5).xls)
	 * @param startTime: ��ʼ������ʱ��
	 * @param endTime ������������ʱ��
	 * @param perPath: ������ļ����е�·��
	 * @param area2equi: ��ȡȫ����Ӧ[����:����]
	 * @param tempShow:  �¶���ʾ����(�¶���ʾ��ʽ(1:���� 2:����))
	 * @param equiMap:  ������������Ϣ
	 * @param normalCol:  ������ֵ
	 * @param highCol:  ����ɫ��ֵ
	 * @param lowCol:  ����ɫ��ֵ 
	 * @param setSysService:  �Ѽ�¼д�����ݿ���ʱ,��Ҫ�ķ���
	 * @param mainService:  д��־ʱ,��Ҫ�ķ���
	 * @date:2010-1-17
	 */
	private static void store2Xls(Date startTime,Date endTime, String perPath,
			Map<String, Map<Integer, BeanForLabel>> area2equi,
			int tempShow, Map<Integer, EquipData> equiMap,
			String normalCol,String highCol, String lowCol,
			SetSysService setSysService, MainService mainService) throws Exception {
		// ģ��xls�ļ�·�� -- jxl�в���Ҫ
        //String templateFileName = getTemplateFileName();
        //if (templateFileName == null){return;} // ģ�岻����,�˳�
        
		// ����xls���ݵ��ļ�·����һ����
        String destFileName_header = getJxlsFileNamePer(startTime, perPath, mainService);
        if (destFileName_header == null){return;}
        String currentTime = FunctionUnit.getDateToStr(startTime, 
        		FunctionUnit.Calendar_END_DAY, FunctionUnit.SHOW_CHINESE);//ʱ��
        destFileName_header = destFileName_header + "/" + currentTime;
        
        String destFileName = "";														// xls���ݵ��ļ�·��-��ʱ����
        //Map<String, Map<Integer, BeanForLabel>> area2equi = CommonDataUnit.area2Equipment();	// ��ȡȫ����Ӧ[����:����]
        Map<Integer, BeanForLabel> placeMaps = null;								        	// ��ȡmap(eqid,������ǩ)
        String areaStr = "";															// ������
        String place = "";																// ��������,��ѯSql��
        List<String> label = null;														// ������ǩList,����xls��
        List<BeanForJxls> datas = null;													// �����б�List,����xls��
        int count ; 																	// ������,���BATCHSIZE,���ֿ���
        int beanSize ; 																	// ���������ڵ���������,���BATCHSIZE,��ʶ�ļ�����
        int pageSize ; 																	// excel ��ҳ��
        int pageCount ; 																// excel ҳ������
        BeanForLabel bean = null;
        
        for (Entry<String, Map<Integer, BeanForLabel>> entry_ : area2equi.entrySet()) {
        	areaStr = entry_.getKey();
        	placeMaps = null;	placeMaps = entry_.getValue();
        	
        	place = "";																	// eqid
        	label = null;	label = new ArrayList<String>();							// ������ǩ
        	datas = null;	datas = new ArrayList<BeanForJxls>();						// �����б�
        	count = 1; 																	// ������
        	beanSize = placeMaps.size(); 												// ��������
        	pageSize = beanSize / BATCHSIZE + 1;
        	pageCount = 1;
        	
        	for (Entry<Integer, BeanForLabel> entry : placeMaps.entrySet()) {
        		bean = entry.getValue();
        		label.add(bean.getAreaName()+"-"+bean.getMark());//sheet��
        		place = String.valueOf(entry.getKey()); // �����õ�������ID
        		// �������������ݿ��ȡ�����
        		datas.add(fillBeanForJxls(entry.getKey(), bean, currentTime, 
        				fillRecordsForJxls(place,startTime,endTime,mainService) ));
        		
        		// ����xls��-��������
        		if (count == BATCHSIZE){
        			if (beanSize == BATCHSIZE){break;}
        			destFileName = destFileName_header + "(" + areaStr + ")" + "("+ pageCount +"_" + pageSize + ").xls";
        			//JxlsUnit.transformXLSFile(areaStr, datas, label, templateFileName, destFileName);
        			JxlUnit.transformXLSFile(areaStr, datas, destFileName, tempShow, equiMap, normalCol, highCol, lowCol);
        	        // �����ݿ��в����¼��־
        	        //SetSysService setSysService = new SetSysService();
        	        setSysService.packTBackUp(destFileName, areaStr);
        	        
        			label = null;	label = new ArrayList<String>();
        			datas = null;	datas = new ArrayList<BeanForJxls>();
        			pageCount++;	count = 0; 
        		}
        		count ++;
        	}// end for		
		
			// ��β-������һ���ļ�
			if (beanSize <= BATCHSIZE){
				destFileName = destFileName_header + "(" + areaStr + ")" + ".xls";
			}else{
				destFileName = destFileName_header + "(" + areaStr + ")" + "("+ pageSize +"_" + pageSize + ").xls";
			}
			if (beanSize != 0){ // ����û������������
				//JxlsUnit.transformXLSFile(areaStr, datas, label, templateFileName, destFileName);
				if (datas.size()> 0){
					JxlUnit.transformXLSFile(areaStr, datas, destFileName, tempShow, equiMap, normalCol, highCol, lowCol);
        	        // �����ݿ��в����¼��־
        	        //SetSysService setSysService = new SetSysService();
        	        setSysService.packTBackUp(destFileName, areaStr);
				}
			}
        }// end for
		
	}
	
	/**
	 * @describe: ��ȡBeanForJxls����	
	 * @param eqid BeanForJxls����:eqid
	 * @param bean BeanForJxls����:title--bean.getAreaName()+"-"+bean.getMark()
	 * @param currentTime BeanForJxls����:rtime
	 * @param beanList BeanForJxls����:beanList
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
	 * @describe: ����������ȡ�����
	 * @param places ����ID
	 * @param startTime ��ʼʱ��
	 * @param endTime ����ʱ��
	 * @param mainService ��ѯ������Ҫ�ķ���
	 * @date:2010-1-17
	 */
	private static List<BeanForRecord> fillRecordsForJxls(String places, Date startTime,Date endTime,MainService mainService) throws Exception{
		List<BeanForRecord> rsRecordBeanListRs = new ArrayList<BeanForRecord>(); // ���صĽ����
		// startTime��һ��:��������:ibatis���['2009-12-18 00:00:00'���2009-12-18 00:00:00.0'],
		// ��mysql��ǰ���ֵ�Ⱥ����ֵС,�������������ǻ��ٵ�һ������
		//MainService mainService = new MainService();
		List<BeanForRecord> recordBeanListRs = mainService.		 // ���ݿⷵ�صĽ����
			searchRecords(fillRecordSearchBean(places,
						FunctionUnit.nextTime(startTime, -1, FunctionUnit.Calendar_END_SECOND).getTime(),
						endTime));		 
		
		Date curtTime = null;		// ������ǰ��ʱ��
		Date perCurTime = null;		// ������ǰ��ǰһ��ʱ��
		
		for (BeanForRecord beanForRecord : recordBeanListRs) {
			curtTime = beanForRecord.getRecTime();
			while (curtTime.after(startTime)){
				if (perCurTime == null){break;}//��һ�ν���ʱ����ѭ��
				if (startTime.after(perCurTime)){// �����ǰ��ǰһ��ʱ���startTime��,
					rsRecordBeanListRs.add(getEmptyBean(startTime));
				}
				//��һ����ʱʱ��(��һ����)
				startTime = FunctionUnit.nextTime(startTime, 1, FunctionUnit.Calendar_END_MINUTE).getTime();
			}// end while
			perCurTime = curtTime;
			// ���ֻ����jxls�в��õ�
			// beanForRecord.setRecTimeStr(FunctionUnit.getTime_Long_Str(beanForRecord.getRecTime()));
			rsRecordBeanListRs.add(beanForRecord);
		}
		return rsRecordBeanListRs;
	}
	
	/**
	 * @describe:	����һ���յ�BeanForRecord����,����һ��ʱ�������,�������յ�����
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
	 * @describe: ��ȡ��ѯ���ݵ�bean
	 * @param places ��ַ�б�
	 * @param startTime ��ʼʱ��
	 * @param endTime ����ʱ��
	 * @date:2010-1-17
	 */
	private static BeanForSearchRecord fillRecordSearchBean(String places, Date startTime,
			Date endTime) throws Exception {
		BeanForSearchRecord beanForSearchRecord = new BeanForSearchRecord();
		
		// ��ַ�б� 
		beanForSearchRecord.setPlaceList(places);
		// ��������  1:order by recTime, equipmentId 2:order by equipmentId, recTime(���ڶ���1)
		beanForSearchRecord.setOrderByType(1);
		//����������������:��ʱ����
		beanForSearchRecord.setStattype(0);			
		// ��ʼʱ�� eg:2009-09-01 18:10:02
		beanForSearchRecord.setAlarmStartFrom(startTime);
		// ����ʱ��
		beanForSearchRecord.setAlarmStartTo(endTime);
		return beanForSearchRecord;
	}
	
}
