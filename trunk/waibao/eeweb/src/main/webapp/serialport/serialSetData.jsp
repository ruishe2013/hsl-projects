<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>	
	<script type="text/javascript" src="../js/page/serial.js"></script>	

	<body>
		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		
		<%-- div���� start  --%>
		<div class="main_content1" style="margin-top:10px;">
			<div align="left" style="padding-left:20px; font-weight:bold;">���ڹ���--[��������]</div>
			<div class="user_list">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
				<tr>
			    <td valign="top" width="50%">
			    <div class="box-line-r">
			    	<!-- ���ô���(��ʼ�����ںͲ���ͨѶ) -->
				    <table width="100%" cellspacing="0" cellpadding="0" style="padding-bottom: 6px;">
				      <tr>
				      	<td class="table-tr-top"><div align="center">���ô���(��ʼ�����ںͲ���ͨѶ)</div></td>
				      </tr>
				      <tr>
				        <td>
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp" style="margin-left: 10px;">
									  <tr>
									  	<td class="user_list_01">
									  		<s:form namespace="/seri" theme="simple" validate="ture" name="configInitForm" cssClass="formOutLine" >
									  		<strong><label>[������]</label></strong>
									  		<s:select list="baudRatelList" name="baudRateNo" value="baudRateNo" listKey="key" listValue="value"/>
									  		<a href="#" onclick="setSerial(0);">���ںŲ���</a>
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
			    	<!-- У׼����ʱ�� -->
				    <table width="100%" cellspacing="0" cellpadding="0" style="padding-bottom: 6px;">
				      <tr>
				      	<td class="table-tr-top"><div align="center">У׼����ʱ��</div></td>
				      </tr>
				      <tr>
				        <td>
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp" style="margin-left: 10px;">
									  <tr>
									  	<td class="user_list_01" align="center"> 
									  		<s:form namespace="/seri" theme="simple" validate="ture" name="configPowerForm">
									  		<a href="#" onclick="setSerial(1);">У�� ʱ��</a>
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
			    	<!-- ���û���(BCD)�͵�ַ�� -->
				    <table width="100%" cellspacing="0" cellpadding="0" style="padding-bottom: 6px;">
				      <tr>
				      	<td class="table-tr-top"><div align="center">���û���(BCD)�͵�ַ��</div></td>
				      </tr>
				      <tr>
				        <td>
				        	<s:form namespace="/seri" theme="simple" validate="ture" name="configSetForm" cssClass="formOutLine">
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp" style="margin-left: 10px;">
									  <tr>
									  	<td width="25%">
									  		<strong><label>[��ַ]</label></strong>
									  		<s:textfield name="serialData.address" id="address" size="8" maxlength="4"/>
									  	</td>
									  	<td width="75%">
												<strong><label>[���ͺ�-����-��ˮ��]</label></strong>
												<s:textfield name="serialData.MCType" id="MCType" size="4" maxlength="4"/>-
												<s:textfield name="serialData.MCOrder" id="MCOrder" size="4" maxlength="4"/>-
												<s:textfield name="serialData.MCNo" id="MCNo" size="4" maxlength="4"/>										  		
									  	</td>
									  </tr>
									  <tr>
									  	<td>
												<label for="agreeAddrsFalg">
													<s:checkbox  name="agreeAddrsFalg" id="agreeAddrsFalg" />ͬʱ�޸ĵ�ַ
												</label>									  	
									  	</td>
									  	<td>
												<label for="addFlag">
													<s:checkbox  name="addFlag" id="addFlag" />������ˮ�ź͵�ַ
												</label>									  	
									  	</td>
									  </tr>
									  <tr>
									  	<td>
												<label for="showPowerFlag">
													<s:checkbox  name="showPowerFlag" id="showPowerFlag" />��ʾ��ѹ��Ϣ
												</label>									  	
									  	</td>
									  	<td>
												<label for="showVision">
													<s:checkbox  name="showVision" id="showVision"  />��ʾ�汾��
												</label>									  	
									  	</td>
									  </tr>
									  <tr>
									  	<td colspan="2" class="user_list_01" align="center">
									  		<a href="#" onclick="setSerial(2);">��ȡ ���� ��ַ</a>
												<a href="#" onclick="setSerial(3);">���� ���� ��ַ</a>									  		
									  	</td>
									  </tr>
										<tr>
									  	<td colspan="2">
												<strong>* ����������ܵ�ʱ��,��ȷ�ϴ���ֻ����һ������......</strong>
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
			    	<!-- ������ֵ�ͼ�¼�������� -->
				    <table width="100%" cellspacing="0" cellpadding="0" style="padding-bottom: 6px;">
				      <tr>
				      	<td class="table-tr-top"><div align="center">������ֵ�ͼ�¼��������</div></td>
				      </tr>
				      <tr>
				        <td>
				        	<s:form namespace="/seri" theme="simple" validate="ture" name="configDataForm" cssClass="formOutLine">
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp" style="margin-left: 10px;">
									  <tr>
									  	<td colspan="4" class="user_list_01">
									  		<strong><label>[��ַ]</label></strong>
									  		<s:textfield name="configaddress" id="configaddress" size="8" maxlength="4"/>
									  		<strong><label>[��������]</label></strong>
									  		<s:textfield name="configvalue" id="configvalue" size="10" maxlength="6"/>
									  		<a href="#" onclick="setSerial(4);"> ȷ�� ���� </a>
									  	</td>
									  </tr>
									  <tr>
									  	<td>
									  		<label for="defaultTemp">
													<input type="radio" name="configId" id="defaultTemp" value="1" checked="checked"/>Ĭ���¶�
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="defaultHumi">
													<input type="radio" name="configId" id="defaultHumi" value="2"/>Ĭ��ʪ��
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="defaultDew">
													<input type="radio" name="configId" id="defaultDew" value="3"/>Ĭ��¶��
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="recInterval">
													<input type="radio" name="configId" id="recInterval" value="4"/>��¼���
												</label>									  	
									  	</td>
									  </tr>
									  <tr>
									  	<td>
									  		<label for="maxtemp">
													<input type="radio" name="configId" id="maxtemp" value="8"/>���±���
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="maxhumi">
													<input type="radio" name="configId" id="maxhumi" value="6"/>��ʪ����
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="maxdew">
													<input type="radio" name="configId" id="maxdew" value="10"/>��¶�㱨��
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="maxStroe">
													<input type="radio" name="configId" id="maxStroe" value="5"/>��ʷ��¼����
												</label>									  	
									  	</td>
									  </tr>
									  <tr>
									  	<td>
									  		<label for="mintemp">
													<input type="radio" name="configId" id="mintemp" value="9"/>���±���
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="minhumi">
													<input type="radio" name="configId" id="minhumi" value="7"/>��ʪ����
												</label>									  	
									  	</td>
									  	<td>
									  		<label for="mindew">
													<input type="radio" name="configId" id="mindew" value="11"/>��¶�㱨��
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
			
		  <!-- ������ʾ�� -->
		  <div class="div_error" id="errorArea" 
		  		style="display: <s:property value="showTipMsg==0?'none':'block'"/>">
					<s:include value="../common/error.jsp"></s:include>
					
					<s:if test="serialData.powerVal != null">
						<label>��ѹ��Ϣ</label>
						<label class="powerLabel" style="margin-right: 200px">
							<s:property value="serialData.powerVal"/>
						</label>
					</s:if>
					<s:if test="serialData.version != null">
						<label>�汾��</label>
						<label class="powerLabel">
							<s:property value="serialData.version"/>
						</label>
					</s:if>					
		  </div>				
			
		</div>
		<%-- div���� end --%>
	</div>		
					
	<!-- includtop�������� -->		
	</div>

	</body>
</html>
