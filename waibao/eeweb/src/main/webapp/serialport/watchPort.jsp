<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/page/serial.js"></script>	

	<body>

		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		
		
		<%-- 串口调试  --%>
		<div class="main_content1">
		  <div align="left" style="padding-left:20px; font-weight:bold;">串口管理--[串口调试]</div>
		  <div class="user_list">
		  	<s:form namespace="/main" theme="simple" validate="ture" name="serialFrom">
		  		<s:hidden name="runFlag" id="runFlag"></s:hidden>
			    <table width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr>
			      	<td class="table-tr-top"><div align="center">串口调试</div></td>
			      </tr>
			      <tr>
			        <td align="center" style="padding-top: 20px; padding-bottom: 20px;">
							<input type="button" id="sendbut" class="button_no_margin" 
								value="<s:property value="stateStr"/>" onclick="openOrClose();"
								style="font-size: 40px;height:100px;width:700px;padding: 30px;"/>		        
							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>	

		<!-- includtop最外层结束 -->		
		</div>

	</body>
</html>
