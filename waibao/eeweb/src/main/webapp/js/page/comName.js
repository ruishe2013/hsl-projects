//获取公司名称
function showCom() {
	var actionPath = '${pageContext.request.contextPath}/main/comJson.action';
	//发送请求
	$.post(actionPath, null, function(data) {
		
		//将JSON转化为一个对象,并且名为joson
		eval("var json=" + data);
		$("#comNamecc").text(json.comName);
	});//end pose
}		