package com.shou.crabscore.controller;

import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 *
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "测试接口")
public class TestController {

    @PostMapping("/test")
    @ApiOperation("测试自己写的JWT校验业务")
    @ApiResponses({@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 500, message = "beyond")})
    public Result<Object> test(@RequestHeader String jwt) {
        log.info("TEST CONTROLLER");
        return new ResultUtil<>().setSuccessMsg("success");
    }
}
