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

// ��ʼ�� -�б� type[1:�б�  2:���ӻ����޸� 3:gprsTesInfo.jspҳ����]
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
	
}// end fun



// type[1:����  2:����ɾ�� 3:���� 4:�޸� 5:ɾ��]
function submitList(type,gprsSetId){
	$("#errorArea").hide(); //������Ϣ����
	if (type == 1){
		document.gprsFormList.action = "gprs_percreate_Action";
		document.gprsFormList.submit();
	}else if (type == 2){
		if ( confirm("ȷ��Ҫɾ����?") && ($("input[name='ids']:checked").size() != 0) ){
				document.gprsFormList.action = "gprs_deleteSelect_Action";
				document.gprsFormList.submit();
		}
	}else if (type == 3){
	}else if (type == 4){
		$("#gprsSet_gprsSetId").val(gprsSetId);
		document.gprsFormList.action = "gprs_perupdate_Action";
		document.gprsFormList.submit();
	}else if (type == 5){
		if (confirm("ȷ��Ҫɾ����?")){
			$("#gprsSet_gprsSetId").val(gprsSetId);
			document.gprsFormList.action = "gprs_delete_Action";
			document.gprsFormList.submit();
		}
	}
}// end fun

// type[1:����  2:����]
function submitAdd(type){
	$("#errorArea").hide(); // ������Ϣ���� 
	var bool = true;
	if (type == 1){
		if (bool){bool = checkData("gprsSet_numId","integer0","���ִ���");}
		if (bool){bool = checkData("gprsSet_alias","CODE","��ĸ����");}
		if (bool){bool = checkData("gprsSet_mesFormat","stringLen","��ʽ");}
		if (bool){bool = checkData("gprsSet_remark","stringLen","˵��");}
		if (bool){
			document.addGprsForm.action = "gprs_create_Action";
			document.addGprsForm.submit();//�ύ��
		}
	}else if (type == 2){
		if ($("#gprsSet_numId").val() == ""){$("#gprsSet_numId").val(0)}
		document.addGprsForm.action = "gprs_getList_Action";
		document.addGprsForm.submit();
	}
}

// type[1:�޸�  2:����]
function submitUpdate(type){
	$("#errorArea").hide(); // ������Ϣ���� 
	var bool = true;
	if (type == 1){
		if (bool){bool = checkData("gprsSet_numId","integer0","���ִ���");}
		if (bool){bool = checkData("gprsSet_alias","CODE","��ĸ����");}
		if (bool){bool = checkData("gprsSet_mesFormat","stringLen","��ʽ");}
		if (bool){bool = checkData("gprsSet_remark","stringLen","˵��");}
			if (bool){
				document.updateGprsForm.action = "gprs_update_Action";
				document.updateGprsForm.submit();//�ύ��
		}			
	}else if (type == 2){
		document.updateGprsForm.action = "gprs_back_Action";
		document.updateGprsForm.submit();
	}
}

// ���Զ������ĺ���
function submitTest(type){
	$("#errorArea").hide(); // ������Ϣ���� 
	var bool = true;
	if(type == 1){
		ableToButton(0);
		if ($("#runFlag").val() == 1){
			sendAjax(2);	// �Ͽ�
		}else{
			sendAjax(1);	// ����
		}
	}else if(type == 2){			
	}else if(type == 3){
		if (bool){bool = checkData("centerNo","PHONE","�������ĺŸ�ʽ����ȷ");}		
		if (bool){bool = checkData("phoneNo","PHONE","Ŀ���ֻ��Ÿ�ʽ����ȷ");}
		if(bool){bool = checkData("sendcontent","stringLen","������Ϣ���� ");}
		if (bool){sendAjax(3);}
	}else if(type == 4){
		$("#sendcontent").attr("value",'');	$("#phoneNo").attr("value",'');
	}
}

// type[1:���� 2:�Ͽ� 3:����]--ajax��������
function sendAjax(type){
	$("#errorArea").hide(); // ������Ϣ���� 
	var actionPath = "${pageContext.request.contextPath}/mana/";
	var paramArgs = null;
	var msgshox;
	
	if (type == 1){
		msgshox = "���ڽ�������...";
		$("#sendbut").val(msgshox);
		actionPath = actionPath + "gprsTestlinkJson.action";
	  //paramArgs = {centerNo:getDecStr("centerNo",1),portNo:getDecStr("portNo",1),baudRateNo:getDecStr("baudRateNo",1)};
	}else if (type == 2){
		msgshox = "���ڶϿ�����...";
		$("#sendbut").val(msgshox);
		actionPath = actionPath + "gprsTestdislinkJson.action";
	}else if (type == 3){
		msgshox = "���ڷ���...";
		paramArgs = {phoneNo:getDecStr("phoneNo",1),sendcontent:getDecStr("sendcontent",1)};
		actionPath = actionPath + "gprsTestsendJson.action";
		$("#errorArea").show();
		$("#errorArea").html(msgshox);
	}		
	// ��������
	$.post(actionPath, paramArgs, function(data) {
		//��JSONת��Ϊһ������,������Ϊjoson
		eval("var json=" + data);
		$("#runFlag").val(json.runFlag);
		if (type != 3){
			$("#sendbut").val(json.stateStr);
			ableToButton(1); // �ָ����а�ť
		}else{
			$("#errorArea").html(json.msgShow);
		}
	});//end post
	
	
}