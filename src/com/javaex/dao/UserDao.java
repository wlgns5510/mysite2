package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";
	
	//드라이버 메소드
	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);
			// System.out.println("접속성공");

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}
	
	//자원정리 메소드
	private void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}
	
	//회원가입 --> 회원정보 저장
	public int insert(UserVo userVo) {
		
		int count = 0;

		this.getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			// SQL문 준비
			String query = "";
			query += " insert into users ";
			query += " values(seq_users_no.nextval, ?, ?, ?, ?) ";
					

			// 바인딩
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, userVo.getId());
			pstmt.setString(2, userVo.getName());
			pstmt.setString(3, userVo.getPassword());
			pstmt.setString(4, userVo.getGender());
			
			// 실행
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건이 저장되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		
		return count;

		
	}

	//사용자 정보 가져오기(로그인시 사용)
	public UserVo getUser(UserVo userVo) {
		
		UserVo authUser = null;
		
		this.getConnection();
		
		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			// SQL문 준비
			String query = "";
			query += " select no, ";
			query += "        id, ";
			query += "        password, ";
			query += "        name, ";
			query += "        gender ";
			query += " from users ";
			query += " where id = ? ";
			query += " and password = ? ";
			
			//바인딩
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, userVo.getId());
			pstmt.setString(2, userVo.getPassword());
			
			//실행
			rs = pstmt.executeQuery();
			
			//4.결과처리
			while(rs.next()) {
				int no = rs.getInt("no");
				String id = rs.getString("id");
				String password = rs.getString("password");
				String name = rs.getString("name");
				String gender = rs.getString("gender");
				
				authUser = new UserVo();
				authUser.setNo(no);
				authUser.setId(id);
				authUser.setPassword(password);
				authUser.setName(name);
				authUser.setGender(gender);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();
		return authUser;
	}

	//사용자 정보 업데이트
	public void update(UserVo userVo) {
		
		int count = -1;
		
		this.getConnection();
		
		try {
			
			// 3. SQL문 준비 / 바인딩 / 실행
			// SQL문 준비
			String query = "";
			query += " update users ";
			query += " set name = ?, ";
			query += "     password = ?, ";
			query += "     gender = ? ";
			query += " where id = ? ";
			
			//바인딩
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, userVo.getName());
			pstmt.setString(2, userVo.getPassword());
			pstmt.setString(3, userVo.getGender());
			pstmt.setString(4, userVo.getId());
			
			//실행
			count = pstmt.executeUpdate();
			
			//4.결과처리
			
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		  }
		close();
		if (count != -1) System.out.println("[" + count + "건 수정되었습니다]");
		}
		

}
