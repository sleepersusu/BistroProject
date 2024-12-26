package com.example.bistro.config;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoggingFrontEndFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("進到FrontEnd LoginFilter");
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		   // 處理 OPTIONS 預檢請求
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
            httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

		HttpSession session = httpRequest.getSession(false);
		System.out.println("前端FILTER擋住");
		if (session != null && session.getAttribute("membersId") != null) {
			System.out.println("判斷session沒銷毀");
			long maxInactiveInterval = session.getMaxInactiveInterval()* 1000; // 转换为毫秒
			session.setAttribute("maxInactiveInterval", maxInactiveInterval);
			long currentTime = System.currentTimeMillis();
			Long lastAccessTime = (Long) session.getAttribute("lastAccessTime");

			if (lastAccessTime != null) {

				long inactiveTime = currentTime - lastAccessTime;
				long remainingTime = maxInactiveInterval - inactiveTime;

				// 如果 session 超时，销毁 session
			    if (remainingTime <= 0) {
			        session.invalidate();
			        System.out.println("超時session銷毀");
			        httpResponse.sendRedirect("http://localhost:5173/index"); // 重定向到登入頁面
			        return;
			    }
			}
			// 如果 session 存在，更新 lastAccessTime
			session.setAttribute("lastAccessTime", currentTime);
			chain.doFilter(request, response);
		}else {
            // Session 为 null，重定向到登录页面
            System.out.println("Session已被销毁！");
            httpResponse.sendRedirect("http://localhost:5173/index");  // 重定向到登录页面
            return;
        }
	}

}
