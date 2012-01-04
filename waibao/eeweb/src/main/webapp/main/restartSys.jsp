<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>

	<script type="text/javascript">

		function submitForm(){
			ableToButton(0); $("#errorArea").hide(); // 隐藏信息区域 所用按钮变灰
			document.rsstartForm.action = "restartAction";
			document.rsstartForm.submit();
		}
	</script>
	
	<body>
	
		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		
		<%-- 增加系统用户  --%>
		<div class="accordian_container" >
			<s:form namespace="/sys" theme="simple" validate="ture" name="rsstartForm">
				<s:hidden name="currentPage"/>
				<!-- 需要输入的字段 -->
			  <div  class="accordion_wrap" style="width:971px;" align="center">
				  <div  class="accordion_headings" >重新启动 </div>
				  <div class="accordion_child" style="width: 965px;">
				  	<input type="button" class="button_no_margin" value="重新启动" onclick="submitForm()"/>
				  </div>
				  <div class="accordion_child" style="width: 965px;">
						<p><strong><br/>* 一般情况下自由,导入数据后才需要重新启动......</strong></p>	  	
				  </div>
				</div>
			  <!-- 错误显示区 -->
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
