function onkeydown(e) { 
	var currKey=0,e=e||event;
	currKey=e.keyCode||e.which||e.charCode;
	if (currKey == 13){
		var pageId = $("#pageId").val();
		if (pageId == "add"){submitAdd(1);}
		else if (pageId == "update"){submitUpdate(1)}
	} 
}
//注册键盘的回车事件--备注回车的时候也会触发,这里注销代码
//document.onkeydown = onkeydown;

// 初始化 -列表
function showList(){
	$(function() {
	   	var havedata = $("#have_data").val();
		if (havedata > 0){
			var pagecount = $("#pagecount").val();   		var currentPage = $("#currentPage").val();
		 	$("#pager").pager({ pagenumber: currentPage, pagecount: pagecount, buttonClickCallback: PageClick });
		}
	
		//交替显示行
		//$('thead').addClass('table-title');
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
		document.phoneFormList.action = "phone_getList_Action";
		document.phoneFormList.submit();
  }	
}// end fun

//type[1:增加  2:批量删除 3:搜索 4:修改 5:删除]
function submitList(type,listId){
	if (type == 1){
		document.phoneFormList.action = "phone_percreate_Action";
		document.phoneFormList.submit();
	}else if (type == 2){
		if ( confirm("确定要删除吗?") && ($("input[name='ids']:checked").size() != 0) ){
				document.phoneFormList.action = "phone_deleteSelect_Action";
				document.phoneFormList.submit();
		}
	}else if (type == 3){
		document.phoneFormList.action = "phone_fuzzySearch_Action";
		document.phoneFormList.submit();		
	}else if (type == 4){
		$("#phoneList_listId").val(listId);
		document.phoneFormList.action = "phone_perupdate_Action";
		document.phoneFormList.submit();
	}else if (type == 5){
		if (confirm("确定要删除吗?")){
			$("#phoneList_listId").val(listId);
			document.phoneFormList.action = "phone_delete_Action";
			document.phoneFormList.submit();
		}
	}
}// end fun

// type[1:增加  2:返回]
function submitAdd(type){
	$("#errorArea").hide(); //  隐藏信息区域
	var bool = true;
	if (type == 1){
		if (bool){bool = checkData("phoneList_name","stringLen","姓名");}
		if (bool){bool = checkData("phoneList_phone","PHONE","手机号格式不正确");}				
		if (bool){
			document.addPhoneForm.action = "phone_create_Action";
			document.addPhoneForm.submit();//提交表单
		}
	}else if (type == 2){
//		document.addPhoneForm.action = "phone_back_Action";
//		document.addPhoneForm.submit();
	}
}

// type[1:修改  2:返回]
function submitUpdate(type){
	$("#errorArea").hide(); //隐藏信息区域
	var bool = true;
	if (type == 1){
			if (bool){bool = checkData("phoneList_name","stringLen","姓名");}
			if (bool){bool = checkData("phoneList_phone","PHONE","手机号格式不正确");}
			if (bool){
				document.updatePhoneForm.action = "phone_update_Action";
				document.updatePhoneForm.submit();//提交表单
		}			
	}else if (type == 2){
		document.updatePhoneForm.action = "phone_back_Action";
		document.updatePhoneForm.submit();
	}
}