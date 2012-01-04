<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>

	<head>
		<title>请登录</title>
		<meta http-equiv="Cache-Control" content="max-age=0" />
		<meta http-equiv="Cache-Control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="Expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
		<meta http-equiv="Pragma" content="no-cache" />
		<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>	
		<script type="text/javascript" src="../js/page/user.js"></script>
			
		<style type="text/css">
		<!--
		body {    
			margin-left: 0px;		margin-top: 0px;		margin-right: 0px;			margin-bottom: 0px;
			background-color:#EE9C0C; 
		}
		.login {
			width: 76px;font-weight:bold;			height: 23px; color:#FFF;
			background: url(../images/login_bg.gif) no-repeat;border: none;		outline: none;
		} 
		.input { width:150px; font-size:16px; font-family:Arial, Helvetica, sans-serif; font-weight:bold;}
		-->
		</style>		
		
	</head>
	<body>
		<input type="hidden" value="load" id="pageId"/>
		<s:hidden name="showTipMsg" id="showTipMsg"/>
		<table width="1003px" border="0" align="center" cellpadding="0" cellspacing="0">
		  <tr>
		    <td height="630px" background="../images/login_bg_m.jpg">
				<div style="width:810px; margin:0px auto; margin-top:0px;border-bottom:2px solid #FF9900; height:50px;">
					<div style="float:left; width:350px;"><img src="../images/logo_login.jpg" width="307px" height="50px" /></div>
					<div style="float:right; width:200px; font-size:12px; text-align:right;">
						<a href="http://www.eefield.com/" target="_blank">进入我们公司网站</a></div>
				</div>
				<div style="width:810px; height:396px; background-image:url(../images/login_bg.jpg); margin:20px auto; clear:both;">
				  <table width="40%" border="0" align="right" cellpadding="0" cellspacing="0" style="font-size:12px;">
				    <tr>
				      <td colspan="2" height="36">&nbsp;</td>
				    </tr>
				    <tr>
				      <td colspan="2" height="30" style="padding-left:10px; font-weight:bold;font-size:14px; ">
				      	欢迎登录温湿度记录系统-WEB版
				      </td>
				    </tr>
				    <tr>
				      <td colspan="2" height="40">&nbsp;</td>
				    </tr>
				    <s:form namespace="/userdo" theme="simple" name="loadForm">
					    <tr>
					      <td width="50" height="60">用户名</td>
					      <td height="30"><s:textfield name="user.name" id="user_name" size="15"  cssClass="input"/></td>
					    </tr>
					    <tr>
					      <td width="50" height="60">密码</td>
					      <td height="30"><s:password name="user.password" id="user_pass" size="15" cssClass="input"/></td>
					    </tr>
					    <tr>
					      <td height="60">&nbsp;</td>
					      <td height="30">
					      <input type="button" class="login" value="现在登录" onclick="submitLoad()"/>
					      </td>
					    </tr>
				    </s:form>
				    <tr>
				      <td height="30" colspan="2">&nbsp;</td>
				    </tr>
				  </table>
				</div>
				<div style="text-align:center; font-size:12px; line-height:45px;">
					版权所有&copy;EEFIELD
				</div>  
				</td>
		  </tr>
		</table> 
		<script type="text/javascript">showMes($("#showTipMsg").val());</script>			
	</body>
</html>

