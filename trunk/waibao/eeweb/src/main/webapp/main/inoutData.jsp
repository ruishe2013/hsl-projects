<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/page/serial.js"></script>
	<style type="text/css">
		ul{ list-style:none;margin:0;} 
	</style>
	<script type="text/javascript" >
	function subCfg(){
		$("#errorArea").hide(); // ������Ϣ���� ���ð�ť���
		var bool = true;
		bool = checkData("configFile","stringLen","�����ļ���");
		if (bool){bool = checkData("configFile","BINFILE","ѡ����ļ���ʽ����");}
		if (bool){
			submitFailure("���ڵ��������ļ�...");
			document.importForm.action = '${pageContext.request.contextPath}/userdo/configUploadAction';
			document.importForm.submit();
		}
	}	
	</script>

	<body>
	
		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		
		<%-- ���뵼������  --%>
		<div class="main_content1">
		  <div align="left" style="padding-left:20px; font-weight:bold;">ϵͳ����--[���뵼������]</div>
		  <div class="user_list">
			  <s:form namespace="/userdo" method="post" enctype="multipart/form-data" theme="simple" name="importForm">
					<!-- ��Ҫ������ֶ� -->		  
			    <table width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr>
			      	<td class="table-tr-top"><div align="center">���뵼������</div></td>
			      </tr>
			      <tr>
			        <td align="center">
								<table width="70%"  border="0" cellpadding="0" cellspacing="0" class="table_warp">
								  <tr>
								    <td align="left" class="user_list_01">		
								    	<strong><label>�����ļ���</label></strong>
								    	<s:file name="configFile" label="configFile" size="50" id="configFile"/>
					        		<a href="#" onclick="subCfg();"> �� ��</a>
					        		<a href="${pageContext.request.contextPath}/userdo/sysArgsAction"> �� �� </a> 
						        </td>							  	
								  </tr>
								  <tr>
								  	<td colspan="2" align="left">
											<p>
												<strong>
													<br/>* Ϊ�˰�ȫ���,��������ǰ,�����Ȱ����õ���,��һ������...
													<br/>* ���������ڼ�,ϵͳ����ʱ�ر�����������ͨѶ,�ȵ��������,ϵͳ���Զ��ָ�����ͨѶ...
												</strong>
											</p>			
								  	</td>
								  </tr>	
								</table>
							</td>
						</tr>
					</table>
				</s:form>
			  <!-- ������ʾ�� -->
			  <div class="div_error" id="errorArea" 
			  		style="display: <s:property value="showTipMsg==0?'none':'block'"/>">
						<s:include value="../common/error.jsp"></s:include>
			  </div>	
			  <%-- ��ʾ--%>					
			</div>
		</div>
		
	<!-- includtop�������� -->		
	</div>
	
	</body>
</html>
