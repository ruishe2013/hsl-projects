function onkeydown(e) { 
	var currKey=0,e=e||event;
	currKey=e.keyCode||e.which||e.charCode;
	if (currKey == 13){
		var pageId = $("#pageId").val();
		if (pageId == "add"){submitAdd(1);}
		else if (pageId == "update"){submitUpdate(1)}
	} 
}
//ע����̵Ļس��¼�--��ע�س���ʱ��Ҳ�ᴥ��,����ע������
//document.onkeydown = onkeydown;

// ��ʼ�� -�б�
function showList(){
	$(function() {
	   	var havedata = $("#have_data").val();
		if (havedata > 0){
			var pagecount = $("#pagecount").val();   		var currentPage = $("#currentPage").val();
		 	$("#pager").pager({ pagenumber: currentPage, pagecount: pagecount, buttonClickCallback: PageClick });
		}
	
		//������ʾ��
		//$('thead').addClass('table-title');
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
		document.phoneFormList.action = "phone_getList_Action";
		document.phoneFormList.submit();
  }	
}// end fun

//type[1:����  2:����ɾ�� 3:���� 4:�޸� 5:ɾ��]
function submitList(type,listId){
	if (type == 1){
		document.phoneFormList.action = "phone_percreate_Action";
		document.phoneFormList.submit();
	}else if (type == 2){
		if ( confirm("ȷ��Ҫɾ����?") && ($("input[name='ids']:checked").size() != 0) ){
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
		if (confirm("ȷ��Ҫɾ����?")){
			$("#phoneList_listId").val(listId);
			document.phoneFormList.action = "phone_delete_Action";
			document.phoneFormList.submit();
		}
	}
}// end fun

// type[1:����  2:����]
function submitAdd(type){
	$("#errorArea").hide(); //  ������Ϣ����
	var bool = true;
	if (type == 1){
		if (bool){bool = checkData("phoneList_name","stringLen","����");}
		if (bool){bool = checkData("phoneList_phone","PHONE","�ֻ��Ÿ�ʽ����ȷ");}				
		if (bool){
			document.addPhoneForm.action = "phone_create_Action";
			document.addPhoneForm.submit();//�ύ��
		}
	}else if (type == 2){
//		document.addPhoneForm.action = "phone_back_Action";
//		document.addPhoneForm.submit();
	}
}

// type[1:�޸�  2:����]
function submitUpdate(type){
	$("#errorArea").hide(); //������Ϣ����
	var bool = true;
	if (type == 1){
			if (bool){bool = checkData("phoneList_name","stringLen","����");}
			if (bool){bool = checkData("phoneList_phone","PHONE","�ֻ��Ÿ�ʽ����ȷ");}
			if (bool){
				document.updatePhoneForm.action = "phone_update_Action";
				document.updatePhoneForm.submit();//�ύ��
		}			
	}else if (type == 2){
		document.updatePhoneForm.action = "phone_back_Action";
		document.updatePhoneForm.submit();
	}
}