package top.spencercjh.crabscore.refactory.config.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import top.spencercjh.crabscore.refactory.service.AuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录认证检查过滤器
 *
 * @author Louis
 * @date Jun 29, 2019
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @NotNull FilterChain chain) throws IOException, ServletException {
        // 获取token, 并检查登录状态
        AuthService.checkAuthentication(request);
        chain.doFilter(request, response);
    }
}