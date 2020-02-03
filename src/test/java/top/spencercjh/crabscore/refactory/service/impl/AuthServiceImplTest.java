package top.spencercjh.crabscore.refactory.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import top.spencercjh.crabscore.refactory.model.dto.JwtAuthenticationToken;
import top.spencercjh.crabscore.refactory.service.AuthService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Spencer
 * @date 2020/2/4
 */
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class AuthServiceImplTest {
    @Autowired
    private AuthService authService;

    @Test
    void login() {
        final String correctPassword = "123456";
        final String correctUsername = "update";
        final JwtAuthenticationToken token = authService.login(correctUsername, correctPassword,
                new MockHttpServletRequest(HttpMethod.POST.name(), "127.0.0.1"));
        assertNotNull(token);
        log.debug(token.toString());
        assertThrows(BadCredentialsException.class, () -> {
            final String wrongPassword = "12345";
            authService.login(correctUsername, wrongPassword,
                    new MockHttpServletRequest(HttpMethod.POST.name(), "127.0.0.1"));
        });
        assertThrows(BadCredentialsException.class, () -> {
            final String wrongUsername = "123456";
            authService.login(wrongUsername, correctPassword,
                    new MockHttpServletRequest(HttpMethod.POST.name(), "127.0.0.1"));
        });
    }
}