//����ͼ����ʾ��Ԫ ����id������Ԫ ,type(0:�������  ��0:���������)
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
	"			<tr><td width=\"50%\" id=\"state" + equipmentId + "\">��������</td>" +
	"				<td align=\"right\" id=\"power" + equipmentId + "\">��������</td>" +
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
		// ÿ�����������ʾ
		
	} else if ( 2 == displayMode) {
		// ��������ϲ���ʾ
		
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

//���ͼ������
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

//�����첽����callType:1 ��һ�ν���ҳ��   2:��ʱ����̬ʵʱ
var timeoutID = null;
function callRun(callType) {
	var actionPath = '${pageContext.request.contextPath}/main/barChartJson.action';
	var paramArgs;
	var userPlaceList;
	
	// ��һ�ν���ҳ��-��ʾ"���ڼ���..."
	if (callType == 1){
		$("#loadingDiv").html("���ڼ���...");
	}
	
	
	// �����û�ѡ��ĵ�ַ�б�
	userPlaceList = $("input:hidden[name='userPlaceList']").val();
	if ( (userPlaceList == undefined) || (userPlaceList == "0")) { // ��ַ�б�Ϊ�ղŷ�������
		$("#loadingDiv").html("��ûѡ�������б�...");
	}else{
		//��������
		try {
			paramArgs = {"userPlaceList": userPlaceList};
		} catch (e) {
			alert(e.description);
			return;
		}
		$.post(actionPath, paramArgs, function(data) {
		//	$("#test_div").text(data);
			//��JSONת��Ϊһ������,������Ϊjson
			var json = null;
			try {
				json = eval("(" + data +")");
			} catch (e) {
				alert(e.description+ ":" + data);
				return;
			}
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
		        	
		  // ϵͳ�������� 
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
			
			//��ʾ��¼ʱ��
			if (json.recTimeStr != ""){
				$("#recTimeLabel").text("�������ݸ���ʱ��:" + json.recTimeStr);
			}else{
				$("#recTimeLabel").text("�������ݸ���...");
			}  
			// ��ʱ������
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
	$("#errorArea").hide(); // ������Ϣ����
	var bool = checkEQLen("EQLEN","ѡ�� �������������ܴ���");
	if (bool){
		var userPlaceList = getEQList();
		userPlaceList = userPlaceList.substring(0,userPlaceList.length-1);//�ض����һ������
		$("#userPlaceList").val(userPlaceList);
 		document.barSetEQForm.action = "MainBarAction!updateUserConfig";
		ableToButton(0); //���ð�ť���
		$("#errorArea").hide(); // ������Ϣ����	 		
	  document.barSetEQForm.submit();
	}	
}

function toggleDiv(){
	$("#addressList").toggle();
}