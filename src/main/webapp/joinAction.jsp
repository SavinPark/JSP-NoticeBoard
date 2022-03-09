<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="user.UserDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>

<jsp:useBean id="user" class="user.User" scope="page" />
<jsp:setProperty name="user" property="userID" />
<jsp:setProperty name="user" property="userPassword" />
<jsp:setProperty name="user" property="userName" />
<jsp:setProperty name="user" property="userGender" />
<jsp:setProperty name="user" property="userEmail" />

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
	    // 이미 로그인 상태인 사람은 회원가입 페이지에 접속하지 못하도록
		String userID = null;
		if (session.getAttribute("userID") != null) {
			userID = (String) session.getAttribute("userID");
		}
		if (userID != null) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('이미 로그인 상태입니다.')");
			script.println("location.href = 'main.jsp'");
			script.println("</script>");
		}
	
		// 입력하지 않은 항목이 있을 경우
	    if (user.getUserID() == null || user.getUserPassword() == null || user.getUserName() == null || 
	        user.getUserGender() == null || user.getUserEmail() == null) {
	    	PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('입력해야하는 항목이 남아있습니다.')");
			script.println("history.back()");
			script.println("</script>");
	    } else {
	    	
	    	UserDAO userDAO = new UserDAO(); // 데이터베이스에 접근할 수 있는 객체
	    	
	    	int result = userDAO.join(user);
	    	
	    	
	    	if (result == -1) {  // 동일한 ID를 입력했을 경우 데이터베이스에서 오류 발생 (userID는 고유한 기본키 PK)
	    		PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('이미 존재하는 아이디입니다.')");
				script.println("history.back()");
				script.println("</script>");
	    	} else {  // 회원가입이 정상적으로 이루어진 경우
	    		session.setAttribute("userID", user.getUserID());  // 세션 관리
	    		
	    		PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("location.href='main.jsp'");
				script.println("</script>");
	    	}
	    }

	%>
    
</body>
</html>