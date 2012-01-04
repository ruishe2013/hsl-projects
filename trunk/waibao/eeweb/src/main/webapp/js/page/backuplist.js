//type(1:收索 2:下载)
function submitForm(fileName, type){
	$("#errorArea").hide(); // 隐藏信息区域
	if (type == 1){
		var bool = checkData("fromTime","stringLen","开始时间不能为空.");
		if (bool){bool = checkData("endTime","stringLen","结束时间不能为空.");}
		if (bool){
			$("#currentPage").val("1");//跳到第一页
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

//交替显示行
$('thead').addClass('table-title');
$('tbody > tr:odd').toggleClass('table-tr-odd');
$('tbody > tr:even').toggleClass('table-tr-even');
//选择行
//为表格行添加选择事件处理
$('tbody > tr').hover(		//注意这里的链式调用
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
