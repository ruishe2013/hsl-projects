<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/page/gprs.js"></script>

	<style type="text/css">
		.content_remark {margin:0px; height:70px;width:300px;overflow:auto;}
		.table_left_{padding-right:10px; width: 30%; text-align:right; height: 35px;}
		.table_right_{padding-top:0px; width: 70%;text-align:left;height: 35px;}		 
	</style>
	
	<body>
	
		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		<input type="hidden" value="add" id="pageId"/>
				
		<%-- div���� start  --%>
		<div class="main_content1" style="margin-top:10px;">
			<div align="left" style="padding-left:20px; font-weight:bold;">���Ÿ�ʽ--[�޸�]</div>
			<div class="user_list">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
				<tr>
			    <td valign="top" width="40%">
			    <div class="box-line-r">
			    	<!-- ��Ч�Ĵ������ -->
				    <table width="100%" cellspacing="1" cellpadding="0" class="table-bg">
							<thead>
								<tr>
									<td width="40px;" class="table-tr-top"><div align="center"><strong>NO.</strong></td>
									<td class="table-tr-top"><div align="center"><strong>����</strong></div></td>
									<td class="table-tr-top"><div align="center"><strong>��ʽ</strong></div></td>
									<td class="table-tr-top"><div align="center"><strong>˵��</strong></div></td>
									<td class="table-tr-top"><div align="center"><strong>����</strong></div></td>
								</tr>
							</thead>
							<tbody id="bodyList">
								<s:iterator value="usableList" status="st" id="lists">
									<tr style=" height: 30px">
										<td class="user_list_line" ><s:property value="#st.count" /></td>
										<td class="user_list_line"><s:property value="signStr" /></td>
										<td class="user_list_line"><s:property value="format" /></td>
										<td class="user_list_line"><s:property value="mean" /></td>
										<td class="user_list_line"><s:property value="sample" /></td>
								</s:iterator>
							</tbody>															
						</table>
					</div>
					</td>
			    <td valign="top" width="60%">
			    	<%-- �����µĸ�ʽ --%>
				    <table width="100%" cellspacing="0" cellpadding="0" style="padding-bottom: 6px;">
				      <tr>
				      	<td class="table-tr-top"><div align="center">�޸Ķ��ŷ��͸�ʽ</div></td>
				      </tr>
				      <tr>
				        <td>
				        	<s:form namespace="/mana" theme="simple" validate="ture" name="updateGprsForm">
				        	<s:hidden name="currentPage"/>
				        	<s:hidden name="gprsSet.gprsSetId"/>
									<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp">
									  <tr>
									  	<td class="table_left_"><strong><label>���ִ���</label></strong></td>
									    <td class="table_right_">		
									    	<s:textfield name="gprsSet.numId" id="gprsSet_numId" size="5" maxlength="5" />	        
									  	</td>
									  </tr>		
									  <tr>
									  	<td class="table_left_"><strong><label>��ĸ����</label></strong></td>
									    <td class="table_right_">		
									    	<s:textfield name="gprsSet.alias" id="gprsSet_alias" size="5" maxlength="5" />	        
									  	</td>
									  </tr>		
									  <tr>
									  	<td class="table_left_"><strong><label>�� ʽ</label></strong></td>
									    <td class="table_right_">		
									    	<s:textarea name="gprsSet.mesFormat" id="gprsSet_mesFormat" cssClass="content_remark"/>	        
									  	</td>
									  </tr>		
									  <tr>
									  	<td class="table_left_"><strong><label>˵ ��</label></strong></td>
									    <td class="table_right_">		
									    	<s:textarea name="gprsSet.remark" id="gprsSet_remark" cssClass="content_remark" />	        
									  	</td>
									  </tr>		
									</table>  
									</s:form>
								</td>
							</tr>
							<tr>
				        <td class="user_list_01">
				        	<div align="center" class="table_buttom">
				        		<a href="#" onclick="submitUpdate(1);">�޸ĸ�ʽ</a>
				        		<a href="#" onclick="history.go(-1);return false;">����</a> 
				        	</div>
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
		  </div>							
		</div>
		<%-- div���� end --%>
	</div>		
					
	<!-- includtop�������� -->		
	</div>
	
	<script type="text/javascript">showList(2);</script>			
		
	</body>
</html>
