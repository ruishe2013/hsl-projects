<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/page/phone.js"></script>	

	<style type="text/css">
		#content_remark {margin:0px; height:80px;width:300px;overflow:auto;}
	</style>

	<body>

		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		<input type="hidden" value="update" id="pageId"/>
		
		<%-- �޸�ͨѶ��Ϣ  --%>		
		<div class="main_content1">
		  <div align="left" style="padding-left:20px; font-weight:bold;">ͨѶ�б�--[�޸�]</div>
		  <div class="user_list">
		  	<s:form namespace="/mana" theme="simple" validate="ture" name="updatePhoneForm">
					<s:hidden name="currentPage"/>
					<s:hidden name="phoneList.listId"/>
					<!-- ��Ҫ������ֶ� -->  
			    <table width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr>
			        <td class="table-tr-top"><div align="center">�޸�ϵͳ�û�</div></td>
			      </tr>
			      <tr>
			        <td>
								<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp">
								  <tr>
								  	<td class="table_left"><strong><label>�� ��</label></strong></td>
								    <td class="table_right">		
								    	<s:textfield name="phoneList.name" id="phoneList_name" size="17" maxlength="17" />	        
								  	</td>
								  </tr>		
								  <tr>
								  	<td class="table_left"><strong><label>�ֻ���</label></strong></td>
								    <td class="table_right">		
								    	<s:textfield name="phoneList.phone" id="phoneList_phone" size="17" maxlength="11" />	        
								  	</td>
								  </tr>		
								  <tr>
								  	<td class="table_left"><strong><label>�� ע</label></strong></td>
								    <td class="table_right">		
								    	<s:textarea name="phoneList.remark" id="content_remark" />	        
								  	</td>
								  </tr>										
								</table>  			        
							</td>
			      </tr>
			      <tr>
			        <td class="user_list_01">
			        	<div align="center" class="table_buttom">
			        		<a href="#" onclick="submitUpdate(1)">�޸�ͨѶ</a> 
			        		<a href="" onclick="history.go(-1);return false;">����</a> 
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
		</div>
		
	<!-- includtop�������� -->		
	</div>			
		
	</body>
</html>
