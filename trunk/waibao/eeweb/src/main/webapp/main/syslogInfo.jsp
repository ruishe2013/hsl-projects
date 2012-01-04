<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../queryUI/ui/jquery.pager.js"></script>
	<script type="text/javascript" src="datepicker/WdatePicker.js"></script>
	<script type="text/javascript" src="../js/page/syslog.js"></script>
	<link rel="stylesheet" href="../queryUI/css/Pager.css" type="text/css" 	media="screen" />
	
	<body>

	<%-- top  --%>
	<s:include value="../common/includetop.jsp" />
	
	<input  type="hidden" value="<s:property value="logList.size"/>" id="have_data"/>
	
	<!-- 列表开始 -->
	<div class="main_content1">
		<div align="left" style="padding-left:20px; font-weight:bold;">系统操作--[系统日志]</div>
		<div class="user_list">
			<s:form namespace="/main" theme="simple"	name="sysLogList" method="post" cssClass="formOutLine">
				<s:hidden id="logId" name="logId" value="0"></s:hidden>
				<s:hidden name="currentPage" id="currentPage"/>
				<s:hidden name="pager.totalPages" id="pagecount"/>
				<div align="left" class="user_list_01">
					[日志类型]	<s:select list="logTypeMap" value="log_type" name="log_type" listKey="key" listValue="value"/>						
					[查询时间]<input type="text" id="fromTime" name="timeFromStr" value="<s:property value="timeFromStr"/>" class="Wdate" style="width: 120px" 
						onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy年MM月dd日',maxDate:'%y-%M-%d',vel:'fromShow'})"/>						
					<input type="hidden" name="startTime" value="<s:property value="startTime"/>" id="fromShow"/>			
					~<input type="text" id="endTime" name="timeToStr" value="<s:property value="timeToStr"/>" class="Wdate" style="width: 120px"
						onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy年MM月dd日',minDate:'#F{$dp.$D(\'fromTime\');}',maxDate:'%y-%M-%d',vel:'endShow'})"/>							
					<input type="hidden" name="endTime" value="<s:property value="endTime"/>" id="endShow" />									
					[记录数]<s:textfield name="maxRecords" id="maxRecords"  size="7" maxlength="10"/>
					<a href="#" onclick="submitForm(2,0)">搜 索</a>
					<a href="#" onclick="submitForm(1,0)">删除选中</a>
					<a href="#" onclick="checkAll(1,'ids')">全 选</a>
					<a href="#" onclick="checkAll(2,'ids')">反 选</a>
					<a href="#" onclick="checkAll(3,'ids')">不 选</a>
				</div>
				<div class="table-bg">
					<table cellspacing="1" cellpadding="0" width="100%" border="0">
						<s:if test="!initFlag">
							<caption>
								<div class="list_no_data">请选择日志查询条件...</div>
							</caption>
						</s:if>
						<s:else>
							<s:if test="logList.size == 0">
								<caption>
									<div class="list_no_data">系统日志列表为空...</div>
								</caption>
							</s:if>						
						</s:else>
						<s:if test="initFlag">
							<s:if test="logList.size != 0">
								<thead>
									<tr>
										<td width="20px" class="table-tr-top">&nbsp;</td>
										<td width="40px" class="table-tr-top"><div align="center"><strong>NO.</strong></div></td>
										<td class="table-tr-top"><div align="center"><strong>类型	</strong></div></td>
										<td class="table-tr-top"><div align="center"><strong>时间</strong></div></td>
										<td class="table-tr-top"><div align="center"><strong>内容</strong></div></td>
										<td width="120px" class="table-tr-top"><div align="center"><strong>操作</strong></div></td>								
									</tr>								
								</thead>
								<tbody>					
									<s:iterator value="logList" status="st" id="lists">
										<tr>
											<td class="user_list_line"><input type="checkbox" name="ids" value="${id}" /></td>
											<td class="user_list_line"><s:property	value="#st.count+pager.currentPage*pager.pageSize-pager.pageSize" /></td>
											<td class="user_list_line"><s:property value="logTypeMap[logtype]" /></td>
											<td class="user_list_line"><s:date name="logtime" format="yyyy-MM-dd  HH:mm:ss" /></td>
											<td class="user_list_line"><s:property value="logcontent"/></td>
											<td class="user_list_line">
												[<a href="#" onclick="submitForm(3,'${id}');">删 除</a>]
											</td>
										</tr>
									</s:iterator>
								</tbody>
							</s:if>
						</s:if>
					</table>
				</div>
			</s:form>
			</div>
			
			<!--分页 -->
			<div class="table-page"><div id="pager" ></div></div>
			
			<%-- 搜索验证显示区 --%>
		  <div class="div_error" id="errorArea"	style="display:none">
					<s:include value="../common/error.jsp"></s:include>
		  </div>				
			
			<!-- 列表结束 -->
		</div>
	<!-- includtop最外层结束 -->		
	</div>

	</body>
</html>
