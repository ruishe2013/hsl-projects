// type[1:����ɾ�� 2:���� 3:ɾ��]
function submitForm(type,logId){
	$("#errorArea").hide("fast"); // ������Ϣ����
  if (type == 1){
		if ( confirm("ȷ��Ҫɾ����?") && ($("input[name='ids']:checked").size() != 0) ){
				document.sysLogList.action = "syslogAction!deleteSelect";
				document.sysLogList.submit();
		}
	}else if (type == 2){
		var bool = checkData("fromTime","stringLen","��ʼʱ�䲻��Ϊ��.");
		if (bool){bool = checkData("endTime","stringLen","����ʱ�䲻��Ϊ��.");}
		if (bool){bool = checkData("maxRecords","integer1","��������ļ�¼��");}
		if (bool){ableToButton(0); $("#errorArea").hide(); //���ð�ť���; ������Ϣ����
			$("#currentPage").val("1");//������һҳ
			document.sysLogList.action = "syslogAction!getList";
			document.sysLogList.submit();
		}
	}else if (type == 3){
		if (confirm("ȷ��Ҫɾ����?")){
			$("#logId").val(logId);
			document.sysLogList.action = "syslogAction!delete";
			document.sysLogList.submit();
		}
	}
}// end fun

$(function() {
var havedata = $("#have_data").val();
if (havedata > 0){
	var pagecount = $("#pagecount").val();   		var currentPage = $("#currentPage").val();
 	$("#pager").pager({ pagenumber: currentPage, pagecount: pagecount, buttonClickCallback: PageClick });
}

//������ʾ��
$('thead').addClass('table-title');
$('tbody > tr:odd').toggleClass('table-tr-odd');
$('tbody > tr:even').toggleClass('table-tr-even');
//ѡ����
//Ϊ��������ѡ���¼�����
$('tbody > tr').hover(		//ע���������ʽ����
	function(){$(this).addClass('table-tr-onmouse');},
	function(){$(this).removeClass('table-tr-onmouse');}
	);		   	

});		
	
PageClick = function(pageclickednumber) {
	var pagecount = $("#pagecount").val();
	$("#pager").pager({ pagenumber: pageclickednumber, pagecount: pagecount, buttonClickCallback: PageClick });
	$("#currentPage").val(pageclickednumber);
	document.sysLogList.action = "syslogAction!getList";
	document.sysLogList.submit();
}		
