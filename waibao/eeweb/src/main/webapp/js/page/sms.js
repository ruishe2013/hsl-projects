// �޸Ķ��Ź���
function submitForm(){
	var bool = true;
	if(bool){bool = checkData("fromTime","stringLen","��ʼʱ�䲻��Ϊ��.");}
	if(bool){bool = checkData("toTime","stringLen","����ʱ�䲻��Ϊ��.");}
	if(bool){bool = checkboxSize("typeStr", "����ѡ��һ��[��ѯ����]");}	
	if(bool){bool = checkPhoneLen();}
	
	if(bool){
		var phoneStr = getEQList();
		phoneStr = phoneStr.substring(0,phoneStr.length-1);//�ض����һ������
		$("#phoneStr").val(phoneStr);
		
		$("#errorArea").hide(); // ������Ϣ����
		document.smsForm.action = "smsRecAction!searchRec";
		document.smsForm.target="_blank";	 
		document.smsForm.submit();
	}
	
}// end fun

function cbt(){
	$("#_selSms").find(":checkbox").each(function(){
		$(this).attr("checked", $(this).attr("checked")==true?false:true);
	});	
}

