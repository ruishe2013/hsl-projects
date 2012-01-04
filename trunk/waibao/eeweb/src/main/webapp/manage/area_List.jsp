<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../queryUI/ui/jquery.pager.js"></script>
	<script type="text/javascript" src="../js/page/area.js"></script>
	<link rel="stylesheet" href="../queryUI/css/Pager.css" type="text/css" 	media="screen" />
	
<body>

	<%-- top  --%>
	<input type="hidden" value="list" id="pageId"/>
	<input  type="hidden" value="<s:property value="placeList.size"/>" id="have_data"/>
	<s:hidden name="showTipMsg" id="showTipMsg"/>
	<s:include value="../common/includetop.jsp" />
	
	<!-- �б�ʼ -->
	<div class="main_content1" >
	  <div align="left" style="padding-left:20px; font-weight:bold;">��������--[�������]</div>
	  <div class="user_list">
	  	<s:form namespace="/mana" theme="simple"	name="areaFormList" method="post" cssClass="formOutLine">
			<s:hidden name="currentPage" id="currentPage"/>
			<s:hidden name="pager.totalPages" id="pagecount"/>				
			<s:hidden id="place_placeId" name="place.placeId" value="0"></s:hidden>
			<s:hidden id="place_placeName" name="place.placeName" value=""></s:hidden>			
			<div align="left" class="user_list_01">
				������<s:textfield name="placeName" size="20" maxlength="15" />
				<a href="#" onclick="submitList(3,0)">�� ��</a>			
				<a href="#" onclick="submitList(1,0)">�½�����</a>				
				<a href="#" onclick="submitList(2,0)">ɾ��ѡ��</a>
				<a href="#" onclick="checkAll(1,'ids')">ȫ ѡ</a>
				<a href="#" onclick="checkAll(2,'ids')">�� ѡ</a>
				<a href="#" onclick="checkAll(3,'ids')">�� ѡ</a>
			</div>
			<div class="table-bg">
				<table cellspacing="1" cellpadding="0" width="100%" border="0">
					<s:if test="placeList.size == 0">
						<caption >
							<div class="list_no_data">�����б�Ϊ��...</div>
						</caption>	
					</s:if>
					<s:else>				
						<thead>
							<tr>
								<td width="20px" height="25" class="table-tr-top"></td>
								<td width="40px" class="table-tr-top"><div align="center"><strong>NO.</strong></div></td>
								<td class="table-tr-top"><div align="center"><strong>������</strong></div></td>
								<td width="160px" class="table-tr-top"><div align="center"><strong>����</strong></div></td>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="placeList" status="st" id="lists">
								<tr>
									<td class="user_list_line"><input type="checkbox" name="ids" value="${placeName}" /></td>
									<td class="user_list_line"><s:property	value="#st.count+pager.currentPage*pager.pageSize-pager.pageSize" /></td>
									<td class="user_list_line"><s:property value="placeName" /></td>
									<td class="user_list_line">
										[<a href="#" onclick="submitList(4,${placeId});">�� ��</a>]&nbsp;&nbsp;
										<s:if test="showSms==2">
											[<a href="#" onclick="submitList(6,${placeId});">��������</a>]
										</s:if>
										[<a href="#" onclick="submitList(5,'${placeName}');">ɾ ��</a>]&nbsp;&nbsp;
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

	<script type="text/javascript">
		showList();
		showMes($("#showTipMsg").val());
	</script>
	
	</body>
</html>
