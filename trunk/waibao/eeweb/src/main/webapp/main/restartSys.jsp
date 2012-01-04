<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>

	<script type="text/javascript">

		function submitForm(){
			ableToButton(0); $("#errorArea").hide(); // ������Ϣ���� ���ð�ť���
			document.rsstartForm.action = "restartAction";
			document.rsstartForm.submit();
		}
	</script>
	
	<body>
	
		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		
		<%-- ����ϵͳ�û�  --%>
		<div class="accordian_container" >
			<s:form namespace="/sys" theme="simple" validate="ture" name="rsstartForm">
				<s:hidden name="currentPage"/>
				<!-- ��Ҫ������ֶ� -->
			  <div  class="accordion_wrap" style="width:971px;" align="center">
				  <div  class="accordion_headings" >�������� </div>
				  <div class="accordion_child" style="width: 965px;">
				  	<input type="button" class="button_no_margin" value="��������" onclick="submitForm()"/>
				  </div>
				  <div class="accordion_child" style="width: 965px;">
						<p><strong><br/>* һ�����������,�������ݺ����Ҫ��������......</strong></p>	  	
				  </div>
				</div>
			  <!-- ������ʾ�� -->
			  <div class="accordion_clear" id="errorArea" 
			  		style="display: <s:property value="showTipMsg==0?'none':'block'"/>">
						<s:include value="../common/error.jsp"></s:include>
			  </div>			  		
						
			</s:form>
		</div>  		

		<%-- foot --%>
		<s:include value="../common/includebottom.jsp"/>

	</body>
</html>
