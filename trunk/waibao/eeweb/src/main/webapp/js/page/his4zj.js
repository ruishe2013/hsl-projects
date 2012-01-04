function submitForm(){
	var bool = true;
	var startTime,endTime;
	var actionName = "hisZjDataAction!hisZjRec";
	bool = checkData("tempDown","float","温度范围-下限.");
	if(bool){bool = checkData("tempUp","float","温度范围-上限.");}
	if(bool){bool = checkMaxMin("tempDown","tempUp","温度下限不能大于或者等于上限");}	
	if(bool){bool = checkData("humiDown","float","湿度范围-下限.");}
	if(bool){bool = checkData("humiUp","float","湿度范围-上限.");}
	if(bool){bool = checkMaxMin("humiDown","humiUp","湿度下限不能大于或者等于上限");}	
	if(bool){bool = checkData("fromTime_A","stringLen","开始时间不能为空.");}
	if(bool){bool = checkData("toTime_A","stringLen","结束时间不能为空.");}
	if (bool){startTime = $("#fromShow_A").val();	endTime = $("#toShow_A").val();}
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
	$("a[id^='sel']").click(function(){
		$("#_"+$(this).attr("id")).find(":checkbox").each(function(){
				$(this).attr("checked", $(this).attr("checked")==true?false:true);
		});
	});
}); 