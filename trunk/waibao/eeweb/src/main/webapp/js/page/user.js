function ableToBut(flag){
	// ʹ���а�ť�ָ�
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
//ע����̵Ļس��¼�
document.onkeydown = onkeydown;

// ��¼�õ��ύ�¼�
function submitLoad(){
	var bool = $("#user_name").val().replace(/[ ]/g,"").length > 0;
	if (!bool){
		$("#user_name").focus();
		alert("�û�������Ϊ��."); 
	}else{
		bool = $("#user_pass").val().replace(/[ ]/g,"").length > 0;
		if (!bool){
			$("#user_pass").focus();
			alert("���벻��Ϊ��.")
			}
	}
	if (bool){ableToBut(0); //���ð�ť���
		document.loadForm.action = 'loginAction';
		document.loadForm.submit();//�ύ��
	}
}

// ��ʼ�� -�û��б�
function showList(){
	if ($("#pageId").val() == "list"){
		$(function() {
		   	var havedata = $("#have_data").val();
			if (havedata > 0){
				var pagecount = $("#pagecount").val();   		var currentPage = $("#currentPage").val();
				$("#pager").pager({ pagenumber: currentPage, pagecount: pagecount, buttonClickCallback: PageClick });
			}
			//������ʾ��
			$('tbody > tr:odd').toggleClass('table-tr-odd');
			$('tbody > tr:even').toggleClass('table-tr-even');
			//ѡ����
			//Ϊ��������ѡ���¼�����
			$('tbody > tr').hover(		//ע���������ʽ����
					function(){$(this).addClass('table-tr-onmouse');},
					function(){$(this).removeClass('table-tr-onmouse');}
			);		   	
		});		
		// ��ҳ		
		PageClick = function(pageclickednumber) {
			var pagecount = $("#pagecount").val();
			$("#pager").pager({ pagenumber: pageclickednumber, pagecount: pagecount, buttonClickCallback: PageClick });
			$("#currentPage").val(pageclickednumber);
			document.userFormList.action = "user_getList_Action";
			document.userFormList.submit();
		}	
	}
}
//�û��б�--type[1:����  2:����ɾ�� 3:���� 4:�޸�	5:ɾ��]
function submitList(type,userId){
	$("#errorArea").hide(); //������Ϣ����
	if (type == 1){
		document.userFormList.action = "user_percreate_Action";
		document.userFormList.submit();
	}else if (type == 2){
		if ( confirm("ȷ��Ҫɾ����?") && ($("input[name='ids']:checked").size() != 0) ){
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
		if (confirm("ȷ��Ҫɾ����?")){
			$("#user_userId").val(userId);
			document.userFormList.action = "user_delete_Action"
			document.userFormList.submit();
		}
	}
}// end fun

// type[1:����  2:����]
function submitAdd(type){
	$("#errorArea").hide(); //  ������Ϣ����
	var bool = true;
	if (type == 1){
		if (bool){bool = checkEquals("user_name","admin","�û�������Ϊadmin,admin�ǲ�����ӵ�ϵͳ�û�...");}
		if (bool){bool = checkData("user_name","stringLen","�û���");}
		if (bool){bool = checkData("user_pass","stringLen","�� ��");}
		if (bool){ableToButton(0); //���ð�ť���
			document.addUserForm.action = "user_create_Action";
			document.addUserForm.submit();//�ύ��
		}
	}else if (type == 2){
		document.addUserForm.action = "user_back_Action";
		document.addUserForm.submit();
	}
}// end fun

// type[1:�޸�  2:����]
function submitUpdate(type){
	if (type == 1){
		var bool = true;
		if (bool){bool = checkEquals("user_name","admin","�û�������Ϊadmin,admin�ǲ�����ӵ�ϵͳ�û�...");}
		if (bool){bool = checkData("user_name","stringLen","�û���");}
		if (bool){ableToButton(0); //���ð�ť���
			document.updateUserForm.action = "user_update_Action";
			document.updateUserForm.submit();//�ύ��
		}
	}else if (type == 2){
		document.updateUserForm.action = "user_back_Action";
		document.updateUserForm.submit();
	}
}

function submitupPass(){
	var bool = true;
	if (bool){bool = checkData("user_pass","stringLen","�� ��");}
	if (bool){
		ableToButton(0); $("#errorArea").hide(); //���ð�ť���; ������Ϣ����
		document.updateUserForm.action = "user_updatepass_Action?changeKey=1";
		document.updateUserForm.submit();//�ύ��
	}
}

function showMes(val){
	if(val == 1){
		alert("�û��������������...");
	}else if(val == 2){
		alert("��ʼ�����ݳ���,����ϵ����...");
	}else if(val == 3){
		alert("���ݻ�û�г�ʼ��,����admin��½һ��.ϵͳ�����Զ���ʼ�����ݿ�...");
	}
	$("#user_name").focus();
}
