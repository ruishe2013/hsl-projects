function autoFalsh(){
	// ϵͳ�������� 
	var sysEqCount = Number($("#systemEqType").val());
	
	// ����flash
	var dataAction = "${pageContext.request.contextPath}/main/tempDataAction.action";
	var configAction = "${pageContext.request.contextPath}/main/tempconfigAction.action";
	if (sysEqCount !=3){
		var so = new SWFObject("../js/amline.swf", "amline_temp", "100%", "375", "8", "#EEEEEE");
		so.addVariable("path", "../js/");
		so.addVariable("chart_id", "amline_temp");
		so.addVariable("settings_file", encodeURIComponent(configAction));
		so.addVariable("data_file", encodeURIComponent(dataAction));
		so.addVariable("loading_data", "���ڼ���...");   
		so.addParam("wmode", "opaque"); 			
		so.write("amlinetemp");
		if (sysEqCount == 2){$("#amlinehumi").text("");}
	}
	
	if (sysEqCount !=2){
		dataAction = "${pageContext.request.contextPath}/main/humiDataAction.action";				
		configAction = "${pageContext.request.contextPath}/main/humiconfigAction.action";
		so = new SWFObject("../js/amline.swf", "amline_humi", "100%", "375", "8", "#EEEEEE");
		so.addVariable("path", "../js/");
		so.addVariable("chart_id", "amline_humi");
		so.addVariable("settings_file", encodeURIComponent(configAction));
		so.addVariable("data_file", encodeURIComponent(dataAction));
		so.addVariable("loading_data", "���ڼ���..."); 
		so.addParam("wmode", "opaque");   			
		so.write("amlinehumi");		
		if (sysEqCount==3){$("#amlinetemp").text("");}
	}
}

$(function() {
	//����ҳ�����,������ʾͼ�� 
	autoFalsh();
	
	$("a[id^='sel']").click(function(){
		$("#_"+$(this).attr("id")).find(":checkbox").each(function(){
				$(this).attr("checked", $(this).attr("checked")==true?false:true);
		});
	});// end $("a[id^='sel']") 		
	
});// $(function(      	

function submitEQ(){
	$("#errorArea").hide(); // ������Ϣ����
	var bool = checkEQLen("EQLEN","ѡ�� �������������ܴ���");
	if (bool){
		var userPlaceList = getEQList();
		userPlaceList = userPlaceList.substring(0,userPlaceList.length-1);//�ض����һ������
		$("#userPlaceList").val(userPlaceList);			
 		document.lineSetEQForm.action = "MainLineAction!updateUserConfig";
		ableToButton(0); //���ð�ť���
		$("#errorArea").hide(); // ������Ϣ����
	 	document.lineSetEQForm.submit();
	}	
}

var timeoutID = null;
function callRun() {
	var actionPath = '${pageContext.request.contextPath}/main/soundJson.action';
	$.post(actionPath, null, function(data) {
		//��JSONת��Ϊһ������,������Ϊjoson
		var json = eval("(" + data +")");
		// ��������
		if (json.palyFalg == 2){ 
			if ($("#sounddl").text()==""){//������������
				$("#sounddl").text($("#alarmPlayFile").val());
				soundplay("sounddl",100,true);					
			}
		}else{
			if ($("#sounddl").text()!=""){//���������ر�
				$("#sounddl").text("");
			}					
		}				
		// ��ʱ������
		if (timeoutID != null){	window.clearTimeout(timeoutID); }
		timeoutID = setTimeout("callRun()" ,Number($("#falshTime").val()) * 1000);
	});//end post
}	

function toggleDiv(){
	$("#addressList").toggle();
}