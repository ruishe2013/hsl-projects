<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../queryUI/ui/jquery.pager.js"></script>
	<script type="text/javascript" src="../js/page/phone.js"></script>
	<link rel="stylesheet" href="../queryUI/css/Pager.css" type="text/css" 	media="screen" />
	
	<body>

	<%-- top  --%>
	<s:include value="../common/includetop.jsp" />
	<input type="hidden" value="list" id="pageId"/>
	<input type="hidden" value="<s:property value="list_phoneList.size"/>" id="have_data"/>
	
	<!-- 列表开始 -->
	<div class="main_content1" >
	  <div align="left" style="padding-left:20px; font-weight:bold;">无线设置--[通讯列表]</div>
	  <div class="user_list">
	  	<s:form namespace="/mana" theme="simple"	name="phoneFormList" method="post" cssClass="formOutLine">
			<s:hidden id="phoneList_listId" name="phoneList.listId" value="0"></s:hidden>
			<s:hidden name="currentPage" id="currentPage"/>
			<s:hidden name="pager.totalPages" id="pagecount"/>				
			<div align="left" class="user_list_01">
				姓名<s:textfield name="searchName" size="10" maxlength="15" />
				<a href="#" onclick="submitList(3,0)">搜 索</a>							
				<a href="#" onclick="submitList(1,0)">新建通讯</a>				
				<a href="#" onclick="submitList(2,0)">删除选中</a>
				<a href="#" onclick="checkAll(1,'ids')">全 选</a>
				<a href="#" onclick="checkAll(2,'ids')">反 选</a>
				<a href="#" onclick="checkAll(3,'ids')">不 选</a>
			</div>
			<div class="table-bg">
				<table cellspacing="1" cellpadding="0" width="100%" border="0">
					<s:if test="list_phoneList.size == 0">
						<caption >
							<div class="list_no_data">短信通讯列表为空...</div>
						</caption>	
					</s:if>
					<s:else>				
						<thead>
							<tr>
								<td width="20px" height="25" class="table-tr-top"></td>
								<td width="40px" class="table-tr-top"><div align="center"><strong>NO.</strong></div></td>
								<td class="table-tr-top"><div align="center"><strong>姓名</strong></div></td>
								<td class="table-tr-top"><div align="center"><strong>手机号</strong></div></td>
								<td width="120px" class="table-tr-top"><div align="center"><strong>操作</strong></div></td>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="list_phoneList" status="st" id="lists">
								<tr>
									<td class="user_list_line"><input type="checkbox" name="ids" value="${listId}" /></td>
									<td class="user_list_line"><s:property	value="#st.count+pager.currentPage*pager.pageSize-pager.pageSize" /></td>
									<td class="user_list_line"><s:property value="name" /></td>
									<td class="user_list_line"><s:property value="phone" /></td>
									<td class="user_list_line">
										[<a href="#" onclick="submitList(4,${listId});">修 改</a>]&nbsp;&nbsp;
										[<a href="#" onclick="submitList(5,${listId});">删 除</a>]
									</td>
								</tr>
							</s:iterator>							
						</tbody>
					</s:else>
				</table>
			</div>
			</s:form>
		</div>
		<!--分页 -->
		<div class="table-page"><div id="pager" ></div></div>
		
	</div>
	<!-- 列表结束 -->	

	<!-- includtop最外层结束 -->		
	</div>

	<script type="text/javascript">showList();</script>	

	</body>
</html>
