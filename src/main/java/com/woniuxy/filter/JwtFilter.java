package com.woniuxy.filter;

import com.woniuxy.component.JwtToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtFilter extends BasicHttpAuthenticationFilter {


    //如果带有jwt的token，则对token进行检查，否则直接通过(游客模式)
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        //如果请求头中带了token，进入if中验证token
        if (isLoginAttempt(request,response)) {
            try {
               return   executeLogin(request,response);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        //如果没带，意味着游客模式，匿名访问，直接放行，由shiro的鉴权操作去处理
        return true;
    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {

        //转换成http的请求
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        //获取请求中的header
        String token = httpServletRequest.getHeader("Token");
        //当不为空则返回true
        return token!=null;
    }


    //执行登录操作
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {

        //转换成http的请求
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        //获取请求中的header
        String token = httpServletRequest.getHeader("Token");
        System.out.println(token);
        //获取一个自定义jwtToken
        JwtToken jwtToken = new JwtToken(token);

        //获取项目对象，进行登录
        Subject subject = getSubject(request, response);
        subject.login(jwtToken);

        return true;
    }


    //对复杂请求进行操作
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        //转换成http的请求和响应
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);

        //解决跨域
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));


        //如果请求方式是options，代表的是预检请求，直接放行
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            //设置响应状态码
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;

        }
        return super.preHandle(request, response);
    }
}
