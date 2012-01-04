<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../queryUI/ui/jquery.pager.js"></script>
	<script type="text/javascript" src="../js/page/gprs.js"></script>
	<link rel="stylesheet" href="../queryUI/css/Pager.css" type="text/css" 	media="screen" />

	<style type="text/css">
		#sendcontent {margin:0px; height:70px;width:230px;overflow:auto;}
		.table_left_{padding-right:10px; width: 30%; text-align:right; height: 35px;}
		.table_right_{padding-top:0px; width: 70%;text-align:left;height: 35px;}
		.test_left {padding-left: 10px;}			
	</style>
	
	<body>

		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		<input  type="hidden" value="<s:property value="gprsSetList.size"/>" id="have_data"/>
		
		<%-- div容器 start  --%>
		<div class="main_content1" style="margin-top:10px;">
			<div align="left" style="padding-left:20px; font-weight:bold;">无线设置--[通讯测试]</div>
			<div class="user_list">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
				<tr>
			    <td valign="top" width="50%">
			    <div class="box-line-r">
			    	<%-- 左侧 端口设置 --%>
				    <table width="100%" cellspacing="0" cellpadding="0" border="0">
							<tr>
								<td class="table-tr-top " align="center">
										<strong>连接短信模块</strong>
								</td>
							</tr>
							<s:form namespace="/mana" theme="simple" validate="ture" name="gprsTestForm">
							<s:hidden name="runFlag" id="runFlag"></s:hidden>
							<tr>
								<td class="user_list_01" align="center">
									<br/>
									<input type="button" id="sendbut" class="button_no_margin" 
										value="<s:property value="stateStr"/>" onclick="submitTest(1)"
										style="font-size: 40px;height:120px;width:440px;padding: 30px;"/> 									
								</td>
							</tr>
							<%-- 
							<tr>
				        <td class="user_list_01 test_left">
				        	<div class="table_buttom">
				        		<a href="#" onclick="submitTest(1);"> 连 接 </a>&nbsp;&nbsp;
				        		<a href="#" onclick="submitTest(2);"> 断 开 </a>
				        	</div>
				        </td>
							</tr>
							--%>
							</s:form>
						</table>
					</div>
					</td>
			    <td valign="top" width="50%">
						<div>
			    	<%-- 右侧 短信测试 --%>
			    <table width="100%" cellspacing="0" cellpadding="0" border="0">
							<s:form namespace="/mana" theme="simple" validate="ture" name="gprsSendForm" >
							<tr>
								<td class="table-tr-top" align="center">
										<strong>短信测试</strong>
								</td>
							</tr>
							<tr>
								<td class="test_left">
									<strong>短信中心号</strong>
						  		<s:textfield name="centerNo" id="centerNo" size="20" maxlength="11"/>
								</td>
							</tr>
							<tr>
								<td class="test_left">
									<strong>目标手机号</strong>
						  		<s:textfield name="phoneNo" id="phoneNo" size="20" maxlength="11"/>
								</td>
							</tr>
							<tr>
								<td class="test_left">
									<strong>发送信息内容</strong>
								</td>
							</tr>
							<tr>
								<td class="test_left">
									<s:textarea name="sendcontent" id="sendcontent" cols="25" rows="5" />
								</td>
							</tr>
							<tr>
				        <td class="user_list_01  test_left">
				        	<div class="table_buttom">
				        		<a href="#" onclick="submitTest(3);"> 发 送 </a>&nbsp;&nbsp;
				        		<a href="#" onclick="submitTest(4);"> 清 空 </a>
				        	</div>
				        </td>								
							</tr>
							</s:form>
						</table>			    	
			    	</div>
					</td>					
				</tr>
			</table>
		  <!-- 错误显示区 -->
		  <div class="div_error" id="errorArea" 
		  		style="display: <s:property value="showTipMsg==0?'none':'block'"/>">
					<s:include value="../common/error.jsp"></s:include>
		  </div>							
		</div>
		<%-- div容器 end --%>
	</div>		
					
	<!-- includtop最外层结束 -->		
	</div>
	
	<script type="text/javascript">showList(3);</script>		
	
	</body>
</html>
