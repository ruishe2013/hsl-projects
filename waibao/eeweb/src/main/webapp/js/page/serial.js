// type[1:����  2:����]����
function submitConfig(type){
	$("#errorArea").hide(); // ������Ϣ���� ���ð�ť���
	if (type == 1){
		var bool = true;
		bool = checkData("configFile","stringLen","�����ļ���");
		if (bool){bool = checkData("configFile","BINFILE","ѡ����ļ���ʽ����");}
		if (bool){
			submitFailure("���ڵ��������ļ�...");
			//document.importForm.action = '${pageContext.request.contextPath}/userdo/configUploadAction';
			document.importForm.action = "configUploadAction";
			document.importForm.submit();
		}
	}
}

//���� ���� �ر� ����
function openOrClose(){
	var bool = true;
	if ($("#runFlag").val() == 1){
		bool = confirm("ȷ��Ҫֹͣ������?")
		// ֹͣ�������дӵĴ���
		if (bool){
			document.serialFrom.action = "serialportAction!endRun"
			$("#sendbut").val("ֹͣ������...");
		}
	}else if ($("#runFlag").val() == 2){
		bool = confirm("ȷ������������?")
		if (bool){
			// �����Ѿ�ֹͣ�Ĵ���
			document.serialFrom.action = "serialportAction!startRun"
			$("#sendbut").val("����������...");
		}
	}
	if (bool){ableToButton(0); 	document.serialFrom.submit();}
}

//type:0:���ںŲ���  	1:У��ʱ��   2:��ȡ ���� ��ַ 3:���û���(BCD)�͵�ַ	4:���������������� 
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
			if (bool){bool = checkData("address","integer1","��ַ");}
			if (bool){bool = checkData("MCType","BCD","���ͺ�");}
			if (bool){bool = checkData("MCOrder","BCD","����");}
			if (bool){bool = checkData("MCNo","BCD","��ˮ��");}
			if (bool){
				document.configSetForm.action = "setMacAddrserialSetAction";
				document.configSetForm.submit();
			}
			break;
		case 4:
			if (bool){bool = checkData("configaddress","integer1","��ַ");}
			if (bool){bool = checkData("configvalue","num","��������");}
			if (bool){
				document.configDataForm.action = "updateEqConfigserialSetAction";
				document.configDataForm.submit();
			}
			break;
	}

	if (bool){
		ableToButton(0); //���ð�ť���
		$("#errorArea").hide(); // ������Ϣ����
	}
}