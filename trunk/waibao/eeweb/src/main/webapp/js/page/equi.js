function onkeydown(e) { 
	var currKey=0,e=e||event;
	currKey=e.keyCode||e.which||e.charCode;
	if (currKey == 13){
		var pageId = $("#pageId").val();
		if (pageId == "add"){submitAdd(1);}
		else if (pageId == "update"){submitUpdate(1);}
	} 
}
//注册键盘的回车事件--备注回车的时候也会触发,这里注销代码
//document.onkeydown = onkeydown;

// 初始化 -用户列表
function showList(){
	if ($("#pageId").val() == "list"){
		$(function() {
		   	var havedata = $("#have_data").val();
		   	if (havedata > 0){
		   		var pagecount = $("#pagecount").val();   		var currentPage = $("#currentPage").val();
		     	$("#pager").pager({ pagenumber: currentPage, pagecount: pagecount, buttonClickCallback: PageClick });
		   	}
		   	
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
			
		  PageClick = function(pageclickednumber) {
	    	var pagecount = $("#pagecount").val();
		    $("#pager").pager({ pagenumber: pageclickednumber, pagecount: pagecount, buttonClickCallback: PageClick });
		    $("#currentPage").val(pageclickednumber);
				document.equiFormList.action = "equi_getList_Action";
				document.equiFormList.submit();
		  }
	}else if ($("#pageId").val() == "update"){
		var eqtype = $("#eqtype").val();
		changeType(eqtype,1);		
	}else if ($("#pageId").val() == "order"){
		$( function() {
			$("#sortable").sortable();
			$("#sortable").disableSelection();
		});
	}
}
// type[1:调整顺序 2:增加  3:批量删除  4:搜索  5:修改	6:删除  7:更新到药监]
function submitList(type,equiId){
	$("#errorArea").hide(); //隐藏信息区域
	if (type == 1){
		document.equiFormList.action = "equi_showOrderList_Action";
		document.equiFormList.submit();
	}else if (type == 2){
		document.equiFormList.action = "equi_percreate_Action";
		document.equiFormList.submit();
	}else if (type == 3){
		if ( confirm("确定要删除吗?") && ($("input[name='ids']:checked").size() != 0) ){
				document.equiFormList.action = "equi_deleteSelect_Action";
				document.equiFormList.submit();
		}
	}else if (type == 4){
		document.equiFormList.action = "equi_fuzzySearch_Action";
		document.equiFormList.submit();
	}else if (type == 5){
		$("#equipData_equipmentId").val(equiId);			
		document.equiFormList.action = "equi_perupdate_Action";
		document.equiFormList.submit();
	}else if (type == 6){
		if (confirm("确定要删除吗?")){
			$("#equipData_equipmentId").val(equiId);
			document.equiFormList.action = "equi_delete_Action";
			document.equiFormList.submit();
		}
	}else if (type == 7){
		document.equiFormList.target="_blank";		
		document.equiFormList.action = "equi_updata4ZJ_Action";
		document.equiFormList.submit();
	}
}// end fun

// type[1:增加  2:返回]
function submitAdd(type){
	$("#errorArea").hide();// 隐藏信息区域
	var bool = true;
	if (type == 1){
		if (bool){bool = checkData("equipData_dsrsn","integer0","仪器编号");}
		if (bool){bool = checkData("equipData_address","integer1","仪器地址");}
		if (bool){bool = checkData("equipData_mark","stringLen","标注");}
		if (bool){bool = checkData("tempDown","float","温度下限");}
		if (bool){bool = checkData("tempUp","float","温度上限");}
		if (bool){bool = checkMaxMin("tempDown","tempUp","温度下限不能大于或者等于上限");}
		if (bool){bool = checkData("tempDev","float","温度偏差");}
		if (bool){bool = checkData("humiDown","float","湿度下限");}
		if (bool){bool = checkData("humiUp","float","湿度上限");}
		if (bool){bool = checkMaxMin("humiDown","humiUp","湿度下限不能大于或者等于上限");}
		if (bool){bool = checkData("humiDev","float","湿度偏差");}
		if (bool){ableToButton(0); // 所用按钮变灰
			document.addequiForm.action = "equi_create_Action";
			document.addequiForm.submit();//提交表单
		}
	}else if (type == 2){
		document.updequiForm.action = "equi_back_Action";
		document.updequiForm.submit();
	}
}// end fun

// type[1:修改  2:返回]
function submitUpdate(type){
	var bool = true;
	if (type == 1){
		if (bool){bool = checkData("equipData_dsrsn","integer0","仪器编号");}
		if (bool){bool = checkData("equipData_address","integer1","仪器地址");}
		if (bool){bool = checkData("equipData_mark","stringLen","标注");}
		if (bool){bool = checkData("tempDown","float","温度下限");}
		if (bool){bool = checkData("tempUp","float","温度上限");}
		if (bool){bool = checkMaxMin("tempDown","tempUp","温度下限不能大于或者等于上限");}				
		if (bool){bool = checkData("humiDown","float","湿度下限");}
		if (bool){bool = checkData("humiUp","float","湿度上限");}
		if (bool){bool = checkMaxMin("humiDown","humiUp","湿度下限不能大于或者等于上限");}				
		if (bool){ableToButton(0); $("#errorArea").hide(); // 隐藏信息区域 所用按钮变灰
			document.updequiForm.action = "equi_update_Action";
			document.updequiForm.submit();//提交表单
		}
	}else if (type == 2){
		document.updequiForm.action = "equi_back_Action";
		document.updequiForm.submit();
	}
}// end fun

//调整顺序
function submitUpOrder(){
	var eqorderStr = "";
	$("input:hidden[name^='place']").each( function() {
		eqorderStr = eqorderStr + this.value + ",";
	});
	eqorderStr = eqorderStr.substring(0, eqorderStr.length - 1);
	$("#eqorderStr").val();
	$("#eqorderStr").val(eqorderStr);
	//var actionurl = "equi_updateOrderList_Action?eqorderStr=" + eqorderStr;
	document.seteqOrderForm.action = "equi_updateOrderList_Action";
	document.seteqOrderForm.submit(); 
}

//设定为：1:温湿度2:单温度 3:单湿度
// type:[0:form对象,1:值]
function changeType(obj,type){
	var typeStr = type == 0 ? obj.value : obj;
	if ((typeStr == "1")||(typeStr==1)){
		changeRead("block","block")
	}else if ((typeStr == "2")||(typeStr==2)){
		changeRead("block","none")
	}else if ((typeStr == "3")||(typeStr==3)){
		changeRead("none","block")
	}
}
//切换文本读写
function changeRead(tempshow,humishow){
	tempshow=="block"?$(".temp_show").show():$(".temp_show").hide();
	humishow=="block"?$(".humi_show").show():$(".humi_show").hide();
	//$(".temp_show").css("height", tempshow=="block"?"35px":"0px");
	//$(".humi_show").css("height", humishow=="block"?"35px":"0px");
	//$(".temp_show").css("display", tempshow);
	//$(".humi_show").css("display", humishow);
}	