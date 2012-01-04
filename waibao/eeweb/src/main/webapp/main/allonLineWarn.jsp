<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/jquery.jmp3.js"></script>
	<script type="text/javascript" src="../js/page/warn.js"></script>		

	<body>
	<%-- top  --%>
	<s:include value="../common/includetop.jsp" />	
	<s:hidden name="palyFalg" id="palyFalg"/>
	<s:hidden name="alarmPlayFile" id="alarmPlayFile"/>
	<s:hidden name="systemEqType" id="systemEqType"/>
	<s:hidden name="falshTime" id="falshTime"/>	
	
	<div class="main_content_address" style="font-size:12px; line-height:27px; margin-bottom: 10px;">
		<div id="sounddl" class="mp3" style="margin-top:2px; margin-left:2px; float: left;"></div>　
		<strong>[实时报警]</strong>目前的报警数据为:<strong id="rowcountLabel"></strong>条
	</div>	
	
	<div class="main_content1"> 
		<div class="user_list">
			<div class="table-bg">
			<table cellspacing="1" cellpadding="0" width="100%" border="0">
				<thead>
					<tr>
						<td width="" height="25px" class="table-tr-top"><div align="center"><strong>仪器名</strong></div></td>
						<s:if test="systemEqType !=3">
							<td width="" class="table-tr-top"><div align="center"><strong>温度</strong></div></td>
						</s:if>
						<s:if test="systemEqType !=2">
							<td width="" class="table-tr-top"><div align="center"><strong>湿度</strong></div></td>
						</s:if>
						<td width="" class="table-tr-top"><div align="center"><strong>正常范围</strong></div></td>
						<td width="" class="table-tr-top"><div align="center"><strong>首次报警时间</strong></div></td>
						<td width="" class="table-tr-top"><div align="center"><strong>最近报警时间</strong></div></td>
						<td width="120px" class="table-tr-top">&nbsp;</td>
					</tr>					
				</thead>
				<tbody>
				</tbody>
			</table>
			</div>
		</div>
	</div>

	<!-- includtop最外层结束 -->		
	</div>

	<script type="text/javascript">callRun();</script>
	
	</body>	
</html>
