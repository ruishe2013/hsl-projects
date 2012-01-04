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
	
	<!-- 列表开始 -->
	<div class="main_content1" >
	  <div align="left" style="padding-left:20px; font-weight:bold;">管理中心--[设备管理]</div>
	  <div class="user_list">
	  	<s:form namespace="/mana" theme="simple"	name="equiFormList" method="post" cssClass="formOutLine">
			<s:hidden id="equipData_equipmentId" name="equipData.equipmentId" value="0"></s:hidden>
			<s:hidden name="currentPage" id="currentPage"/>
			<s:hidden name="pager.totalPages" id="pagecount"/>
							
			<div align="left" class="user_list_01">
				<s:select list="searchEquiType" name="searchType" listKey="key"
					listValue="value" value="searchType">类型</s:select>
				<s:select list="searchAreaMap" name="searchPlaceId" listKey="key"
					listValue="value" value="searchPlaceId">区域名	</s:select>								
				标注	<s:textfield name="searchMark" size="15" maxlength="15"/>
				<a href="#" onclick="submitList(4,0)">搜 索</a>
				<s:if test="equiList.size > 0">				
					<a href="#" onclick="submitList(1,0)">调整顺序</a>
				</s:if>		
				<a href="#" onclick="submitList(2,0)">新建仪器</a>
				<a href="#" onclick="submitList(3,0)">删除选中</a>
				<a href="#" onclick="checkAll(1,'ids')">全 选</a>
				<a href="#" onclick="checkAll(2,'ids')">反 选</a>
				<a href="#" onclick="checkAll(3,'ids')">不 选</a>
				<s:if test="showZj == 2">						
					<a href="#" onclick="submitList(7,0)">更新到药监</a>
				</s:if>						
			</div>
			<div class="table-bg">
				<table cellspacing="1" cellpadding="0" width="100%" border="0">
					<s:if test="equiList.size == 0">
						<caption >
							<div class="list_no_data">设备列表为空...</div>
						</caption>	
					</s:if>
					<s:else>				
						<thead>
							<tr>
								<s:if test="#session.userpowertag == 86">
									<td width="20px" class="table-tr-top">&nbsp;</td>
								</s:if>
								<td width="20px" class="table-tr-top"><div align="center"><strong>NO.</strong></div></td>
								<td width="" class="table-tr-top"><div align="center"><strong>仪器编号</strong></div></td>
								<td width="35px" class="table-tr-top"><div align="center"><strong>地址</strong></div></td>
								<td width="40px" class="table-tr-top"><div align="center"><strong>类型</strong></div></td>
								<td class="table-tr-top"><div align="center"><strong>区域名</strong></div></td>
								<td class="table-tr-top"><div align="center"><strong>标注</strong></div></td>
								<td class="table-tr-top"><div align="center"><strong>显示电量</strong></div></td>
								<s:if test="showZj==2">
									<td class="table-tr-top"><div align="center"><strong>Access状态</strong></div></td>
								</s:if>
								<s:if test="#session.userpowertag == 86">
									<td class="table-tr-top"><div align="center"><strong>发送位数</strong></div></td>
									<td class="table-tr-top"><div align="center"><strong>电量(V)</strong></div></td>
								</s:if>
								
								<s:if test="systemEqType!=3">
									<td class="table-tr-top"><div align="center"><strong>温度下限</strong></div></td>
									<td class="table-tr-top"><div align="center"><strong>温度上限</strong></div></td>
									<s:if test="#session.userpowertag == 86">
										<td class="table-tr-top"><div align="center"><strong>温度校正值</strong></div></td>
									</s:if>
								</s:if>
								
								<s:if test="systemEqType!=2">
									<td class="table-tr-top"><div align="center"><strong>湿度下限</strong></div></td>
									<td class="table-tr-top"><div align="center"><strong>湿度上限</strong></div></td>
									<s:if test="#session.userpowertag == 86">
										<td class="table-tr-top"><div align="center"><strong>湿度校正值</strong></div></td>
									</s:if>
								</s:if>
								
								<td width="80px" class="table-tr-top"><div align="center"><strong>操作</strong></div></td>
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
										
										<s:if test="systemEqType!=3"><%-- 系统全局类型 --%>
											<!-- 1:温湿度 2:单温度 3:单湿度 -->
											<s:if test="equitype==3">
												<td class="user_list_line">-</td><td class="user_list_line">-</td>
												<s:if test="#session.userpowertag == 86">
													<td class="user_list_line">-</td>
												</s:if>									
											</s:if>
											<s:else>
												<td class="user_list_line"><s:property value="tempDown" />
														<s:property value="tempshowType == 1?'℃':'F'"/>	</td>
												<td class="user_list_line"><s:property value="tempUp" />
													<s:property value="tempshowType == 1?'℃':'F'"/></td>
												<s:if test="#session.userpowertag == 86">
													<td class="user_list_line"><s:property value="tempDev" />
														<s:property value="tempshowType == 1?'℃':'F'"/></td>
												</s:if>									
											</s:else>
										</s:if>
										
										<s:if test="systemEqType!=2"><%-- 系统全局类型 --%>
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
											[<a href="#" onclick="submitList(5,${equipmentId});">编 辑</a>]&nbsp;&nbsp;
											[<a href="#" onclick="submitList(6,${equipmentId});">删 除</a>]
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
