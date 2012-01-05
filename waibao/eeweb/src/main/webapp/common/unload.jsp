<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=gbk" />

<title>没有登陆</title>

<style type="text/css">

<!--

body { 

	margin-left: 0px;	margin-top: 0px;	margin-right: 0px;	margin-bottom: 0px;	background-color:#EE9C0C;

}

-->

</style>

</head>



<body>

<table width="1003px" border="0" align="center" cellpadding="0" cellspacing="0">

  <tr>

		<td height="630px" background="../images/login_bg_m.jpg">

			<div style="width:810px; margin:0px auto; margin-top:0px;border-bottom:2px solid #FF9900; height:50px;">

				<div style="float:left; width:350px;"><img src="../images/logo_login.jpg" width="307" height="50" /></div>

				<div style="float:right; width:200px; font-size:12px; text-align:right;"><!-- <a href="#">进入我们公司网站</a> --></div>

			</div>

			

			<div style="width:810px; height:396px; background-image:url(../images/login_bg.jpg); margin:20px auto; clear:both;">

			  <table width="40%" border="0" align="right" cellpadding="0" cellspacing="0" style="font-size:12px;">

			    <tr>

			      <td height="36px" >&nbsp;</td>

			    </tr>

			    <tr>

			      <td height="30px" style="padding-left:10px; font-weight:bold;font-size:14px; ">您还没有登陆！</td>

			    </tr>

			    <tr>

			      <td height="180px" style="font-size:25px;">

							现在要<a href="${pageContext.request.contextPath}/userdo/userload.action">登陆</a>吗？      

			      </td>

			    </tr>

			  </table>

			</div>

			

			<div style="text-align:center; font-size:12px; line-height:45px;">版权所有&copy;EEFIELD</div>  

		</td>

  </tr>

</table> 

</body>

</html>