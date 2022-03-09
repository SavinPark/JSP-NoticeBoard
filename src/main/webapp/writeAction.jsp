<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="bbs.BbsDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>

<jsp:useBean id="bbs" class="bbs.Bbs" scope="page" />
<jsp:setProperty name="bbs" property="bbsTitle" />
<jsp:setProperty name="bbs" property="bbsContent" />

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
		if (userID == null) {
			// 로그인 상태 X
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("location.href = 'login.jsp'");
			script.println("</script>");
		} else {
			// 로그인 상태 O
			
			// 입력하지 않은 항목이 있을 경우
	    	if (bbs.getBbsTitle() == null || bbs.getBbsContent() == null) {
	    		PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('입력해야하는 항목이 남아있습니다.')");
				script.println("history.back()");
				script.println("</script>");
	    	} else {
	    	
	    		BbsDAO bbsDAO = new BbsDAO(); // 데이터베이스에 접근할 수 있는 객체
	    	
	    		int result = bbsDAO.write(bbs.getBbsTitle(), userID, bbs.getBbsContent());
	    	
	    	
	    		if (result == -1) {  // 동일한 ID를 입력했을 경우 데이터베이스에서 오류 발생 (userID는 고유한 기본키 PK)
	    			PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('글쓰기에 실패하였습니다.')");
					script.println("history.back()");
					script.println("</script>");
	    		} else {  // 회원가입이 정상적으로 이루어진 경우
	    			PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("location.href='bbs.jsp'");
					script.println("</script>");
	    		}
	    	}
			
		}
	%>
    
</body>
</html>