//--------------------只能输入数据验证的js
// window.event.keyCode
// 13:回车键		48~57为0~9;	45:负号	46:小数点
function JHshNumberText(){
  if ( !( ((window.event.keyCode >= 48) && (window.event.keyCode <= 57)) || 
  	(window.event.keyCode == 46) ||
  	(window.event.keyCode == 45) ||
  	(window.event.keyCode == 13) )){
   window.event.keyCode = 0 ;
  }
}

function chekcNumber(obj, type){ // type(1:无符号正整数    2:有符号的数字,包括浮点数)
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

// type[undefined:错误显示在div(不输入参数) 1:错误跳出窗口形式 ]
function checkData(id,dataType,msg,type){
	var obj = $("#"+id);
	var val = obj.val();
	var bool = true;
	switch(dataType){
		case "integer1":	// 大于零的正整数
			bool = (/^[1-9]\d*$/).test(val);
			msg = msg + "--只能输入大于0的整数,也不能是0开头的数字."		
			break;
		case "integer0":	// 正整数或0
			bool = (/^\d+$/).test(val);
			msg = msg + "--只能输入大于或等于0的整数."		
			break;		
		case "num":			// 整数-正负皆可
			bool = (/^[+|-]?\d+$/).test(val);
			msg = msg + "--输入的不是整数."
			break;		
		case "float":		// 浮点数和整数=所有数字
			bool = (/^[+|-]?\d+\.?\d*$/).test(val);
			msg = msg + "--输入的不是数字."		
			break;
		case "stringLen":	// 字符串长度大于零
			bool = val.replace(/[ ]/g,"").length > 0;
			msg = msg + "--不能为空."		
			break;
		case "color":		// 颜色
			bool = (/^#[a-fA-F0-9]{6}$/).test(val)||(/^#[a-fA-F0-9]{3}$/).test(val);
			msg = msg + "--输入颜色不正确."		
			break;		
		case "BCD":			// BCD码4个数字
			bool = (/^[0-9]{4}$/).test(val);
			msg = msg + "--需要输入4个数字."		
			break;
		case "PHONE":		// 手机号码
			bool = (/^(13|15|18)\d{9}$/).test(val);
			msg = msg + "--手机号码是以13,15,18开头的11个数字."		
			break;
		case "CODE":		// 字母
			bool = (/^[A-Za-z]+$/).test(val);
			msg = msg + "--只能输入小写或者大写的字母."		
			break;
		case "BINFILE":		// 字母
			bool = val.substring(val.lastIndexOf(".")+1) == "bin";
			msg = msg + "--不是要导入的类型(正确的类型要以.bin结尾)."		
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
			tempmsg = "没有选择仪器...";
		}
		if (bool){
			bool = $("input:hidden[name='maxSelector']").val() >= $("input[id^='eq']:checked").size();
			tempmsg = msg + $("input:hidden[name='maxSelector']").val() + "个";
		}
		break;	
	}
	if (!bool){submitFailure(tempmsg)} 
	return bool;	
}  

function checkPhoneLen(){
	var bool = $("input[id^='eq']:checked").size() != 0;
	if (!bool){
		var tempmsg = "没有选择手机号码...";
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

// --------------------错误时-提示信息--------------------//
function submitFailure(msg){
	$("#errorArea").show("fast");
	$("#errorArea").css({ color: "red", display: "block" });
	$("#errorArea").html(msg);
}
//--------------------按钮变灰-恢复----------------//
// 1:恢复 0:变灰
function ableToButton(flag){
	// 使所有按钮恢复
	$(":button").each(function(){
		this.disabled = flag == 0 ? true : false;
	});		
}
//---------------截取字符,多于Ilength后加"..."----------------//
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
//---------------post请求encodeURIComponent编码----------------//
//---java中用URLDecoder.decode(sendcontent, "UTF-8");解码------//
//---id,type[1:Id,需要用jquery转换,2:要处理的内容,不用转换]------//
	function getDecStr(id,type){
		var val;
		if (type == 1){var obj = $("#"+id);val = obj.val();}
		else if(type == 2){val = id;}
		if (window.RegExp && window.encodeURIComponent){
			val = encodeURIComponent(val);
		}
		return val;
	}


//---------------全选/反选/不选----------------//
// type[1:全选 2:反选 3:不选]
	function checkAll(type,name){
		$(":checkbox[name='" + name+ "']").each(function(){
			if (type == 1){//全选
				$(this).attr("checked", true);
			}else if (type == 2){//反选
				$(this).attr("checked", $(this).attr("checked")==true?false:true);
			}else if (type == 3){//不选
				$(this).attr("checked", false)
			}
		});
	}		
		
//---------------播放背景音乐----------------//
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
//---------------判断浏览器是否安装了flash----------------//	
//用法:	var fls = getFlashVer();
//		if (fls.f==1){document.write("您安装了flash,当前版本为: "+fls.v+".x");
//		}else{document.write("您没有安装flash");	}
	function getFlashVer() {
		var hasFlash=0;			//是否安装了flash
		var f="";				//flash版本
		var p=1 ;				// 是否是IE浏览器[1:IE	2:Fx等非IE]
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
			// 方法一 有点问题
			//try {
			//	var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash'); 
			//	if(swf) {
			//		var VSwf = swf.GetVariable("$version");
			//		f = parseInt(VSwf.split(" ")[1].split(",")[0]); 
			//	}
			//}catch(e) {hasFlash=0}

			// 方法二
			for (var ii = 10;ii >= 2; ii--) {
				try {
					var fl=eval("new ActiveXObject('ShockwaveFlash.ShockwaveFlash."+ii+"');");
					if (fl) {f=ii + ".0"; hasFlash = 1; break; }
				}catch(e) {hasFlash=0}
			}

		}
		return {f:hasFlash,v:f,p:p};
	}	
// 页面处理flash版本信息,及下载管理
	function displayFlash(){
		var fls = getFlashVer();
		if (fls.f==0){
			//$("#showFlash").hide();
			var down=null;
			if (fls.p == 1){
				down = "请更新Flash player版本,[<a href='/eeweb/flash/ie_flash_player.exe'>点击下载Falsh</a>] "
			}else if (fls.p == 2){
				down = "请更新Flash player版本,[<a href='/eeweb/flash/fx_flash_player.exe'>点击下载Falsh</a>] "
			}
			var com = "或者Flash palyer官方网站[<a href=\"http://get.adobe.com/flashplayer/\" target=_blank>下载</a>].注意: 安装时请关闭所有浏览器,待安装完毕后,再打开浏览器访问,如果还是不能正常显示,请联系我们...";
			$("#showFlash").html(down+com);
		}else{$("#showFlash").hide();}		
	}