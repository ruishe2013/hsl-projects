//type(1:��ˮ��¼ 2:�ձ��� 3:�±���)
function submitForm(){
	var type = $(":radio:checked[name='report_type']").val(); // ��������
	var startTime,endTime;
	var bool = true;
	var showmsg = "����ʱ�䳬����Χ.";
	var actionName = $("#showType").val() == "1" ? "historyDataAction!" : "historyFlashAction!";
	bool =$("#showType").val()=="1"?checkboxSize("statBox", "����ѡ��һ��ͳ������,ƽ��ֵ,���ֵ ����Сֵ"): true;
	if (!bool){return;}
	switch (type) {
		case "1":
			bool = checkData("fromTime_A","stringLen","��ʼʱ�䲻��Ϊ��.");
			if (bool){bool = checkData("toTime_A","stringLen","����ʱ�䲻��Ϊ��.");}
			if (bool){startTime = $("#fromShow_A").val();	endTime = $("#toShow_A").val();}
			//���ʱ�䷶Χ
			if (bool){bool = checkDate_time("MINUTE",startTime,endTime,$("#max_rs_now").val()*$("#interval").val(),showmsg);}
			actionName += $("#showType").val() == "1" ? "freeTime" : "freeTimeJson";
			break;
		case "2":
			bool = checkData("fromTime_B","stringLen","��ʼʱ�䲻��Ϊ��.");
			if (bool){bool = checkData("toTime_B","stringLen","����ʱ�䲻��Ϊ��.");}
			if (bool){startTime = $("#fromShow_B").val();	endTime = $("#toShow_B").val();}
			//���ʱ�䷶Χ
			if (bool){bool = checkDate_time("DAY",startTime,endTime,$("#max_rs_day").val(),showmsg);}
			actionName += $("#showType").val() == "1" ? "daily" : "dailyJson";
			break;
		case "3":
			bool = checkData("fromTime_C","stringLen","��ʼʱ�䲻��Ϊ��.");
			if (bool){bool = checkData("toTime_C","stringLen","����ʱ�䲻��Ϊ��.");}
			if (bool){
				startTime = $("#fromShow_C").val().replace("dd","01");	
				endTime = $("#toShow_C").val().replace("dd","01");
			}
			//���ʱ�䷶Χ
			if (bool){bool = checkDate_time("MONTH",startTime,endTime,$("#max_rs_month").val(),showmsg);}
			actionName += $("#showType").val() == "1" ? "month" : "monthJson";										
			break;
	}		
	
	if(bool){bool = checkEQLen("EQLEN","ѡ�� �������������ܴ���",1);}
	
	if(bool){
		$("#errorArea").hide(); // ������Ϣ����
		var userPlaceList = getEQList();
		userPlaceList = userPlaceList.substring(0,userPlaceList.length-1);//�ض����һ������
		$("#timeFrom").val(startTime);
		$("#timeTo").val(endTime);
		$("#placeListids").val(userPlaceList);
 		document.statForm.action = actionName;
		document.statForm.target="_blank";	 		
	 	document.statForm.submit();
	}
}

$(function() {
	$(".fromTo_A").show();	$(".fromTo_B").hide();	$(".fromTo_C").hide();
	
	$(":radio[name='report_type']").click(function() {
	 	var showType = $("#showType").val();
	 	var showName = showType == 1? "����" : "����";
		switch (this.value) {
			case "1":
				$("#submit_label").text(" ��ѯ [��ˮ" + showName + "] ");
				$("#interval_label").show();	$("#interval").show();	
				$(".fromTo_A").show();				$(".fromTo_B").hide();				$(".fromTo_C").hide();
				showName = $("#max_rs_now").val()*$("#interval").val() + "����";
				$("#areaTipLabel").text(showName);
				
				break;
			case "2":
				$("#submit_label").text(" ��ѯ [��" + showName + "] ");
				$("#interval_label").hide();	$("#interval").hide();	
				$(".fromTo_A").hide();				$(".fromTo_B").show();				$(".fromTo_C").hide();
				showName = $("#max_rs_day").val() + "��";
				$("#areaTipLabel").text(showName);
				break;
			case "3":
				$("#submit_label").text(" ��ѯ [��" + showName + "] ");
				$("#interval_label").hide();	$("#interval").hide();	
				$(".fromTo_A").hide();				$(".fromTo_B").hide();				$(".fromTo_C").show();
				showName = $("#max_rs_month").val() + "��";
				$("#areaTipLabel").text(showName);
				break;
			}
	});
	
	$("#interval").click(function(){
		$("#areaTipLabel").text($("#max_rs_now").val()*$("#interval").val() + "����");
	});
	
	$("a[id^='sel']").click(function(){
		$("#_"+$(this).attr("id")).find(":checkbox").each(function(){
				$(this).attr("checked", $(this).attr("checked")==true?false:true);
		});
	});
	
}); 