<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/swfobject.js"></script>
	<script type="text/javascript" src="../js/jquery.jmp3.js"></script>			
	<script type="text/javascript" src="../js/page/line.js"></script>
	
	<body>
		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		<s:hidden name="maxSelector" />
		<s:hidden name="alarmPlayFile" id="alarmPlayFile"/>	
		<s:hidden name="falshTime" id="falshTime"/>	
		<s:hidden name="systemEqType" id="systemEqType"/>

		<%-- 地址选择列表 left:0px; top: 120px; position: absolute;z-index: 2;  --%>
		<div class="main_content_address">
			<div class="main_content_address_left">
				<img src="../images/title_add.jpg" style="cursor: pointer;" onclick="toggleDiv();"/>
			</div>
			<div class="main_content_address_right">
				<div id="sounddl" class="mp3" style="margin-top:2px; margin-right: 7px; float: left;"></div>
				<strong style="color: black; padding-right: 7px;">[实时画面] </strong>
				<strong id="recTimeLabel"></strong>
			</div>
		</div>		
		
		<div class="main_content1" id="addressList">
			<s:if test="areaToEqList.size == 0">
				<div class="historydata_check">
					<div class="list_no_data">当前系统中,仪器列表为空...</div>
				</div>					
			</s:if>
			<s:else>
					<s:iterator value="areaToEqList" status="st" id="lists" >
					  <div class="historydata_data" id="_sel<s:property	value="#st.count"/>">
					    <table width="100%" border="0" cellspacing="0" cellpadding="0">
					    	<thead>
						      <tr>
						        <td class="historydata_data_title" colspan="5">
											<a id="sel<s:property	value="#st.count"/>" style="cursor: pointer;">
												<s:property value="key"/>
											</a>
						        </td>
						      </tr>
					      </thead>
					      <tbody>
					      	<tr>
						        <td>
						        	<div class="stat-box" id="_sel<s:property	value="#st.count"/>">
						        		<ul><s:property value="value" escape="false"/></ul>
						        	<div>
						        </td>
						       <tr>
					      </tbody>
					    </table>
					  </div>			
					</s:iterator>	
					
					<div class="user_list_01" style="margin: 10px auto; text-align: center; font-size: 12px;">
						<s:form namespace="/main" name="lineSetEQForm" theme="simple" cssClass="formOutLine">
							<s:hidden name="userPlaceList" id="userPlaceList"/>
							<a href="#" onclick="submitEQ();"> 确 定 </a>	
						</s:form>
					</div>					
					
					<%-- 验证显示区 --%>
					<div class="div_error" id="errorArea"  style="display:none;"></div>						
			</s:else>
		</div>		

		<!-- flash 显示区域 -->
		<div id="resultDiv" class="main_content1 mi-top" >
			<div id="amlinetemp"></div>
			<div id="amlinehumi"></div>
		</div>
		<%-- 
			// 背景音乐
			if (json.palyFalg == 2){
				if ($("#backSound").attr("src")==""){//背景声音开启
					$("#backSound").attr("src","${pageContext.request.contextPath}/music/" + $("#alarmPlayFile").val());				
				}
			}else{
				if ($("#backSound").attr("src")!=""){//背景声音关闭
					$("#backSound").attr("src","");				
				}					
			}		
		<BGSOUND id="backSound" SRC="" LOOP="-1"/>
		--%>

		<!-- includtop最外层结束 -->		
		</div>				
		
		<script type="text/javascript">
			$("#addressList").hide();	callRun();
		</script>				

	</body>
</html>
