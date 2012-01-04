$(function() {
	//������ʾ��
	$('thead').addClass('table-title');
	
	$("tbody #del").live("click", function(){
		$(this).parents(".t_repeat").remove();
		swapColor();
	});		
	
});
		
// �б���ɫ
function swapColor(){
	$('tbody > tr:odd').css("background-color","#D6DFF5");
	$('tbody > tr:even').css("background-color","#FFFFFF");
}

//��������  -- �����첽����
function doWarn(alarmId) {
	var actionPath = '${pageContext.request.contextPath}/main/doWarnJson.action';
	var paramArgs = {alarmId: alarmId};
	//��������
	$.post(actionPath, paramArgs, function(data) {
		//��JSONת��Ϊһ������,������Ϊjoson
		eval("var json=" + data);
		if (json.doWarnFalg == 0){
			alert("���ȵ�½...");
		}else if (json.doWarnFalg == 1){
			alert("��λ�����ɹ�...");
		}else if (json.doWarnFalg == 2){
			alert("�Ѿ����˸�λ����...");
		}
	});//end pose
}		
// �����ݵ�Ԫ
function createNoDataTr(sysEqCount){
	var cando = $('tbody').find("#t_no_data").length == 0;
	if (cando){
		// ɾ����¼�б�
		$(".t_data").each(function(){
			$(this).parents(".t_repeat").remove();
		});
		var tdCount = sysEqCount == 0? 7 : sysEqCount == 1? 7 :6;
		var trele = "<tr class=\"t_repeat\"> " +
						"<td colspan=\"" + tdCount + "\" id=\"t_no_data\" style=\"line-height: 100px;\">" +
							"<div class=\"list_no_data\">Ŀǰ��û�б�����¼...</div>"+
						"</td>"+
					"</tr>";
		addtrtotable(trele);
	}		
}

// �����ݵ�Ԫ
function createDateTr(sysEqCount, dlabel,dtemp,dhumi,darea,dtime_s,dtime_n,alarmId){
	var cando = $('tbody').find("#eq"+alarmId).length == 0;
	if (cando){
		cando = $('tbody').find("#t_no_data").length != 0;// ɾ��û�м�¼��ʾ
		if (cando){$("#t_no_data").parents(".t_repeat").remove();}	
		var trele = "<tr class=\"t_repeat\">"+
				"	<td class=\"user_list_line t_data\" id=\"eq"+alarmId+"\">" + dlabel + "</td>" +
				(sysEqCount != 3 ? "<td class=\"user_list_line\">" + dtemp + "</td>" :"") +
				(sysEqCount != 2 ? "<td class=\"user_list_line\">" + dhumi + "</td>" :"") +
				"	<td class=\"user_list_line\">" + darea + "</td>" +
				"	<td class=\"user_list_line\">" + changeMills(dtime_s)+ "</td>" +
				"	<td class=\"user_list_line\" id=\"ev" + alarmId + "\">" + changeMills(dtime_n)+ "</td>" +
				"	<td class=\"user_list_line\">" + 
				"		[<a href=\"#\" id=\"del\" onclick=\"doWarn("+alarmId+")\">����ȷ��</a>]" +
				"	</td>" +
				"</tr>";
		addtrtotable(trele);
	}else{
		$('tbody').find("#eq"+alarmId).removeClass("delsel");
		$("#ev"+alarmId).text(changeMills(dtime_n));
	}
}

// �Ѻ�����ת����ʱ���ʽ
function changeMills(mills){
	return new Date(mills).toLocaleString();
}

// ����tableԪ��
function addtrtotable(ele){
	$('tbody').append(ele);
	swapColor();
}

//�����첽����callType:1 ��һ�ν���ҳ��   2:��ʱ����̬ʵʱ
var timeoutID = null;
function callRun() {
	var actionPath = '${pageContext.request.contextPath}/main/warnInfoJson.action';
	//��������
	$.post(actionPath, null, function(data) {
		//��JSONת��Ϊһ������,������Ϊjoson
		var json = eval("(" + data +")");
	  // ϵͳ�������� 
	  var sysEqCount = Number($("#systemEqType").val());
	  						
		$("#rowcountLabel").text(json.warnItemCount);
		if (json.warnItemCount == 0){
			if ($("#sounddl").text()!=""){//���������ر�
					$("#sounddl").text("");
				}			
			createNoDataTr(sysEqCount);
		}else{
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
						
			// ����������,������ʱ����δ����ľ����еı��
			for(var i=0; i<json.warnList.length; i++){
				createDateTr(sysEqCount, json.warnList[i].placeName,json.warnList[i].temperature,
										 json.warnList[i].humidity, json.warnList[i].normalArea,	
										 json.warnList[i].alarmStart,json.warnList[i].alarmEnd, 	
										 json.warnList[i].alarmId);
			}
			// ɾ���Ѿ�������ľ���.�ﵽ���пͻ�����ͬʱ���µ�Ч��
			$(".delsel").each(function(){
				$(this).parents(".t_repeat").remove();
				swapColor();
			});
			// ���±��û�д���ľ���
			$(".t_data").each(function(){
				$(this).addClass("delsel");
			});
		}		        			
		// ��ʱ������
		if (timeoutID != null){	window.clearTimeout(timeoutID); }
		timeoutID = setTimeout("callRun()" ,Number($("#falshTime").val()) * 1000);
	});//end post
}