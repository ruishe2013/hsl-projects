function onkeydown(e) { 
	var currKey=0,e=e||event;
	currKey=e.keyCode||e.which||e.charCode;
	if (currKey == 13){
		var pageId = $("#pageId").val();
		if (pageId == "add"){submitAdd(1);}
		else if (pageId == "update"){submitUpdate(1);}
	} 
}
//ע����̵Ļس��¼�
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
				document.areaFormList.action = "area_getList_Action";
				document.areaFormList.submit();
		  }
	}
}
// type[1:����  2:����ɾ�� 3:���� 4:�޸� 5:ɾ��]
function submitList(type,placeId){
	$("#errorArea").hide(); //������Ϣ����
	if (type == 1){
		document.areaFormList.action = "area_percreate_Action";
		document.areaFormList.submit();
	}else if (type == 2){
		if ( confirm("ȷ��Ҫɾ����?") && ($("input[name='ids']:checked").size() != 0) ){
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
		if (confirm("ȷ��Ҫɾ����?")){
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

// type[1:����  2:����]
function submitAdd(type){
	$("#errorArea").hide();// ������Ϣ����
	var bool = true;
	if (type == 1){
		if (bool){bool = checkData("place_placeName","stringLen","������");}
		if (bool){bool = checkEquals("place_placeName","δ����","����������ʹ��Ĭ�ϵ�[δ����]����...");}				
		if (bool){ableToButton(0); // ���ð�ť���
			document.addAreaForm.action = "area_create_Action";
			document.addAreaForm.submit();//�ύ��
		}
	}else if (type == 2){
		document.addAreaForm.action = "area_back_Action";
		document.addAreaForm.submit();
	}
}// end fun

// type[1:�޸�  2:����]
function submitUpdate(type){
	var bool = true;
	if (type == 1){
		if (bool){bool = checkData("place_placeName","stringLen","������");}
		if (bool){bool = checkEquals("place_placeName","δ����","����������ʹ��Ĭ�ϵ�[δ����]����...");}
		if (bool){ableToButton(0); $("#errorArea").hide(); // ������Ϣ���� ���ð�ť���
		document.updateAreaForm.action = "area_update_Action";
		document.updateAreaForm.submit();//�ύ��
		}			
	}else if (type == 2){
		document.updateAreaForm.action = "area_back_Action";
		document.updateAreaForm.submit();
	}
}// end fun

// �޸Ķ��Ź���
function submitUpGsm(){
	var place_gsm = getEQList();
	place_gsm = place_gsm.substring(0,place_gsm.length-1);//�ض����һ������
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
		alert("��������,�����ӱ�ע.��ɾ�������ӱ�ע,Ȼ���ٽ���ɾ��...");
	}
}
