package com.shou.crabscore.controller;

import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.service.SecurityService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 测试接口
 *
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "测试接口")
public class TestController {
    private final SecurityService securityService;

    @Autowired
    public TestController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/test")
    @ApiOperation("测试自己写的JWT校验业务")
    @ApiResponses({@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 500, message = "beyond")})
    public Result<Object> test(@ApiParam(name = "jwt", value = "JWT串", type = "String") @RequestParam String jwt,
                               HttpServletRequest request) {
        if (this.securityService.verify(jwt, request)) {
            return new ResultUtil<>().setSuccessMsg("success");
        } else {
            return new ResultUtil<>().setErrorMsg("beyond");
        }
    }
}
