//创建图表显示单元 根据id创建单元 ,type(0:清除内容  非0:不清除内容)
function autoCreateDiv(sysEqCount, equipmentId, type, workPlaceId){
	if (type == 0){
		$("#resultDiv").empty();
		$("#resultDiv").append("<div id=\"result_data_container\" >");		
	}
	var elements =
	"<li><table width=\"242px\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">" +
	"	<tr><td class=\"bar_head\"><a href=\"#\" id=\"label" + equipmentId + "\" onclick=\"jumpToLine("+equipmentId+")\" style=\"color:#fff;\"></a>" +
	"	</td></tr>" +
	"	<tr><td class=\"bar_body\">" +
	"		<table width=\"238px\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">" + 
			(sysEqCount !=3?"<tr><td id=\"temp" + equipmentId + "\"></td>" +
					"<td class=\"barfone\" id=\"temp" + equipmentId + "_t\"></td></tr>":"") +
			(sysEqCount !=2?"<tr><td id=\"humi" + equipmentId + "\"></td>" +
					"<td class=\"barfone\" id=\"humi" + equipmentId + "_h\"></td></tr>":"") +
	"		</table>" +
	"	</td></tr>" + 	
	"	<tr><td height=\"24px\" class=\"bar_foot\">" +
	"		<table width=\"94%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">" +
	"			<tr><td width=\"50%\" id=\"state" + equipmentId + "\">运行正常</td>" +
	"				<td align=\"right\" id=\"power" + equipmentId + "\">电量过低</td>" +
	"			</tr>" +
	"		</table></td>" +
	"	</tr>" + 
	"</table></li>";
	
	var workPlaceDiv = queryOrCreateWorkPlaceDiv(workPlaceId);
	workPlaceDiv.append(elements);
}


function barsDisplayMode(mode) {
	setCookie('bars_display_mode', mode);
	location.reload();
}

function getBarsDisplayMode() {
	if (typeof window.g_displayMode == "undefined" || !window.g_displayMode) {
		window.g_displayMode = getCookie('bars_display_mode');
	}
	return window.g_displayMode;
}

function setCookie(key, value) {
	var date = new Date();
	date.setYear(2030);
	var cookieString = key + "=" + value + "; expires=" + date.toGMTString();
	document.cookie = cookieString;
}


function getCookie(key) {
	var cookie = document.cookie;
	if (!cookie) {
		return null;
	}
	var parts = cookie.split(';');
	for(var partIndex in parts) {
		var part = parts[partIndex];
		var pos = part.indexOf('=');
		if (pos < 0) {
			continue;
		}
		var name = part.substring(0, pos);
		name = jQuery.trim(name);
		if (name == key) {
			var ret = part.substring(pos+1);
			return jQuery.trim(ret);
		}
	}
	return null;
}

function queryOrCreateWorkPlaceDiv(workPlaceId) {
	var displayMode = getBarsDisplayMode();
	if ( 1 == displayMode) {
		// 每个区域独立显示
		
	} else if ( 2 == displayMode) {
		// 所有区域合并显示
		
		workPlaceId = "0";
	}
	var workPlaceAttrId = "workPlaceId-" + workPlaceId;
	var obj = $("#" + workPlaceAttrId);
	if (obj.length > 0) {
		return obj;
	}
	$("#result_data_container").append("<ul id=\""+ workPlaceAttrId + "\" style=\"display:block; \"></ul><div style=\"clear:both;\"></div>");
	return $("#" + workPlaceAttrId);
}

function autoAdjustSize() {
	var adjustData = getAdjustData();
	var padding = adjustData.padding;
	var width = adjustData.width;
	//$("#test_msg2").text('padding: ' + padding + ", width:" + width);
	$("#result_data_container").css('padding-left', padding);
}

function getAdjustData() {
	var w = $(document).width();
	//$("#test_msg").text('document width:' + w);
	var a = 0;
	var b = 248;
	var n = 1;
	var x = (w-2*a-n*b);
	while (x > b) {
		++n;
		x = (w-2*a-n*b);
	}
	return {'padding': x/2, 'width': (b * n)};
}

function jumpToLine(equipmentId){
	$("#jumpPlaceList").val(equipmentId);
	document.jumpToLineForm.action = "MainLineAction!jumpToLine";
	document.jumpToLineForm.submit();	
}

//填充图表数据
function fillDivData(sysEqCount, equipmentId, temp, appt, humi, apph, label, cTemp, cHumi, stateStr, power){
	$("#label" + equipmentId).attr("title",label);
	$("#label" + equipmentId).text(CutStrLength(label,32));
	if(sysEqCount !=3){
		$("#temp" + equipmentId).text(temp);
		$("#temp" + equipmentId).css({background : cTemp});
		$("#temp" + equipmentId+"_t").text(appt);
		$("#temp" + equipmentId+"_t").css({background : cTemp});
	}
	if (sysEqCount !=2){
		$("#humi" + equipmentId).text(humi);
		$("#humi" + equipmentId).css({background : cHumi});
		$("#humi" + equipmentId+"_h").text(apph);
		$("#humi" + equipmentId+"_h").css({background : cHumi});		
	}
	$("#state" + equipmentId).text(stateStr);
	$("#power" + equipmentId).text(power);
}

//发送异步请求callType:1 第一次进入页面   2:计时器动态实时
var timeoutID = null;
function callRun(callType) {
	var actionPath = '${pageContext.request.contextPath}/main/barChartJson.action';
	var paramArgs;
	var userPlaceList;
	
	// 第一次进入页面-显示"正在加载..."
	if (callType == 1){
		$("#loadingDiv").html("正在加载...");
	}
	
	
	// 发送用户选择的地址列表
	userPlaceList = $("input:hidden[name='userPlaceList']").val();
	if ( (userPlaceList == undefined) || (userPlaceList == "0")) { // 地址列表不为空才发送请求
		$("#loadingDiv").html("还没选择仪器列表...");
	}else{
		//发送请求
		try {
			paramArgs = {"userPlaceList": userPlaceList};
		} catch (e) {
			alert(e.description);
			return;
		}
		$.post(actionPath, paramArgs, function(data) {
		//	$("#test_div").text(data);
			//将JSON转化为一个对象,并且名为json
			var json = null;
			try {
				json = eval("(" + data +")");
			} catch (e) {
				alert(e.description+ ":" + data);
				return;
			}
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
		        	
		  // 系统仪器类型 
		  var sysEqCount = Number($("#systemEqType").val());
		  	var barIndex = 0;
		  	var workPlaceBarDatas = json.workPlaceBarDatas;
		  	for (var workPlaceId in workPlaceBarDatas) {
		  		var workPlace = workPlaceBarDatas[workPlaceId];
		  		var workPlaceName = workPlace.name;
		  		var workPlaceDiv = queryOrCreateWorkPlaceDiv(workPlaceId);
		  		for (var i = 0; i < workPlace.barDatas.length; ++i) {
		  			var barData = workPlace.barDatas[i];
			  		(function() {
			  			var equipmentId = barData.equipmentId;
			  			if (callType == 1){
			  				autoCreateDiv(sysEqCount, equipmentId, barIndex, workPlaceId);
			  			}
			  			fillDivData(sysEqCount,
							equipmentId, 
							barData.temp, 
							barData.appt,
							barData.humi,
							barData.apph,
							barData.label,
							barData.colorTemp,
							barData.colorHumi,
							barData.stateStr,
							barData.power);
			  		})();
			  		++barIndex;
		  		}
		  	}
			
			//显示记录时间
			if (json.recTimeStr != ""){
				$("#recTimeLabel").text("最新数据更新时间:" + json.recTimeStr);
			}else{
				$("#recTimeLabel").text("暂无数据更新...");
			}  
			// 计时器运行
			if (timeoutID != null){	window.clearTimeout(timeoutID); }
			timeoutID = setTimeout("callRun(2)" ,Number($("#falshTime").val()) * 1000);
						
			actionPath = null;	paramArgs = null;
			
			autoAdjustSize();
		});//end post
	}// end if(userPlaceList == "")
}

$(function() {
	$("a[id^='sel']").click(function(){
		$("#_"+$(this).attr("id")).find(":checkbox").each(function(){
				$(this).attr("checked", $(this).attr("checked")==true?false:true);
		});
	});// end $("a[id^='sel']") 		
	
});// end $(function()      

function submitEQ(){
	$("#errorArea").hide(); // 隐藏信息区域
	var bool = checkEQLen("EQLEN","选择 的仪器个数不能大于");
	if (bool){
		var userPlaceList = getEQList();
		userPlaceList = userPlaceList.substring(0,userPlaceList.length-1);//截断最后一个逗号
		$("#userPlaceList").val(userPlaceList);
 		document.barSetEQForm.action = "MainBarAction!updateUserConfig";
		ableToButton(0); //所用按钮变灰
		$("#errorArea").hide(); // 隐藏信息区域	 		
	  document.barSetEQForm.submit();
	}	
}

function toggleDiv(){
	$("#addressList").toggle();
}