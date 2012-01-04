function ableToBut(flag){
	// 使所有按钮恢复
	$(":button").each(function(){
		this.disabled = flag == 0 ? true : false;
	});		
}
function onkeydown(e) { 
	var currKey=0,e=e||event;
	currKey=e.keyCode||e.which||e.charCode;
	if (currKey == 13){
		var pageId = $("#pageId").val();
		if(pageId == "load"){submitLoad();}
//		else if (pageId == "add"){submitAdd(1);}
//		else if (pageId == "update"){submitUpdate(1)}
//		else if (pageId == "upPass"){submitupPass();}
	} 
}
//注册键盘的回车事件
document.onkeydown = onkeydown;

// 登录用的提交事件
function submitLoad(){
	var bool = $("#user_name").val().replace(/[ ]/g,"").length > 0;
	if (!bool){
		$("#user_name").focus();
		alert("用户名不能为空."); 
	}else{
		bool = $("#user_pass").val().replace(/[ ]/g,"").length > 0;
		if (!bool){
			$("#user_pass").focus();
			alert("密码不能为空.")
			}
	}
	if (bool){ableToBut(0); //所用按钮变灰
		document.loadForm.action = 'loginAction';
		document.loadForm.submit();//提交表单
	}
}

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
		// 分页		
		PageClick = function(pageclickednumber) {
			var pagecount = $("#pagecount").val();
			$("#pager").pager({ pagenumber: pageclickednumber, pagecount: pagecount, buttonClickCallback: PageClick });
			$("#currentPage").val(pageclickednumber);
			document.userFormList.action = "user_getList_Action";
			document.userFormList.submit();
		}	
	}
}
//用户列表--type[1:增加  2:批量删除 3:搜索 4:修改	5:删除]
function submitList(type,userId){
	$("#errorArea").hide(); //隐藏信息区域
	if (type == 1){
		document.userFormList.action = "user_percreate_Action";
		document.userFormList.submit();
	}else if (type == 2){
		if ( confirm("确定要删除吗?") && ($("input[name='ids']:checked").size() != 0) ){
				document.userFormList.action = "user_deleteSelect_Action";
				document.userFormList.submit();
		}
	}else if (type == 3){
		document.userFormList.action = "user_fuzzySearch_Action";
		document.userFormList.submit();
	}else if (type == 4){
		$("#user_userId").val(userId);
		document.userFormList.action = "user_perupdate_Action";
		document.userFormList.submit();
	}else if (type == 5){
		if (confirm("确定要删除吗?")){
			$("#user_userId").val(userId);
			document.userFormList.action = "user_delete_Action"
			document.userFormList.submit();
		}
	}
}// end fun

// type[1:增加  2:返回]
function submitAdd(type){
	$("#errorArea").hide(); //  隐藏信息区域
	var bool = true;
	if (type == 1){
		if (bool){bool = checkEquals("user_name","admin","用户名不能为admin,admin是不能添加的系统用户...");}
		if (bool){bool = checkData("user_name","stringLen","用户名");}
		if (bool){bool = checkData("user_pass","stringLen","密 码");}
		if (bool){ableToButton(0); //所用按钮变灰
			document.addUserForm.action = "user_create_Action";
			document.addUserForm.submit();//提交表单
		}
	}else if (type == 2){
		document.addUserForm.action = "user_back_Action";
		document.addUserForm.submit();
	}
}// end fun

// type[1:修改  2:返回]
function submitUpdate(type){
	if (type == 1){
		var bool = true;
		if (bool){bool = checkEquals("user_name","admin","用户名不能为admin,admin是不能添加的系统用户...");}
		if (bool){bool = checkData("user_name","stringLen","用户名");}
		if (bool){ableToButton(0); //所用按钮变灰
			document.updateUserForm.action = "user_update_Action";
			document.updateUserForm.submit();//提交表单
		}
	}else if (type == 2){
		document.updateUserForm.action = "user_back_Action";
		document.updateUserForm.submit();
	}
}

function submitupPass(){
	var bool = true;
	if (bool){bool = checkData("user_pass","stringLen","密 码");}
	if (bool){
		ableToButton(0); $("#errorArea").hide(); //所用按钮变灰; 隐藏信息区域
		document.updateUserForm.action = "user_updatepass_Action?changeKey=1";
		document.updateUserForm.submit();//提交表单
	}
}

function showMes(val){
	if(val == 1){
		alert("用户名或者密码错误...");
	}else if(val == 2){
		alert("初始化数据出错,请联系厂家...");
	}else if(val == 3){
		alert("数据还没有初始化,请用admin登陆一次.系统将会自动初始化数据库...");
	}
	$("#user_name").focus();
}
