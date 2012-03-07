package com.pratech.accesscontroldb.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionTimeOutControlFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;		
		HttpSession httpSession = httpRequest.getSession();
		
		String requestURI = httpRequest.getRequestURI();
		//System.out.println("URI: "+requestURI);
		
		if(requestURI.equals("/AccessControlDB-war/")) {
			httpSession.setAttribute("sessionId", httpSession.getId());
			filterChain.doFilter(request, response);
		} else {
			//System.out.println("SessionId: "+ httpSession.getAttribute("sessionId"));
			if(httpSession.getAttribute("sessionId") == null) {
				httpSession.invalidate();
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.sendError(512, "SessionTimeout");
			} else {
				filterChain.doFilter(request, response);
			}
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
