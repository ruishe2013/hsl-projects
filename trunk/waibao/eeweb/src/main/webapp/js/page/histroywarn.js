function submitForm(){
	var bool = true;
	var startTime,endTime;
	var doSetPerson = "";
	
	if(bool) {bool = checkboxSize("warnBox", "至少选择一种[报警类型]");}
	var enterPerson = $(":checkbox[id='doperson']:checked").size() == 1;
	if (enterPerson&&bool){
		bool = checkData("do_reset_person","stringLen","[复位人员]");
		doSetPerson = "&doSetPerson="+$("#do_reset_person").val();
	}
	
	var showmsg = "搜索时间超出范围.";
	if (bool){bool = checkData("maxcount","integer1","返回前多少条记录");}
	if (bool){bool = checkData("fromTime","stringLen","开始时间不能为空.");}
	if (bool){bool = checkData("toTime","stringLen","结束时间不能为空.");}
	if (bool){startTime = $("#fromShow").val();	endTime = $("#toShow").val();}
	// 检测时间范围
	if (bool){bool = checkDate_time("YEAR",startTime,endTime,$("#max_rs_alarm").val(),showmsg);}
	// 检测选择个数
	if(bool){bool = checkEQLen("EQLEN","选择 的仪器个数不能大于",1);}
	
	if(bool){
		$("#errorArea").hide(); // 隐藏信息区域
		var userPlaceList = getEQList();
		var actionName = "warnDataAction!getWarnList";
		userPlaceList = userPlaceList.substring(0,userPlaceList.length-1);//截断最后一个逗号
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
				$("#statwarn").attr("checked",true);//打勾
				$("#statreset").attr("checked",false);//不打勾
				$("#doperson").attr("checked",false);//不打勾
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