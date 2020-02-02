package top.spencercjh.crabscore.refactory.model.dto;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 自定义令牌对象
 *
 * @author Louis
 * @date Jun 29, 2019
 */
@Getter
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private static final long serialVersionUID = 6007198742394021851L;

    private String token;

    /**
     * 没有登录成功时创建这个对象
     *
     * @param principal   the principal;
     * @param credentials the credentials;
     */
    public JwtAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    /**
     * 登录校验通过后创建这个对象
     *
     * @param principal   the principal;
     * @param credentials the credentials;
     * @param authorities the authorities;
     * @param token       the token;
     */
    public JwtAuthenticationToken(@NotNull Object principal, @Nullable Object credentials, @NotNull Collection<? extends GrantedAuthority> authorities,
                                  @NotNull String token) {
        super(principal, credentials, authorities);
        this.token = token;
    }
}
