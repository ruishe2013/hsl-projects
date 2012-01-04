<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>

	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/page/sms.js"></script>	
	<script type="text/javascript" src="${pageContext.request.contextPath}/main/datepicker/WdatePicker.js"></script>
	<style type="text/css"> 
		.label_space {text-align:left; margin-left: 30px;} 
	</style>

	<body>

	<s:include value="../common/includetop.jsp" />
	
	<%-- �������� --%>
	<div class="main_content1">
		<div align="left" style="padding-left:20px; font-weight:bold;">�������ݲ�ѯ</div>
		<%-- �������� --%>
		<div class="historydata_check">
		<s:form namespace="/main" name="smsForm" theme="simple" cssClass="formOutLine">
			<s:hidden name="phoneStr" id="phoneStr"/>
			<%--  
			<s:hidden name="typeStr" id="typeStr" value="1"/>--%>
			<table cellspacing="0" cellpadding="0" width="100%" border="0">
				<tr>
					<td width="30%">
						<label><strong>[��ʼʱ��]</strong></label>
						<input type="text" id="fromTime" name="fromTime" class="Wdate" style="width: 200px"
							onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy��MM��dd�� HHʱmm��ss��', 
							maxDate:'%y-%M-%d %H:%m:%s',vel:'fromShow'})"/>
						<input type="hidden" name="startTime" id="fromShow"/>								
					</td>
					<td width="30%">
						<label><strong>[����ʱ��]</strong></label>
						<input type="text" id="toTime" name="toTime" class="Wdate" style="width: 200px"
							onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy��MM��dd�� HHʱmm��ss��', 
							minDate:'#F{$dp.$D(\'fromTime\');}',maxDate:'%y-%M-%d %H:%m:%s',vel:'toShow'})" />
						<input type="hidden" name="endTime" id="toShow" />		
					</td>
					<%-- --%>
					<td width="25%">
						<label><strong>[��ѯ����]</strong></label>
							<label for="min_temp" style="cursor: pointer">				
								<input type="checkbox"	name="typeStr" id="send_type" value="1" checked="checked"/>ϵͳ����		
							</label>	
							<label for="min_temp" style="cursor: pointer">				
								<input type="checkbox"	name="typeStr" id="rece_type" value="2" />ϵͳ����		
							</label>														
					</td>
					<td class="user_list_01">
						<a href="#" onclick="submitForm();" id="submit_label">[��ѯ ��������]</a>
					</td>
				</tr>
			</table>
		</s:form>
		</div>
	
		<%-- ��������-��ַ�б� --%>		
		<s:if test="phoneShow == ''">
			<div class="historydata_check">
				<div class="list_no_data">��ǰϵͳ��,����ͨѶ�б�Ϊ��...</div>
			</div>									
		</s:if>
		<s:else>
		  <div class="historydata_data">
		    <table width="100%" border="0" cellspacing="0" cellpadding="0">
		    	<thead>
			      <tr>
			        <td class="historydata_data_title">
								<a style="cursor: pointer;" onclick="cbt();">�����ֻ��б�</a>
			        </td>
			      </tr>
		    	</thead>
		    	<tbody>
		    		<tr>
			        <td>
			        	<div class="stat-box" id="_selSms">
		        			<ul>
			        				<s:property value="phoneShow" escape="false"/>
		        			</ul>
			        	<div>
			        </td>
		        </tr>
		    	</tbody>
		    </table>
		  </div>		
		</s:else>
		
		<%-- ������֤��ʾ�� --%>
		<div class="div_error" id="errorArea"  style="display:none;"></div>
	</div>	
		
	<!-- includtop�������� -->		
	</div>	

	</body>
</html>
