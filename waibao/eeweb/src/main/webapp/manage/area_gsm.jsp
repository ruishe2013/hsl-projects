<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/page/area.js"></script>

	<body>

		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		<input type="hidden" value="gsm" id="pageId"/>
		
	<%-- 区域管理--[修改短信关联] --%>
	<div class="main_content1">
		<div align="left" style="padding-left:20px; font-weight:bold;">区域管理--[修改短信关联]</div>	
		<%-- 短信列表 --%>		
		<s:if test="phonelist.size == 0">
			<div class="historydata_check">
				<div class="list_no_data">当前系统中,短信通讯列表为空...</div>
			</div>
     	<div align="center" class="user_list_01 table_buttom" style="font-size: 12px;">
     		<a href="#" onclick="history.go(-1);return false;">返回</a> 
     	</div>												
		</s:if>
		<s:else>
			<s:form namespace="/mana" theme="simple" validate="ture" name="updateAreaForm">
				<s:hidden name="currentPage"/>
				<s:hidden name="place.placeId"/>		
				<s:hidden name="place.remark" id="place_gsm"/>		
				<s:iterator value="phonelist" status="st" id="lists" >	  									
				  <div class="historydata_data">
				    <table width="100%" border="0" cellspacing="0" cellpadding="0">
				    	<thead>
					      <tr>
					        <td class="historydata_data_title" colspan="5">
										<a style="cursor: pointer;" onclick="cbt();">短信号码列表</a>
					        </td>
					      </tr>
				    	</thead>
				    	<tbody>
				    		<tr>
					        <td>
					        	<div class="stat-box" id="_selGsm">
				        			<ul>
					        				<s:property value="value" escape="false"/>
				        			</ul>
					        	<div>
					        </td>
				        </tr>
				    	</tbody>
				    </table>
				  </div>	
        	<div align="center" class="user_list_01 table_buttom" style="font-size: 12px;">
        		<a href="#" onclick="submitUpGsm(1);">修改关联</a>
        		<a href="#" onclick="history.go(-1);return false;">返回</a> 
        	</div>
				</s:iterator>			
			</s:form>	
		</s:else>
	</div>
			
	<!-- includtop最外层结束 -->		
	</div>
		
	</body>
</html>
