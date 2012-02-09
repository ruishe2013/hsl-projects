<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>

	<head>
		<title>提示信息</title>
		<meta http-equiv="Cache-Control" content="max-age=0" />
		<meta http-equiv="Cache-Control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="Expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
		<meta http-equiv="Pragma" content="no-cache" />
			
		<style type="text/css">
		<!--
		body {margin-left: 0px;	margin-top: 0px;	margin-right: 0px;	margin-bottom: 0px;	background-color:#EE9C0C;	}
		.content {width:810px; height:396px; background-image:url(../images/login_bg01.jpg); margin:20px auto; clear:both;}
		.foot {text-align:center; font-size:12px; line-height:45px;}
		.no_data {font-size:14px;}
		.no_data label{text-align:left; margin-left: 30px;}			
		-->
		</style>		
		
	</head>
	<body>
		<table width="1003px" border="0" align="center" cellpadding="0" cellspacing="0">
		  <tr>
		    <td height="630px" background="../images/login_bg_m.jpg">
					<div style="width:810px; margin:0px auto; margin-top:0px;border-bottom:2px solid #FF9900; height:50px;">
						<div style="float:left; width:350px;"><img src="../images/logo_login.jpg" width="307px" height="50px" /></div>
						<div style="float:right; width:200px; font-size:12px; text-align:right;">
							<a href="http://www.eefield.com/" target="_blank"></a><!--  进入我们公司网站--></div>
					</div>
					<div class="content">
					  <table border="0" cellpadding="0" cellspacing="0" style="font-size:12px; padding: 0px 110px;">
					    <tr>
					      <td height="45px">&nbsp;</td>
					    </tr>
					    <tr>
					      <td height="30px" style="font-weight:bold;font-size:14px; ">
					      	<s:property value="headTitle" escape="false"/>
					      </td>
					    </tr>
					    <tr>
					      <td height="18px">&nbsp;</td>
					    </tr>					    
					    <tr>
					      <td height="180px" class="no_data">
					      	<s:property value="headDetail" escape="false"/>
					      	<br/><br/><br/>
					      	<a href="#" onclick="window.close();">[关闭本页]</a>
					      	
					      	<a href="${pageContext.request.contextPath}/main/MainBarAction">[返回总览画面]</a>
					      </td>
					    </tr>
					  </table>
					</div>
					<div class="foot">版权所有&copy;杭州成前科技有限公司</div>  
				</td>
		  </tr>
		</table> 
	</body>
</html>

