<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/main/datepicker/WdatePicker.js"></script>		
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../queryUI/ui/jquery.pager.js"></script>
	<script type="text/javascript" src="../js/page/backuplist.js"></script>
	<link rel="stylesheet" href="../queryUI/css/Pager.css" type="text/css" />	
	
	<script type="text/javascript">
	function down(fileName){
		$("#filename").val(fileName);
		document.downfileForm.target="_blank";
		var actionPath = '${pageContext.request.contextPath}/userdo/excelAction.action';
		document.downfileForm.action = actionPath;
		document.downfileForm.submit();
	}	
	</script>
	
	<body>

		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		<input  type="hidden" value="<s:property value="backUpLists.size"/>" id="have_data"/>

		<!-- �б�ʼ -->	
		<div class="main_content1" >
		  <div align="left" style="padding-left:20px; font-weight:bold;">��ʷ����--[��ʷ����]</div>
		  <div class="user_list">		
				<s:form namespace="/mana" theme="simple" name="backupdataForm" method="post" cssClass="formOutLine">
					<s:hidden name="currentPage" id="currentPage"/>
					<s:hidden name="pager.totalPages" id="pagecount"/>					
					<div align="left" class="user_list_01">
						<label>������</label>
						<s:select list="searchAreaMap" name="searchPlace" listKey="value"	listValue="value" value="searchPlace"/>
						<label>[��ʼʱ��]</label>
						<input type="text" id="fromTime" name="timeFromStr" value="<s:property value="timeFromStr"/>" class="Wdate" style="width: 120px" 
							onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy��MM��dd��',maxDate:'%y-%M-%d',vel:'fromShow'})"/>						
						<input type="hidden" name="startTime" value="<s:property value="startTime"/>" id="fromShow"/>			
						<label>[����ʱ��]</label>
						<input type="text" id="endTime" name="timeToStr" value="<s:property value="timeToStr"/>" class="Wdate" style="width: 120px"
							onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy��MM��dd��',minDate:'#F{$dp.$D(\'fromTime\');}',maxDate:'%y-%M-%d',vel:'endShow'})"/>							
						<input type="hidden" name="endTime" value="<s:property value="endTime"/>" id="endShow" />
						<a href="#" onclick="submitForm('',1)"> ��ѯ��ʷ���� </a>										
					</div>
					<div class="table-bg">
						<table cellspacing="1" cellpadding="0" width="100%" border="0">
							<s:if test="backUpLists.size == 0">
								<caption>
									<div class="list_no_data">�����б�Ϊ��...</div>
								</caption>		
							</s:if>
							<s:if test="backUpLists.size > 0">
								<thead>
									<tr>
										<td width="40px" class="table-tr-top"><div align="center"><strong>NO.</strong></div></td>
										<td class="table-tr-top"><div align="center"><strong>������	</strong></div></td>
										<td class="table-tr-top"><div align="center"><strong>�ļ���</strong></div></td>
										<td class="table-tr-top"><div align="center"><strong>����ʱ��</strong></div></td>
										<td width="120px" class="table-tr-top"><div align="center"><strong>����</strong></div></td>								
									</tr>
								</thead>
								<tbody>
									<s:iterator value="backUpLists" status="st" id="lists">
										<tr>
											<td class="user_list_line">
												<s:property	value="#st.count+pager.currentPage*pager.pageSize-pager.pageSize" />
											</td>										
											<td class="user_list_line"><s:property value="remark"/></td>
											<td class="user_list_line"><s:property value="subFileName(fileName)" /></td>
											<td class="user_list_line"><s:date name="backtime" format="yyyy��MM��dd��" /></td>
											<td class="user_list_line">
												[<a href="#" onclick="down('${fileName}',2);">�� ��</a>]
											</td>
										</tr>
									</s:iterator>
								</tbody>
							</s:if>		
						</table>
					</div>	
				</s:form>
			</div>
			<!--��ҳ -->
			<div class="table-page"><div id="pager" ></div></div>				
			<%-- ������֤��ʾ�� --%>
		  <div class="div_error" id="errorArea" 
		  		style="display: <s:property value="showTipMsg==0?'none':'block'"/>">
					<s:include value="../common/error.jsp"></s:include>
		  </div>
		<!-- �б���� -->
		</div>
		<s:form namespace="/userdo" theme="simple" name="downfileForm" method="post" cssClass="formOutLine">
			<s:hidden id="filename" name="filename"/>
		</s:form>
	<!-- includtop�������� -->		
	</div>

	</body>
</html>
