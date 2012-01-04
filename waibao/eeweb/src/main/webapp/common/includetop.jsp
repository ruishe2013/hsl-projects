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
			��ʪ�ȼ�¼ϵͳ-WEB��
		</title>
		<meta http-equiv="x-ua-compatible" content="ie=7" /> 
		<meta http-equiv="Cache-Control" content="max-age=0" />
		<meta http-equiv="Cache-Control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="Expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
		<meta http-equiv="Pragma" content="no-cache" />
	</head> 
	<%-- ����㿪ʼ --%> 
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
							${sessionScope.managerName}�����ѵ�¼��
							<%-- ���˳���--%>
							<s:if test="#session.userpowertag != 86 ">
								<a href="${pageContext.request.contextPath}/userdo/user_perpass_Action?user.name=${sessionScope.managerName}&changeKey=1">
								������Ϣ</a>|
							</s:if>
						</s:if>
						<s:else>
							<a href="${pageContext.request.contextPath}/userdo/userload.action">��¼	</a> |							
						</s:else>
						<a href="${pageContext.request.contextPath}/userdo/loginOutAction.action">�˳�</a>
					</div>
				</div>
			</div>
			
			<%-- �˵���ʼ --%>
			<div id="menu_w">
				<div id="menu">
					<ul class="topnav">
						<li id="cur"><a href="${pageContext.request.contextPath}/main/MainBarAction">��������</a></li>
						<li><a href="${pageContext.request.contextPath}/main/MainLineAction">ʵʱ����</a></li>
						<li><a href="${pageContext.request.contextPath}/main/OnLineWarnAction">ʵʱ����</a></li>
						<li><a href="${pageContext.request.contextPath}/main/warnDataAction">�������� </a></li>
						<li><a href="${pageContext.request.contextPath}/main/historyDataAction">��ʷ����</a>
							<ul class="subnav">
								<li><a href="${pageContext.request.contextPath}/main/historyDataAction">��ʷ����</a></li>
								<li><a href="${pageContext.request.contextPath}/main/historyFlashAction">��ʷ���� </a></li>
								<li><a href="${pageContext.request.contextPath}/mana/sys_getLists_Action">��ʷ���� </a></li>
								<s:if test="showZj==2">
									<li><a href="${pageContext.request.contextPath}/main/hisZjDataAction">ҩ������ </a></li>
								</s:if>								
							</ul>
						</li>
						<s:if test="#session.userpowertag >= 2">
							<li><a href="${pageContext.request.contextPath}/mana/equi_getList_Action">��������</a>
								<ul class="subnav">
									<li><a href="${pageContext.request.contextPath}/userdo/user_getList_Action">�û�����</a></li>
									<li><a href="${pageContext.request.contextPath}/mana/area_getList_Action">�������</a></li>
									<li><a href="${pageContext.request.contextPath}/mana/equi_getList_Action">�豸����</a></li>
								</ul>
							</li>
							<li><a href="${pageContext.request.contextPath}/mana/sys_initSys_Action">ϵͳ����</a>
								<ul class="subnav">
									<s:if test="#session.userpowertag == 86">
										<li><a href="${pageContext.request.contextPath}/main/syslogAction">ϵͳ��־</a></li>
									</s:if>
									<li><a href="${pageContext.request.contextPath}/mana/sys_initSys_Action">ϵͳ����</a></li>
			            <li><a href="${pageContext.request.contextPath}/sys/imexport">����/��������</a></li>
								</ul>
							</li>
							<s:if test="showSms==2">
								<li><a href="${pageContext.request.contextPath}/mana/phone_getList_Action">��������</a>
									<ul class="subnav">
										<s:if test="#session.userpowertag == 86">
											<li><a href="${pageContext.request.contextPath}/mana/gprsTestAction">ͨѶ����</a></li>
										</s:if>
				            <!-- 
				            <li><a href="${pageContext.request.contextPath}/mana/gprs_getList_Action">���Ÿ�ʽ</a></li>
				             -->
										<li><a href="${pageContext.request.contextPath}/mana/phone_getList_Action">ͨѶ�б�</a></li>
										<li><a href="${pageContext.request.contextPath}/main/smsRecAction">ͨѶ��ʷ</a></li>
									</ul>
								</li>
							</s:if>
						</s:if>
						<s:if test="#session.userpowertag == 86">
							<li><a href="${pageContext.request.contextPath}/seri/serialportAction">���ڹ���</a>
								<ul class="subnav">
									<li><a href="${pageContext.request.contextPath}/seri/serialportAction">���ڵ���</a></li>
									<li><a href="${pageContext.request.contextPath}/seri/serialSetDataAction">��������</a></li>
								</ul>
							</li>
						</s:if>	
					</ul>
				</div>
			</div>
			<!-- �˵� ����-->
		</div>
		<br />
	
		<div id="showFlash" class="flash_tip"></div>
		<script language="javascript">displayFlash();</script>			
		
