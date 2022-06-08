package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.dao.GuestbookDao;
import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.GuestbookVo;
import com.javaex.vo.UserVo;


@WebServlet("/bdc")
public class BoardController extends HttpServlet {
	
	//필드
	private static final long serialVersionUID = 1L;
    
	//메소드 gs
	
	
	//메소드 일반
	//get형식
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//포스트 방식일 때 한글깨짐 방지
		request.setCharacterEncoding("UTF-8");
				
		//action 파라미터일 때 꺼내기
		String action = request.getParameter("action");
		
		if("list".equals(action)) {
			System.out.println("BoardController>list");

			BoardDao boardDao = new BoardDao();
			List<BoardVo> bList = boardDao.getList();
			
			request.setAttribute("gList", bList);
			
			WebUtil.forword(request, response, "./WEB-INF/views/board/list.jsp");
		}
		else if("writeForm".equals(action)) {
			System.out.println("BoardController>writeForm");

			//로그인한 사용자의  no 값을 세션에서 가져오기
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			int no = authUser.getNo();
			
			//no 로 사용자 정보 가져오기
			UserDao userDao = new UserDao();
			UserVo userVo = userDao.getUser(no);
			
			request.setAttribute("userVo", userVo);
			WebUtil.forword(request, response, "./WEB-INF/views/board/writeForm.jsp");
		}
		else if("add".equals(action)) {
			System.out.println("BoardController>add");
			
			//세션의 no값 가져오기
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			int no = authUser.getNo();
			
			//파라미터 꺼내기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			//vo만들기
			BoardVo boardVo = new BoardVo();
			boardVo.setUserNo(no);
			boardVo.setTitle(title);
			boardVo.setContent(content);
			
			//Dao를 이용해서 저장하기
			BoardDao dao = new BoardDao();
			dao.insert(boardVo);
			System.out.println(boardVo);
			
			
	
			//다시 bdc로 돌아감
			WebUtil.redirect(request, response, "/mysite2/bdc?action=list");
			
		}
		else if("read".equals(action)) {
			System.out.println("boardController->read");
			
			WebUtil.forword(request, response, "/WEB-INF/views/board/read.jsp");
		}
	}

	//post형식
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
