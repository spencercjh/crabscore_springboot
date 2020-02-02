package top.spencercjh.crabscore.refactory.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import top.spencercjh.crabscore.refactory.config.security.AuthUtils;
import top.spencercjh.crabscore.refactory.model.dto.JwtAuthenticationToken;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Spencer
 * @date 2020/2/3
 */
public interface AuthService {
    static void checkAuthentication(HttpServletRequest request) {
        // 获取令牌并根据令牌获取登录认证信息
        Authentication authentication = AuthUtils.getAuthenticationFromToken(request);
        // 设置登录认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    JwtAuthenticationToken login(String username, String password, HttpServletRequest request);
}
