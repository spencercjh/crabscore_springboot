package com.shou.crabscore.controller.common;

import com.shou.crabscore.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户相关接口
 *
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "用户相关接口")
@RequestMapping("/api/common")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


}
