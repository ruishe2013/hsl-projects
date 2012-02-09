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
		
		<%-- div���� start  --%>
		<div class="main_content1" style="margin-top:10px;">
			<div align="left" style="padding-left:20px; font-weight:bold;">ϵͳ����--[ϵͳ����]</div>
			<div class="user_list">
			<s:form namespace="/mana" theme="simple" validate="ture" id="sysArgsForm" name="sysArgsForm" cssClass="formOutLine">
			<s:hidden name="oldRecTime"></s:hidden>
			<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
				<tr>
			    <td valign="top" width="28%">
		    	<div class="box-line-r">
			    	<!-- �������� start -->
				    <table width="100%" cellspacing="0" cellpadding="0">
				      <tr>
				      	<td class="table-tr-top"><div align="center">��������</div></td>
				      </tr>
				      <tr>
				        <td>
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp" style="margin-left: 10px;">
									  <tr>
									  	<td>
									  		<strong><label>����</label></strong>
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
									  
									  <tr>
									  	<td><strong><label>���ڲ�����</label></strong>
									    	<s:select list="baudRatelList" name="sysArgs['baudrate_number']" listKey="key" listValue="value"/>	        
									  	</td>
									  </tr>		
									  <tr>
									  	<td><strong><label>��������ͨѶ����</label></strong>
									    	<s:select list="flashTimeList" name="sysArgs['data_flashtime']" listKey="key" listValue="value"/>	        
									  	</td>
									  </tr>		
									 <s:if test="#session.userpowertag == 86">
										  <tr>
										  	<td><strong><label>�¶���ʾ��ʽ</label></strong>
										    	<s:radio list="tempshowSel" name="sysArgs['temp_show_type']"></s:radio>	        
										  	</td>
										  </tr>		
										  <tr>
										  	<td>
										  		<strong><label>ͨ�ų����ط�����</label></strong>
										  		<s:textfield name="sysArgs['seri_retry_time']" id="seri_retry_time" size="3" maxlength="2"></s:textfield>
										  	</td>
										  </tr>		
										  <tr>
										  	<td>
										  		<strong><label>����֡���Ӻ�����</label></strong>
										  		<s:textfield name="sysArgs['seri_addition_time']" id="seri_addition_time" size="5" maxlength="3"></s:textfield>
										  	</td>
										  </tr>	
										  	
									  </s:if>
									  <s:else>
										  <tr>
										  	<td><strong><label>�¶���ʾ��ʽ</label></strong>
										    	<label class="showFont"><s:property value="sysArgs['temp_show_type']==1?'����':'����'"/></label>	        
										  	</td>
										  </tr>		
									  </s:else>
									  <tr>
									  	<td><strong><label>��ʷ�б���·��</label></strong>
									    	<s:select list="backuppath" name="sysArgs['backup_path']"/>	        
									  	</td>
									  </tr>		
									  <tr>
									  	<td><strong><label>��������ʱ��ʾ����ɫ</label></strong>
												<s:textfield  name="sysArgs['high_colordef']" id="syscolora" value="%{sysArgs['high_colordef']}"
													cssStyle="width: 60px;" cssClass="iColorPicker"/>								    		        
									  	</td>
									  </tr>		
									  <tr>
									  	<td><strong><label>��������ʱ��ʾ����ɫ</label></strong>
												<s:textfield  name="sysArgs['normal_colordef']" id="syscolorb" value="%{sysArgs['normal_colordef']}"
													cssStyle="width: 60px;" cssClass="iColorPicker"/>	
									  	</td>
									  </tr>		
									  <tr>
									  	<td><strong><label>��������ʱ��ʾ����ɫ</label></strong>
												<s:textfield  name="sysArgs['low_colordef']" id="syscolorc" value="%{sysArgs['low_colordef']}"
													cssStyle="width: 60px;" cssClass="iColorPicker" />									    		        
									  	</td>
									  </tr>
									  
										<tr>
											<td><br/>
												<div style="color:#f00;">
												<strong></strong><br/>
												<strong>��Ҫ����ʡҩ���ϵͳ����ҵ</strong><br/>
												<strong>����������ϵ���绰 0571-88192051</strong><br/>
												</div>
											</td>
										</tr>		
									  
									</table>  		        
								</td>
				      </tr>
				    </table>	  
				    <!-- �������� end -->  
			    </div>
					</td>
				  <td valign="top" width="44%">
				  	<!-- ��ѯ������������  start -->
				    <table width="100%" cellspacing="0" cellpadding="0" >
				      <tr>
				      	<td class="table-tr-top"><div align="center">��ѯ������������</div></td>
				      </tr>
				      <tr>
				        <td>
							  	<!-- ��ʱ���� start -->
									<table width="100%" border="5" align="center" cellpadding="0" cellspacing="1" 
										class="table_warp" style="text-align: center;">
									  <tr>
									  	<td width="80px" >&nbsp;</td>
									  	<td width="" >����[��]</td>
									  	<td >���ݷ�Χ</td>
									  </tr>							
									  <s:if test="#session.userpowertag == 86">
										  <tr>
										  	<td>��������</td>
										  	<td>
														<s:textfield name="sysArgs['max_select_count_bar']" id="max_select_count_bar" size="5" maxlength="3"></s:textfield>
												</td>
										  	<td>1[��]</td>
										  </tr>							  
									  
										  <tr>
										  	<td>��ʱ����</td>
										  	<td>
														<s:textfield name="sysArgs['max_select_count_line']" id="max_select_count_line" size="5" maxlength="5"/>
												</td>
										  	<td>
														<s:textfield name="sysArgs['max_rs_count_line']" id="max_rs_count_line" size="5" maxlength="5"/>[��]							  	
												</td>
										  </tr>		
										  				
										  <tr>
										  	<td>��������</td>
										  	<td>
													<s:textfield name="sysArgs['max_select_count_alarm']" id="max_select_count_alarm" size="5" maxlength="5"></s:textfield>
										  	</td>
										  	<td>
														<s:textfield name="sysArgs['max_rs_count_alarm']" id="max_rs_count_alarm" size="5" maxlength="5"></s:textfield>[��]							  	
												</td>
										  </tr>								  					  
									  </s:if>
									  <s:else>
										  <tr>
										  	<td>��������</td>
										  	<td>
														<label class="showFont"><s:property value="sysArgs['max_select_count_bar']"/></label>
														<s:hidden name="sysArgs['max_select_count_bar']" id="max_select_count_bar"></s:hidden>
												</td>
										  	<td><label class="showFont">1</label>[��]</td>
										  </tr>									  
										  <tr>
										  	<td>��ʱ����</td>
										  	<td>
														<label class="showFont"><s:property value="sysArgs['max_select_count_line']"/></label>
														<s:hidden name="sysArgs['max_select_count_line']" id="max_select_count_line"></s:hidden>							
												</td>
										  	<td>
														<label class="showFont"><s:property value="sysArgs['max_rs_count_line']"/></label>
														<s:hidden name="sysArgs['max_rs_count_line']" id="max_rs_count_line"></s:hidden>[��]							  	
												</td>
										  </tr>
										  
										  <tr>
										  	<td>��������</td>
										  	<td>
														<label class="showFont"><s:property value="sysArgs['max_select_count_alarm']"/></label>
														<s:hidden name="sysArgs['max_select_count_alarm']" id="max_select_count_alarm"></s:hidden>										
										  	</td>
										  	<td>
														<label class="showFont"><s:property value="sysArgs['max_rs_count_alarm']"/></label>
														<s:hidden name="sysArgs['max_rs_count_alarm']" id="max_rs_count_alarm"></s:hidden>[��]							  	
												</td>
										  </tr>								  							  
									  </s:else>
									</table>
								
									<!-- ��ʷ���� -->
									<table width="100%" border="5" align="center" cellpadding="0" cellspacing="1" 
										class="table_warp" style="text-align: center;">
									  <tr>
									  	<td width="80px">&nbsp;</td>
									  	<td width="">����[��]</td>
									  	<td width="">��ˮ��Χ[��]</td>
									  	<td width="">�ձ���Χ[��]</td>
									  	<td >�±���Χ[��]</td>
									  </tr>	
										<s:if test="#session.userpowertag == 86">
										  <tr>
										  	<td>��ʷ����</td>
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
										  	<td>��ʷ����</td>
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
												<td>��ʷ����</td>
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
												<td>��ʷ����</td>
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
									<strong>* ���ݷ�Χ : ��ѯ��ʱ������</strong><br/>									
									<strong>* ���� : �����б���ѡ���������</strong><br/>										
									<strong>* �� :����Ϊ1,����Ϊ100,��ô��Ч�Ĳ�ѯʱ�䷶ΧΪ:1*100=100���� </strong>									
								</td>
							</tr>
						</table>
				  	<!-- ����ѡ�����,��ѯ�������������  end -->
				  </td>
				  <td valign="top" width="28%">
				  <div class="box-line-l">
				  	<!-- ��������  start -->
				    <table width="100%" border="0" cellspacing="0" cellpadding="0">
				      <tr>
				      	<td class="table-tr-top"><div align="center">��������</div></td>
				      </tr>
				      <tr>
				        <td>
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp" style="margin-left: 10px;">
									  <s:if test="#session.userpowertag == 86"> 
										  <tr>
										  	<td>
													<label for="shortmessageLabel" style="cursor: pointer">
													<s:checkbox  name="showGroup['msssage']" id="shortmessageLabel" />���ö�Ϣ��������</label> 	
													<%--<s:hidden name="showGroup['msssage']" value="false"></s:hidden>--%>
										  	</td>
										  </tr>
										  <tr>
										  	<td><strong><label>CNMI����</label></strong>
										    	<s:select list="cnmitype" name="sysArgs['sms_store_type']" listKey="key" listValue="value"/>	        
										  	</td>
										  </tr>											  
										  <tr>
										  	<td>
													<strong><label>�������ĺ���</label></strong>				
													<s:textfield name="sysArgs['sms_center_number']" id="sms_center_number" size="17" maxlength="11"></s:textfield>	  	
										  	</td>
										  </tr>
									  </s:if>
									  <tr>
									  	<td>
												<label for="pcSoundLabel" style="cursor: pointer">
												<s:checkbox  name="showGroup['pcsound']" id="pcSoundLabel" />���õ�����������</label>							  	
									  	</td>
									  </tr>
									  <tr>
									  <tr>
									  	<td>
												<label>����ʱ�����ļ�</label>
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
													<strong>* ��Ϣ�������� ,��Ҫ�������3��:<br/>
													��: "��������->ͨ���б�"<br/>
													��: "��������->�������->��������"<br/>
													��: "���ö�Ϣ��������"
													</strong>									
												</td>
											</tr>
										</s:if>									  
									</table>
								</td>
							</tr>
						</table>		  	
				  	<!-- �����������  end -->
				  </div>
				  </td>	 
				</tr>
				<tr>
					<td colspan="3" class="user_list_01">
						<div align="center" class="user_list_01" style="font-size:12px; margin-top:10px;margin-right: 10px;">
							<a href="#" onclick="submitForm();"> ����ϵͳ���� </a>
							<a href="#" onclick="checkSerial();" style="margin-left: 10px;"> ���ϵͳ״̬  </a>
						</div>
					</td>
				</tr>
			</table>
			</s:form>
			
		  <!-- ������ʾ�� -->
		  <div class="div_error" id="errorArea" 
		  		style="display: <s:property value="showTipMsg==0?'none':'block'"/>">
					<s:include value="../common/error.jsp"></s:include>
		  </div>	
			</div>
		<%-- div����  end  --%>
		</div>
		
	<!-- includtop�������� -->		
	</div>
	
	<script type="text/javascript">showAuto();</script>	
			
	</body>
</html>
