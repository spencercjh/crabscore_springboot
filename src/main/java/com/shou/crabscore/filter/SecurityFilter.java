package com.shou.crabscore.filter;

import com.shou.crabscore.common.constant.CommonConstant;
import com.shou.crabscore.service.SecurityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * reference:https://blog.csdn.net/zjcjava/article/details/78237164
 *
 * @author spencercjh
 */
@Log4j2
@Component
public class SecurityFilter implements Filter {

    private final SecurityService securityService;

    @Autowired
    public SecurityFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        log.info("进入Security Filter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "token,Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader("content-type", "application/json");
        response.setCharacterEncoding("UTF-8");
        String jwt = request.getHeader("jwt");
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/crabscore";
        Integer requestApiRoleId = CommonConstant.USER_TYPE_NORMAL;
        if (request.getRequestURI().contains(CommonConstant.ADMIN)) {
            requestApiRoleId = CommonConstant.USER_TYPE_ADMIN;
        } else if (request.getRequestURI().contains(CommonConstant.JUDGE)) {
            requestApiRoleId = CommonConstant.USER_TYPE_JUDGE;
        } else if (request.getRequestURI().contains(CommonConstant.STAFF)) {
            requestApiRoleId = CommonConstant.USER_TYPE_STAFF;
        } else if (request.getRequestURI().contains(CommonConstant.COMPANY)) {
            requestApiRoleId = CommonConstant.USER_TYPE_COMPANY;
        } else if (request.getRequestURI().contains(CommonConstant.PERSON_CENTER)) {
            requestApiRoleId = CommonConstant.USER_TYPE_COMMON;
        }
        if (requestApiRoleId.equals(CommonConstant.USER_TYPE_NORMAL)) {
            try {
                filterChain.doFilter(request, response);
            } catch (IOException | ServletException e) {
                e.printStackTrace();
                log.error("doFilter失败");
            }
        } else if (this.securityService.verify(jwt, requestApiRoleId, request).getCode().equals(CommonConstant.SUCCESS)) {
            try {
                filterChain.doFilter(request, response);
            } catch (IOException | ServletException e) {
                e.printStackTrace();
                log.error("doFilter失败");
            }
        } else {
            try {
                response.sendRedirect(basePath + "/403.html");
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
                log.error("重定向跳转失败");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
