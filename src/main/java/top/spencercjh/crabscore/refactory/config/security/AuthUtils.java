package top.spencercjh.crabscore.refactory.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import top.spencercjh.crabscore.refactory.model.dto.JwtAuthenticationToken;
import top.spencercjh.crabscore.refactory.model.dto.RoleAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Louis
 * @date Jun 29, 2019
 */
public final class AuthUtils {
    private static final String USERNAME = Claims.SUBJECT;
    private static final String CREATED = "created";
    private static final String AUTHORITIES = "authorities";
    private static final String SECRET = "abcdefgh";
    private static final long EXPIRE_TIME = 12 * 60 * 60 * 1000;

    private AuthUtils() {
    }

    @NotNull
    public static String generateToken(@NotNull Authentication authentication) {
        Map<String, Object> claims = new HashMap<>(8);
        claims.put(USERNAME, getUsername(authentication));
        claims.put(CREATED, new Date());
        claims.put(AUTHORITIES, authentication.getAuthorities());
        return actualGenerateToken(claims);
    }

    @Nullable
    private static String getUsername() {
        String username = null;
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            }
        }
        return username;
    }

    @Nullable
    private static String getUsername(Authentication authentication) {
        String username = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            }
        }
        return username;
    }

    @Nullable
    public static Authentication getAuthentication() {
        if (SecurityContextHolder.getContext() == null) {
            return null;
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @NotNull
    private static String actualGenerateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    @SuppressWarnings("rawtypes")
    @Nullable
    public static Authentication getAuthenticationFromToken(@NotNull HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            return null;
        }
        // 请求令牌不能为空
        if (getAuthentication() == null) {
            // 上下文中Authentication为空
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return null;
            }
            String username = claims.getSubject();
            if (username == null) {
                return null;
            }
            if (isTokenExpired(token)) {
                return null;
            }
            Object authors = claims.get(AUTHORITIES);
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (authors instanceof List) {
                for (Object object : (List) authors) {
                    authorities.add(new RoleAuthority((String) ((Map) object).get("authority")));
                }
            }
            return new JwtAuthenticationToken(username, null, authorities, token);
        }
        if (validateToken(token, getUsername())) {
            // 如果上下文中Authentication非空，且请求令牌合法，直接返回当前登录认证信息
            return getAuthentication();
        }
        return null;
    }

    private static Claims getClaimsFromToken(@NotNull String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    private static boolean validateToken(String token, String username) {
        String userName = getClaimsFromToken(token).getSubject();
        return (userName.equals(username) && !isTokenExpired(token));
    }

    @NotNull
    public static String refreshToken(@NotNull String token) {
        Claims claims = getClaimsFromToken(token);
        claims.put(CREATED, new Date());
        return actualGenerateToken(claims);
    }

    private static boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
}