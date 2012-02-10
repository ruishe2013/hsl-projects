<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<%@ page language="java" contentType="text/html; charset=gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<script type="text/javascript" src="../js/script.js"></script>
	<script type="text/javascript" src="../js/topnav2.js"></script>
	<%--<script type="text/javascript" src="${pageContext.request.contextPath}/js/page/comName.js" ></script>--%>
	<link rel="StyleSheet" href="../css/css.css" type="text/css"	media="screen" />

	<head>
		<title>
			温湿度记录系统-WEB版
		</title>
		<meta http-equiv="x-ua-compatible" content="ie=7" /> 
		<meta http-equiv="Cache-Control" content="max-age=0" />
		<meta http-equiv="Cache-Control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="Expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
		<meta http-equiv="Pragma" content="no-cache" />
	</head> 
	<%-- 最外层开始 --%> 
	<div id="wrap">
	
		<div id="main" class="clearfix">
			<!-- logo -->
			<div class="top_w">
				<div class="top">
					<div id="top_left" style="text-align: center;">
						<s:property value="comName" />
						<%--<label id="comNamecc"></label>
						<script language="javascript">showCom();</script>--%> 
					</div>
					<div id="top_right">
						<img src="../images/logo.jpg"/><br />
						<s:if test="#session.managerName!=null">
							${sessionScope.managerName}，您已登录！
							<%-- 过滤厂家--%>
							<s:if test="#session.userpowertag != 86 ">
								<a href="${pageContext.request.contextPath}/userdo/user_perpass_Action?user.name=${sessionScope.managerName}&changeKey=1">
								个人信息</a>|
							</s:if>
						</s:if>
						<s:else>
							<a href="${pageContext.request.contextPath}/userdo/userload.action">登录	</a> |							
						</s:else>
						<a href="${pageContext.request.contextPath}/userdo/loginOutAction.action">退出</a>
					</div>
				</div>
			</div>
			
			<%-- 菜单开始 --%>
			<div id="menu_w">
				<div id="menu">
					<ul class="topnav">
						<li id="cur"><a href="${pageContext.request.contextPath}/main/MainBarAction">总览画面</a></li>
						<li><a href="${pageContext.request.contextPath}/main/MainLineAction">实时曲线</a></li>
						<li><a href="${pageContext.request.contextPath}/main/OnLineWarnAction">实时警报</a></li>
						<li><a href="${pageContext.request.contextPath}/main/warnDataAction">报警数据 </a></li>
						<li><a href="${pageContext.request.contextPath}/main/historyDataAction">历史数据</a>
							<ul class="subnav">
								<li><a href="${pageContext.request.contextPath}/main/historyDataAction">历史报表</a></li>
								<li><a href="${pageContext.request.contextPath}/main/historyFlashAction">历史曲线 </a></li>
								<li><a href="${pageContext.request.contextPath}/mana/sys_getLists_Action">历史备份 </a></li>
								<s:if test="showZj==2">
									<li><a href="${pageContext.request.contextPath}/main/hisZjDataAction">药监数据 </a></li>
								</s:if>								
							</ul>
						</li>
						<s:if test="#session.userpowertag >= 2">
							<li><a href="${pageContext.request.contextPath}/mana/equi_getList_Action">管理中心</a>
								<ul class="subnav">
									<li><a href="${pageContext.request.contextPath}/userdo/user_getList_Action">用户管理</a></li>
									<li><a href="${pageContext.request.contextPath}/mana/area_getList_Action">区域管理</a></li>
									<li><a href="${pageContext.request.contextPath}/mana/equi_getList_Action">设备管理</a></li>
								</ul>
							</li>
							<li><a href="${pageContext.request.contextPath}/mana/sys_initSys_Action">系统操作</a>
								<ul class="subnav">
									<s:if test="#session.userpowertag == 86">
										<li><a href="${pageContext.request.contextPath}/main/syslogAction">系统日志</a></li>
									</s:if>
									<li><a href="${pageContext.request.contextPath}/mana/sys_initSys_Action">系统设置</a></li>
			            <li><a href="${pageContext.request.contextPath}/sys/imexport">导入/导出配置</a></li>
								</ul>
							</li>
							<s:if test="showSms==2">
								<li><a href="${pageContext.request.contextPath}/mana/phone_getList_Action">无线设置</a>
									<ul class="subnav">
									<s:if test="#session.userpowertag >= 2">
										<li><a href="${pageContext.request.contextPath}/mana/gprsTestAction">通讯测试</a></li>
									</s:if>	
				            <!-- 
				            <li><a href="${pageContext.request.contextPath}/mana/gprs_getList_Action">短信格式</a></li>
				             -->
										<li><a href="${pageContext.request.contextPath}/mana/phone_getList_Action">通讯列表</a></li>
										<li><a href="${pageContext.request.contextPath}/main/smsRecAction">通讯历史</a></li>
									</ul>
								</li>
							</s:if>
						</s:if>
						<s:if test="#session.userpowertag >= 2">
							<li><a href="${pageContext.request.contextPath}/seri/serialportAction">串口管理</a>
								<ul class="subnav">
									<li><a href="${pageContext.request.contextPath}/seri/serialportAction">串口调试</a></li>
									<s:if test="#session.userpowertag == 86">
										<li><a href="${pageContext.request.contextPath}/seri/serialSetDataAction">串口设置</a></li>
									</s:if>	
								</ul>
							</li>
						</s:if>	
					</ul>
				</div>
			</div>
			<!-- 菜单 结束-->
		</div>
		<br />
	
		<div id="showFlash" class="flash_tip"></div>
		<script language="javascript">displayFlash();</script>			
		
