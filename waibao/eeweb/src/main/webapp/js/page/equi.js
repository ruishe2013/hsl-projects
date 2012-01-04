function onkeydown(e) { 
	var currKey=0,e=e||event;
	currKey=e.keyCode||e.which||e.charCode;
	if (currKey == 13){
		var pageId = $("#pageId").val();
		if (pageId == "add"){submitAdd(1);}
		else if (pageId == "update"){submitUpdate(1);}
	} 
}
//ע����̵Ļس��¼�--��ע�س���ʱ��Ҳ�ᴥ��,����ע������
//document.onkeydown = onkeydown;

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
				$('#bodyList > tr:odd').toggleClass('table-tr-odd');
				$('#bodyList > tr:even').toggleClass('table-tr-even');
				//ѡ����
				//Ϊ��������ѡ���¼�����
				$('#bodyList > tr').hover(		//ע���������ʽ����
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
// type[1:����˳�� 2:����  3:����ɾ��  4:����  5:�޸�	6:ɾ��  7:���µ�ҩ��]
function submitList(type,equiId){
	$("#errorArea").hide(); //������Ϣ����
	if (type == 1){
		document.equiFormList.action = "equi_showOrderList_Action";
		document.equiFormList.submit();
	}else if (type == 2){
		document.equiFormList.action = "equi_percreate_Action";
		document.equiFormList.submit();
	}else if (type == 3){
		if ( confirm("ȷ��Ҫɾ����?") && ($("input[name='ids']:checked").size() != 0) ){
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
		if (confirm("ȷ��Ҫɾ����?")){
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

// type[1:����  2:����]
function submitAdd(type){
	$("#errorArea").hide();// ������Ϣ����
	var bool = true;
	if (type == 1){
		if (bool){bool = checkData("equipData_dsrsn","integer0","�������");}
		if (bool){bool = checkData("equipData_address","integer1","������ַ");}
		if (bool){bool = checkData("equipData_mark","stringLen","��ע");}
		if (bool){bool = checkData("tempDown","float","�¶�����");}
		if (bool){bool = checkData("tempUp","float","�¶�����");}
		if (bool){bool = checkMaxMin("tempDown","tempUp","�¶����޲��ܴ��ڻ��ߵ�������");}
		if (bool){bool = checkData("tempDev","float","�¶�ƫ��");}
		if (bool){bool = checkData("humiDown","float","ʪ������");}
		if (bool){bool = checkData("humiUp","float","ʪ������");}
		if (bool){bool = checkMaxMin("humiDown","humiUp","ʪ�����޲��ܴ��ڻ��ߵ�������");}
		if (bool){bool = checkData("humiDev","float","ʪ��ƫ��");}
		if (bool){ableToButton(0); // ���ð�ť���
			document.addequiForm.action = "equi_create_Action";
			document.addequiForm.submit();//�ύ��
		}
	}else if (type == 2){
		document.updequiForm.action = "equi_back_Action";
		document.updequiForm.submit();
	}
}// end fun

// type[1:�޸�  2:����]
function submitUpdate(type){
	var bool = true;
	if (type == 1){
		if (bool){bool = checkData("equipData_dsrsn","integer0","�������");}
		if (bool){bool = checkData("equipData_address","integer1","������ַ");}
		if (bool){bool = checkData("equipData_mark","stringLen","��ע");}
		if (bool){bool = checkData("tempDown","float","�¶�����");}
		if (bool){bool = checkData("tempUp","float","�¶�����");}
		if (bool){bool = checkMaxMin("tempDown","tempUp","�¶����޲��ܴ��ڻ��ߵ�������");}				
		if (bool){bool = checkData("humiDown","float","ʪ������");}
		if (bool){bool = checkData("humiUp","float","ʪ������");}
		if (bool){bool = checkMaxMin("humiDown","humiUp","ʪ�����޲��ܴ��ڻ��ߵ�������");}				
		if (bool){ableToButton(0); $("#errorArea").hide(); // ������Ϣ���� ���ð�ť���
			document.updequiForm.action = "equi_update_Action";
			document.updequiForm.submit();//�ύ��
		}
	}else if (type == 2){
		document.updequiForm.action = "equi_back_Action";
		document.updequiForm.submit();
	}
}// end fun

//����˳��
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

//�趨Ϊ��1:��ʪ��2:���¶� 3:��ʪ��
// type:[0:form����,1:ֵ]
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
//�л��ı���д
function changeRead(tempshow,humishow){
	tempshow=="block"?$(".temp_show").show():$(".temp_show").hide();
	humishow=="block"?$(".humi_show").show():$(".humi_show").hide();
	//$(".temp_show").css("height", tempshow=="block"?"35px":"0px");
	//$(".humi_show").css("height", humishow=="block"?"35px":"0px");
	//$(".temp_show").css("display", tempshow);
	//$(".humi_show").css("display", humishow);
}	