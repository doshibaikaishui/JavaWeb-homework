~~~

13-Oct-2024 18:18:35.611 信息 [http-nio-8080-exec-1] org.example.ServletRequestListener.requestInitialized 请求初始化: 时间: 2024-10-13T18:18:35.610054300, 客户端IP: 127.0.0.1, 请求方法: GET, 请求URI: /listener/, 用户代理: IntelliJ IDEA/241.14494.240
13-Oct-2024 18:18:36.196 信息 [http-nio-8080-exec-1] org.example.ServletRequestListener.requestDestroyed 请求结束: 时间: 2024-10-13T18:18:36.196003300, 处理时间: 585 毫秒
13-Oct-2024 18:18:36.263 信息 [http-nio-8080-exec-3] org.example.ServletRequestListener.requestInitialized 请求初始化: 时间: 2024-10-13T18:18:36.263085100, 客户端IP: 0:0:0:0:0:0:0:1, 请求方法: GET, 请求URI: /listener/, 用户代理: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36 Edg/129.0.0.0
13-Oct-2024 18:18:36.264 信息 [http-nio-8080-exec-3] org.example.ServletRequestListener.requestDestroyed 请求结束: 时间: 2024-10-13T18:18:36.264083500, 处理时间: 0 毫秒
13-Oct-2024 18:18:38.108 信息 [http-nio-8080-exec-4] org.example.ServletRequestListener.requestInitialized 请求初始化: 时间: 2024-10-13T18:18:38.108036700, 客户端IP: 0:0:0:0:0:0:0:1, 请求方法: GET, 请求URI: /listener/hello-servlet, 用户代理: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36 Edg/129.0.0.0
13-Oct-2024 18:18:38.109 信息 [http-nio-8080-exec-4] org.example.ServletRequestListener.requestDestroyed 请求结束: 时间: 2024-10-13T18:18:38.109039500, 处理时间: 1 毫秒
~~~

## HTTP监听器

本项目实现基于 **Servlet Listener** 的需求日志监听器，监听所有的HTTP请求并返回相应信息。

---

#### **1. 请求流程**

1. **请求初始化**：

   - 当新的 HTTP 请求到达时，`requestInitialized` 方法会被调用。

   - 在这个方法中，记录请求的关键信息，如时间戳、客户端 IP、请求方法、请求 URI、查询字符串和用户代理。

     ~~~java
     HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();  
     
             String clientIP = request.getRemoteAddr();
             String userAgent = request.getHeader("User-Agent");
             String method = request.getMethod();
             String uri = request.getRequestURI();
             String queryString = request.getQueryString();
             LocalDateTime startTime = LocalDateTime.now();
     
             // 保存开始时间在 requestDestroyed 中使用
             request.setAttribute("startTime", startTime);
     
             LOGGER.info("请求初始化: 时间: " + startTime +
                     ", 客户端IP: " + clientIP +
                     ", 请求方法: " + method +
                     ", 请求URI: " + uri +
                     (queryString != null ? ", 查询字符串: " + queryString : "") +
                     ", 用户代理: " + userAgent);
     ~~~

     

2. **请求结束**：

   - 当请求处理完毕后，`requestDestroyed` 方法会被调用。

   - 该方法记录请求处理的结束时间，并计算请求的处理时间（以毫秒为单位）。

     ~~~java
     HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
             LocalDateTime endTime = LocalDateTime.now();
             LocalDateTime startTime = (LocalDateTime) request.getAttribute("startTime");
             long processTime = java.time.Duration.between(startTime, endTime).toMillis();
     
             LOGGER.info("请求结束: 时间: " + endTime +
                     ", 处理时间: " + processTime + " 毫秒");
     ~~~

     

#### **2. 记录的信息**

- **时间**：请求的初始化和结束时间。
- **客户端 IP**：请求的来源 IP 地址。
- **请求方法**：如 GET、POST 等 HTTP 方法。
- **请求 URI**：请求的统一资源标识符。
- **查询字符串**：如果请求包含查询参数，记录查询字符串。
- **用户代理**：请求发起时客户端的用户代理信息。

---

### **项目结构**

- **监听器类**：`ServletRequestListener` 处理请求的日志记录逻辑。
- **测试类**：`HelloServlet`简单http请求，提供日志输出
- **测试页面**：`index.jsp`展示初始页面，并提供测试的超链接

