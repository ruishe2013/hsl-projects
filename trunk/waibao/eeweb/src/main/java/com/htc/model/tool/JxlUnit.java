package com.htc.model.tool;

import java.io.*;
import java.util.*;

import jxl.*;
import jxl.format.*;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.htc.bean.BeanForJxls;
import com.htc.bean.BeanForRecord;
import com.htc.common.FunctionUnit;
import com.htc.domain.EquipData;

public class JxlUnit {
	
	/**
	 * @describe:	对搜索结果，进行分析,并包装Excel数据
	 * @param areaStr 区域名,用来保存到数据中
	 * @param datas 数据结果集合
	 * @param destFileName: xls文件保存路径
	 * @param tempShow:  温度显示类型(温度显示格式(1:摄氏 2:华氏))
	 * @param equiMap:  所有仪器的信息
	 * @param normalCol:  正常的值
	 * @param highCol:  高颜色的值
	 * @param lowCol:  底颜色的值 
	 * @date:2010-1-18
	 */
	public static void transformXLSFile(String areaStr, List<BeanForJxls> datas, 
			String destFileName,int tempShow, Map<Integer, EquipData> equiMap,
			String normalCol,String highCol, String lowCol) throws Exception{  
		
        //  打开excel要保存的文件 
        WritableWorkbook workbook = Workbook.createWorkbook(new File(destFileName));
        // 重定义第一颜色
        reSetColor(workbook, normalCol, highCol, lowCol);
        // 温度显示格式(1:摄氏 2:华氏)
//		int tempShow = Integer.parseInt(CommonDataUnit.
//				getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)); // 温度显示类型(温度显示格式(1:摄氏 2:华氏))		        
        
        WritableSheet sheet = null;
        EquipData equipData = null;
        
        int count = 0; // sheet 索引号
        for (BeanForJxls beanForJxls : datas) {
        	//equipData = CommonDataUnit.getEquipByID(beanForJxls.getEqid());
        	equipData = equiMap.get(beanForJxls.getEqid());
        	// 创建一个sheet
        	sheet = workbook.createSheet(beanForJxls.getTitle(), count);
        	// 初始化表格属性
        	initialSheetSetting(sheet);
        	// 插入标sheet标题
        	insertHeadCellData(sheet, beanForJxls.getTitle(), beanForJxls.getDsrsn(), beanForJxls.getRtime(), tempShow);
        	// 插入数据内容
        	insertDataContent(sheet, beanForJxls.getBeanList(), equipData,tempShow, 4);
        	count++;
		}
        
        // 写入数据并关闭文件 
        workbook.write();    
        workbook.close();   
        
	}	

	/**
	 * @describe: 插入excel头标签
	 * ①: 仪器名	测试区-22号(变量)
	 * ②: 报表时间	2009年12月17日(变量)
	 * ③: 时间	温度 湿度	
	 * @param sheet sheet 对象名
	 * @param equipmentName 仪器标签
	 * @param Dsrsn 仪器编号
	 * @param storeTime excel保存的时间
	 * @param tempShow 温度显示类型(温度显示格式(1:摄氏 2:华氏))	
	 * @date:2010-1-22
	 */
	private static void insertHeadCellData(WritableSheet sheet,String equipmentName,String dsrsn,
			String storeTime, int tempShow){
		WritableCellFormat formatA = getHeadCellFormatA(CellType.STRING_FORMULA);
		WritableCellFormat formatB = getHeadCellFormatB(CellType.STRING_FORMULA);
		int temp = 0;
		// 背景色 + 粗体
		insertOneCellData(sheet, 0, temp, "仪器编号", formatA);
		// 无背景色 + 合并单元格
		mergeCellsAndInsertData(sheet, 1, temp, 2, temp, dsrsn, formatA);
		
		temp++; //temp=1;
		// 背景色 + 粗体
		insertOneCellData(sheet, 0, temp, "仪器名", formatA);
		// 无背景色 + 合并单元格
		mergeCellsAndInsertData(sheet, 1, temp, 2, temp, equipmentName, formatA);
		
		temp++;//temp=2;
		// 背景色 + 粗体		
		insertOneCellData(sheet, 0, temp, "报表时间", formatA);
		// 无背景色 + 合并单元格
		mergeCellsAndInsertData(sheet, 1, temp, 2, temp, storeTime, formatA);
		
		temp++;//temp=3;
		// 背景色 + 粗体		
		insertOneCellData(sheet, 0, temp, "时间", formatB);
		// 背景色 + 粗体		
		insertOneCellData(sheet, 1, temp, "温度" + (tempShow==1?"(℃)":"(F)"), formatB);
		// 背景色 + 粗体		
		insertOneCellData(sheet, 2, temp, "湿度(%RH)", formatB);
	}
	
	/**
	 * @describe:	插入sheet数据内容
	 * @param sheet sheet对象名 
	 * @param beanList: 数据内容列表
	 * @param tempShow 温度显示类型(温度显示格式(1:摄氏 2:华氏)) 
	 * @param startRow 数据开始的行(现在start取4:表示数据是从第四行开始的) 
	 * @date:2010-1-22
	 */
	private static void insertDataContent(WritableSheet sheet, List<BeanForRecord> beanList, 
			EquipData equipData, int tempShow, int startRow) {
		WritableCellFormat formatDate = getDataCellFormat(CellType.DATE);
		WritableCellFormat formatNormal = getNormalCellFormat(CellType.STRING_FORMULA);
		WritableCellFormat formatHigh = getHighCellFormat(CellType.STRING_FORMULA);
		WritableCellFormat formatLow = getLowCellFormat(CellType.STRING_FORMULA);
		
		// 数据区从(0, 4)开始
		int count = startRow;
		float tempFloat;
		String tempStr;
		for (BeanForRecord bean : beanList) {
			insertOneCellData(sheet, 0, count, bean.getRecTime(), formatDate);
			
			tempStr = bean.getTemperature();
			if (!tempStr.equals("-")){
				tempFloat = Float.parseFloat(tempStr); // 温度数据
				if (tempFloat < equipData.getTempDown()){// 低
					if (tempShow == 2){ // 转换成华氏-- 因为数据库保存的是摄氏的数据
						tempStr = FunctionUnit.FLOAT_DATA_FORMAT.format( tempFloat *9/5 + 32);
					}
					insertOneCellData(sheet, 1, count, tempStr, formatLow);
				}else if (tempFloat >equipData.getTempUp()){// 高
					if (tempShow == 2){ // 转换成华氏
						tempStr = FunctionUnit.FLOAT_DATA_FORMAT.format( tempFloat *9/5 + 32);
					}
					insertOneCellData(sheet, 1, count, tempStr, formatHigh);
				}else{// 正常
					if (tempShow == 2){ // 转换成华氏
						tempStr = FunctionUnit.FLOAT_DATA_FORMAT.format( tempFloat *9/5 + 32);
					}
					insertOneCellData(sheet, 1, count, tempStr, formatNormal);
				}
			}else{
				insertOneCellData(sheet, 1, count, tempStr, formatNormal);
			}
			
			tempStr = bean.getHumidity();
			if (!tempStr.equals("-")){
				tempFloat = Float.parseFloat(tempStr); // 湿度数据
				if (tempFloat < equipData.getHumiDown()){// 低
					insertOneCellData(sheet, 2, count, tempStr, formatLow);
				}else if (tempFloat >equipData.getHumiUp()){// 高
					insertOneCellData(sheet, 2, count, tempStr, formatHigh);
				}else{// 正常
					insertOneCellData(sheet, 2, count, tempStr, formatNormal);
				}
			}else{
				insertOneCellData(sheet, 2, count, tempStr, formatNormal);
			}
			
			count++;
		}
	}
	
	/**
	 * @describe: 重定义第一颜色reSetColor
	 * @param normalCol:  正常的值
	 * @param highCol:  高颜色的值
	 * @param lowCol:  底颜色的值
	 * @date:2010-1-22
	 */
	private static void reSetColor(WritableWorkbook workbook,String normalCol,String highCol, String lowCol){
		int r=0,g=0,b=0;
		String colorstr = "";
		
		// 重定义颜色值- 绿色,用来显示正常的值
		//colorstr = CommonDataUnit.getSysArgsByKey(BeanForSysArgs.NORMAL_COLORDEF);
		colorstr = normalCol.substring(1);// 去掉#号.如:#FF00FF
		if (colorstr.length() == 3){
			r = Integer.parseInt(colorstr.substring(0, 1)+colorstr.substring(0, 1),16);
			g = Integer.parseInt(colorstr.substring(1, 2)+colorstr.substring(1, 2),16);
			b = Integer.parseInt(colorstr.substring(2, 3)+colorstr.substring(2, 3),16);
		}else{
			r = Integer.parseInt(colorstr.substring(0, 2),16);
			g = Integer.parseInt(colorstr.substring(2, 4),16);
			b = Integer.parseInt(colorstr.substring(4, 6),16);
		} 
		workbook.setColourRGB(Colour.GREEN, r, g, b);
		
        // 重定义颜色值 - 红色,用来显示高于上限的值
		//colorstr = CommonDataUnit.getSysArgsByKey(BeanForSysArgs.HIGH_COLORDEF);
		colorstr = highCol.substring(1);// 去掉#号.如:#FF00FF
		if (colorstr.length() == 3){
			r = Integer.parseInt(colorstr.substring(0, 1)+colorstr.substring(0, 1),16);
			g = Integer.parseInt(colorstr.substring(1, 2)+colorstr.substring(1, 2),16);
			b = Integer.parseInt(colorstr.substring(2, 3)+colorstr.substring(2, 3),16);
		}else{
			r = Integer.parseInt(colorstr.substring(0, 2),16);
			g = Integer.parseInt(colorstr.substring(2, 4),16);
			b = Integer.parseInt(colorstr.substring(4, 6),16);
		} 
		workbook.setColourRGB(Colour.RED, r, g, b);
		
		// 重定义颜色值- 蓝色2,用来显示低于下限的值
		//colorstr = CommonDataUnit.getSysArgsByKey(BeanForSysArgs.LOW_COLORDEF);
		colorstr = lowCol.substring(1);// 去掉#号.如:#FF00FF
		if (colorstr.length() == 3){
			r = Integer.parseInt(colorstr.substring(0, 1)+colorstr.substring(0, 1),16);
			g = Integer.parseInt(colorstr.substring(1, 2)+colorstr.substring(1, 2),16);
			b = Integer.parseInt(colorstr.substring(2, 3)+colorstr.substring(2, 3),16);
		}else{
			r = Integer.parseInt(colorstr.substring(0, 2),16);
			g = Integer.parseInt(colorstr.substring(2, 4),16);
			b = Integer.parseInt(colorstr.substring(4, 6),16);
		} 
		workbook.setColourRGB(Colour.BLUE2, r, g, b);
	}
	
   /**  
    * 初始化表格属性  
    * @param sheet  
    */  
   private static void initialSheetSetting(WritableSheet sheet){   
      try{   
           //sheet.getSettings().setProtected(true); //设置xls的保护，单元格为只读的   
           sheet.getSettings().setDefaultColumnWidth(20); //设置列的默认宽度   
           //sheet.setRowView(2,false);//行高自动扩展    
           //setRowView(int row, int height);--行高    
           //setColumnView(int  col,int width); --列宽   
           sheet.setColumnView(0,25);//设置第一列宽度
      }catch(Exception e){   
          e.printStackTrace();   
      }   
   }   
      
   /**  
    * 插入单元格数据  
    * @param sheet  
    * @param col  
    * @param row  
    * @param data  
    */  
   private static void insertOneCellData(WritableSheet sheet,Integer col,Integer row,Object data,WritableCellFormat format){   
       try{   
           if(data instanceof Double){   
               jxl.write.Number  labelNF = new jxl.write.Number(col,row,(Double)data,format);    
               sheet.addCell(labelNF);    
           }else if(data instanceof Boolean){   
               jxl.write.Boolean labelB = new jxl.write.Boolean(col,row,(Boolean)data,format);    
               sheet.addCell(labelB);    
           }else if(data instanceof Date){   
               jxl.write.DateTime labelDT = new jxl.write.DateTime(col,row,(Date)data,format);    
               sheet.addCell(labelDT);    
               //setCellComments(labelDT, "这是个创建表的日期说明！");   
           }else{   
               Label label = new Label(col,row,data.toString(),format);   
               sheet.addCell(label);                  
           }   
       }catch(Exception e){   
           e.printStackTrace();   
       }   
  
  }   
      
   /**  
    * 合并单元格，并插入数据  
    * @param sheet  
    * @param col_start  
    * @param row_start  
    * @param col_end  
    * @param row_end  
    * @param data  
    * @param format  
    */  
   private static void mergeCellsAndInsertData(WritableSheet sheet,Integer col_start,Integer row_start,Integer col_end,Integer row_end,Object data, WritableCellFormat format){   
      try{   
          sheet.mergeCells(col_start, row_start, col_end, row_end);//左上角到右下角   
          insertOneCellData(sheet, col_start, row_start, data, format);   
      }catch(Exception e){   
          e.printStackTrace();   
      }   
  
   }  
   
   //  标题用格式 -- 粗体+12+黑色字体+冰蓝色背景+非斜体 - 第一二行用
   private static WritableCellFormat getHeadCellFormatA(CellType type){
	   return getDataCellFormat(type, true, 12, Colour.BLACK, Colour.ICE_BLUE, false);
   }
   //  标题用格式 -- 粗体+12+黑色字体+蓝灰色背景+非斜体 - 第三行用
   private static WritableCellFormat getHeadCellFormatB(CellType type){
	   return getDataCellFormat(type, true, 12, Colour.BLACK, Colour.BLUE_GREY, false);
   }
   // 正常数据格式 -- 非粗体+10+绿色(被重定义)字体+白色背景+非斜体
   private static WritableCellFormat getNormalCellFormat(CellType type){
	   return getDataCellFormat(type, false, 10, Colour.GREEN, Colour.WHITE, false);
   }
   // 异常数据格式-高于上限 -- 非粗体+10+红色(被重定义)字体+白色背景+斜体
   private static WritableCellFormat getHighCellFormat(CellType type){
	   return getDataCellFormat(type, false, 10, Colour.RED, Colour.WHITE, true);
   }
   // 异常数据格式-低于下限 -- 非粗体+10+蓝色(被重定义)字体+白色背景+斜体
   private static WritableCellFormat getLowCellFormat(CellType type){
	   return getDataCellFormat(type, false, 10, Colour.BLUE2, Colour.WHITE, true);
   }
   // 时间数据
   private static WritableCellFormat getDataCellFormat(CellType type){
	   return getDataCellFormat(type, false, 10, Colour.BLACK, Colour.WHITE, false);
   }
   
   /**
	 * @describe: 得到数据格式 
	 * @param type 数据类型
	 * @param boldType 是否粗体[true:粗体 false:不是粗体]
	 * @param fontSize 字体大小
	 * @param fontColor 字体颜色
	 * @param backColor 背景颜色
	 * @param italic 最后一个为是否斜体
	 */
   private static WritableCellFormat getDataCellFormat(CellType type,boolean boldType,
		   int fontSize, Colour fontColor, Colour backColor, boolean italic){   
       WritableCellFormat wcf = null;   
       try {   
           //字体样式   
           if(type == CellType.NUMBER || type == CellType.NUMBER_FORMULA){//数字   
              NumberFormat nf = new NumberFormat("#.00");   
              wcf = new WritableCellFormat(nf);    
           }else if(type == CellType.DATE || type == CellType.DATE_FORMULA){//日期   
        	   jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-MM-dd hh:mm:ss");    
               //jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-MM-dd hh:mm");    
               wcf = new jxl.write.WritableCellFormat(df);    
           }else{   
               WritableFont wf = new WritableFont(WritableFont.TIMES, fontSize, 
            		   boldType?WritableFont.BOLD:WritableFont.NO_BOLD,italic);//最后一个为是否italic
               wf.setColour(fontColor);
               wcf = new WritableCellFormat(wf);   
           }   
           //对齐方式   
           wcf.setAlignment(Alignment.CENTRE);   
           wcf.setVerticalAlignment(VerticalAlignment.CENTRE);   
           //边框   
           wcf.setBorder(Border.LEFT,BorderLineStyle.THIN);   
           wcf.setBorder(Border.BOTTOM,BorderLineStyle.THIN);   
           wcf.setBorder(Border.RIGHT,BorderLineStyle.THIN);   
           //背景色   
           //wcf.setBackground(Colour.WHITE);   
           wcf.setBackground(backColor);   
           wcf.setWrap(true);//自动换行   
              
       } catch (WriteException e) {   
        e.printStackTrace();   
       }   
       return wcf;   
   }    
   
   // 目前没有用到下面的方法
   /**  
    * 插入公式  
    * @param sheet  
    * @param col  
    * @param row  
    * @param formula  
    * @param format  
    */  
   private static void insertFormula(WritableSheet sheet,Integer col,Integer row,String formula,WritableCellFormat format){   
       try{   
           Formula f = new Formula(col, row, formula, format);   
           sheet.addCell(f);   
       }catch(Exception e){   
           e.printStackTrace();   
       }   
   }   
      
   /**  
    * 插入一行数据  
    * @param sheet       工作表  
    * @param row         行号  
    * @param content     内容  
    * @param format      风格  
    */  
   private static void insertRowData(WritableSheet sheet,Integer row,String[] dataArr,WritableCellFormat format){   
       try{   
           Label label;   
           for(int i=0;i<dataArr.length;i++){   
               label = new Label(i,row,dataArr[i],format);   
               sheet.addCell(label);   
           }   
       }catch(Exception e){   
           e.printStackTrace();   
       }   
   }      
   
   /**  
    * 得到数据表头格式  
    * @return  
    */  
   private static WritableCellFormat getTitleCellFormat(){   
       WritableCellFormat wcf = null;   
       try {   
           //字体样式   
           WritableFont wf = new WritableFont(WritableFont.TIMES, 12, WritableFont.NO_BOLD, false);//最后一个为是否italic   
           wf.setColour(Colour.RED);   
           wcf = new WritableCellFormat(wf);   
           //对齐方式   
           wcf.setAlignment(Alignment.CENTRE);   
           wcf.setVerticalAlignment(VerticalAlignment.CENTRE);   
           //边框   
           wcf.setBorder(Border.ALL,BorderLineStyle.THIN);   
              
           //背景色   
           wcf.setBackground(Colour.GREY_25_PERCENT);   
       } catch (WriteException e) {   
        e.printStackTrace();   
       }   
       return wcf;
   }    
   
   /**  
    * 给单元格加注释  
    * @param label  
    * @param comments  
    */  
   @SuppressWarnings("unused")
private static void setCellComments(Object label,String comments){   
       WritableCellFeatures cellFeatures = new WritableCellFeatures();   
       cellFeatures.setComment(comments);   
       if(label instanceof jxl.write.Number){   
           jxl.write.Number num = (jxl.write.Number)label;   
           num.setCellFeatures(cellFeatures);   
       }else if(label instanceof jxl.write.Boolean){   
           jxl.write.Boolean bool = (jxl.write.Boolean)label;   
           bool.setCellFeatures(cellFeatures);   
       }else if(label instanceof jxl.write.DateTime){   
           jxl.write.DateTime dt = (jxl.write.DateTime)label;   
           dt.setCellFeatures(cellFeatures);   
       }else{   
           Label _label = (Label)label;   
           _label.setCellFeatures(cellFeatures);   
       }   
   }   
      
   /**  
   * 读取excel  
   * @param inputFile  
   * @param inputFileSheetIndex  
   * @throws Exception  
   */  
   @SuppressWarnings("unused")
private static ArrayList<String> readDataFromExcel(File inputFile, int inputFileSheetIndex){   
      ArrayList<String> list = new ArrayList<String>();   
      Workbook book = null;   
      Cell cell = null;   
      WorkbookSettings setting = new WorkbookSettings();    
      java.util.Locale locale = new java.util.Locale("zh","CN");    
      setting.setLocale(locale);   
      setting.setEncoding("ISO-8859-1");   
      try{   
          book = Workbook.getWorkbook(inputFile, setting);   
      }catch(Exception e){   
          e.printStackTrace();     
      }   
  
      Sheet sheet = book.getSheet(inputFileSheetIndex);   
      for (int rowIndex = 0; rowIndex < sheet.getRows(); rowIndex++) {//行   
       for (int colIndex = 0; colIndex < sheet.getColumns(); colIndex++) {//列   
           cell = sheet.getCell(colIndex, rowIndex);   
           //System.out.println(cell.getContents());   
           list.add(cell.getContents());   
       }   
      }   
      book.close();   
  
      return list;   
   }   
  
   /**  
    * 打开文件看看  
    * @param exePath  
    * @param filePath  
    */  
   @SuppressWarnings("unused")
private static void openExcel(String exePath,String filePath){   
       Runtime r=Runtime.getRuntime();    
       String cmd[]={exePath,filePath};    
       try{    
           r.exec(cmd);    
       }catch(Exception e){   
           e.printStackTrace();   
       }   
   }   
   
	/**  
	 * 生成Excel文件  -- 测试
	 * @param path         文件路径  
	 * @param sheetName    工作表名称  
	 * @param dataTitles   数据标题  
	 */  
  public static void createExcelFile(String path,String sheetName,String[] dataTitles){   
      WritableWorkbook workbook;   
      try{   
          OutputStream os=new FileOutputStream(path);    
          workbook=Workbook.createWorkbook(os);    
 
          WritableSheet sheet = workbook.createSheet(sheetName, 0); //添加第一个工作表   
          initialSheetSetting(sheet);   
             
          Label label;   
          for (int i=0; i<dataTitles.length; i++){   
              // Label(列号,行号,内容,风格)   
              label = new Label(i, 0, dataTitles[i],getTitleCellFormat());    
              sheet.addCell(label);    
          }   
 
          // 插入一行   
          insertRowData(sheet,1,new String[]{"200201001","张三","100","60","100","260"},getDataCellFormat(CellType.STRING_FORMULA));   
             
          // 一个一个插入行   
          label = new Label(0, 2,"200201002",getDataCellFormat(CellType.STRING_FORMULA));    
          sheet.addCell(label);   
             
          label = new Label(1, 2,"李四",getDataCellFormat(CellType.STRING_FORMULA));    
          sheet.addCell(label);   
             
          insertOneCellData(sheet,2,2,70.5,getDataCellFormat(CellType.NUMBER));   
          insertOneCellData(sheet,3,2,90.523,getDataCellFormat(CellType.NUMBER));   
          insertOneCellData(sheet,4,2,60.5,getDataCellFormat(CellType.NUMBER));   
 
          insertFormula(sheet,5,2,"C3+D3+E3",getDataCellFormat(CellType.NUMBER_FORMULA));   
             
          // 插入日期   
          mergeCellsAndInsertData(sheet, 0, 3, 5, 3, new Date(), getDataCellFormat(CellType.DATE));   
          // 写入数据并关闭文件 
          workbook.write();    
          workbook.close();   
      }catch(Exception e){   
          e.printStackTrace();   
      }   
  }      
      
//   public static void main(String[] args){   
//       String[] titles = {"学号","姓名","语文","数学","英语","总分"};    
//       JxlUnit jxl = new JxlUnit();   
//       String filePath = "E:/test.xls";
//       jxl.createExcelFile(filePath,"成绩单",titles);   
//       System.out.println("end...");
//       //jxl.readDataFromExcel(new File(filePath),0);   
//       //jxl.openExcel("C:/Program Files/Microsoft Office/OFFICE11/EXCEL.EXE",filePath);   
//   } 	

}
