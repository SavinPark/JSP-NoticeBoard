<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<!-- HEAD -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<!-- BODY -->
<body>
	<%
		// 세션 전체 제거, 무효화
		session.invalidate();
	%>
	<script>
		location.href= 'main.jsp';
	</script>
</body>
</html>