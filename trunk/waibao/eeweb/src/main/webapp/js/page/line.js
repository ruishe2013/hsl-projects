function autoFalsh(){
	// 系统仪器类型 
	var sysEqCount = Number($("#systemEqType").val());
	
	// 启动flash
	var dataAction = "${pageContext.request.contextPath}/main/tempDataAction.action";
	var configAction = "${pageContext.request.contextPath}/main/tempconfigAction.action";
	if (sysEqCount !=3){
		var so = new SWFObject("../js/amline.swf", "amline_temp", "100%", "375", "8", "#EEEEEE");
		so.addVariable("path", "../js/");
		so.addVariable("chart_id", "amline_temp");
		so.addVariable("settings_file", encodeURIComponent(configAction));
		so.addVariable("data_file", encodeURIComponent(dataAction));
		so.addVariable("loading_data", "正在加载...");   
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
		so.addVariable("loading_data", "正在加载..."); 
		so.addParam("wmode", "opaque");   			
		so.write("amlinehumi");		
		if (sysEqCount==3){$("#amlinetemp").text("");}
	}
}

$(function() {
	//进入页面调用,用来显示图表 
	autoFalsh();
	
	$("a[id^='sel']").click(function(){
		$("#_"+$(this).attr("id")).find(":checkbox").each(function(){
				$(this).attr("checked", $(this).attr("checked")==true?false:true);
		});
	});// end $("a[id^='sel']") 		
	
});// $(function(      	

function submitEQ(){
	$("#errorArea").hide(); // 隐藏信息区域
	var bool = checkEQLen("EQLEN","选择 的仪器个数不能大于");
	if (bool){
		var userPlaceList = getEQList();
		userPlaceList = userPlaceList.substring(0,userPlaceList.length-1);//截断最后一个逗号
		$("#userPlaceList").val(userPlaceList);			
 		document.lineSetEQForm.action = "MainLineAction!updateUserConfig";
		ableToButton(0); //所用按钮变灰
		$("#errorArea").hide(); // 隐藏信息区域
	 	document.lineSetEQForm.submit();
	}	
}

var timeoutID = null;
function callRun() {
	var actionPath = '${pageContext.request.contextPath}/main/soundJson.action';
	$.post(actionPath, null, function(data) {
		//将JSON转化为一个对象,并且名为joson
		var json = eval("(" + data +")");
		// 背景音乐
		if (json.palyFalg == 2){ 
			if ($("#sounddl").text()==""){//背景声音开启
				$("#sounddl").text($("#alarmPlayFile").val());
				soundplay("sounddl",100,true);					
			}
		}else{
			if ($("#sounddl").text()!=""){//背景声音关闭
				$("#sounddl").text("");
			}					
		}				
		// 计时器运行
		if (timeoutID != null){	window.clearTimeout(timeoutID); }
		timeoutID = setTimeout("callRun()" ,Number($("#falshTime").val()) * 1000);
	});//end post
}	

function toggleDiv(){
	$("#addressList").toggle();
}