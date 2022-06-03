package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;


@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//action을 꺼낸다
		String action = request.getParameter("action");		
		
		if("joinForm".equals(action)) { //회원가입폼
			System.out.println("UserController>joinForm");
			WebUtil.forward(request, response, "WEB-INF/views/user/joinForm.jsp");
		}
		else if("join".equals(action)) {
			System.out.println("UserController>join"); //회원가입
			//파라미터 꺼내기
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			System.out.println(id);
			System.out.println(password);
			System.out.println(name);
			System.out.println(gender);
			
			//Vo에 데이터 만들기
			UserVo userVo = new UserVo(id, name, password, gender);
			//Dao를 이용해서 저장
			UserDao userDao = new UserDao();
			userDao.insert(userVo);
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinOk.jsp");
		}
		
		else if("loginForm".equals(action)) { //로그인 폼
			System.out.println("UserController>loginForm");
			
			WebUtil.forward(request, response, "/WEB-INF/views/user/loginForm.jsp");
		}
		
		else if("login".equals(action)) { //로그인
			System.out.println("UserController>login");
			
			//파라미터 꺼내기
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			System.out.println(id);
			System.out.println(password);
			
			//Vo만들기
			UserVo userVo = new UserVo(); //생성자를 계속 만들기 힘드니깐 set을 이용해 만듬
			userVo.setId(id);
			userVo.setPassword(password);
			
			//dao
			UserDao userDao = new UserDao();
			UserVo authUser = userDao.getUser(userVo); //id,password --> User 전체가져옴
			
			//authUser 주소값이 있으면 --> 로그인 성공
			//authUser 주소값이 없으면(null) --> 로그인 실패
			if(authUser == null) {
				System.out.println("로그인 실패");
			}
			else {
				System.out.println("로그인 성공");
				
				HttpSession session = request.getSession();
				session.setAttribute("authUser",authUser);
				
				//메인 리다이렉트
				WebUtil.redirect(request, response, "/mysite2/main");
				
			}
		}
		else if("logout".equals(action)) { //로그아웃
			System.out.println("UserController>logout");
			
			//세션값을 지운다
			HttpSession session = request.getSession();
			session.removeAttribute("authUser"); //세션이 authUser인것을 지움
			session.invalidate(); //세션값 완전히 비움
			
			//메인 리다이렉트
			WebUtil.redirect(request, response, "/mysite2/main");
		}
		else if("modifyForm".equals(action)) { //회원정보 수정폼
			System.out.println("UserController>modifyForm");
			WebUtil.forward(request, response, "/WEB-INF/views/user/modifyForm.jsp");
			
		}
		else if("modify".equals(action)) { //회원정보 수정
			System.out.println("UserController>modify");
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			
			authUser.getId(); //현재의 세션값 Id
			
			//파라미터 꺼내기
			
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			
			//Vo만들기
			
			UserVo userVo = new UserVo(); //생성자를 계속 만들기 힘드니깐 set을 이용해 만듬
			userVo.setId(authUser.getId());
			userVo.setPassword(password);
			userVo.setName(name);
			userVo.setGender(gender);
			
			
				UserDao userdao = new UserDao();
				userdao.update(userVo);
			
			
			
			
			//메인 리다이렉트
			WebUtil.redirect(request, response, "/mysite2/main");
		}
			
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
