<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../queryUI/ui/jquery.pager.js"></script>
	<script type="text/javascript" src="../js/page/equi.js"></script>
	<link rel="stylesheet" href="../queryUI/css/Pager.css" type="text/css" 	media="screen" />

	<body>

	<%-- top  --%>
	<s:include value="../common/includetop.jsp" />
	<input type="hidden" value="list" id="pageId"/>
	<input  type="hidden" value="<s:property value="equiList.size"/>" id="have_data"/>
	
	<!-- �б�ʼ -->
	<div class="main_content1" >
	  <div align="left" style="padding-left:20px; font-weight:bold;">��������--[�豸����]</div>
	  <div class="user_list">
	  	<s:form namespace="/mana" theme="simple"	name="equiFormList" method="post" cssClass="formOutLine">
			<s:hidden id="equipData_equipmentId" name="equipData.equipmentId" value="0"></s:hidden>
			<s:hidden name="currentPage" id="currentPage"/>
			<s:hidden name="pager.totalPages" id="pagecount"/>
							
			<div align="left" class="user_list_01">
				<s:select list="searchEquiType" name="searchType" listKey="key"
					listValue="value" value="searchType">����</s:select>
				<s:select list="searchAreaMap" name="searchPlaceId" listKey="key"
					listValue="value" value="searchPlaceId">������	</s:select>								
				��ע	<s:textfield name="searchMark" size="15" maxlength="15"/>
				<a href="#" onclick="submitList(4,0)">�� ��</a>
				<s:if test="equiList.size > 0">				
					<a href="#" onclick="submitList(1,0)">����˳��</a>
				</s:if>		
				<a href="#" onclick="submitList(2,0)">�½�����</a>
				<a href="#" onclick="submitList(3,0)">ɾ��ѡ��</a>
				<a href="#" onclick="checkAll(1,'ids')">ȫ ѡ</a>
				<a href="#" onclick="checkAll(2,'ids')">�� ѡ</a>
				<a href="#" onclick="checkAll(3,'ids')">�� ѡ</a>
				<s:if test="showZj == 2">						
					<a href="#" onclick="submitList(7,0)">���µ�ҩ��</a>
				</s:if>						
			</div>
			<div class="table-bg">
				<table cellspacing="1" cellpadding="0" width="100%" border="0">
					<s:if test="equiList.size == 0">
						<caption >
							<div class="list_no_data">�豸�б�Ϊ��...</div>
						</caption>	
					</s:if>
					<s:else>				
						<thead>
							<tr>
								<s:if test="#session.userpowertag == 86">
									<td width="20px" class="table-tr-top">&nbsp;</td>
								</s:if>
								<td width="20px" class="table-tr-top"><div align="center"><strong>NO.</strong></div></td>
								<td width="" class="table-tr-top"><div align="center"><strong>�������</strong></div></td>
								<td width="35px" class="table-tr-top"><div align="center"><strong>��ַ</strong></div></td>
								<td width="40px" class="table-tr-top"><div align="center"><strong>����</strong></div></td>
								<td class="table-tr-top"><div align="center"><strong>������</strong></div></td>
								<td class="table-tr-top"><div align="center"><strong>��ע</strong></div></td>
								<td class="table-tr-top"><div align="center"><strong>��ʾ����</strong></div></td>
								<s:if test="showZj==2">
									<td class="table-tr-top"><div align="center"><strong>Access״̬</strong></div></td>
								</s:if>
								<s:if test="#session.userpowertag == 86">
									<td class="table-tr-top"><div align="center"><strong>����λ��</strong></div></td>
									<td class="table-tr-top"><div align="center"><strong>����(V)</strong></div></td>
								</s:if>
								
								<s:if test="systemEqType!=3">
									<td class="table-tr-top"><div align="center"><strong>�¶�����</strong></div></td>
									<td class="table-tr-top"><div align="center"><strong>�¶�����</strong></div></td>
									<s:if test="#session.userpowertag == 86">
										<td class="table-tr-top"><div align="center"><strong>�¶�У��ֵ</strong></div></td>
									</s:if>
								</s:if>
								
								<s:if test="systemEqType!=2">
									<td class="table-tr-top"><div align="center"><strong>ʪ������</strong></div></td>
									<td class="table-tr-top"><div align="center"><strong>ʪ������</strong></div></td>
									<s:if test="#session.userpowertag == 86">
										<td class="table-tr-top"><div align="center"><strong>ʪ��У��ֵ</strong></div></td>
									</s:if>
								</s:if>
								
								<td width="80px" class="table-tr-top"><div align="center"><strong>����</strong></div></td>
							</tr>						
						</thead>
						<tbody id="bodyList">
							<s:iterator value="equiList" status="st" id="lists">
									<tr>
										<s:if test="#session.userpowertag == 86">
											<td class="user_list_line"><input type="checkbox" name="ids" value="${equipmentId}" /></td>
										</s:if>
										<td class="user_list_line"><s:property	value="#st.count+pager.currentPage*pager.pageSize-pager.pageSize" /></td>
										<td class="user_list_line"><s:property value="dsrsn" /></td>
										<td class="user_list_line"><s:property value="address" /></td>
										<td class="user_list_line"><s:property value="typeStr" /></td>
										<td class="user_list_line"><s:property value="placeStr" /></td>
										<td class="user_list_line"><s:property value="mark" /></td>
										<td class="user_list_line"><s:property value="powerShows[showPower]" /></td>
										<s:if test="showZj==2">
											<td class="user_list_line"><s:property value="powerShows[showAccess]" /></td>
										</s:if>
										<s:if test="#session.userpowertag == 86">
										<td class="user_list_line"><s:property value="conndatas[conndata]" /></td>
										<td class="user_list_line"><s:property value="powerValues[powerType]" /></td>
										</s:if>
										
										<s:if test="systemEqType!=3"><%-- ϵͳȫ������ --%>
											<!-- 1:��ʪ�� 2:���¶� 3:��ʪ�� -->
											<s:if test="equitype==3">
												<td class="user_list_line">-</td><td class="user_list_line">-</td>
												<s:if test="#session.userpowertag == 86">
													<td class="user_list_line">-</td>
												</s:if>									
											</s:if>
											<s:else>
												<td class="user_list_line"><s:property value="tempDown" />
														<s:property value="tempshowType == 1?'��':'F'"/>	</td>
												<td class="user_list_line"><s:property value="tempUp" />
													<s:property value="tempshowType == 1?'��':'F'"/></td>
												<s:if test="#session.userpowertag == 86">
													<td class="user_list_line"><s:property value="tempDev" />
														<s:property value="tempshowType == 1?'��':'F'"/></td>
												</s:if>									
											</s:else>
										</s:if>
										
										<s:if test="systemEqType!=2"><%-- ϵͳȫ������ --%>
											<s:if test="equitype == 2">
												<td class="user_list_line">-</td><td class="user_list_line">-</td>
												<s:if test="#session.userpowertag == 86">
													<td class="user_list_line">-</td>
												</s:if>									
											</s:if>
											<s:else>	
												<td class="user_list_line"><s:property value="humiDown" />%RH</td>
												<td class="user_list_line"><s:property value="humiUp" />%RH</td>
												<s:if test="#session.userpowertag == 86">
													<td class="user_list_line"><s:property value="humiDev" />%RH</td>
												</s:if>									
											</s:else>
										</s:if>
										<td class="user_list_line">
											[<a href="#" onclick="submitList(5,${equipmentId});">�� ��</a>]&nbsp;&nbsp;
											[<a href="#" onclick="submitList(6,${equipmentId});">ɾ ��</a>]
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
