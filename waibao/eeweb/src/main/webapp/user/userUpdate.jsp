<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/page/user.js"></script>

	<body>

		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		<input type="hidden" value="update" id="pageId"/>
		
		<%-- �޸�ϵͳ�û�  --%>		
		<div class="main_content1">
		  <div align="left" style="padding-left:20px; font-weight:bold;">�û�����--[�޸�]</div>
		  <div class="user_list">
				<s:form namespace="/userdo" theme="simple" validate="ture" name="updateUserForm">
					<s:hidden name="currentPage"/>
					<s:hidden name="user.userId"></s:hidden>
					<!-- ��Ҫ������ֶ� -->  
			    <table width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr>
			        <td class="table-tr-top"><div align="center">�޸�ϵͳ�û�</div></td>
			      </tr>
			      <tr>
			        <td>
								<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="table_warp">
								  <tr>
								  	<td class="table_left"><strong><label>�û���</label></strong></td>
								    <td class="table_right">		
								    	<s:textfield name="user.name" id="user_name" size="20" maxlength="10"/>	        
								  	</td>
								  </tr>		
								  <tr>
								  	<td class="table_left"><strong><label>Ȩ ��</label></strong></td>
								    <td class="table_right">		
								    	<s:select list="powers" name="user.power" listKey="key" listValue="value"/>	        
								  	</td>
								  </tr>		
								</table>  			        
							</td>
			      </tr>
			      <tr>
			        <td class="user_list_01">
			        	<div align="center" class="table_buttom">
			        		<a href="#" onclick="submitUpdate(1)">�޸��û�</a> 
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
