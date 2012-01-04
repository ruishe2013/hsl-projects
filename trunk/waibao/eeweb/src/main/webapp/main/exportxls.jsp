<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="application/vnd.ms-excel;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>导出excel</title> 
		<%@ taglib prefix="s" uri="/struts-tags"%>
		<style type="text/css"> 
			.lowdata {color: red;font-weight:bold;font-style: oblique;} 
			.normaldata {color: blue;font-weight:bold;font-style: oblique;}
			.highdata {color: fuchsia;font-weight:bold;font-style: oblique;}
			
			*{margin:0; padding:0; list-style:none; color:#154567;}  
			body{font-size:12px; font-family:'宋体',Arial, Helvetica, sans-serif; color:#154567;padding:0; margin-left:1px;}
			/*表格*/
			.top{background:#e0ebfc; color:#24497d; text-align:left; padding:0 5px; line-height:25px; border:1px #3673cc solid; 
				border-width:0px 1px 1px 0px;font-weight:bold; line-height:20px; background:#e0ebfc;}
			.compact_tb {border:1px #3673cc solid; border-width:1px 1px 1px 1px; margin-bottom:2px;}
			.compact_tb td {padding:0 5px; line-height:25px;  border:1px #3673cc solid; border-width:0px 1px 1px 0px;}
			.no_data_div {font-size:36px; padding:100px; width:400px; margin:100px auto; text-align:center;
				border:3px #3673cc solid; background:#e0ebfc}	
			.tip_div_a {font-weight: bold; text-align: center; margin-top: 10px;padding-bottom: 10px;border:0px; font-size: 20px;}	
			.tip_div {font-weight: bold; text-align: center; margin-bottom: 5px;padding: 3px;border:0px;font-size: 13px;}					
			.tip_div label{text-align:center;margin-left: 30px;}
		</style>		
  </head>
  <body>
  	<s:property value="tablecontent" escape="false"/>
  </body>
</html>
