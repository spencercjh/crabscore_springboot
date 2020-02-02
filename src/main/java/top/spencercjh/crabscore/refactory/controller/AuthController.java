package top.spencercjh.crabscore.refactory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.spencercjh.crabscore.refactory.model.dto.JwtAuthenticationToken;
import top.spencercjh.crabscore.refactory.model.dto.LoginUser;
import top.spencercjh.crabscore.refactory.model.vo.Result;
import top.spencercjh.crabscore.refactory.service.AuthService;
import top.spencercjh.crabscore.refactory.util.ResponseEntityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Spencer
 * @date 2020 2/2
 */
@Validated
@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Result<JwtAuthenticationToken>> login(@org.jetbrains.annotations.NotNull
                                                                @RequestBody @Valid @NotNull LoginUser loginUser,
                                                                HttpServletRequest request) {
        final String username = loginUser.getUsername();
        final String password = loginUser.getPassword();
        return ResponseEntityUtil.success(authService.login(username, password, request));
    }
}