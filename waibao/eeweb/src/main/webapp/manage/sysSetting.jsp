<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>

	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/iColorPicker.packed.js"></script>
	<script type="text/javascript" src="../js/jquery.jmp3.js"></script>
	<script type="text/javascript" src="../js/page/syssetting.js"></script>
	<style type="text/css">
		.showFont{color: #5e5ca7; font-weight: bold; font-size: 14px;}
	</style>	
	
	<body>
	
		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		
		<%-- div容器 start  --%>
		<div class="main_content1" style="margin-top:10px;">
			<div align="left" style="padding-left:20px; font-weight:bold;">系统操作--[系统设置]</div>
			<div class="user_list">
			<s:form namespace="/mana" theme="simple" validate="ture" id="sysArgsForm" name="sysArgsForm" cssClass="formOutLine">
			<s:hidden name="oldRecTime"></s:hidden>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
				<tr>
			    <td valign="top" width="28%">
		    	<div class="box-line-r">
			    	<!-- 基本属性 start -->
				    <table width="100%" cellspacing="0" cellpadding="0">
				      <tr>
				      	<td class="table-tr-top"><div align="center">基本属性</div></td>
				      </tr>
				      <tr>
				        <td>
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp" style="margin-left: 10px;">
									  <tr>
									  	<td>
									  		<strong><label>名称</label></strong>
												<s:if test="#session.userpowertag == 86">
													<s:textfield name="sysArgs['company_name']" id="company_name" size="24"></s:textfield>
												</s:if>
												<s:else>
													<label class="showFont"><s:property value="sysArgs['company_name']"/></label>
													<s:hidden name="sysArgs['company_name']" id="company_name"></s:hidden>
													<s:hidden name="sysArgs['baudrate_number']"></s:hidden>
													<s:hidden name="sysArgs['data_flashtime']"></s:hidden>
													<s:hidden name="sysArgs['temp_show_type']"></s:hidden>
													<s:hidden name="sysArgs['seri_retry_time']" id="seri_retry_time"></s:hidden>
													<s:hidden name="sysArgs['seri_addition_time']" id="seri_addition_time"></s:hidden>
													<s:hidden name="showGroup['doaccess']"></s:hidden>
													<s:hidden name="showGroup['msssage']"></s:hidden>
													<s:hidden name="sysArgs['sms_center_number']" id="sms_center_number"></s:hidden>
													<s:hidden name="sysArgs['sms_store_type']" ></s:hidden>
												</s:else>							    		        
									  	</td>
									  </tr>		
									  <s:if test="#session.userpowertag == 86">
										  <tr>
										  	<td><strong><label>串口波特率</label></strong>
										    	<s:select list="baudRatelList" name="sysArgs['baudrate_number']" listKey="key" listValue="value"/>	        
										  	</td>
										  </tr>		
										  <tr>
										  	<td><strong><label>串口数据通讯周期</label></strong>
										    	<s:select list="flashTimeList" name="sysArgs['data_flashtime']" listKey="key" listValue="value"/>	        
										  	</td>
										  </tr>		
										  <tr>
										  	<td><strong><label>温度显示格式</label></strong>
										    	<s:radio list="tempshowSel" name="sysArgs['temp_show_type']"></s:radio>	        
										  	</td>
										  </tr>		
										  <tr>
										  	<td>
										  		<strong><label>通信出错重发次数</label></strong>
										  		<s:textfield name="sysArgs['seri_retry_time']" id="seri_retry_time" size="3" maxlength="2"></s:textfield>
										  	</td>
										  </tr>		
										  <tr>
										  	<td>
										  		<strong><label>发送帧附加毫秒数</label></strong>
										  		<s:textfield name="sysArgs['seri_addition_time']" id="seri_addition_time" size="5" maxlength="3"></s:textfield>
										  	</td>
										  </tr>		
									  </s:if>
									  <s:else>
										  <tr>
										  	<td><strong><label>温度显示格式</label></strong>
										    	<label class="showFont"><s:property value="sysArgs['temp_show_type']==1?'摄氏':'华氏'"/></label>	        
										  	</td>
										  </tr>		
									  </s:else>
									  <tr>
									  	<td><strong><label>历史列表备份路径</label></strong>
									    	<s:select list="backuppath" name="sysArgs['backup_path']"/>	        
									  	</td>
									  </tr>		
									  <tr>
									  	<td><strong><label>超出上限时显示的颜色</label></strong>
												<s:textfield  name="sysArgs['high_colordef']" id="syscolora" value="%{sysArgs['high_colordef']}"
													cssStyle="width: 60px;" cssClass="iColorPicker"/>								    		        
									  	</td>
									  </tr>		
									  <tr>
									  	<td><strong><label>正常运行时显示的颜色</label></strong>
												<s:textfield  name="sysArgs['normal_colordef']" id="syscolorb" value="%{sysArgs['normal_colordef']}"
													cssStyle="width: 60px;" cssClass="iColorPicker"/>	
									  	</td>
									  </tr>		
									  <tr>
									  	<td><strong><label>低于下限时显示的颜色</label></strong>
												<s:textfield  name="sysArgs['low_colordef']" id="syscolorc" value="%{sysArgs['low_colordef']}"
													cssStyle="width: 60px;" cssClass="iColorPicker" />									    		        
									  	</td>
									  </tr>
									  <s:if test="#session.userpowertag == 86">
										  <tr class="user_list_01">
										  	<td>
													<label for="accessLabel" style="cursor: pointer">
													<s:checkbox  name="showGroup['doaccess']" id="accessLabel" />启用药监联功能</label>
													<s:if test="showGroup['doaccess']">
														<a href="#" onclick="checkAccess();">检测</a>
													</s:if>							  	
										  	</td>
										  </tr>
											<tr>
												<td><br/>
													<strong>* 药监联网功能,目前应用群体:</strong><br/>
													<strong>①:需要接入浙江省药监局系统的企业</strong>
												</td>
											</tr>		
									  </s:if>
									</table>  		        
								</td>
				      </tr>
				    </table>	  
				    <!-- 基本属性 end -->  
			    </div>
					</td>
				  <td valign="top" width="44%">
				  	<!-- 查询控制数据设置  start -->
				    <table width="100%" cellspacing="0" cellpadding="0" >
				      <tr>
				      	<td class="table-tr-top"><div align="center">查询控制数据设置</div></td>
				      </tr>
				      <tr>
				        <td>
							  	<!-- 及时数据 start -->
									<table width="100%" border="5" align="center" cellpadding="0" cellspacing="1" 
										class="table_warp" style="text-align: center;">
									  <tr>
									  	<td width="80px" >&nbsp;</td>
									  	<td width="" >点数[点]</td>
									  	<td >数据范围</td>
									  </tr>							
									  <s:if test="#session.userpowertag == 86">
										  <tr>
										  	<td>总览数据</td>
										  	<td>
														<s:textfield name="sysArgs['max_select_count_bar']" id="max_select_count_bar" size="5" maxlength="3"></s:textfield>
												</td>
										  	<td>1[条]</td>
										  </tr>							  
									  
										  <tr>
										  	<td>即时曲线</td>
										  	<td>
														<s:textfield name="sysArgs['max_select_count_line']" id="max_select_count_line" size="5" maxlength="5"/>
												</td>
										  	<td>
														<s:textfield name="sysArgs['max_rs_count_line']" id="max_rs_count_line" size="5" maxlength="5"/>[条]							  	
												</td>
										  </tr>		
										  				
										  <tr>
										  	<td>报警数据</td>
										  	<td>
													<s:textfield name="sysArgs['max_select_count_alarm']" id="max_select_count_alarm" size="5" maxlength="5"></s:textfield>
										  	</td>
										  	<td>
														<s:textfield name="sysArgs['max_rs_count_alarm']" id="max_rs_count_alarm" size="5" maxlength="5"></s:textfield>[年]							  	
												</td>
										  </tr>								  					  
									  </s:if>
									  <s:else>
										  <tr>
										  	<td>总览数据</td>
										  	<td>
														<label class="showFont"><s:property value="sysArgs['max_select_count_bar']"/></label>
														<s:hidden name="sysArgs['max_select_count_bar']" id="max_select_count_bar"></s:hidden>
												</td>
										  	<td><label class="showFont">1</label>[条]</td>
										  </tr>									  
										  <tr>
										  	<td>即时曲线</td>
										  	<td>
														<label class="showFont"><s:property value="sysArgs['max_select_count_line']"/></label>
														<s:hidden name="sysArgs['max_select_count_line']" id="max_select_count_line"></s:hidden>							
												</td>
										  	<td>
														<label class="showFont"><s:property value="sysArgs['max_rs_count_line']"/></label>
														<s:hidden name="sysArgs['max_rs_count_line']" id="max_rs_count_line"></s:hidden>[条]							  	
												</td>
										  </tr>
										  
										  <tr>
										  	<td>报警数据</td>
										  	<td>
														<label class="showFont"><s:property value="sysArgs['max_select_count_alarm']"/></label>
														<s:hidden name="sysArgs['max_select_count_alarm']" id="max_select_count_alarm"></s:hidden>										
										  	</td>
										  	<td>
														<label class="showFont"><s:property value="sysArgs['max_rs_count_alarm']"/></label>
														<s:hidden name="sysArgs['max_rs_count_alarm']" id="max_rs_count_alarm"></s:hidden>[年]							  	
												</td>
										  </tr>								  							  
									  </s:else>
									</table>
								
									<!-- 历史数据 -->
									<table width="100%" border="5" align="center" cellpadding="0" cellspacing="1" 
										class="table_warp" style="text-align: center;">
									  <tr>
									  	<td width="80px">&nbsp;</td>
									  	<td width="">点数[点]</td>
									  	<td width="">流水范围[倍]</td>
									  	<td width="">日报范围[天]</td>
									  	<td >月报范围[月]</td>
									  </tr>	
										<s:if test="#session.userpowertag == 86">
										  <tr>
										  	<td>历史数据</td>
										  	<td>
										  		<s:textfield name="sysArgs['max_select_count_table']" id ="max_select_count_table" size="5" maxlength="5"></s:textfield>
										  	</td>
										  	<td>
										  		<s:textfield name="sysArgs['max_rs_count_table_now']" id="max_rs_count_table_now" size="5" maxlength="5"></s:textfield>
										  	</td>
										  	<td>
										  		<s:textfield name="sysArgs['max_rs_count_table_day']" id ="max_rs_count_table_day" size="5" maxlength="5"></s:textfield>
										  	</td>
										  	<td>
										  		<s:textfield name="sysArgs['max_rs_count_table_month']" id ="max_rs_count_table_month" size="5" maxlength="5"></s:textfield>
										  	</td>								  	
										  </tr>
										  
										  <tr>
										  	<td>历史曲线</td>
										  	<td>
										  		<s:textfield name="sysArgs['max_select_count_flash']" id ="max_select_count_flash" size="5" maxlength="5"></s:textfield>
										  	</td>
										  	<td>
										  		<s:textfield name="sysArgs['max_rs_count_flash_now']" id="max_rs_count_flash_now" size="5" maxlength="5"></s:textfield>
										  	</td>
										  	<td>
										  		<s:textfield name="sysArgs['max_rs_count_flash_day']" id ="max_rs_count_flash_day" size="5" maxlength="5"></s:textfield>
										  	</td>
										  	<td>
										  		<s:textfield name="sysArgs['max_rs_count_flash_month']" id ="max_rs_count_flash_month" size="5" maxlength="5"></s:textfield>
										  	</td>								  	
										  </tr>								  
										</s:if>
										<s:else>
											<tr>
												<td>历史数据</td>
												<td>
													<label class="showFont"><s:property value="sysArgs['max_select_count_table']"/></label>
													<s:hidden name="sysArgs['max_select_count_table']" id="max_select_count_table"></s:hidden>
												</td>
												<td>
													<label class="showFont"><s:property value="sysArgs['max_rs_count_table_now']"/></label>
													<s:hidden name="sysArgs['max_rs_count_table_now']" id="max_rs_count_table_now"></s:hidden>
												</td>
												<td>
													<label class="showFont"><s:property value="sysArgs['max_rs_count_table_day']"/></label>
													<s:hidden name="sysArgs['max_rs_count_table_day']" id="max_rs_count_table_day"></s:hidden>
												</td>
												<td>
													<label class="showFont"><s:property value="sysArgs['max_rs_count_table_month']"/></label>
													<s:hidden name="sysArgs['max_rs_count_table_month']" id="max_rs_count_table_month"></s:hidden>
												</td>										
											</tr>		
		
											<tr>
												<td>历史曲线</td>
												<td>
													<label class="showFont"><s:property value="sysArgs['max_select_count_flash']"/></label>
													<s:hidden name="sysArgs['max_select_count_flash']" id="max_select_count_flash"></s:hidden>
												</td>
												<td>
													<label class="showFont"><s:property value="sysArgs['max_rs_count_flash_now']"/></label>
													<s:hidden name="sysArgs['max_rs_count_flash_now']" id="max_rs_count_flash_now"></s:hidden>
												</td>
												<td>
													<label class="showFont"><s:property value="sysArgs['max_rs_count_flash_day']"/></label>
													<s:hidden name="sysArgs['max_rs_count_flash_day']" id="max_rs_count_flash_day"></s:hidden>
												</td>
												<td>
													<label class="showFont"><s:property value="sysArgs['max_rs_count_flash_month']"/></label>
													<s:hidden name="sysArgs['max_rs_count_flash_month']" id="max_rs_count_flash_month"></s:hidden>
												</td>										
											</tr>																
										</s:else>		
										
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<strong>* 数据范围 : 查询的时间区间</strong><br/>									
									<strong>* 点数 : 仪器列表能选择的最大个数</strong><br/>										
									<strong>* 陪 :设间隔为1,倍数为100,那么有效的查询时间范围为:1*100=100分钟 </strong>									
								</td>
							</tr>
						</table>
				  	<!-- 仪器选择点数,查询结果数控制属性  end -->
				  </td>
				  <td valign="top" width="28%">
				  <div class="box-line-l">
				  	<!-- 报警设置  start -->
				    <table width="100%" border="0" cellspacing="0" cellpadding="0">
				      <tr>
				      	<td class="table-tr-top"><div align="center">报警设置</div></td>
				      </tr>
				      <tr>
				        <td>
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp" style="margin-left: 10px;">
									  <s:if test="#session.userpowertag == 86"> 
										  <tr>
										  	<td>
													<label for="shortmessageLabel" style="cursor: pointer">
													<s:checkbox  name="showGroup['msssage']" id="shortmessageLabel" />启用短息报警功能</label> 	
													<%--<s:hidden name="showGroup['msssage']" value="false"></s:hidden>--%>
										  	</td>
										  </tr>
										  <tr>
										  	<td><strong><label>CNMI类型</label></strong>
										    	<s:select list="cnmitype" name="sysArgs['sms_store_type']" listKey="key" listValue="value"/>	        
										  	</td>
										  </tr>											  
										  <tr>
										  	<td>
													<strong><label>短信中心号码</label></strong>				
													<s:textfield name="sysArgs['sms_center_number']" id="sms_center_number" size="17" maxlength="11"></s:textfield>	  	
										  	</td>
										  </tr>
									  </s:if>
									  <tr>
									  	<td>
												<label for="pcSoundLabel" style="cursor: pointer">
												<s:checkbox  name="showGroup['pcsound']" id="pcSoundLabel" />启用电脑声卡功能</label>							  	
									  	</td>
									  </tr>
									  <tr>
									  <tr>
									  	<td>
												<label>报警时播放文件</label>
												<s:select list="alarmFileSel" id="bgsound" name="sysArgs['alarm_playfile']" listKey="value" listValue="value"
												onchange="changebs(true)"/>									  	
									  	</td>
									  </tr>
									  	<td>
									  		<div id="sounddl" class="mp3"></div>
									  		<%-- 
												<EMBED style="FILTER: gray()" src="${pageContext.request.contextPath}/music/${sysArgs['alarm_playfile']}"
													width="270" height="45" showpositioncontrols="false" showtracker="true" EnableContextMenu="0"
													showaudiocontrols="true" showstatusbar="false" autostart="false" hidden="false" loop="-1"> 
												</EMBED>	 --%>									  		
									  	</td>
									  </tr>
									  <s:if test="#session.userpowertag == 86">
											<tr>
												<td>
													<br/><br/><br/><br/>	
													<strong>* 短息报警功能 ,需要结合以下3点:<br/>
													①: "无线设置->通信列表"<br/>
													②: "管理中心->区域管理->关联短信"<br/>
													③: "启用短息报警功能"
													</strong>									
												</td>
											</tr>
										</s:if>									  
									</table>
								</td>
							</tr>
						</table>		  	
				  	<!-- 报警相关属性  end -->
				  </div>
				  </td>	 
				</tr>
				<tr>
					<td colspan="3" class="user_list_01">
						<div align="center" class="user_list_01" style="font-size:12px; margin-top:10px;margin-right: 10px;">
							<a href="#" onclick="submitForm();"> 保存系统设置 </a>
							<a href="#" onclick="checkSerial();" style="margin-left: 10px;"> 检测系统状态  </a>
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
		<%-- div容器  end  --%>
		</div>
		
	<!-- includtop最外层结束 -->		
	</div>
	
	<script type="text/javascript">showAuto();</script>	
			
	</body>
</html>
