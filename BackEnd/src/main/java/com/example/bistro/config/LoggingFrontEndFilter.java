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
    
		if("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {//處理跨域請求，接收到OPTIONS
			chain.doFilter(request, response);
			return;
		}
		

		HttpSession session = httpRequest.getSession(false);
		if (session != null && session.getAttribute("membersId") != null) {
			
			String requestUri = httpRequest.getRequestURI();
	        String[] uriParts = requestUri.split("/");
	        if (uriParts.length > 2 && "members".equals(uriParts[uriParts.length - 2])) {
	        	System.out.println("只針對members");
				try {
					// 假設 uriParts[uriParts.length - 1] 是 memberId
					String requestedMemberId = uriParts[uriParts.length - 1];
					Object currentMemberId = session.getAttribute("membersId");
					System.out.println("SESSION保存的ID"+currentMemberId.toString());
					System.out.println("網址的ID"+requestedMemberId);
					// 如果當前會員ID與請求中的會員ID不一致，則返回403
					if (requestedMemberId != null && !requestedMemberId.equals(currentMemberId.toString())) {
						System.out.println("會員ID不匹配，無權訪問");
						httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource.");
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					// 若解析出錯，可以返回 400 或 403
					httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request");
					return;
				}
			}
			
			long maxInactiveInterval = session.getMaxInactiveInterval()* 1000;
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
			        httpResponse.sendRedirect("http://localhost:5173/index");
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
