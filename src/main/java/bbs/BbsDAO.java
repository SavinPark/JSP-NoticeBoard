package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	// ----------- MySQL 접속 ----------- //
	// UserDAO : 데이터 접근 객체
	public BbsDAO() {
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
	
	// 1) 현재 시간을 가져오는 함수
	public String getDate() {
		String SQL = "SELECT NOW()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";  // 데이터베이스 오류
	}
	
	// 2) bbdID를 가져오는 함수
	public int getNext() {
		String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1 ; // 첫 번재 게시물인 경우
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  // 데이터베이스 오류 (게시물 번호로 적절하지 않은 -1 리턴)
	}
	
	// 3) 하나의 게시물을 데이터베이스에 삽입하는 함수
	public int write(String bbsTitle, String userID, String bbsContent) {
		String SQL = "INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			return pstmt.executeUpdate(); // 성공적으로 수행하면 0이상의 숫자 리턴
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  // 데이터베이스 오류 (게시물 번호로 적절하지 않은 -1 리턴)
	}
	
	// 4) 데이터베이스에서 글의 목록을 가져오는 함수
	public ArrayList<Bbs> getList(int pageNumber) {
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1 ORDER BY bbsID DESC LIMIT 10";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			while  (rs.next()) {
				Bbs bbs = new Bbs();
				
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				
				list.add(bbs);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;  // 데이터베이스 오류 (게시물 번호로 적절하지 않은 -1 리턴)
	}
	
	// 5) 페이징 처리를 위한 함수
	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable = 1";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			if  (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;  // 데이터베이스 오류 (게시물 번호로 적절하지 않은 -1 리턴)
	}
	
	// 6) 하나의 게시물 내용을 불러오는 함수
	public Bbs getBbs(int bbsID) {
		String SQL = "SELECT * FROM BBS WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			rs = pstmt.executeQuery();
			if  (rs.next()) {
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;  // 데이터베이스 오류 (게시물 번호로 적절하지 않은 -1 리턴)
	}
	
	// 7) 수정한 게시글을 DB에 UPDATE하는 함수
	public int update(int bbsID, String bbsTitle, String bbsContent) {
		String SQL = "UPDATE BBS SET bbsTitle = ?, bbsContent = ? WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate(); // 성공적으로 수행하면 0 이상의 숫자 리턴
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;  // 데이터베이스 오류 (게시물 번호로 적절하지 않은 -1 리턴)
	}
	
	// 8) parameter로 전달된 ID값을 가진 게시글의 Available 상태를 0으로 변경시키는 함수
	public int delete(int bbsID) {
		String SQL = "UPDATE BBS SET bbsAvailable = 0 WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
}
