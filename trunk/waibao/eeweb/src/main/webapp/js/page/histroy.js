//type(1:流水记录 2:日报表 3:月报表)
function submitForm(){
	var type = $(":radio:checked[name='report_type']").val(); // 报表类型
	var startTime,endTime;
	var bool = true;
	var showmsg = "搜索时间超出范围.";
	var actionName = $("#showType").val() == "1" ? "historyDataAction!" : "historyFlashAction!";
	bool =$("#showType").val()=="1"?checkboxSize("statBox", "至少选择一种统计类型,平均值,最大值 或最小值"): true;
	if (!bool){return;}
	switch (type) {
		case "1":
			bool = checkData("fromTime_A","stringLen","开始时间不能为空.");
			if (bool){bool = checkData("toTime_A","stringLen","结束时间不能为空.");}
			if (bool){startTime = $("#fromShow_A").val();	endTime = $("#toShow_A").val();}
			//检测时间范围
			if (bool){bool = checkDate_time("MINUTE",startTime,endTime,$("#max_rs_now").val()*$("#interval").val(),showmsg);}
			actionName += $("#showType").val() == "1" ? "freeTime" : "freeTimeJson";
			break;
		case "2":
			bool = checkData("fromTime_B","stringLen","开始时间不能为空.");
			if (bool){bool = checkData("toTime_B","stringLen","结束时间不能为空.");}
			if (bool){startTime = $("#fromShow_B").val();	endTime = $("#toShow_B").val();}
			//检测时间范围
			if (bool){bool = checkDate_time("DAY",startTime,endTime,$("#max_rs_day").val(),showmsg);}
			actionName += $("#showType").val() == "1" ? "daily" : "dailyJson";
			break;
		case "3":
			bool = checkData("fromTime_C","stringLen","开始时间不能为空.");
			if (bool){bool = checkData("toTime_C","stringLen","结束时间不能为空.");}
			if (bool){
				startTime = $("#fromShow_C").val().replace("dd","01");	
				endTime = $("#toShow_C").val().replace("dd","01");
			}
			//检测时间范围
			if (bool){bool = checkDate_time("MONTH",startTime,endTime,$("#max_rs_month").val(),showmsg);}
			actionName += $("#showType").val() == "1" ? "month" : "monthJson";										
			break;
	}		
	
	if(bool){bool = checkEQLen("EQLEN","选择 的仪器个数不能大于",1);}
	
	if(bool){
		$("#errorArea").hide(); // 隐藏信息区域
		var userPlaceList = getEQList();
		userPlaceList = userPlaceList.substring(0,userPlaceList.length-1);//截断最后一个逗号
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
	 	var showName = showType == 1? "报表" : "曲线";
		switch (this.value) {
			case "1":
				$("#submit_label").text(" 查询 [流水" + showName + "] ");
				$("#interval_label").show();	$("#interval").show();	
				$(".fromTo_A").show();				$(".fromTo_B").hide();				$(".fromTo_C").hide();
				showName = $("#max_rs_now").val()*$("#interval").val() + "分钟";
				$("#areaTipLabel").text(showName);
				
				break;
			case "2":
				$("#submit_label").text(" 查询 [日" + showName + "] ");
				$("#interval_label").hide();	$("#interval").hide();	
				$(".fromTo_A").hide();				$(".fromTo_B").show();				$(".fromTo_C").hide();
				showName = $("#max_rs_day").val() + "天";
				$("#areaTipLabel").text(showName);
				break;
			case "3":
				$("#submit_label").text(" 查询 [月" + showName + "] ");
				$("#interval_label").hide();	$("#interval").hide();	
				$(".fromTo_A").hide();				$(".fromTo_B").hide();				$(".fromTo_C").show();
				showName = $("#max_rs_month").val() + "月";
				$("#areaTipLabel").text(showName);
				break;
			}
	});
	
	$("#interval").click(function(){
		$("#areaTipLabel").text($("#max_rs_now").val()*$("#interval").val() + "分钟");
	});
	
	$("a[id^='sel']").click(function(){
		$("#_"+$(this).attr("id")).find(":checkbox").each(function(){
				$(this).attr("checked", $(this).attr("checked")==true?false:true);
		});
	});
	
}); 