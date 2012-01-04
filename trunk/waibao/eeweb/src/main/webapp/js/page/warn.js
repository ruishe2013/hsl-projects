$(function() {
	//交替显示行
	$('thead').addClass('table-title');
	
	$("tbody #del").live("click", function(){
		$(this).parents(".t_repeat").remove();
		swapColor();
	});		
	
});
		
// 列表颜色
function swapColor(){
	$('tbody > tr:odd').css("background-color","#D6DFF5");
	$('tbody > tr:even').css("background-color","#FFFFFF");
}

//报警数据  -- 发送异步请求
function doWarn(alarmId) {
	var actionPath = '${pageContext.request.contextPath}/main/doWarnJson.action';
	var paramArgs = {alarmId: alarmId};
	//发送请求
	$.post(actionPath, paramArgs, function(data) {
		//将JSON转化为一个对象,并且名为joson
		eval("var json=" + data);
		if (json.doWarnFalg == 0){
			alert("请先登陆...");
		}else if (json.doWarnFalg == 1){
			alert("复位操作成功...");
		}else if (json.doWarnFalg == 2){
			alert("已经有人复位过了...");
		}
	});//end pose
}		
// 无数据单元
function createNoDataTr(sysEqCount){
	var cando = $('tbody').find("#t_no_data").length == 0;
	if (cando){
		// 删除记录列表
		$(".t_data").each(function(){
			$(this).parents(".t_repeat").remove();
		});
		var tdCount = sysEqCount == 0? 7 : sysEqCount == 1? 7 :6;
		var trele = "<tr class=\"t_repeat\"> " +
						"<td colspan=\"" + tdCount + "\" id=\"t_no_data\" style=\"line-height: 100px;\">" +
							"<div class=\"list_no_data\">目前还没有报警记录...</div>"+
						"</td>"+
					"</tr>";
		addtrtotable(trele);
	}		
}

// 有数据单元
function createDateTr(sysEqCount, dlabel,dtemp,dhumi,darea,dtime_s,dtime_n,alarmId){
	var cando = $('tbody').find("#eq"+alarmId).length == 0;
	if (cando){
		cando = $('tbody').find("#t_no_data").length != 0;// 删除没有记录提示
		if (cando){$("#t_no_data").parents(".t_repeat").remove();}	
		var trele = "<tr class=\"t_repeat\">"+
				"	<td class=\"user_list_line t_data\" id=\"eq"+alarmId+"\">" + dlabel + "</td>" +
				(sysEqCount != 3 ? "<td class=\"user_list_line\">" + dtemp + "</td>" :"") +
				(sysEqCount != 2 ? "<td class=\"user_list_line\">" + dhumi + "</td>" :"") +
				"	<td class=\"user_list_line\">" + darea + "</td>" +
				"	<td class=\"user_list_line\">" + changeMills(dtime_s)+ "</td>" +
				"	<td class=\"user_list_line\" id=\"ev" + alarmId + "\">" + changeMills(dtime_n)+ "</td>" +
				"	<td class=\"user_list_line\">" + 
				"		[<a href=\"#\" id=\"del\" onclick=\"doWarn("+alarmId+")\">报警确认</a>]" +
				"	</td>" +
				"</tr>";
		addtrtotable(trele);
	}else{
		$('tbody').find("#eq"+alarmId).removeClass("delsel");
		$("#ev"+alarmId).text(changeMills(dtime_n));
	}
}

// 把毫秒数转换成时间格式
function changeMills(mills){
	return new Date(mills).toLocaleString();
}

// 增加table元素
function addtrtotable(ele){
	$('tbody').append(ele);
	swapColor();
}

//发送异步请求callType:1 第一次进入页面   2:计时器动态实时
var timeoutID = null;
function callRun() {
	var actionPath = '${pageContext.request.contextPath}/main/warnInfoJson.action';
	//发送请求
	$.post(actionPath, null, function(data) {
		//将JSON转化为一个对象,并且名为joson
		var json = eval("(" + data +")");
	  // 系统仪器类型 
	  var sysEqCount = Number($("#systemEqType").val());
	  						
		$("#rowcountLabel").text(json.warnItemCount);
		if (json.warnItemCount == 0){
			if ($("#sounddl").text()!=""){//背景声音关闭
					$("#sounddl").text("");
				}			
			createNoDataTr(sysEqCount);
		}else{
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
						
			// 创建警报列,或者暂时清理未处理的警报列的标记
			for(var i=0; i<json.warnList.length; i++){
				createDateTr(sysEqCount, json.warnList[i].placeName,json.warnList[i].temperature,
										 json.warnList[i].humidity, json.warnList[i].normalArea,	
										 json.warnList[i].alarmStart,json.warnList[i].alarmEnd, 	
										 json.warnList[i].alarmId);
			}
			// 删除已经处理过的警报.达到所有客户机都同时更新的效果
			$(".delsel").each(function(){
				$(this).parents(".t_repeat").remove();
				swapColor();
			});
			// 重新标记没有处理的警报
			$(".t_data").each(function(){
				$(this).addClass("delsel");
			});
		}		        			
		// 计时器运行
		if (timeoutID != null){	window.clearTimeout(timeoutID); }
		timeoutID = setTimeout("callRun()" ,Number($("#falshTime").val()) * 1000);
	});//end post
}