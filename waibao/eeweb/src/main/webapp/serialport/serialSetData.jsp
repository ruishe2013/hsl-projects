<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>	
	<script type="text/javascript" src="../js/page/serial.js"></script>	

	<body>
		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		
		<%-- div容器 start  --%>
		<div class="main_content1" style="margin-top:10px;">
			<div align="left" style="padding-left:20px; font-weight:bold;">串口管理--[串口设置]</div>
			<div class="user_list">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
				<tr>
			    <td valign="top" width="50%">
			    <div class="box-line-r">
			    	<!-- 设置串口(初始化串口和测试通讯) -->
				    <table width="100%" cellspacing="0" cellpadding="0" style="padding-bottom: 6px;">
				      <tr>
				      	<td class="table-tr-top"><div align="center">设置串口(初始化串口和测试通讯)</div></td>
				      </tr>
				      <tr>
				        <td>
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp" style="margin-left: 10px;">
									  <tr>
									  	<td class="user_list_01">
									  		<s:form namespace="/seri" theme="simple" validate="ture" name="configInitForm" cssClass="formOutLine" >
									  		<strong><label>[波特率]</label></strong>
									  		<s:select list="baudRatelList" name="baudRateNo" value="baudRateNo" listKey="key" listValue="value"/>
									  		<a href="#" onclick="setSerial(0);">串口号测试</a>
									  		</s:form>
									  	</td>
									  </tr>
									</table>
								</td>
							</tr>
						</table>
					</div>
					</td>
			    <td valign="top" width="50%">
			    	<!-- 校准仪器时间 -->
				    <table width="100%" cellspacing="0" cellpadding="0" style="padding-bottom: 6px;">
				      <tr>
				      	<td class="table-tr-top"><div align="center">校准仪器时间</div></td>
				      </tr>
				      <tr>
				        <td>
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp" style="margin-left: 10px;">
									  <tr>
									  	<td class="user_list_01" align="center"> 
									  		<s:form namespace="/seri" theme="simple" validate="ture" name="configPowerForm">
									  		<a href="#" onclick="setSerial(1);">校验 时间</a>
									  		</s:form>
									  	</td>
									  </tr>
									</table>
								</td>
							</tr>
						</table>
					</td>					
				</tr>
				<tr>
			    <td valign="top" width="50%">
			    <div class="box-line-r">
			    	<!-- 设置机号(BCD)和地址号 -->
				    <table width="100%" cellspacing="0" cellpadding="0" style="padding-bottom: 6px;">
				      <tr>
				      	<td class="table-tr-top"><div align="center">设置机号(BCD)和地址号</div></td>
				      </tr>
				      <tr>
				        <td>
				        	<s:form namespace="/seri" theme="simple" validate="ture" name="configSetForm" cssClass="formOutLine">
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp" style="margin-left: 10px;">
									  <tr>
									  	<td width="25%">
									  		<strong><label>[地址]</label></strong>
									  		<s:textfield name="serialData.address" id="address" size="8" maxlength="4"/>
									  	</td>
									  	<td width="75%">
												<strong><label>[类型号-批次-流水号]</label></strong>
												<s:textfield name="serialData.MCType" id="MCType" size="4" maxlength="4"/>-
												<s:textfield name="serialData.MCOrder" id="MCOrder" size="4" maxlength="4"/>-
												<s:textfield name="serialData.MCNo" id="MCNo" size="4" maxlength="4"/>										  		
									  	</td>
									  </tr>
									  <tr>
									  	<td>
												<label for="agreeAddrsFalg">
													<s:checkbox  name="agreeAddrsFalg" id="agreeAddrsFalg" />同时修改地址
												</label>									  	
									  	</td>
									  	<td>
												<label for="addFlag">
													<s:checkbox  name="addFlag" id="addFlag" />递增流水号和地址
												</label>									  	
									  	</td>
									  </tr>
									  <tr>
									  	<td>
												<label for="showPowerFlag">
													<s:checkbox  name="showPowerFlag" id="showPowerFlag" />显示电压信息
												</label>									  	
									  	</td>
									  	<td>
												<label for="showVision">
													<s:checkbox  name="showVision" id="showVision"  />显示版本号
												</label>									  	
									  	</td>
									  </tr>
									  <tr>
									  	<td colspan="2" class="user_list_01" align="center">
									  		<a href="#" onclick="setSerial(2);">读取 机号 地址</a>
												<a href="#" onclick="setSerial(3);">设置 机号 地址</a>									  		
									  	</td>
									  </tr>
										<tr>
									  	<td colspan="2">
												<strong>* 操作这个功能的时候,请确认串口只连接一个仪器......</strong>
									  	</td>
									  </tr>	
									</table>
									</s:form>
								</td>
							</tr>
						</table>
					</div>
					</td>
			    <td valign="top" width="50%">
			    	<!-- 设置数值和记录属性数据 -->
				    <table width="100%" cellspacing="0" cellpadding="0" style="padding-bottom: 6px;">
				      <tr>
				      	<td class="table-tr-top"><div align="center">设置数值和记录属性数据</div></td>
				      </tr>
				      <tr>
				        <td>
				        	<s:form namespace="/seri" theme="simple" validate="ture" name="configDataForm" cssClass="formOutLine">
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp" style="margin-left: 10px;">
									  <tr>
									  	<td colspan="4" class="user_list_01">
									  		<strong><label>[地址]</label></strong>
									  		<s:textfield name="configaddress" id="configaddress" size="8" maxlength="4"/>
									  		<strong><label>[属性数据]</label></strong>
									  		<s:textfield name="configvalue" id="configvalue" size="10" maxlength="6"/>
									  		<a href="#" onclick="setSerial(4);"> 确认 数据 </a>
									  	</td>
									  </tr>
									  <tr>
									  	<td>
									  		<label for="defaultTemp">
													<input type="radio" name="configId" id="defaultTemp" value="1" checked="checked"/>默认温度
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="defaultHumi">
													<input type="radio" name="configId" id="defaultHumi" value="2"/>默认湿度
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="defaultDew">
													<input type="radio" name="configId" id="defaultDew" value="3"/>默认露点
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="recInterval">
													<input type="radio" name="configId" id="recInterval" value="4"/>记录间隔
												</label>									  	
									  	</td>
									  </tr>
									  <tr>
									  	<td>
									  		<label for="maxtemp">
													<input type="radio" name="configId" id="maxtemp" value="8"/>高温报警
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="maxhumi">
													<input type="radio" name="configId" id="maxhumi" value="6"/>高湿报警
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="maxdew">
													<input type="radio" name="configId" id="maxdew" value="10"/>高露点报警
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="maxStroe">
													<input type="radio" name="configId" id="maxStroe" value="5"/>历史记录容量
												</label>									  	
									  	</td>
									  </tr>
									  <tr>
									  	<td>
									  		<label for="mintemp">
													<input type="radio" name="configId" id="mintemp" value="9"/>低温报警
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="minhumi">
													<input type="radio" name="configId" id="minhumi" value="7"/>低湿报警
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="mindew">
													<input type="radio" name="configId" id="mindew" value="11"/>低露点报警
												</label>									  	
									  	</td>
									  	<td>&nbsp;</td>
									  </tr>									  									  
									</table>
									</s:form>
								</td>
							</tr>
						</table>
					</td>					
				</tr>				
			</table>
			
		  <!-- 错误显示区 -->
		  <div class="div_error" id="errorArea" 
		  		style="display: <s:property value="showTipMsg==0?'none':'block'"/>">
					<s:include value="../common/error.jsp"></s:include>
					
					<s:if test="serialData.powerVal != null">
						<label>电压信息</label>
						<label class="powerLabel" style="margin-right: 200px">
							<s:property value="serialData.powerVal"/>
						</label>
					</s:if>
					<s:if test="serialData.version != null">
						<label>版本号</label>
						<label class="powerLabel">
							<s:property value="serialData.version"/>
						</label>
					</s:if>					
		  </div>				
			
		</div>
		<%-- div容器 end --%>
	</div>		
					
	<!-- includtop最外层结束 -->		
	</div>

	</body>
</html>
