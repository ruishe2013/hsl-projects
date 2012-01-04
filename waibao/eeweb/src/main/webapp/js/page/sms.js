// 修改短信关联
function submitForm(){
	var bool = true;
	if(bool){bool = checkData("fromTime","stringLen","开始时间不能为空.");}
	if(bool){bool = checkData("toTime","stringLen","结束时间不能为空.");}
	if(bool){bool = checkboxSize("typeStr", "至少选择一种[查询类型]");}	
	if(bool){bool = checkPhoneLen();}
	
	if(bool){
		var phoneStr = getEQList();
		phoneStr = phoneStr.substring(0,phoneStr.length-1);//截断最后一个逗号
		$("#phoneStr").val(phoneStr);
		
		$("#errorArea").hide(); // 隐藏信息区域
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

