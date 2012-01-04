<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../queryUI/ui/jquery.pager.js"></script>
	<script type="text/javascript" src="../js/page/user.js"></script>	
	<link rel="stylesheet" href="../queryUI/css/Pager.css" type="text/css" 	media="screen" />

	<body>

	<%-- top  --%>
	<s:include value="../common/includetop.jsp" />
	<input type="hidden" value="list" id="pageId"/>
	<input  type="hidden" value="<s:property value="userList.size"/>" id="have_data"/>
	
	<!-- �б�ʼ -->
	<div class="main_content1" >
	  <div align="left" style="padding-left:20px; font-weight:bold;">��������--[�û�����]</div>
	  <div class="user_list">
			<s:form namespace="/userdo" theme="simple"	name="userFormList" method="post" cssClass="formOutLine">
			<s:hidden id="user_userId" name="user.userId" value="0"></s:hidden>
			<s:hidden name="currentPage" id="currentPage"/>
			<s:hidden name="pager.totalPages" id="pagecount"/>				
			<div align="left" class="user_list_01">
				Ȩ��<s:select list="searchPowers" name="searchPower" listKey="key"	listValue="value" value="searchPower"/>
				�û���<s:textfield name="searchName" size="20" maxlength="15" />
				<a href="#" onclick="submitList(3,0)">�� ��</a>
				<a href="#" onclick="submitList(1,0)">�½��û�</a>				
				<a href="#" onclick="submitList(2,0)">ɾ��ѡ��</a>
				<a href="#" onclick="checkAll(1,'ids')">ȫ ѡ</a>
				<a href="#" onclick="checkAll(2,'ids')">�� ѡ</a>
				<a href="#" onclick="checkAll(3,'ids')">�� ѡ</a>
			</div>
			<div class="table-bg">
				<table cellspacing="1" cellpadding="0" width="100%" border="0">
					<s:if test="userList.size == 0">
						<caption >
							<div class="list_no_data">�û��б�Ϊ��...</div>
						</caption>	
					</s:if>
					<s:else>				
						<thead>
							<tr>
								<td width="20px" height="25" class="table-tr-top"></td>
								<td width="40px" class="table-tr-top"><div align="center"><strong>NO.</strong></div></td>
								<td class="table-tr-top"><div align="center"><strong>�û���</strong></div></td>
								<td class="table-tr-top"><div align="center"><strong>Ȩ��</strong></div></td>
								<td width="120px" class="table-tr-top"><div align="center"><strong>����</strong></div></td>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="userList" status="st" id="lists">
								<tr>
									<td class="user_list_line"><input type="checkbox" name="ids" value="${userId}" /></td>
									<td class="user_list_line"><s:property	value="#st.count+pager.currentPage*pager.pageSize-pager.pageSize" /></td>
									<td class="user_list_line"><s:property value="name" /></td>
									<td class="user_list_line"><s:property value="powerStr" /></td>
									<td class="user_list_line">
										[<a href="#" onclick="submitList(4,${userId});">�� ��</a>]&nbsp;&nbsp;
										[<a href="#" onclick="submitList(5,${userId});">ɾ ��</a>]
									</td>
								</tr>
							</s:iterator>							
						</tbody>
					</s:else>
				</table>
			</div>
			</s:form>
		</div>
		<!--��ҳ -->
		<div class="table-page"><div id="pager" ></div></div>
		
	</div>
	<!-- �б���� -->
		
	<!-- includtop�������� -->		
	</div>

	<script type="text/javascript">showList();</script>	

	</body>
</html>
