//type(1:���� 2:����)
function submitForm(fileName, type){
	$("#errorArea").hide(); // ������Ϣ����
	if (type == 1){
		var bool = checkData("fromTime","stringLen","��ʼʱ�䲻��Ϊ��.");
		if (bool){bool = checkData("endTime","stringLen","����ʱ�䲻��Ϊ��.");}
		if (bool){
			$("#currentPage").val("1");//������һҳ
			document.backupdataForm.action = "sys_getList_Action";
			document.backupdataForm.submit();
		}							
	}else if(type == 2){
//		$("#filename").val(fileName);
//		document.downfileForm.target="_blank";
//		var actionPath = '${pageContext.request.contextPath}/userdo/excelAction.action';
//		document.downfileForm.action = actionPath;
//		document.downfileForm.submit();
	}
}

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
document.backupdataForm.action = "sys_getList_Action";
	document.backupdataForm.submit();
  }			
