function submitForm(){
	var bool = true;			
	if (bool){bool = checkData("company_name","stringLen","����");}
	if (bool){bool = checkData("syscolora","color","��������ʱ��ʾ����ɫ");}
	if (bool){bool = checkData("syscolorb","color","��������ʱ��ʾ����ɫ");}
	if (bool){bool = checkData("syscolorc","color","��������ʱ��ʾ����ɫ");}
	
	if (bool){bool = checkData("seri_retry_time","integer1","ͨ�ų����ط�����");}
	if (bool){bool = checkData("seri_addition_time","integer1","����֡���Ӻ�����");}
	
	if (bool){bool = checkData("max_select_count_bar","integer1","��������:����");}
	if (bool){bool = checkData("max_select_count_line","integer1","��ʱ����:����");}
	if (bool){bool = checkData("max_rs_count_line","integer1","��ʱ����:�����");}
	if (bool){bool = checkData("max_select_count_alarm","integer1","��������:����");}
	if (bool){bool = checkData("max_rs_count_alarm","integer1","��������:�����");}
	
	if (bool){bool = checkData("max_select_count_table","integer1","��ʷ����:����");}
	if (bool){bool = checkData("max_rs_count_table_now","integer1","��ʷ����:��ˮ��Χ");}
	if (bool){bool = checkData("max_rs_count_table_day","integer1","��ʷ����:�ձ���Χ");}
	if (bool){bool = checkData("max_rs_count_table_month","integer1","��ʷ����:�±���Χ");}
	
	if (bool){bool = checkData("max_select_count_flash","integer1","��ʷ����:����");}
	if (bool){bool = checkData("max_rs_count_flash_now","integer1","��ʷ����:��ˮ��Χ");}
	if (bool){bool = checkData("max_rs_count_flash_day","integer1","��ʷ����:�ձ���Χ");}
	if (bool){bool = checkData("max_rs_count_flash_month","integer1","��ʷ����:�±�");}
	
	if (bool){bool = checkData("sms_center_number","PHONE","�������ĺŸ�ʽ����ȷ");}
	
	if (bool){																						
		$("#errorArea").hide(); // ������Ϣ����
	 	document.sysArgsForm.action = "sys_update_Action";
	 	document.sysArgsForm.submit();
	 }
}

function checkAccess(){
	$("#errorArea").hide(); // ������Ϣ����
	document.sysArgsForm.target="_blank";
	document.sysArgsForm.action = "sys_chkAccess_Action";
	document.sysArgsForm.submit();	
}

function checkSerial(){
	$("#errorArea").hide(); // ������Ϣ����
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