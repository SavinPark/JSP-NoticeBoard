package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	// ----------- MySQL 접속 ----------- //
	// UserDAO : 데이터 접근 객체
	public UserDAO() {
		// try-catch 예외처리
		try {
			// localhost:3306 은 MySQL 서버 자체를 의미하고, BBS는 데이터베이스
			String dbURL = "jdbc:mysql://localhost:3306/BBS";
			String dbID = "root";
			String dbPassword = "root";
			
			// mysql Driver 찾기
			// Driver : mysql에 접속할 수 있도록 매개체 역할을 하는 하나의 라이브러리
			Class.forName("com.mysql.jdbc.Driver");
			
			// dbURL에 dbID, dbPassword로 접속
			// 접속이 완료되면 conn 객체 안에 접속된 정보가 담기게 되는 것!
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
		} catch (Exception e) {
			e.printStackTrace(); // 어떤 오류인지 출력
		}
	}
	
	
	// ----------- 로그인 ----------- //
	// 이 로그인 기능이 호출되는 페이지가 바로 loginAction.jsp
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE userID = ?";
		
		try {
			// 
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(1).equals(userPassword)) {
					return 1; // '로그인 성공'
				} else {
					return 0; // '비밀번호 불일치'
				}
			} 
			return -1;  // '아이디 없음'
		} catch (Exception e) {
			e.printStackTrace(); // 어떤 오류인지 출력
		}
		return -2; // '데이터베이스 오류'
	}
	
	
	// --------- 회원가입 --------- //
	public int join(User user) {
		String SQL ="INSERT INTO USER VALUES(?, ?, ?, ?, ?)";
		
		try {
			// 차례대로 ID, Password, Name, Gender, Email 
			pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			
			return pstmt.executeUpdate(); 
			
		} catch (Exception e) {
			// Error
			e.printStackTrace(); // 어떤 오류인지 출력
		}
	    return -1 ; // '데이터베이스 오류' : INSERT를 실행할 경우 0이상의 숫자를 반환하기 때문에 -1이면 Error
	
	}

}
