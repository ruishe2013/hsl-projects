<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>

	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/page/equi.js"></script>
	
	<script type="text/javascript" src="../queryUI/ui/ui.core.js"></script>
	<script type="text/javascript" src="../queryUI/ui/ui.sortable.js"></script>
	<link type="text/css" href="../queryUI/css/ui.all.css" rel="stylesheet" />

	<style type="text/css">
		#sortable {list-style-type: none;	margin: 0px auto;padding-left: 5px;}  
		#sortable li {margin: 5px 5px 5px 0px;padding: 10px;float: left;
			width: 160px;height: 95px;	font-size: 1em;	text-align: center;}
	</style>
	
	<body>

		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		<input type="hidden" value="order" id="pageId"/>
		
		<%-- 调整设备顺序  --%>
		<div class="main_content1">
		  <div align="left" style="padding-left:20px; font-weight:bold;">调整顺序--[用法:用鼠标拖动位置后,保存即可]</div>
		  <div class="user_list">
		  	<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
							<s:form namespace="/mana" theme="simple" name="seteqOrderForm" cssClass="formOutLine" >
								<s:hidden name="currentPage" id="currentPage"/>
								<s:hidden name="eqorderStr" id="eqorderStr"/>
								<ul id="sortable">				
									<s:iterator value="equiList" status="st" id="lists">
										<li class="ui-state-hover">
											<input type="hidden" name="place${equipmentId}" value="${equipmentId}"/>
											区域名:<s:property value="placeStr"/><br/>
											仪器地址:<s:property value="address"/><br/>
											标注:<s:property value="mark"/><br/>
											仪器编号:<s:property value="dsrsn"/><br/>
										</li>
									</s:iterator>
								</ul>
							</s:form>			  
						</td>
					</tr>
				</table>					  	
		  </div>
	   	<div class="user_list_01 table_buttom" align="center" style="font-size: 12px;">
	   		<a href="#" id="orderSave" onclick="submitUpOrder();">保存顺序</a>
	   		<a href="#" onclick="history.go(-1);return false;">返回</a> 
	   	</div>
		</div>		
		
		<!-- includtop最外层结束 -->		
		</div>
		
		<script type="text/javascript">showList();</script>		
	</body>
</html>
