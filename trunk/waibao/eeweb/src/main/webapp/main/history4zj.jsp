<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>

	<script type="text/javascript" src="${pageContext.request.contextPath}/main/datepicker/WdatePicker.js"></script>	
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/page/his4zj.js"></script>

	<body>

	<s:include value="../common/includetop.jsp" />
	<s:hidden name="maxSelector" value="10"/>
	
	<%-- ҩ����ʷ���� --%>
	<div class="main_content1" >
		<div align="left" style="padding-left:20px; font-weight:bold;">��ʷ����--[ҩ����ʷ����]��ѯ</div>
		<%-- �������� --%>
		<div class="historydata_check">
			<s:form namespace="/main" name="statForm" theme="simple" cssClass="formOutLine">
				<s:hidden name="timeFrom" id="timeFrom"/>
				<s:hidden name="timeTo" id="timeTo"/>
				<s:hidden name="placeListids" id="placeListids"/>
				<table cellspacing="0" cellpadding="0" width="100%" border="0">
					<tr>
						<td width="30%">
							<label><strong>[�¶ȷ�Χ]</strong></label>
						  <s:textfield name="tempDown" id="tempDown" size="6" maxlength="6"/>
						  <label>~</label>
							<s:textfield name="tempUp" id="tempUp" size="6" maxlength="6"/>��
						</td>
						<td width="30%">
							<label><strong>[ʪ�ȷ�Χ]</strong></label>
							<s:textfield name="humiDown" id="humiDown" size="6" maxlength="6"/>
							<label>~</label>
							<s:textfield name="humiUp" id="humiUp" size="6" maxlength="6"/>%RH						
						</td>
						<td width="40%">
						</td>
					</tr>
					<tr>
						<td>
							<label><strong>[��ʼʱ��]</strong></label>
							<input type="text" id="fromTime_A" class="Wdate" style="width: 200px"
								onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy��MM��dd�� HHʱmm��ss��',
									maxDate:'%y-%M-%d %H:%m:%s',vel:'fromShow_A'})"/>
							<input type="hidden" name="timeFrom_A" id="fromShow_A"/>			
						</td>
						<td>
							<label><strong>[����ʱ��]</strong></label>
							<input type="text" id="toTime_A" class="Wdate" style="width: 200px"
								onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy��MM��dd�� HHʱmm��ss��',
									minDate:'#F{$dp.$D(\'fromTime_A\');}',maxDate:'%y-%M-%d %H:%m:%s',vel:'toShow_A'})" />
							<input type="hidden" name="timeTo_A" id="toShow_A" />		
						</td>
						<td class="user_list_01">
				  		<a href="#" onclick="submitForm();" id="submit_label">[��ѯ ҩ����ʷ����]</a>
						</td>
					</tr>
				</table>				
			</s:form>
		</div>
		
		<%-- ��������-��ַ�б� --%>		
		<s:if test="areaToEqList.size == 0">
			<div class="historydata_check">
				<div class="list_no_data">ҩ������ݿ���,��û�����ݼ�¼...</div>
			</div>									
		</s:if>
		<s:else>
			<s:iterator value="areaToEqList" status="st" id="lists" >
			  <div class="historydata_data">
			    <table width="100%" border="0" cellspacing="0" cellpadding="0">
			    	<thead>
				      <tr>
				        <td class="historydata_data_title">
									<a id="sel<s:property	value="#st.count"/>" style="cursor: pointer;">
										<s:property value="key"/></a>
				        </td>
				      </tr>
				    </thead>
				    <tbody>
				    	<tr>
				        <td>
				        	<div class="stat-box" id="_sel<s:property	value="#st.count"/>">
				        		<ul><s:property value="value" escape="false"/></ul>
				        	</div>
				        </td>
			        </tr>
				    </tbody>
			    </table>
			  </div>			
			</s:iterator>			
		</s:else>		
		
		<%-- ������֤��ʾ�� --%>
		<div class="div_error" id="errorArea"  style="display:none;"></div>
	</div>	
		
	<!-- includtop�������� -->		
	</div>	

	</body>
</html>
