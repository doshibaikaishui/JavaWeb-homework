package org.example;


import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@WebListener
public class ServletRequestListener implements jakarta.servlet.ServletRequestListener {

    private static final Logger LOGGER = Logger.getLogger(ServletRequestListener.class.getName());

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();

        String clientIP = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        LocalDateTime startTime = LocalDateTime.now();

        // 保存开始时间到请求属性中，以便在 requestDestroyed 中使用
        request.setAttribute("startTime", startTime);

        LOGGER.info("请求初始化: 时间: " + startTime +
                ", 客户端IP: " + clientIP +
                ", 请求方法: " + method +
                ", 请求URI: " + uri +
                (queryString != null ? ", 查询字符串: " + queryString : "") +
                ", 用户代理: " + userAgent);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = (LocalDateTime) request.getAttribute("startTime");
        long processTime = java.time.Duration.between(startTime, endTime).toMillis();

        LOGGER.info("请求结束: 时间: " + endTime +
                ", 处理时间: " + processTime + " 毫秒");
    }
}
