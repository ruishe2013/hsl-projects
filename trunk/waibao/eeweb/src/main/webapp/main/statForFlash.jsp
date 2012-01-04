<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<head>
		<title>统计结果</title>

		<script type="text/javascript" src="../js/swfobject.js"></script>
		<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>

		<style type="text/css">
			body {MARGIN: 0px;PADDING: 0px;}
			.container {border: 0px solid #EEE;	margin: 0;padding: 0px;	background: #EEE;	width: 100%;
			font-family: Arial, Helvetica, sans-serif;font-size: 12px;}
			.tab_container {width:975px; margin:10px auto;border:1px solid #EEE; padding:1px; 
				background: #EEE;	font-family: Arial, Helvetica, sans-serif;	font-size: 12px;}			
		</style>
		
	</head>
	<body>
		
		<div id="showState">正在加载...</div>
		<!-- flash 显示区域 -->
		<div class="container">
			<div id="amlinetemp">
				请更新Flash player版本
			</div>
			<div id="amlinehumi">
				请更新Flash player版本
			</div>
		</div>
		
		<s:hidden id="tempDataStr" name="tempDataStr"></s:hidden>
		<s:hidden id="tempConfigStr" name="tempConfigStr"></s:hidden>
		<s:hidden id="humiDataStr" name="humiDataStr"></s:hidden>
		<s:hidden id="humiConfigStr" name="humiConfigStr"></s:hidden>
		<s:hidden id="systemEqType" name="systemEqType"></s:hidden>

		<script type="text/javascript">
			// <![CDATA[	
			
			var sysEqCount = Number($("#systemEqType").val());
			
			var dataStr = null;
			var configStr = null;
			
			if ( sysEqCount != 3){
				configStr = $("#tempConfigStr").val();
				dataStr = $("#tempDataStr").val();
				var so = new SWFObject("../js/amline.swf", "amline_temp", "100%", "100%",	"8", "#EEEEEE");
				so.addVariable("path", "../js/");//--显示官方图标.这样就没有侵权了
				so.addVariable("chart_id", "amline_temp");
				so.addVariable("chart_data", encodeURIComponent(dataStr));
				so.addVariable("chart_settings", encodeURIComponent(configStr));
				so.addVariable("loading_data", "正在加载...");
				so.write("amlinetemp");
			  if (sysEqCount == 2){$("#amlinehumi").text("");}				
			}
		
			if ( sysEqCount != 2){
				dataStr = $("#humiDataStr").val();
				configStr = $("#humiConfigStr").val();
				so = new SWFObject("../js/amline.swf", "amline_humi", "100%", "100%", "8", "#EEEEEE");
				so.addVariable("path", "../js/");
				so.addVariable("chart_id", "amline_humi");
				so.addVariable("chart_data", encodeURIComponent(dataStr));
				so.addVariable("chart_settings", encodeURIComponent(configStr));
				so.addVariable("loading_data", "正在加载...");
				so.write("amlinehumi");
				if (sysEqCount==3){$("#amlinetemp").text("");}
			}
			// ]]>
		</script>
		
		<script type="text/javascript">
			$("#showState").hide();
		</script>		
		
	</body>
</html>