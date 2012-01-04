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

		<!-- 列表开始 -->	
		<div class="main_content1" >
		  <div align="left" style="padding-left:20px; font-weight:bold;">历史数据--[历史备份]</div>
		  <div class="user_list">		
				<s:form namespace="/mana" theme="simple" name="backupdataForm" method="post" cssClass="formOutLine">
					<s:hidden name="currentPage" id="currentPage"/>
					<s:hidden name="pager.totalPages" id="pagecount"/>					
					<div align="left" class="user_list_01">
						<label>区域名</label>
						<s:select list="searchAreaMap" name="searchPlace" listKey="value"	listValue="value" value="searchPlace"/>
						<label>[开始时间]</label>
						<input type="text" id="fromTime" name="timeFromStr" value="<s:property value="timeFromStr"/>" class="Wdate" style="width: 120px" 
							onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy年MM月dd日',maxDate:'%y-%M-%d',vel:'fromShow'})"/>						
						<input type="hidden" name="startTime" value="<s:property value="startTime"/>" id="fromShow"/>			
						<label>[结束时间]</label>
						<input type="text" id="endTime" name="timeToStr" value="<s:property value="timeToStr"/>" class="Wdate" style="width: 120px"
							onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy年MM月dd日',minDate:'#F{$dp.$D(\'fromTime\');}',maxDate:'%y-%M-%d',vel:'endShow'})"/>							
						<input type="hidden" name="endTime" value="<s:property value="endTime"/>" id="endShow" />
						<a href="#" onclick="submitForm('',1)"> 查询历史备份 </a>										
					</div>
					<div class="table-bg">
						<table cellspacing="1" cellpadding="0" width="100%" border="0">
							<s:if test="backUpLists.size == 0">
								<caption>
									<div class="list_no_data">备份列表为空...</div>
								</caption>		
							</s:if>
							<s:if test="backUpLists.size > 0">
								<thead>
									<tr>
										<td width="40px" class="table-tr-top"><div align="center"><strong>NO.</strong></div></td>
										<td class="table-tr-top"><div align="center"><strong>区域名	</strong></div></td>
										<td class="table-tr-top"><div align="center"><strong>文件名</strong></div></td>
										<td class="table-tr-top"><div align="center"><strong>备份时间</strong></div></td>
										<td width="120px" class="table-tr-top"><div align="center"><strong>操作</strong></div></td>								
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
											<td class="user_list_line"><s:date name="backtime" format="yyyy年MM月dd日" /></td>
											<td class="user_list_line">
												[<a href="#" onclick="down('${fileName}',2);">下 载</a>]
											</td>
										</tr>
									</s:iterator>
								</tbody>
							</s:if>		
						</table>
					</div>	
				</s:form>
			</div>
			<!--分页 -->
			<div class="table-page"><div id="pager" ></div></div>				
			<%-- 搜索验证显示区 --%>
		  <div class="div_error" id="errorArea" 
		  		style="display: <s:property value="showTipMsg==0?'none':'block'"/>">
					<s:include value="../common/error.jsp"></s:include>
		  </div>
		<!-- 列表结束 -->
		</div>
		<s:form namespace="/userdo" theme="simple" name="downfileForm" method="post" cssClass="formOutLine">
			<s:hidden id="filename" name="filename"/>
		</s:form>
	<!-- includtop最外层结束 -->		
	</div>

	</body>
</html>
