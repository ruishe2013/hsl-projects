//--------------------ֻ������������֤��js
// window.event.keyCode
// 13:�س���		48~57Ϊ0~9;	45:����	46:С����
function JHshNumberText(){
  if ( !( ((window.event.keyCode >= 48) && (window.event.keyCode <= 57)) || 
  	(window.event.keyCode == 46) ||
  	(window.event.keyCode == 45) ||
  	(window.event.keyCode == 13) )){
   window.event.keyCode = 0 ;
  }
}

function chekcNumber(obj, type){ // type(1:�޷���������    2:�з��ŵ�����,����������)
  var pattern = type == 1 ? /^[0-9]\d*$/ : /^[+|-]?\d*\.?\d*$/;
	var tempValue = obj.value;
	
	while (!pattern.test(tempValue)) {
			tempValue = tempValue.substring(0, tempValue.length - 1);
			if (tempValue.length == 0){
				tempValue = 0;
				break;
			}
	}
	obj.value = tempValue ;
}

function checkEquals(id,value,msg){
	var obj = $("#"+id);
	var val = obj.val();
	var bool = val != value;
	if (!bool){obj.focus();
	submitFailure(msg)} 
	return bool;	
}

function checkMaxMin(minId,maxId,msg){
	var min = $("#"+minId);
	var max = $("#"+maxId);
	var bool = Number(min.val()) < Number(max.val());
	if (!bool){min.focus();
	submitFailure(msg)} 
	return bool;	
}

// type[undefined:������ʾ��div(���������) 1:��������������ʽ ]
function checkData(id,dataType,msg,type){
	var obj = $("#"+id);
	var val = obj.val();
	var bool = true;
	switch(dataType){
		case "integer1":	// �������������
			bool = (/^[1-9]\d*$/).test(val);
			msg = msg + "--ֻ���������0������,Ҳ������0��ͷ������."		
			break;
		case "integer0":	// ��������0
			bool = (/^\d+$/).test(val);
			msg = msg + "--ֻ��������ڻ����0������."		
			break;		
		case "num":			// ����-�����Կ�
			bool = (/^[+|-]?\d+$/).test(val);
			msg = msg + "--����Ĳ�������."
			break;		
		case "float":		// ������������=��������
			bool = (/^[+|-]?\d+\.?\d*$/).test(val);
			msg = msg + "--����Ĳ�������."		
			break;
		case "stringLen":	// �ַ������ȴ�����
			bool = val.replace(/[ ]/g,"").length > 0;
			msg = msg + "--����Ϊ��."		
			break;
		case "color":		// ��ɫ
			bool = (/^#[a-fA-F0-9]{6}$/).test(val)||(/^#[a-fA-F0-9]{3}$/).test(val);
			msg = msg + "--������ɫ����ȷ."		
			break;		
		case "BCD":			// BCD��4������
			bool = (/^[0-9]{4}$/).test(val);
			msg = msg + "--��Ҫ����4������."		
			break;
		case "PHONE":		// �ֻ�����
			bool = (/^(13|15|18)\d{9}$/).test(val);
			msg = msg + "--�ֻ���������13,15,18��ͷ��11������."		
			break;
		case "CODE":		// ��ĸ
			bool = (/^[A-Za-z]+$/).test(val);
			msg = msg + "--ֻ������Сд���ߴ�д����ĸ."		
			break;
		case "BINFILE":		// ��ĸ
			bool = val.substring(val.lastIndexOf(".")+1) == "bin";
			msg = msg + "--����Ҫ���������(��ȷ������Ҫ��.bin��β)."		
			break;
	}		
	
	if (!bool){
		obj.focus();
		if (type == undefined){
			submitFailure(msg);
		}else if (type ==1){
			alert(msg);
		}
	}
	return bool;
}

function checkEQLen(dataType,msg,append){
	var tempmsg;
	var bool = true;
	switch(dataType){
	case "EQLEN":
		if (append == 1){
			bool = $("input[id^='eq']:checked").size() != 0;
			tempmsg = "û��ѡ������...";
		}
		if (bool){
			bool = $("input:hidden[name='maxSelector']").val() >= $("input[id^='eq']:checked").size();
			tempmsg = msg + $("input:hidden[name='maxSelector']").val() + "��";
		}
		break;	
	}
	if (!bool){submitFailure(tempmsg)} 
	return bool;	
}  

function checkPhoneLen(){
	var bool = $("input[id^='eq']:checked").size() != 0;
	if (!bool){
		var tempmsg = "û��ѡ���ֻ�����...";
		submitFailure(tempmsg);
	}
	return bool;
}

function checkboxSize(name, msg){
	var bool = $(":checkbox[name^='" + name+ "']:checked").size() != 0;
	if (!bool){submitFailure(msg)}
	return bool;	
}

function checkDate_time(dataType, startTime,endTime, compare_value, msg){
	var bool = true;
	startTime =  startTime.replace("-","/").replace("-","/");
	endTime = endTime.replace("-","/").replace("-","/");
	compare_value = Number(compare_value);
	var rsTime = new Date(endTime) - new Date(startTime);
	bool = rsTime >= 0;
	if (bool){
		switch(dataType){
		case "MINUTE":
			bool = (compare_value - rsTime/60000) >= 0 ;
			break;
		case "DAY":
			bool = (compare_value - rsTime/60000/60/24) >= 0 ;
			break;
		case "MONTH":
			bool = (compare_value - rsTime/60000/60/24/31) >= 0 ;
		case "YEAR":
			bool = (compare_value - rsTime/60000/60/24/31/365) >= 0 ;
			break;
		}
	}
	if (!bool){submitFailure(msg)}
	return bool;	
}  

function getEQList(){
	var userPlaceList = "";
	$("input[id^='eq']:checked").each(function(){
		userPlaceList += $(this).val()+",";
	 });
	return userPlaceList;
}

// --------------------����ʱ-��ʾ��Ϣ--------------------//
function submitFailure(msg){
	$("#errorArea").show("fast");
	$("#errorArea").css({ color: "red", display: "block" });
	$("#errorArea").html(msg);
}
//--------------------��ť���-�ָ�----------------//
// 1:�ָ� 0:���
function ableToButton(flag){
	// ʹ���а�ť�ָ�
	$(":button").each(function(){
		this.disabled = flag == 0 ? true : false;
	});		
}
//---------------��ȡ�ַ�,����Ilength���"..."----------------//
function CutStrLength(str,ilength)   {   
    var tmp=0;   
    var len=0;   
    var okLen=0;  
    var rs="";
    for(var i=0;i<ilength;i++)   {   
          if(str.charCodeAt(i)>255)   
              tmp+=2  
          else  
              len+=1  
          okLen+=1  
          if(tmp+len==ilength)   {
        	  rs = str.substring(0,okLen);   
              break;   
          }   
          if(tmp+len>ilength)   {
        	  rs = str.substring(0,okLen-1);   
              break;   
          }   
      }
    rs = rs + (str.replace(/[^\x00-\xff]/g,"**").length >ilength?"...":"");
    return rs;
  }   
//---------------post����encodeURIComponent����----------------//
//---java����URLDecoder.decode(sendcontent, "UTF-8");����------//
//---id,type[1:Id,��Ҫ��jqueryת��,2:Ҫ���������,����ת��]------//
	function getDecStr(id,type){
		var val;
		if (type == 1){var obj = $("#"+id);val = obj.val();}
		else if(type == 2){val = id;}
		if (window.RegExp && window.encodeURIComponent){
			val = encodeURIComponent(val);
		}
		return val;
	}


//---------------ȫѡ/��ѡ/��ѡ----------------//
// type[1:ȫѡ 2:��ѡ 3:��ѡ]
	function checkAll(type,name){
		$(":checkbox[name='" + name+ "']").each(function(){
			if (type == 1){//ȫѡ
				$(this).attr("checked", true);
			}else if (type == 2){//��ѡ
				$(this).attr("checked", $(this).attr("checked")==true?false:true);
			}else if (type == 3){//��ѡ
				$(this).attr("checked", false)
			}
		});
	}		
		
//---------------���ű�������----------------//
	function soundplay(id,width,auto){
		$("#"+id).jmp3({
			filepath:"../music/",
			showfilename: "false",
			backcolor: "000000",
			forecolor: "00ff00",
			width: width,
			showdownload: "false",
			autoplay: auto,
			repeat: "true",
			volume:"100"
		});				
	}
//---------------�ж�������Ƿ�װ��flash----------------//	
//�÷�:	var fls = getFlashVer();
//		if (fls.f==1){document.write("����װ��flash,��ǰ�汾Ϊ: "+fls.v+".x");
//		}else{document.write("��û�а�װflash");	}
	function getFlashVer() {
		var hasFlash=0;			//�Ƿ�װ��flash
		var f="";				//flash�汾
		var p=1 ;				// �Ƿ���IE�����[1:IE	2:Fx�ȷ�IE]
		var n=navigator;
		if (n.plugins && n.plugins.length) {
			p = 2;
			for (var ii=0;ii<n.plugins.length;ii++) {
				if (n.plugins[ii].name.indexOf('Shockwave Flash')!=-1) {
					f=n.plugins[ii].description.split('Shockwave Flash ')[1].split(' ')[0];
					hasFlash = 1;
					break;
				}
			}
		} else if (window.ActiveXObject) {
			p = 1;
			// ����һ �е�����
			//try {
			//	var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash'); 
			//	if(swf) {
			//		var VSwf = swf.GetVariable("$version");
			//		f = parseInt(VSwf.split(" ")[1].split(",")[0]); 
			//	}
			//}catch(e) {hasFlash=0}

			// ������
			for (var ii = 10;ii >= 2; ii--) {
				try {
					var fl=eval("new ActiveXObject('ShockwaveFlash.ShockwaveFlash."+ii+"');");
					if (fl) {f=ii + ".0"; hasFlash = 1; break; }
				}catch(e) {hasFlash=0}
			}

		}
		return {f:hasFlash,v:f,p:p};
	}	
// ҳ�洦��flash�汾��Ϣ,�����ع���
	function displayFlash(){
		var fls = getFlashVer();
		if (fls.f==0){
			//$("#showFlash").hide();
			var down=null;
			if (fls.p == 1){
				down = "�����Flash player�汾,[<a href='/eeweb/flash/ie_flash_player.exe'>�������Falsh</a>] "
			}else if (fls.p == 2){
				down = "�����Flash player�汾,[<a href='/eeweb/flash/fx_flash_player.exe'>�������Falsh</a>] "
			}
			var com = "����Flash palyer�ٷ���վ[<a href=\"http://get.adobe.com/flashplayer/\" target=_blank>����</a>].ע��: ��װʱ��ر����������,����װ��Ϻ�,�ٴ����������,������ǲ���������ʾ,����ϵ����...";
			$("#showFlash").html(down+com);
		}else{$("#showFlash").hide();}		
	}