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

// 初始化 -列表 type[1:列表  2:增加或者修改 3:gprsTesInfo.jsp页面用]
function showList(type){
	if (type != 2){
		$(function() {
		   	var havedata = $("#have_data").val();
		   	if (havedata > 0){
		   		var pagecount = $("#pagecount").val();   		var currentPage = $("#currentPage").val();
		     	$("#pager").pager({ pagenumber: currentPage, pagecount: pagecount, buttonClickCallback: PageClick });
		   	}
		});		
		
	  	PageClick = function(pageclickednumber) {
		var pagecount = $("#pagecount").val();
	    $("#pager").pager({ pagenumber: pageclickednumber, pagecount: pagecount, buttonClickCallback: PageClick });
	    $("#currentPage").val(pageclickednumber);
	    if ( type == 1){
	    	document.gprsFormList.action = "gprs_getList_Action";
	    	document.gprsFormList.submit();
	    }else if(type == 3){
			document.formatFormList.action = "gprsTestAction";
			document.formatFormList.submit();	    	
	    }
	  }
	}

	$(function() {
		//交替显示行
		$('#bodyList > tr:odd').toggleClass('table-tr-odd');
		$('#bodyList > tr:even').toggleClass('table-tr-even');
		//选择行
		//为表格行添加选择事件处理
		$('#bodyList > tr').hover(		//注意这里的链式调用
				function(){$(this).addClass('table-tr-onmouse');},
				function(){$(this).removeClass('table-tr-onmouse');}
		);		   	
	});	  	
	
}// end fun



// type[1:增加  2:批量删除 3:搜索 4:修改 5:删除]
function submitList(type,gprsSetId){
	$("#errorArea").hide(); //隐藏信息区域
	if (type == 1){
		document.gprsFormList.action = "gprs_percreate_Action";
		document.gprsFormList.submit();
	}else if (type == 2){
		if ( confirm("确定要删除吗?") && ($("input[name='ids']:checked").size() != 0) ){
				document.gprsFormList.action = "gprs_deleteSelect_Action";
				document.gprsFormList.submit();
		}
	}else if (type == 3){
	}else if (type == 4){
		$("#gprsSet_gprsSetId").val(gprsSetId);
		document.gprsFormList.action = "gprs_perupdate_Action";
		document.gprsFormList.submit();
	}else if (type == 5){
		if (confirm("确定要删除吗?")){
			$("#gprsSet_gprsSetId").val(gprsSetId);
			document.gprsFormList.action = "gprs_delete_Action";
			document.gprsFormList.submit();
		}
	}
}// end fun

// type[1:增加  2:返回]
function submitAdd(type){
	$("#errorArea").hide(); // 隐藏信息区域 
	var bool = true;
	if (type == 1){
		if (bool){bool = checkData("gprsSet_numId","integer0","数字代号");}
		if (bool){bool = checkData("gprsSet_alias","CODE","字母代号");}
		if (bool){bool = checkData("gprsSet_mesFormat","stringLen","格式");}
		if (bool){bool = checkData("gprsSet_remark","stringLen","说明");}
		if (bool){
			document.addGprsForm.action = "gprs_create_Action";
			document.addGprsForm.submit();//提交表单
		}
	}else if (type == 2){
		if ($("#gprsSet_numId").val() == ""){$("#gprsSet_numId").val(0)}
		document.addGprsForm.action = "gprs_getList_Action";
		document.addGprsForm.submit();
	}
}

// type[1:修改  2:返回]
function submitUpdate(type){
	$("#errorArea").hide(); // 隐藏信息区域 
	var bool = true;
	if (type == 1){
		if (bool){bool = checkData("gprsSet_numId","integer0","数字代号");}
		if (bool){bool = checkData("gprsSet_alias","CODE","字母代号");}
		if (bool){bool = checkData("gprsSet_mesFormat","stringLen","格式");}
		if (bool){bool = checkData("gprsSet_remark","stringLen","说明");}
			if (bool){
				document.updateGprsForm.action = "gprs_update_Action";
				document.updateGprsForm.submit();//提交表单
		}			
	}else if (type == 2){
		document.updateGprsForm.action = "gprs_back_Action";
		document.updateGprsForm.submit();
	}
}

// 测试短信中心号码
function submitTest(type){
	$("#errorArea").hide(); // 隐藏信息区域 
	var bool = true;
	if(type == 1){
		ableToButton(0);
		if ($("#runFlag").val() == 1){
			sendAjax(2);	// 断开
		}else{
			sendAjax(1);	// 连接
		}
	}else if(type == 2){			
	}else if(type == 3){
		if (bool){bool = checkData("centerNo","PHONE","短信中心号格式不正确");}		
		if (bool){bool = checkData("phoneNo","PHONE","目标手机号格式不正确");}
		if(bool){bool = checkData("sendcontent","stringLen","发送信息内容 ");}
		if (bool){sendAjax(3);}
	}else if(type == 4){
		$("#sendcontent").attr("value",'');	$("#phoneNo").attr("value",'');
	}
}

// type[1:连接 2:断开 3:发送]--ajax发送请求
function sendAjax(type){
	$("#errorArea").hide(); // 隐藏信息区域 
	var actionPath = "${pageContext.request.contextPath}/mana/";
	var paramArgs = null;
	var msgshox;
	
	if (type == 1){
		msgshox = "正在进行连接...";
		$("#sendbut").val(msgshox);
		actionPath = actionPath + "gprsTestlinkJson.action";
	  //paramArgs = {centerNo:getDecStr("centerNo",1),portNo:getDecStr("portNo",1),baudRateNo:getDecStr("baudRateNo",1)};
	}else if (type == 2){
		msgshox = "正在断开连接...";
		$("#sendbut").val(msgshox);
		actionPath = actionPath + "gprsTestdislinkJson.action";
	}else if (type == 3){
		msgshox = "正在发送...";
		paramArgs = {phoneNo:getDecStr("phoneNo",1),sendcontent:getDecStr("sendcontent",1)};
		actionPath = actionPath + "gprsTestsendJson.action";
		$("#errorArea").show();
		$("#errorArea").html(msgshox);
	}		
	// 发送请求
	$.post(actionPath, paramArgs, function(data) {
		//将JSON转化为一个对象,并且名为joson
		eval("var json=" + data);
		$("#runFlag").val(json.runFlag);
		if (type != 3){
			$("#sendbut").val(json.stateStr);
			ableToButton(1); // 恢复所有按钮
		}else{
			$("#errorArea").html(json.msgShow);
		}
	});//end post
	
	
}