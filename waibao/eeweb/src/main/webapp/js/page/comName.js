//��ȡ��˾����
function showCom() {
	var actionPath = '${pageContext.request.contextPath}/main/comJson.action';
	//��������
	$.post(actionPath, null, function(data) {
		
		//��JSONת��Ϊһ������,������Ϊjoson
		eval("var json=" + data);
		$("#comNamecc").text(json.comName);
	});//end pose
}		