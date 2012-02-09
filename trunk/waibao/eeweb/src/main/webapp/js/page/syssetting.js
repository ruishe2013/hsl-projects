function submitForm(){
	var bool = true;			
	if (bool){bool = checkData("company_name","stringLen","名称");}
	if (bool){bool = checkData("syscolora","color","超出上限时显示的颜色");}
	if (bool){bool = checkData("syscolorb","color","正常运行时显示的颜色");}
	if (bool){bool = checkData("syscolorc","color","低于下限时显示的颜色");}
	
	if (bool){bool = checkData("seri_retry_time","integer1","通信出错重发次数");}
	if (bool){bool = checkData("seri_addition_time","integer1","发送帧附加毫秒数");}
	
	if (bool){bool = checkData("max_select_count_bar","integer1","总览数据:点数");}
	if (bool){bool = checkData("max_select_count_line","integer1","即时曲线:点数");}
	if (bool){bool = checkData("max_rs_count_line","integer1","即时曲线:结果数");}
	if (bool){bool = checkData("max_select_count_alarm","integer1","报警数据:点数");}
	if (bool){bool = checkData("max_rs_count_alarm","integer1","报警数据:结果数");}
	
	if (bool){bool = checkData("max_select_count_table","integer1","历史数据:点数");}
	if (bool){bool = checkData("max_rs_count_table_now","integer1","历史数据:流水范围");}
	if (bool){bool = checkData("max_rs_count_table_day","integer1","历史数据:日报范围");}
	if (bool){bool = checkData("max_rs_count_table_month","integer1","历史数据:月报范围");}
	
	if (bool){bool = checkData("max_select_count_flash","integer1","历史曲线:点数");}
	if (bool){bool = checkData("max_rs_count_flash_now","integer1","历史曲线:流水范围");}
	if (bool){bool = checkData("max_rs_count_flash_day","integer1","历史数据:日报范围");}
	if (bool){bool = checkData("max_rs_count_flash_month","integer1","历史数据:月报");}
	
	if (bool){bool = checkData("sms_center_number","PHONE","短信中心号格式不正确");}
	
	if (bool){																						
		$("#errorArea").hide(); // 隐藏信息区域
	 	document.sysArgsForm.action = "sys_update_Action";
	 	document.sysArgsForm.submit();
	 }
}

function checkAccess(){
	$("#errorArea").hide(); // 隐藏信息区域
	document.sysArgsForm.target="_blank";
	document.sysArgsForm.action = "sys_chkAccess_Action";
	document.sysArgsForm.submit();	
}

function checkSerial(){
	$("#errorArea").hide(); // 隐藏信息区域
	document.sysArgsForm.target="_blank";
 	document.sysArgsForm.action = "sys_chkSerial_Action";
 	document.sysArgsForm.submit();	
}

function showAuto(){
	$(function() {
		changebs(false);
	});	
}

function changebs(auto){
	$("#sounddl").text($("#bgsound").val()); 
	soundplay("sounddl",210,auto);
}	