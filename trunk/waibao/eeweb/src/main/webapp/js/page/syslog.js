// type[1:批量删除 2:搜索 3:删除]
function submitForm(type,logId){
	$("#errorArea").hide("fast"); // 隐藏信息区域
  if (type == 1){
		if ( confirm("确定要删除吗?") && ($("input[name='ids']:checked").size() != 0) ){
				document.sysLogList.action = "syslogAction!deleteSelect";
				document.sysLogList.submit();
		}
	}else if (type == 2){
		var bool = checkData("fromTime","stringLen","开始时间不能为空.");
		if (bool){bool = checkData("endTime","stringLen","结束时间不能为空.");}
		if (bool){bool = checkData("maxRecords","integer1","返回最近的记录数");}
		if (bool){ableToButton(0); $("#errorArea").hide(); //所用按钮变灰; 隐藏信息区域
			$("#currentPage").val("1");//跳到第一页
			document.sysLogList.action = "syslogAction!getList";
			document.sysLogList.submit();
		}
	}else if (type == 3){
		if (confirm("确定要删除吗?")){
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
	document.sysLogList.action = "syslogAction!getList";
	document.sysLogList.submit();
}		
