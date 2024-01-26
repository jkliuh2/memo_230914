package com.memo.interceptor;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j // log 찍기위한 용도. 필수는 아니긴 함
@Component // 설정은 아닌 spring bean
public class PermissionInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler) throws IOException {
		// 컨트롤러 입장 전 검사?
		
		// 요청 URL Path를 꺼낸다.
		String uri = request.getRequestURI();
		
		// 로그인 여부를 세션에서 뽑아오기
		HttpSession session = request.getSession();
		Integer userId = (Integer)session.getAttribute("userId"); // NULL 가능(로그인 안됬을 때)
		
		// 비로그인 && /post => 1. 로그인 페이지로 이동, 2. 컨트롤러 수행 방지 (URL 설계상 이렇게 되야함)
		if (userId == null && uri.startsWith("/post")) {
			response.sendRedirect("/user/sign-in-view"); // 1. 로그인 페이지로 리다이렉트
			return false; // 2. 기존 요청에 대한 컨트롤러 수행 방지
		}
		
		// 로그인 && /user => 1. 글목록 페이지로 이동, 2. 컨트롤러 수행 방지
		if (userId != null && uri.startsWith("/user")) {
			response.sendRedirect("/post/post-list-view");
			return false;
		}
		
		log.info("[@@@@@@@ preHandle] uri:{}", uri);
		return true; // 컨트롤러 수행 허락 여부
	}
	
	@Override
	public void postHandle(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler,
			ModelAndView mav) {
		// 후처리
		
		// mav: view객체가 있다는 건 아직 jsp가 html로 변환되기 전
		log.info("[####### postHandle]");
	}
	
	@Override
	public void afterCompletion(
			HttpServletRequest request,
			HttpServletResponse response,
			Object handler,
			Exception ex) {
		// 해석이 다 끝난 상태.
		
		// jsp가 html로 최종 변환된 후
		log.info("[$$$$$$$$$$ afterCompletion]");
	}
}
