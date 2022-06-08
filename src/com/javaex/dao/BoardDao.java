package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDao {
	//필드
		private Connection conn = null;
		private PreparedStatement pstmt = null;
		private ResultSet rs = null;
		
		private String driver = "oracle.jdbc.driver.OracleDriver";
		private String url = "jdbc:oracle:thin:@localhost:1521:xe";
		private String id = "webdb";
		private String pw = "webdb";
		//생성자
		
		//메소드 gs
		
		//메소드 일반
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

		//전체 불러오기 메소드
		public List<BoardVo> getList() {
			List<BoardVo> gList = new ArrayList<BoardVo>();
			getConnection();
			
			try {
				String query = "";
				query += " select br.no, ";
				query += "     	  br.title, ";
				query += "        br.content, ";
				query += "        br.hit, ";
				query += "        to_char(br.reg_date, 'YYYY-MM-DD HH24:MM') reg_date, ";
				query += "        us.name ";
				query += " from board br, users us ";
				query += " where br.user_no = us.no ";
				query += " order by no desc ";
				
				pstmt = conn.prepareStatement(query);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					int no = rs.getInt("no");
					String title = rs.getString("title");
					String name = rs.getString("name");
					String content = rs.getString("content");
					int hit = rs.getInt("hit");
					String date = rs.getString("reg_date");
					
					BoardVo boardVo = new BoardVo(no, title, name, content, hit, date);
					
					gList.add(boardVo);
				}
				
			}catch(SQLException e) {
				System.out.println("error" + e);
			}
			
			close();
			return gList;
		}
		
		//값 넣기 메소드
		public int insert(BoardVo vo) {
			int count = -1;

			this.getConnection();

			try {

				// 3. SQL문 준비 / 바인딩 / 실행
				// SQL문 준비
				String query = "";
				query += " insert into board ";
				query += " values(seq_board_no.nextval, ?, ?, 0, sysdate, ?) ";

				// 바인딩
				pstmt = conn.prepareStatement(query);

				pstmt.setString(1, vo.getTitle());
				pstmt.setString(2, vo.getContent());
				pstmt.setInt(3, vo.getUserNo());

				// 실행
				count = pstmt.executeUpdate();

				// 4.결과처리
				System.out.println(count + "건 등록");

			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

			this.close();

			return count;


		}



}
