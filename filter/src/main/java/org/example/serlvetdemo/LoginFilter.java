package org.example.serlvetdemo;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")  // 将过滤器应用于所有 URL
public class LoginFilter implements Filter {

    // 不需要登录即可访问的路径排除列表
    private static final List<String> EXCLUDED_URLS = Arrays.asList("/login", "/register", "/public");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 获取当前请求路径
        String requestURI = httpRequest.getRequestURI();

        // 检查当前请求路径是否在排除列表中
        if (isExcludedPath(requestURI)) {
            // 如果是排除路径，直接放行请求
            chain.doFilter(request, response);
            return;
        }


        // 获取当前session
        HttpSession session = httpRequest.getSession(false);

        // 检查用户是否已登录（用户登录后在会话中设z置 "user" 属性）
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        // 如果用户已登录，继续处理请求
        if (isLoggedIn) {
            chain.doFilter(request, response);
        } else {
            // 如果用户未登录，重定向到登录页面
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.html");
        }
    }

    @Override
    public void destroy() {}


    private boolean isExcludedPath(String requestURI) {
        // 遍历排除列表，检查当前请求路径是否匹配
        for (String excluded : EXCLUDED_URLS) {
            if (requestURI.contains(excluded)) {
                return true;
            }
        }
        return false;
    }
}
