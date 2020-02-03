package top.spencercjh.crabscore.refactory.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import top.spencercjh.crabscore.refactory.model.dto.JwtAuthenticationToken;
import top.spencercjh.crabscore.refactory.service.AuthService;

import javax.servlet.http.HttpServletRequest;

import static top.spencercjh.crabscore.refactory.config.security.AuthUtils.generateToken;


/**
 * @author Spencer
 * @date 2020/2/3
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public JwtAuthenticationToken login(String username, String password, HttpServletRequest request) {
        final JwtAuthenticationToken input = new JwtAuthenticationToken(username, password);
        // 执行登录认证过程
        final Authentication authentication = authenticationManager.authenticate(input);
        // 认证成功存储认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌并返回给客户端
        final JwtAuthenticationToken result = new JwtAuthenticationToken(username, password,
                authentication.getAuthorities(), generateToken(authentication));
        if (request != null) {
            result.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        }
        return result;
    }
}
