function submitForm(){
	var bool = true;
	var startTime,endTime;
	var doSetPerson = "";
	
	if(bool) {bool = checkboxSize("warnBox", "����ѡ��һ��[��������]");}
	var enterPerson = $(":checkbox[id='doperson']:checked").size() == 1;
	if (enterPerson&&bool){
		bool = checkData("do_reset_person","stringLen","[��λ��Ա]");
		doSetPerson = "&doSetPerson="+$("#do_reset_person").val();
	}
	
	var showmsg = "����ʱ�䳬����Χ.";
	if (bool){bool = checkData("maxcount","integer1","����ǰ��������¼");}
	if (bool){bool = checkData("fromTime","stringLen","��ʼʱ�䲻��Ϊ��.");}
	if (bool){bool = checkData("toTime","stringLen","����ʱ�䲻��Ϊ��.");}
	if (bool){startTime = $("#fromShow").val();	endTime = $("#toShow").val();}
	// ���ʱ�䷶Χ
	if (bool){bool = checkDate_time("YEAR",startTime,endTime,$("#max_rs_alarm").val(),showmsg);}
	// ���ѡ�����
	if(bool){bool = checkEQLen("EQLEN","ѡ�� �������������ܴ���",1);}
	
	if(bool){
		$("#errorArea").hide(); // ������Ϣ����
		var userPlaceList = getEQList();
		var actionName = "warnDataAction!getWarnList";
		userPlaceList = userPlaceList.substring(0,userPlaceList.length-1);//�ض����һ������
		$("#placeListids").val(userPlaceList);
 		document.warnForm.action = actionName;
		document.warnForm.target="_blank";	 		
	 	document.warnForm.submit();
	}
}

$(function() {
	$(":radio[name='resetBox']").click(function() {
		switch (this.value) {
			case "1":
				$("#statresetLabel").show();
				$("#doperson_label").show();
				break;
			case "2":
				$("#statwarn").attr("checked",true);//��
				$("#statreset").attr("checked",false);//����
				$("#doperson").attr("checked",false);//����
				$("#statresetLabel").hide();
				$("#doperson_label").hide();
				break;
		}
	});// end $(":radio
	
	$("a[id^='sel']").click(function(){
		$("#_"+$(this).attr("id")).find(":checkbox").each(function(){
				$(this).attr("checked", $(this).attr("checked")==true?false:true);
		});
	});// end $("a[id		
	
}); //end fun