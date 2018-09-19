package com.shou.crabscore.controller;

import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.dao.UserMapper;
import com.shou.crabscore.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final UserService userService;

    @Autowired
    public TestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/1")
    @ApiOperation(value = "第一个测试接口")
    public Result<Object> test1() {
        return new ResultUtil<>().setSuccessMsg("测试测试测试");
    }

    @GetMapping(value = "/2")
    @ApiOperation(value = "第二个测试接口")
    public Result<Object> test2() {
        return new ResultUtil<>().setSuccessMsg("测试Spring Security");
    }

    @GetMapping(value = "/3")
    @ApiOperation(value = "第三个测试接口")
    public Result<Object> test3() {
        return new ResultUtil<>().setData(this.userService.selectAllUser(), "测试数据库");
    }
}
