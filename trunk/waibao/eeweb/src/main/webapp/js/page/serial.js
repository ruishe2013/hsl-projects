// type[1:导入  2:导出]配置
function submitConfig(type){
	$("#errorArea").hide(); // 隐藏信息区域 所用按钮变灰
	if (type == 1){
		var bool = true;
		bool = checkData("configFile","stringLen","导入文件名");
		if (bool){bool = checkData("configFile","BINFILE","选择的文件格式有误");}
		if (bool){
			submitFailure("正在导入配置文件...");
			//document.importForm.action = '${pageContext.request.contextPath}/userdo/configUploadAction';
			document.importForm.action = "configUploadAction";
			document.importForm.submit();
		}
	}
}

//开启 或者 关闭 串口
function openOrClose(){
	var bool = true;
	if ($("#runFlag").val() == 1){
		bool = confirm("确定要停止串口吗?")
		// 停止正在运行从的串口
		if (bool){
			document.serialFrom.action = "serialportAction!endRun"
			$("#sendbut").val("停止串口中...");
		}
	}else if ($("#runFlag").val() == 2){
		bool = confirm("确定启动串口吗?")
		if (bool){
			// 启动已经停止的串口
			document.serialFrom.action = "serialportAction!startRun"
			$("#sendbut").val("启动串口中...");
		}
	}
	if (bool){ableToButton(0); 	document.serialFrom.submit();}
}

//type:0:串口号测试  	1:校验时间   2:读取 机号 地址 3:设置机号(BCD)和地址	4:更改仪器其他常量 
function setSerial(type){
	var bool = true;
	switch (type) {
		case 0:	
			document.configInitForm.action = "portSetserialSetAction";
			document.configInitForm.submit();
			break;
		case 1:
			document.configPowerForm.action = "checkTimeserialSetAction";
			document.configPowerForm.submit();
			break;
		case 2:
			document.configSetForm.action = "readInfoserialSetAction";
			document.configSetForm.submit();
			break;
		case 3:
			if (bool){bool = checkData("address","integer1","地址");}
			if (bool){bool = checkData("MCType","BCD","类型号");}
			if (bool){bool = checkData("MCOrder","BCD","批次");}
			if (bool){bool = checkData("MCNo","BCD","流水号");}
			if (bool){
				document.configSetForm.action = "setMacAddrserialSetAction";
				document.configSetForm.submit();
			}
			break;
		case 4:
			if (bool){bool = checkData("configaddress","integer1","地址");}
			if (bool){bool = checkData("configvalue","num","属性数据");}
			if (bool){
				document.configDataForm.action = "updateEqConfigserialSetAction";
				document.configDataForm.submit();
			}
			break;
	}

	if (bool){
		ableToButton(0); //所用按钮变灰
		$("#errorArea").hide(); // 隐藏信息区域
	}
}