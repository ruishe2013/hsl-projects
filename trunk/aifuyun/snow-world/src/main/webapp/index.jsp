<%@page import="java.util.*"%>
<html>
<body>
<h2>Hello World!</h2>

<form>
	<input name="name" value="" />
	<input type="submit" value="submit!" />
</form>
<%
	String name = request.getParameter("name");
	out.println(name);
	out.println("<br>");
	out.println("<br>");
	Map m = request.getParameterMap();
	
	for (Object v : m.values()) {
		String[] values = (String[])v;
		for (String value : values) {
		out.println(value);
		out.println("<br>");
		}
	}
%>
</body>
</html>
