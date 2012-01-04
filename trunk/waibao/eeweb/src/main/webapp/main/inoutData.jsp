<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/page/serial.js"></script>
	<style type="text/css">
		ul{ list-style:none;margin:0;} 
	</style>
	<script type="text/javascript" >
	function subCfg(){
		$("#errorArea").hide(); // 隐藏信息区域 所用按钮变灰
		var bool = true;
		bool = checkData("configFile","stringLen","导入文件名");
		if (bool){bool = checkData("configFile","BINFILE","选择的文件格式有误");}
		if (bool){
			submitFailure("正在导入配置文件...");
			document.importForm.action = '${pageContext.request.contextPath}/userdo/configUploadAction';
			document.importForm.submit();
		}
	}	
	</script>

	<body>
	
		<%-- top  --%>
		<s:include value="../common/includetop.jsp" />
		
		<%-- 导入导出配置  --%>
		<div class="main_content1">
		  <div align="left" style="padding-left:20px; font-weight:bold;">系统操作--[导入导出配置]</div>
		  <div class="user_list">
			  <s:form namespace="/userdo" method="post" enctype="multipart/form-data" theme="simple" name="importForm">
					<!-- 需要输入的字段 -->		  
			    <table width="100%" border="0" cellspacing="0" cellpadding="0">
			      <tr>
			      	<td class="table-tr-top"><div align="center">导入导出配置</div></td>
			      </tr>
			      <tr>
			        <td align="center">
								<table width="70%"  border="0" cellpadding="0" cellspacing="0" class="table_warp">
								  <tr>
								    <td align="left" class="user_list_01">		
								    	<strong><label>导入文件名</label></strong>
								    	<s:file name="configFile" label="configFile" size="50" id="configFile"/>
					        		<a href="#" onclick="subCfg();"> 导 入</a>
					        		<a href="${pageContext.request.contextPath}/userdo/sysArgsAction"> 导 出 </a> 
						        </td>							  	
								  </tr>
								  <tr>
								  	<td colspan="2" align="left">
											<p>
												<strong>
													<br/>* 为了安全起见,导入数据前,可以先把配置导出,做一个备份...
													<br/>* 导入数据期间,系统将暂时关闭仪器的数据通讯,等到导入完毕,系统会自动恢复数据通讯...
												</strong>
											</p>			
								  	</td>
								  </tr>	
								</table>
							</td>
						</tr>
					</table>
				</s:form>
			  <!-- 错误显示区 -->
			  <div class="div_error" id="errorArea" 
			  		style="display: <s:property value="showTipMsg==0?'none':'block'"/>">
						<s:include value="../common/error.jsp"></s:include>
			  </div>	
			  <%-- 提示--%>					
			</div>
		</div>
		
	<!-- includtop最外层结束 -->		
	</div>
	
	</body>
</html>
