package com.shou.crabscore.controller;

import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.common.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试接口
 *
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "测试接口")
@RequestMapping("/api/test")
public class TestController {
    @PostMapping(value = "/1")
    @ApiOperation(value = "第一个测试接口")
    public Result<Object> test() {
        return new ResultUtil<>().setSuccessMsg("测试测试测试");
    }

}
