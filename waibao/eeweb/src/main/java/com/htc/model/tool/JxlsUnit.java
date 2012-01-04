package com.htc.model.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import net.sf.jxls.transformer.XLSTransformer;
import net.sf.jxls.util.Util;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.htc.bean.BeanForJxls;
import com.htc.model.SetSysService;

public class JxlsUnit {
	
	/**
	 * @describe:	对搜索结果，进行分析,并包装Excel数据-直接输入,没有拷贝合并-暂时不使用
	 * @param count 计数器
	 * @param resultList 搜索结果
	 * @param fileName: xls文件保存路径
	 * @date:2010-1-18
	 */
	@SuppressWarnings("unchecked")
	public static void transformXLSFile(String areaStr, List<BeanForJxls> datas, List<String> label,
			String templateFileName, String destFileName) throws Exception{  
		// 把数据集导入到xls
		XLSTransformer transformer = new XLSTransformer();
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(templateFileName));
			HSSFWorkbook workBook = transformer.transformMultipleSheetsList(is, datas, label, "record", new HashMap(), 0);
			saveWorkbook(areaStr, workBook,destFileName);
		}catch (Exception e) {
			throw e;
		}finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
	/**
	 * @describe:	对搜索结果，进行分析,并包装Excel数据 -- 这里是对sheet进行拷贝合并处理,出现异常,没有使用
	 * @param count 计数器
	 * @param resultList 搜索结果
	 * @param fileName: xls文件保存路径
	 * @date:2010-1-18
	 */
	@SuppressWarnings({ "unused", "unchecked", "null" })
	private void transformXLSFile(int count, List<BeanForJxls> datas, List<String> label,
			String templateFileName, String destFileName) throws Exception{
		
		// 把数据集导入到xls
		XLSTransformer transformer = new XLSTransformer();
		InputStream is = null;
		HSSFWorkbook resultWorkbook = null;
	    try {
	        is = new BufferedInputStream(new FileInputStream(templateFileName));
	        HSSFWorkbook workBook = transformer.transformMultipleSheetsList(is, datas, label, "record", new HashMap(), 0);
	        
           if (count == 0){    
        	   resultWorkbook = workBook;    
           } else {    
        	 // 这里workBook只有一个sheet.方便可以扩展
             for (int i = 0;i < workBook.getNumberOfSheets();i++){    
            	 HSSFSheet newSheet = resultWorkbook.createSheet( label.get(0) );
                  //HSSFSheet newSheet = workBook.createSheet( label.get(0) );            	 
                  HSSFSheet hssfSheet = workBook.getSheetAt(i);
                  Util.copySheets(newSheet, hssfSheet);    
                  Util.copyPageSetup(newSheet, hssfSheet);    
                  Util.copyPrintSetup(newSheet, hssfSheet);    
             }    
          }
	      saveWorkbook("", resultWorkbook,destFileName);
	    }catch (Exception e) {
	    	throw e;
	    }finally {
	    	if (is != null) {
	    		try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	         }
	    }
	}
	
    /**
     * @describe: 把数据保存到xls中	
     * @param areaStr : 保存到数据库中的区域名
     * @param resultWorkbook 工作簿
     * @param fileName 导出的文件名
     * @date:2010-1-18
     */
    private static void saveWorkbook(String areaStr, HSSFWorkbook resultWorkbook, String fileName) throws Exception{
        OutputStream os = new BufferedOutputStream(new FileOutputStream(fileName));    
        resultWorkbook.write(os);    
        os.flush();    
        os.close();  
        SetSysService setSysService = new SetSysService();
        setSysService.packTBackUp(fileName, areaStr);
    }	
	
}
