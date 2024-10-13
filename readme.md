## 登录验证过滤器

本项目实现基于 **Servlet Filter** 的登录验证机制，确保只有已登录的用户才能访问受保护资源。未登录用户的请求会被自动重定向到登录页面。

---

#### **1. 请求流程**
1. **所有请求**都会被过滤器拦截。`@WebFilter("/*")`

2. 过滤器会**检查当前请求的路径**是否属于不需要登录的资源（如登录页面、注册页面、公共资源等）。

   ~~~java
   private static final List<String> EXCLUDED_URLS = Arrays.asList("/login", "/register", "/public");
   
   // 获取当前请求路径
           String requestURI = httpRequest.getRequestURI();
   
           // 检查当前请求路径是否在排除列表中
           if (isExcludedPath(requestURI)) {
               // 如果是排除路径，直接放行请求
               chain.doFilter(request, response);
               return;
           }
   
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
   ~~~

   

3. 如果请求来自**未登录的用户**并且不在排除路径列表中，则会**重定向到登录页面**。

4. 如果用户已登录或请求属于**排除路径**，则继续处理请求，进入目标页面。

~~~java
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
~~~

#### **2. 排除路径**
过滤器实现了一份**排除路径列表**，其中包括：
- 登录页面（`/login.html`）
- 注册页面（`/register.html`）(页面未实现，跳转到404)
- 其他公共资源路径 (页面未实现，跳转到404)

只有这些路径可以在未登录状态下访问，其他路径都需要用户登录。

#### **3. 用户登录状态的判断**
过滤器通过检查**Session 中的用户属性**来判断用户是否已登录。如果会话中存在某个属性（如 `"user"`），则认为用户已登录，否则需要重定向到登录页面。

---

### **附加功能**
- **Session 验证**：使用 `HttpSession` 判断会话中的登录状态，避免每次请求都重新认证。
- **自动重定向**：如果未登录用户访问受保护资源，系统会自动将其重定向到登录页面。
- **登录与登出**：另外实现了登录页面与密码错误页面，以及登录成功后的欢迎页面和推出登录功能。

---

### **项目结构**
- **过滤器类**：`LoginFilter` 处理所有请求的过滤逻辑。
- **登录页面**：`login.html` 提供登录表单。
- **欢迎页面**：`welcome.html` 展示用户登录后的欢迎信息。
- **登录逻辑**：`LoginServlet`实现登录功能。
- **退出登录**：`LogoutServlet`实现退出功能。
- **登录错误页面**：`loginError`展示登录错误页面



