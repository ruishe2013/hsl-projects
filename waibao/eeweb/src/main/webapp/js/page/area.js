function onkeydown(e) { 
	var currKey=0,e=e||event;
	currKey=e.keyCode||e.which||e.charCode;
	if (currKey == 13){
		var pageId = $("#pageId").val();
		if (pageId == "add"){submitAdd(1);}
		else if (pageId == "update"){submitUpdate(1);}
	} 
}
//注册键盘的回车事件
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
				document.areaFormList.action = "area_getList_Action";
				document.areaFormList.submit();
		  }
	}
}
// type[1:增加  2:批量删除 3:搜索 4:修改 5:删除]
function submitList(type,placeId){
	$("#errorArea").hide(); //隐藏信息区域
	if (type == 1){
		document.areaFormList.action = "area_percreate_Action";
		document.areaFormList.submit();
	}else if (type == 2){
		if ( confirm("确定要删除吗?") && ($("input[name='ids']:checked").size() != 0) ){
				document.areaFormList.action = "area_deleteSelect_Action";
				document.areaFormList.submit();
		}
	}else if (type == 3){
		document.areaFormList.action = "area_fuzzySearch_Action";
		document.areaFormList.submit();
	}else if (type == 4){
		$("#place_placeId").val(placeId);
		document.areaFormList.action = "area_perupdate_Action";
		document.areaFormList.submit();
	}else if (type == 5){
		if (confirm("确定要删除吗?")){
			$("#place_placeName").val(placeId);
			document.areaFormList.action = "area_delete_Action";
			document.areaFormList.submit();
		}
	}else if (type == 6){
		$("#place_placeId").val(placeId);
		document.areaFormList.action = "area_pergsm_Action";
		document.areaFormList.submit();
	}
}// end fun

// type[1:增加  2:返回]
function submitAdd(type){
	$("#errorArea").hide();// 隐藏信息区域
	var bool = true;
	if (type == 1){
		if (bool){bool = checkData("place_placeName","stringLen","区域名");}
		if (bool){bool = checkEquals("place_placeName","未定义","区域名不用使用默认的[未定义]命名...");}				
		if (bool){ableToButton(0); // 所用按钮变灰
			document.addAreaForm.action = "area_create_Action";
			document.addAreaForm.submit();//提交表单
		}
	}else if (type == 2){
		document.addAreaForm.action = "area_back_Action";
		document.addAreaForm.submit();
	}
}// end fun

// type[1:修改  2:返回]
function submitUpdate(type){
	var bool = true;
	if (type == 1){
		if (bool){bool = checkData("place_placeName","stringLen","区域名");}
		if (bool){bool = checkEquals("place_placeName","未定义","区域名不用使用默认的[未定义]命名...");}
		if (bool){ableToButton(0); $("#errorArea").hide(); // 隐藏信息区域 所用按钮变灰
		document.updateAreaForm.action = "area_update_Action";
		document.updateAreaForm.submit();//提交表单
		}			
	}else if (type == 2){
		document.updateAreaForm.action = "area_back_Action";
		document.updateAreaForm.submit();
	}
}// end fun

// 修改短信关联
function submitUpGsm(){
	var place_gsm = getEQList();
	place_gsm = place_gsm.substring(0,place_gsm.length-1);//截断最后一个逗号
	$("#place_gsm").val(place_gsm);	
	document.updateAreaForm.action = "area_upgsm_Action";
	document.updateAreaForm.submit();
}// end fun

function cbt(){
	$("#_selGsm").find(":checkbox").each(function(){
		$(this).attr("checked", $(this).attr("checked")==true?false:true);
	});	
}

function showMes(val){
	if(val == 2){
		alert("该区域下,还有子标注.请删除所用子标注,然后再进行删除...");
	}
}
