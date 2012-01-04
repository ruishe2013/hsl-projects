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
	 * @describe:	��������������з���,����װExcel����
	 * @param areaStr ������,�������浽������
	 * @param datas ���ݽ������
	 * @param destFileName: xls�ļ�����·��
	 * @param tempShow:  �¶���ʾ����(�¶���ʾ��ʽ(1:���� 2:����))
	 * @param equiMap:  ������������Ϣ
	 * @param normalCol:  ������ֵ
	 * @param highCol:  ����ɫ��ֵ
	 * @param lowCol:  ����ɫ��ֵ 
	 * @date:2010-1-18
	 */
	public static void transformXLSFile(String areaStr, List<BeanForJxls> datas, 
			String destFileName,int tempShow, Map<Integer, EquipData> equiMap,
			String normalCol,String highCol, String lowCol) throws Exception{  
		
        //  ��excelҪ������ļ� 
        WritableWorkbook workbook = Workbook.createWorkbook(new File(destFileName));
        // �ض����һ��ɫ
        reSetColor(workbook, normalCol, highCol, lowCol);
        // �¶���ʾ��ʽ(1:���� 2:����)
//		int tempShow = Integer.parseInt(CommonDataUnit.
//				getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)); // �¶���ʾ����(�¶���ʾ��ʽ(1:���� 2:����))		        
        
        WritableSheet sheet = null;
        EquipData equipData = null;
        
        int count = 0; // sheet ������
        for (BeanForJxls beanForJxls : datas) {
        	//equipData = CommonDataUnit.getEquipByID(beanForJxls.getEqid());
        	equipData = equiMap.get(beanForJxls.getEqid());
        	// ����һ��sheet
        	sheet = workbook.createSheet(beanForJxls.getTitle(), count);
        	// ��ʼ���������
        	initialSheetSetting(sheet);
        	// �����sheet����
        	insertHeadCellData(sheet, beanForJxls.getTitle(), beanForJxls.getDsrsn(), beanForJxls.getRtime(), tempShow);
        	// ������������
        	insertDataContent(sheet, beanForJxls.getBeanList(), equipData,tempShow, 4);
        	count++;
		}
        
        // д�����ݲ��ر��ļ� 
        workbook.write();    
        workbook.close();   
        
	}	

	/**
	 * @describe: ����excelͷ��ǩ
	 * ��: ������	������-22��(����)
	 * ��: ����ʱ��	2009��12��17��(����)
	 * ��: ʱ��	�¶� ʪ��	
	 * @param sheet sheet ������
	 * @param equipmentName ������ǩ
	 * @param Dsrsn �������
	 * @param storeTime excel�����ʱ��
	 * @param tempShow �¶���ʾ����(�¶���ʾ��ʽ(1:���� 2:����))	
	 * @date:2010-1-22
	 */
	private static void insertHeadCellData(WritableSheet sheet,String equipmentName,String dsrsn,
			String storeTime, int tempShow){
		WritableCellFormat formatA = getHeadCellFormatA(CellType.STRING_FORMULA);
		WritableCellFormat formatB = getHeadCellFormatB(CellType.STRING_FORMULA);
		int temp = 0;
		// ����ɫ + ����
		insertOneCellData(sheet, 0, temp, "�������", formatA);
		// �ޱ���ɫ + �ϲ���Ԫ��
		mergeCellsAndInsertData(sheet, 1, temp, 2, temp, dsrsn, formatA);
		
		temp++; //temp=1;
		// ����ɫ + ����
		insertOneCellData(sheet, 0, temp, "������", formatA);
		// �ޱ���ɫ + �ϲ���Ԫ��
		mergeCellsAndInsertData(sheet, 1, temp, 2, temp, equipmentName, formatA);
		
		temp++;//temp=2;
		// ����ɫ + ����		
		insertOneCellData(sheet, 0, temp, "����ʱ��", formatA);
		// �ޱ���ɫ + �ϲ���Ԫ��
		mergeCellsAndInsertData(sheet, 1, temp, 2, temp, storeTime, formatA);
		
		temp++;//temp=3;
		// ����ɫ + ����		
		insertOneCellData(sheet, 0, temp, "ʱ��", formatB);
		// ����ɫ + ����		
		insertOneCellData(sheet, 1, temp, "�¶�" + (tempShow==1?"(��)":"(F)"), formatB);
		// ����ɫ + ����		
		insertOneCellData(sheet, 2, temp, "ʪ��(%RH)", formatB);
	}
	
	/**
	 * @describe:	����sheet��������
	 * @param sheet sheet������ 
	 * @param beanList: ���������б�
	 * @param tempShow �¶���ʾ����(�¶���ʾ��ʽ(1:���� 2:����)) 
	 * @param startRow ���ݿ�ʼ����(����startȡ4:��ʾ�����Ǵӵ����п�ʼ��) 
	 * @date:2010-1-22
	 */
	private static void insertDataContent(WritableSheet sheet, List<BeanForRecord> beanList, 
			EquipData equipData, int tempShow, int startRow) {
		WritableCellFormat formatDate = getDataCellFormat(CellType.DATE);
		WritableCellFormat formatNormal = getNormalCellFormat(CellType.STRING_FORMULA);
		WritableCellFormat formatHigh = getHighCellFormat(CellType.STRING_FORMULA);
		WritableCellFormat formatLow = getLowCellFormat(CellType.STRING_FORMULA);
		
		// ��������(0, 4)��ʼ
		int count = startRow;
		float tempFloat;
		String tempStr;
		for (BeanForRecord bean : beanList) {
			insertOneCellData(sheet, 0, count, bean.getRecTime(), formatDate);
			
			tempStr = bean.getTemperature();
			if (!tempStr.equals("-")){
				tempFloat = Float.parseFloat(tempStr); // �¶�����
				if (tempFloat < equipData.getTempDown()){// ��
					if (tempShow == 2){ // ת���ɻ���-- ��Ϊ���ݿⱣ��������ϵ�����
						tempStr = FunctionUnit.FLOAT_DATA_FORMAT.format( tempFloat *9/5 + 32);
					}
					insertOneCellData(sheet, 1, count, tempStr, formatLow);
				}else if (tempFloat >equipData.getTempUp()){// ��
					if (tempShow == 2){ // ת���ɻ���
						tempStr = FunctionUnit.FLOAT_DATA_FORMAT.format( tempFloat *9/5 + 32);
					}
					insertOneCellData(sheet, 1, count, tempStr, formatHigh);
				}else{// ����
					if (tempShow == 2){ // ת���ɻ���
						tempStr = FunctionUnit.FLOAT_DATA_FORMAT.format( tempFloat *9/5 + 32);
					}
					insertOneCellData(sheet, 1, count, tempStr, formatNormal);
				}
			}else{
				insertOneCellData(sheet, 1, count, tempStr, formatNormal);
			}
			
			tempStr = bean.getHumidity();
			if (!tempStr.equals("-")){
				tempFloat = Float.parseFloat(tempStr); // ʪ������
				if (tempFloat < equipData.getHumiDown()){// ��
					insertOneCellData(sheet, 2, count, tempStr, formatLow);
				}else if (tempFloat >equipData.getHumiUp()){// ��
					insertOneCellData(sheet, 2, count, tempStr, formatHigh);
				}else{// ����
					insertOneCellData(sheet, 2, count, tempStr, formatNormal);
				}
			}else{
				insertOneCellData(sheet, 2, count, tempStr, formatNormal);
			}
			
			count++;
		}
	}
	
	/**
	 * @describe: �ض����һ��ɫreSetColor
	 * @param normalCol:  ������ֵ
	 * @param highCol:  ����ɫ��ֵ
	 * @param lowCol:  ����ɫ��ֵ
	 * @date:2010-1-22
	 */
	private static void reSetColor(WritableWorkbook workbook,String normalCol,String highCol, String lowCol){
		int r=0,g=0,b=0;
		String colorstr = "";
		
		// �ض�����ɫֵ- ��ɫ,������ʾ������ֵ
		//colorstr = CommonDataUnit.getSysArgsByKey(BeanForSysArgs.NORMAL_COLORDEF);
		colorstr = normalCol.substring(1);// ȥ��#��.��:#FF00FF
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
		
        // �ض�����ɫֵ - ��ɫ,������ʾ�������޵�ֵ
		//colorstr = CommonDataUnit.getSysArgsByKey(BeanForSysArgs.HIGH_COLORDEF);
		colorstr = highCol.substring(1);// ȥ��#��.��:#FF00FF
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
		
		// �ض�����ɫֵ- ��ɫ2,������ʾ�������޵�ֵ
		//colorstr = CommonDataUnit.getSysArgsByKey(BeanForSysArgs.LOW_COLORDEF);
		colorstr = lowCol.substring(1);// ȥ��#��.��:#FF00FF
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
    * ��ʼ���������  
    * @param sheet  
    */  
   private static void initialSheetSetting(WritableSheet sheet){   
      try{   
           //sheet.getSettings().setProtected(true); //����xls�ı�������Ԫ��Ϊֻ����   
           sheet.getSettings().setDefaultColumnWidth(20); //�����е�Ĭ�Ͽ��   
           //sheet.setRowView(2,false);//�и��Զ���չ    
           //setRowView(int row, int height);--�и�    
           //setColumnView(int  col,int width); --�п�   
           sheet.setColumnView(0,25);//���õ�һ�п��
      }catch(Exception e){   
          e.printStackTrace();   
      }   
   }   
      
   /**  
    * ���뵥Ԫ������  
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
               //setCellComments(labelDT, "���Ǹ������������˵����");   
           }else{   
               Label label = new Label(col,row,data.toString(),format);   
               sheet.addCell(label);                  
           }   
       }catch(Exception e){   
           e.printStackTrace();   
       }   
  
  }   
      
   /**  
    * �ϲ���Ԫ�񣬲���������  
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
          sheet.mergeCells(col_start, row_start, col_end, row_end);//���Ͻǵ����½�   
          insertOneCellData(sheet, col_start, row_start, data, format);   
      }catch(Exception e){   
          e.printStackTrace();   
      }   
  
   }  
   
   //  �����ø�ʽ -- ����+12+��ɫ����+����ɫ����+��б�� - ��һ������
   private static WritableCellFormat getHeadCellFormatA(CellType type){
	   return getDataCellFormat(type, true, 12, Colour.BLACK, Colour.ICE_BLUE, false);
   }
   //  �����ø�ʽ -- ����+12+��ɫ����+����ɫ����+��б�� - ��������
   private static WritableCellFormat getHeadCellFormatB(CellType type){
	   return getDataCellFormat(type, true, 12, Colour.BLACK, Colour.BLUE_GREY, false);
   }
   // �������ݸ�ʽ -- �Ǵ���+10+��ɫ(���ض���)����+��ɫ����+��б��
   private static WritableCellFormat getNormalCellFormat(CellType type){
	   return getDataCellFormat(type, false, 10, Colour.GREEN, Colour.WHITE, false);
   }
   // �쳣���ݸ�ʽ-�������� -- �Ǵ���+10+��ɫ(���ض���)����+��ɫ����+б��
   private static WritableCellFormat getHighCellFormat(CellType type){
	   return getDataCellFormat(type, false, 10, Colour.RED, Colour.WHITE, true);
   }
   // �쳣���ݸ�ʽ-�������� -- �Ǵ���+10+��ɫ(���ض���)����+��ɫ����+б��
   private static WritableCellFormat getLowCellFormat(CellType type){
	   return getDataCellFormat(type, false, 10, Colour.BLUE2, Colour.WHITE, true);
   }
   // ʱ������
   private static WritableCellFormat getDataCellFormat(CellType type){
	   return getDataCellFormat(type, false, 10, Colour.BLACK, Colour.WHITE, false);
   }
   
   /**
	 * @describe: �õ����ݸ�ʽ 
	 * @param type ��������
	 * @param boldType �Ƿ����[true:���� false:���Ǵ���]
	 * @param fontSize �����С
	 * @param fontColor ������ɫ
	 * @param backColor ������ɫ
	 * @param italic ���һ��Ϊ�Ƿ�б��
	 */
   private static WritableCellFormat getDataCellFormat(CellType type,boolean boldType,
		   int fontSize, Colour fontColor, Colour backColor, boolean italic){   
       WritableCellFormat wcf = null;   
       try {   
           //������ʽ   
           if(type == CellType.NUMBER || type == CellType.NUMBER_FORMULA){//����   
              NumberFormat nf = new NumberFormat("#.00");   
              wcf = new WritableCellFormat(nf);    
           }else if(type == CellType.DATE || type == CellType.DATE_FORMULA){//����   
        	   jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-MM-dd hh:mm:ss");    
               //jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-MM-dd hh:mm");    
               wcf = new jxl.write.WritableCellFormat(df);    
           }else{   
               WritableFont wf = new WritableFont(WritableFont.TIMES, fontSize, 
            		   boldType?WritableFont.BOLD:WritableFont.NO_BOLD,italic);//���һ��Ϊ�Ƿ�italic
               wf.setColour(fontColor);
               wcf = new WritableCellFormat(wf);   
           }   
           //���뷽ʽ   
           wcf.setAlignment(Alignment.CENTRE);   
           wcf.setVerticalAlignment(VerticalAlignment.CENTRE);   
           //�߿�   
           wcf.setBorder(Border.LEFT,BorderLineStyle.THIN);   
           wcf.setBorder(Border.BOTTOM,BorderLineStyle.THIN);   
           wcf.setBorder(Border.RIGHT,BorderLineStyle.THIN);   
           //����ɫ   
           //wcf.setBackground(Colour.WHITE);   
           wcf.setBackground(backColor);   
           wcf.setWrap(true);//�Զ�����   
              
       } catch (WriteException e) {   
        e.printStackTrace();   
       }   
       return wcf;   
   }    
   
   // Ŀǰû���õ�����ķ���
   /**  
    * ���빫ʽ  
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
    * ����һ������  
    * @param sheet       ������  
    * @param row         �к�  
    * @param content     ����  
    * @param format      ���  
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
    * �õ����ݱ�ͷ��ʽ  
    * @return  
    */  
   private static WritableCellFormat getTitleCellFormat(){   
       WritableCellFormat wcf = null;   
       try {   
           //������ʽ   
           WritableFont wf = new WritableFont(WritableFont.TIMES, 12, WritableFont.NO_BOLD, false);//���һ��Ϊ�Ƿ�italic   
           wf.setColour(Colour.RED);   
           wcf = new WritableCellFormat(wf);   
           //���뷽ʽ   
           wcf.setAlignment(Alignment.CENTRE);   
           wcf.setVerticalAlignment(VerticalAlignment.CENTRE);   
           //�߿�   
           wcf.setBorder(Border.ALL,BorderLineStyle.THIN);   
              
           //����ɫ   
           wcf.setBackground(Colour.GREY_25_PERCENT);   
       } catch (WriteException e) {   
        e.printStackTrace();   
       }   
       return wcf;
   }    
   
   /**  
    * ����Ԫ���ע��  
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
   * ��ȡexcel  
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
      for (int rowIndex = 0; rowIndex < sheet.getRows(); rowIndex++) {//��   
       for (int colIndex = 0; colIndex < sheet.getColumns(); colIndex++) {//��   
           cell = sheet.getCell(colIndex, rowIndex);   
           //System.out.println(cell.getContents());   
           list.add(cell.getContents());   
       }   
      }   
      book.close();   
  
      return list;   
   }   
  
   /**  
    * ���ļ�����  
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
	 * ����Excel�ļ�  -- ����
	 * @param path         �ļ�·��  
	 * @param sheetName    ����������  
	 * @param dataTitles   ���ݱ���  
	 */  
  public static void createExcelFile(String path,String sheetName,String[] dataTitles){   
      WritableWorkbook workbook;   
      try{   
          OutputStream os=new FileOutputStream(path);    
          workbook=Workbook.createWorkbook(os);    
 
          WritableSheet sheet = workbook.createSheet(sheetName, 0); //��ӵ�һ��������   
          initialSheetSetting(sheet);   
             
          Label label;   
          for (int i=0; i<dataTitles.length; i++){   
              // Label(�к�,�к�,����,���)   
              label = new Label(i, 0, dataTitles[i],getTitleCellFormat());    
              sheet.addCell(label);    
          }   
 
          // ����һ��   
          insertRowData(sheet,1,new String[]{"200201001","����","100","60","100","260"},getDataCellFormat(CellType.STRING_FORMULA));   
             
          // һ��һ��������   
          label = new Label(0, 2,"200201002",getDataCellFormat(CellType.STRING_FORMULA));    
          sheet.addCell(label);   
             
          label = new Label(1, 2,"����",getDataCellFormat(CellType.STRING_FORMULA));    
          sheet.addCell(label);   
             
          insertOneCellData(sheet,2,2,70.5,getDataCellFormat(CellType.NUMBER));   
          insertOneCellData(sheet,3,2,90.523,getDataCellFormat(CellType.NUMBER));   
          insertOneCellData(sheet,4,2,60.5,getDataCellFormat(CellType.NUMBER));   
 
          insertFormula(sheet,5,2,"C3+D3+E3",getDataCellFormat(CellType.NUMBER_FORMULA));   
             
          // ��������   
          mergeCellsAndInsertData(sheet, 0, 3, 5, 3, new Date(), getDataCellFormat(CellType.DATE));   
          // д�����ݲ��ر��ļ� 
          workbook.write();    
          workbook.close();   
      }catch(Exception e){   
          e.printStackTrace();   
      }   
  }      
      
//   public static void main(String[] args){   
//       String[] titles = {"ѧ��","����","����","��ѧ","Ӣ��","�ܷ�"};    
//       JxlUnit jxl = new JxlUnit();   
//       String filePath = "E:/test.xls";
//       jxl.createExcelFile(filePath,"�ɼ���",titles);   
//       System.out.println("end...");
//       //jxl.readDataFromExcel(new File(filePath),0);   
//       //jxl.openExcel("C:/Program Files/Microsoft Office/OFFICE11/EXCEL.EXE",filePath);   
//   } 	

}
