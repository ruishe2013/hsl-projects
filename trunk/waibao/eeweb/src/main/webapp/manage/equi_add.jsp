<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/page/equi.js"></script>

	<style type="text/css">
		#content_remark {margin:0px; height:50px;width:400px;overflow:auto;} 
	</style>

	<body>
	
		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		<input type="hidden" value="add" id="pageId"/>		
		
		<%-- 增加设备  --%>
		<div class="main_content1">
		  <div align="left" style="padding-left:20px; font-weight:bold;">设备管理--[新建]</div>
		  <div class="user_list">
			<s:form namespace="/mana" theme="simple" validate="ture" name="addequiForm" cssClass="formOutLine">
				<s:hidden name="currentPage"/>
				<!-- 需要输入的字段 -->		  
		    <table width="100%" border="0" cellspacing="0" cellpadding="0">
		      <tr>
		      	<td class="table-tr-top"><div align="center">新建仪器</div></td>
		      </tr>
		      <tr>
		        <td>
							<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp">
							  <tr>
							    <td class="table_left"><strong><label>仪器编号</label></strong></td>
							    <td class="table_right">
							    	<s:textfield name="equipData.dsrsn" id="equipData_dsrsn" size="20" maxlength="20"/>
						  			<s:if test="showZj==2">
							  			(对应药监局下发地址)
						  			</s:if>							    		
							    </td>
							  </tr>							
							  <tr>
							    <td class="table_left"><strong><label>仪器地址</label></strong></td>
							    <td class="table_right">
							    	<s:textfield name="equipData.address" id="equipData_address" size="4" maxlength="4"/>(仪器地址不能重复)	
							    </td>
							  </tr>
							  <tr>
							    <td class="table_left"><strong><label>类型</label></strong></td>
							    <td class="table_right">
							    	<s:select list="equiTypes" name="equipData.equitype" listKey="key" 
											listValue="value" value="equipData.equitype" onchange="changeType(this,0)"/>
									</td>
							  </tr>
							  <tr>
							    <td class="table_left"><strong><label>串口发送位数</label></strong></td>
							    <td class="table_right">
							  		<s:select list="conndatas" name="equipData.conndata" listKey="key" id="eqtype"
							  			listValue="value" value="equipData.conndata"/>											
									</td>
							  </tr>
							  <%--
							  <tr>
							    <td class="table_left"><strong>电量值(v)</strong></td>
							    <td class="table_right"><s:select list="powerValues" name="equipData.powerType" listKey="key"
							     			listValue="value" value="equipData.powerType"/>
							    </td>
							  </tr>	
							  --%>			  
							  <tr>
							    <td class="table_left"><strong><label>区域名</label></strong></td>
							    <td class="table_right"><s:select list="areaMap" name="equipData.placeId" listKey="key"	listValue="value"/></td>
							  </tr>
							  <tr>
							    <td class="table_left"><strong><label>标注</label></strong></td>
							    <td class="table_right">
										<s:textfield name="equipData.mark" id="equipData_mark" size="26" maxlength="13"/>							    
							    	(同一个区域内,标注不能重复)</td>
							  </tr>
							  <tr class="temp_show">
							    <td class="table_left"><strong><label>温度范围</label></strong></td>
							    <td class="table_right">
									  <s:textfield name="tempDown" id="tempDown" size="6" maxlength="6"/>~
										<s:textfield name="tempUp" id="tempUp" size="6" maxlength="6"/>
										<s:if test="#session.userpowertag == 86">
											偏差<s:textfield name="tempDev" id="tempDev" size="5" maxlength="5"/>
										</s:if>
										<s:else>
											<s:hidden name="tempDev" id="tempDev" value="0"/>
										</s:else>
										<s:property value="tempshowType == 1?'℃':'F'"/>
										<s:if test="#session.userpowertag == 86">
											(显示为华氏时,偏差需要换成摄氏)
										</s:if>		    
									</td>
							  </tr>
							  <tr class="humi_show">
							    <td class="table_left"><strong>湿度范围</strong></td>
							    <td class="table_right">
										<s:textfield name="humiDown" id="humiDown" size="6" maxlength="6"/>~
										<s:textfield name="humiUp" id="humiUp" size="6" maxlength="6"/>
										<s:if test="#session.userpowertag == 86">
											偏差	<s:textfield name="humiDev" id="humiDev" size="5" maxlength="5"/>
										</s:if>
										<s:else>
											<s:hidden name="humiDev" id="humiDev" value="0"/>
										</s:else>
										%RH	
							  	</td>
							  </tr>
							  <tr>
							    <td class="table_left"><strong>电量信息</strong></td>
							    <td class="table_right">
								 		<s:select list="powerShows" name="equipData.showPower" listKey="key" 
								 			listValue="value" value="equipData.showPower"/>(在总览页面中,是否显示电量信息)		
							  	</td>
							  </tr>
							  <s:if test="showZj==2">
								  <tr>
								    <td class="table_left"><strong>Access状态</strong></td>
								    <td class="table_right">
									 		<s:select list="powerShows" name="equipData.showAccess" listKey="key" 
									 			listValue="value" value="equipData.showAccess"/>(是否需要把数据显示到Access中)		
								  	</td>
								  </tr>
							  </s:if>
							  <s:else>
							  	<s:hidden name="equipData.showAccess" value="0"/>
							  </s:else>
							  <tr>
							    <td class="table_left"><strong>备注</strong></td>
							    <td class="table_right"><s:textarea name="equipData.remark" id="content_remark" /></td>
							  </tr>
							</table>
						</td>
		      </tr>
		      <tr>
		        <td class="user_list_01">
		        	<div align="center" class="table_buttom">
		        		<a href="#" onclick="submitAdd(1);">增加仪器</a>
		        		<a href="#" onclick="history.go(-1);return false;">返回</a> 
		        	</div>
		        </td>
		      </tr>
		    </table>
		  </s:form>
		  <!-- 错误显示区 -->
		  <div class="div_error" id="errorArea" 
		  		style="display: <s:property value="showTipMsg==0?'none':'block'"/>">
					<s:include value="../common/error.jsp"></s:include>
		  </div>				  
		</div>
	</div>
		
	<!-- includtop最外层结束 -->		
	</div>
	
	

	</body>
</html>
