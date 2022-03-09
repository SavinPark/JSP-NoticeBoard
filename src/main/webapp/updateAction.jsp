<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="bbs.Bbs" %>
<%@ page import="bbs.BbsDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>

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
	    
	    // 사용자
		String userID = null;
		if (session.getAttribute("userID") != null) {
			userID = (String) session.getAttribute("userID");
		}
		if (userID == null) {    // 로그인 상태 X
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("location.href = 'login.jsp'");
			script.println("</script>");
		}
		
		// 게시글
		int bbsID = 0;
		if (request.getParameter("bbsID") != null) {
			bbsID = Integer.parseInt(request.getParameter("bbsID"));
		}
		if (bbsID == 0) {    // 
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 글입니다.')");
			script.println("location.href = 'bbs.jsp'");
			script.println("</script>");
		}
		
		// 
		Bbs bbs = new BbsDAO().getBbs(bbsID);
		if (!userID.equals(bbs.getUserID())) {  // 권한이 없는 경우 ( 글쓴이ID != 접속자ID )
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('권한이 없습니다.')");
			script.println("location.href = 'bbs.jsp'");
			script.println("</script>");
		} else {   // 권한이 있는 경우 ( 글쓴이ID == 접속자ID )
			
	    	if (request.getParameter("bbsTitle") == null || request.getParameter("bbsContent") == null
	    	    || request.getParameter("bbsTitle").equals("") || request.getParameter("bbsContent").equals("")) { // 입력하지 않은 항목이 있는 경우
	    		PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('입력할 항목이 남아 있습니다.')");
				script.println("history.back()");
				script.println("</script>");
	    	} else {
	    		BbsDAO bbsDAO = new BbsDAO();
	    		int result = bbsDAO.update(bbsID, request.getParameter("bbsTitle"), request.getParameter("bbsContent"));
	    		if (result == -1) {    // 데이터베이스 오류가 발생했을 경우
	    			PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('글수정에 실패하였습니다.')");
					script.println("history.back()");
					script.println("</script>");
	    		} else {    // 정상처리 되었을 경우 게시판메인화면(bbs.jsp)로 이동
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