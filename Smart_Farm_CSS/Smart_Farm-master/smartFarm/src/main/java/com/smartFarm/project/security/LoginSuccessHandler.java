package com.smartFarm.project.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@RequiredArgsConstructor
@Log4j2
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private RequestCache requestCache = new HttpSessionRequestCache();

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// 쿠팡 둘러보기 하다가 로그인 성공 시 다시 거기로 가는 경우
		setDefaultTargetUrl("/home");
//		SavedRequest savedRequest = requestCache.getRequest(request, response);
//		if (savedRequest != null) {
//			// 인증 받기 전 url로 이동하기
//			String targetUrl = savedRequest.getRedirectUrl();
//			log.info(targetUrl);
//			requestCache.removeRequest(request, response);// 세션에 저장된 객체를 다 사용한 뒤에는 지워줘서 메모리 누수 방지
//			
//			redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
//		} else {
//			// 기본 url로 가도록 함함
//			redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
//		}
		
		redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
	}
}