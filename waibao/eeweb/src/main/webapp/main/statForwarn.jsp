<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
		<title>统计结果</title>
		<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
		<script type="text/javascript" src="../queryUI/jquery.jPrintArea.js"></script>
		<link rel="StyleSheet" href="../css/xls.css" type="text/css"	media="screen" />		
		
		<script type="text/javascript">
			$(function() {
				//交替显示行
				$('tr:odd').toggleClass('table-tr-odd');
				$('tr:even').toggleClass('table-tr-even');
			});				
			
		function  submitform(){
			$("#myPrintArea").clone().prependTo("#datatodisplay");
			$("#tablecontent").val($("#datatodisplay").html());
			$("#datatodisplay").html("");
			document.excleform.action = '${pageContext.request.contextPath}/userdo/htcexcel.action';
			document.excleform.submit();
		}
		
		function togglebutton(){
			$("#tools").toggle();
		}		
		</script>
		
</head>
	
	<body>
	<div id="showState"></div>
	
	<script type="text/javascript">
		$("#showState").text("正在加载...");
	</script>			
	
	
	<s:form namespace="/userdo" name="excleform" theme="simple">
		<s:hidden name="tablecontent" id="tablecontent"></s:hidden>
		<div id="datatodisplay"></div>
	</s:form>		
					 
	<div class="tip_tools" id="tools">					 
		<input class="btn1" type="button" value="打印"  onclick="recordPrint();" />
		<input class="btn1" type="button" value="导出excel"	onclick="submitform();" />
		导出可能需要一段时间,请耐心等待...
	</div>
		
	<div align="center" id="myPrintArea">
		<div class="tip_div_a">
			<label onclick="togglebutton();"><s:property value="headTitle"/></label>
		</div>
		<div class="tip_div">
			<label><s:property value="headDetail" escape="false"/></label>
		</div>
		<table id="myTable" class="compact_tb" cellpadding="0" cellspacing="0" border="0">
			<tr class="top" style="text-align: center;">
				<td>NO</td>
				<td>仪器名	</td>
				<s:if test="systemEqType !=3">
					<td>温度	</td>
				</s:if>
				<s:if test="systemEqType !=2">
					<td>湿度	</td>
				</s:if>
				<td>正常区域	</td>
				<td>是否复位</td>
				<td>报警时间	</td>
				<td>复位时间</td>
				<td>复位人员</td>
			</tr>	
			<s:iterator value="alarmRecList" status="st" id="lists">
				<tr>
					<td>
						<s:property value="#st.count" />
					</td>
					<td>
						<s:property value="placeName"/>
					</td>
					<s:if test="systemEqType !=3">
						<td>
							<s:if test="equitype!=3">
								<s:property	value="temperature" />
								<%--<s:property value="getRealTemperature(temperature,tempshowType)"/>
								<s:property value="tempshowType == 1?'℃':'F'"/>
								<s:property	value="getAlarmType(alarmtype,1)" />--%>
							</s:if>
							<s:else>-</s:else>
						</td>
					</s:if>
					<s:if test="systemEqType !=2">
						<td>
							<s:if test="equitype!=2">
								<s:property	value="humidity" />
								<%--<s:property value="humidity"/>%RH
								<s:property	value="getAlarmType(alarmtype,2)" />--%>
							</s:if>
							<s:else>-</s:else>					
						</td>
					</s:if>
					<td>
						<s:property	value="normalArea" />
						<%--<s:property	value="getAreaString(equipmentId)" />--%>
					</td>
					<td>
						<s:property value="state==1?'已':'未'"/>复位
					</td>
					<td>
						<s:property	value="getDateStrFromMills(alarmStart)" />
						<%--<s:date name="alarmStart" format="yyyy-MM-dd HH:mm:ss" />--%>
					</td>
					<td>
						<s:if test="state == 1">
							<s:property	value="getDateStrFromMills(alarmEnd)" />
						</s:if>
						<s:if test="state == 2">暂无</s:if>
					</td>
					<td>
						<s:property value="username"/>
					</td>
				</tr>		
			</s:iterator>	
		</table>
	</div>
	
	<script type="text/javascript">
		$("#showState").hide();
		function recordPrint(){
			$("div#myPrintArea").printArea(); 
		}	
	</script>		
	
	</body>
</html>
