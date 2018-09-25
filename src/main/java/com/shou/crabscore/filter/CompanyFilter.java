package com.shou.crabscore.filter;

import com.shou.crabscore.common.constant.CommonConstant;
import com.shou.crabscore.service.SecurityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author spencercjh
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@Log4j2
@WebFilter(urlPatterns = "/api/company/*", filterName = "参选单位用户组")
public class CompanyFilter implements Filter {
    private final SecurityService securityService;

    @Autowired
    public CompanyFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        log.info("进入company filter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //设置允许跨域的配置
        // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 允许的访问方法
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
        // Access-Control-Max-Age 用于 CORS 相关配置的缓存
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "token,Origin, X-Requested-With, Content-Type, Accept");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        String jwt = request.getHeader("jwt");
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        if (!this.securityService.verify(jwt, CommonConstant.USER_TYPE_COMPANY, request).getCode().equals(CommonConstant.SUCCESS)) {
            try {
                response.sendRedirect(basePath + "/403.html");
            } catch (IOException e) {
                e.printStackTrace();
                log.error("重定向跳转失败");
            }
        } else {
            try {
                filterChain.doFilter(request, response);
            } catch (IOException | ServletException e) {
                e.printStackTrace();
                log.error("doFilter失败");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
