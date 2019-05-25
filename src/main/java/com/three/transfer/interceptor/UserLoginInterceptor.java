package com.three.transfer.interceptor;

import com.three.transfer.common.Const;
import com.three.transfer.entity.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class UserLoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从session中取出用户数据来
        Object userObj = request.getSession().getAttribute(Const.CURRENT_USER);
        if (userObj != null) {
            User user = (User) userObj;
            if (user.getUserId() > 0 ) {
                return true;
            }
        }
        //若不满足登录验证，则直接跳转到账号登录页面
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("window.location.href=" + "'" + request.getContextPath() + "/transfer/login'");
        out.println("</script>");
        out.println("</html");
        return false;
    }





}
