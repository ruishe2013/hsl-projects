function submitForm(){
	var bool = true;
	var startTime,endTime;
	var actionName = "hisZjDataAction!hisZjRec";
	bool = checkData("tempDown","float","�¶ȷ�Χ-����.");
	if(bool){bool = checkData("tempUp","float","�¶ȷ�Χ-����.");}
	if(bool){bool = checkMaxMin("tempDown","tempUp","�¶����޲��ܴ��ڻ��ߵ�������");}	
	if(bool){bool = checkData("humiDown","float","ʪ�ȷ�Χ-����.");}
	if(bool){bool = checkData("humiUp","float","ʪ�ȷ�Χ-����.");}
	if(bool){bool = checkMaxMin("humiDown","humiUp","ʪ�����޲��ܴ��ڻ��ߵ�������");}	
	if(bool){bool = checkData("fromTime_A","stringLen","��ʼʱ�䲻��Ϊ��.");}
	if(bool){bool = checkData("toTime_A","stringLen","����ʱ�䲻��Ϊ��.");}
	if (bool){startTime = $("#fromShow_A").val();	endTime = $("#toShow_A").val();}
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
	$("a[id^='sel']").click(function(){
		$("#_"+$(this).attr("id")).find(":checkbox").each(function(){
				$(this).attr("checked", $(this).attr("checked")==true?false:true);
		});
	});
}); 