package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestbookDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestbookVo;


@WebServlet("/gbc")
public class GuestbookController extends HttpServlet {
	//필드
	private static final long serialVersionUID = 1L;
       
    //생성자
	
    //메소드 gs
	
	//메소드 일반
	//get형식으로 받을때 사용메소드
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//포스트 방식일 때 한글깨짐 방지
		request.setCharacterEncoding("UTF-8");
		
		//action 파라미터일 때 꺼내기
		String action = request.getParameter("action");
		
		if("add".equals(action)) { //action이 add일때
		
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String content = request.getParameter("content");
			
			GuestbookDao dao = new GuestbookDao();
			GuestbookVo vo = new GuestbookVo(name, password, content);
			dao.insert(vo);
			
			//다시 gbc로 돌아감
			WebUtil.redirect(request, response, "/mysite2/gbc?action=addList");			
		}
		
		else if("deleteform".equals(action)) { //action이 deleteform일때 
			//WEB-INF파일안에 바로 들어갈수 없기 때문에 forword를 통해 접근함.
			System.out.println("GusetBookController>deleteform");
			WebUtil.forword(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");
		}
		
		else if("delete".equals(action)) {
			System.out.println("GusetBookController>delete");
			int no = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("password");
			
			GuestbookVo vo = new GuestbookVo();
			vo.setNo(no);
			vo.setPassword(password);
			
			GuestbookDao dao = new GuestbookDao();
			dao.delete(vo);
			
			WebUtil.redirect(request, response, "/mysite2/gbc?action=addList");		
		}
		else if("addList".equals(action)) {
			System.out.println("GusetBookController>addList");
			GuestbookDao dao = new GuestbookDao();
			List<GuestbookVo> gList = dao.getList();
			
			//리스트
			request.setAttribute("guestList", gList);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/guestbook/addList.jsp");
			rd.forward(request, response);
		}

	}

	//post형식으로 받을때 사용메소드
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}