package com.shou.crabscore.controller.common;

import com.alibaba.fastjson.JSON;
import com.shou.crabscore.common.constant.CommonConstant;
import com.shou.crabscore.common.util.JwtUtil;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.User;
import com.shou.crabscore.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户相关接口
 *
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "登录相关接口")
@RequestMapping("/api/common")
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    @ApiOperation(value = "用户登录", notes = "参数检查交给Android端完成")
    @ApiResponses({@ApiResponse(code = 200, message = "登录成功"),
            @ApiResponse(code = 501, message = "用户组参数错误"),
            @ApiResponse(code = 502, message = "用户名不存在"),
            @ApiResponse(code = 503, message = "用户组选择错误"),
            @ApiResponse(code = 504, message = "密码错误")})
    public Result<Object> login(@ApiParam(name = "username", value = "用户名", type = "String") @RequestParam String username,
                                @ApiParam(name = "password", value = "密码", type = "String") @RequestParam String password,
                                @ApiParam(name = "roleId", value = "用户组", type = "Integer") @RequestParam Integer roleId) {
        User searchResult = this.userService.selectByUserName(username);
        if (searchResult == null) {
            return new ResultUtil<>().setErrorMsg(502, "用户名不存在");
        } else if (!searchResult.getRoleId().equals(roleId)) {
            return new ResultUtil<>().setErrorMsg(503, "用户组选择错误");
        } else if (!searchResult.getPassword().equals(password)) {
            return new ResultUtil<>().setErrorMsg(504, "密码错误");
        } else if (searchResult.getUserName().equals(username) &&
                searchResult.getPassword().equals(password) &&
                searchResult.getRoleId().equals(roleId)) {
            Map<String, Object> subject = new HashMap<>(2);
            subject.put("username", username);
            subject.put("roleId", roleId);
            String jwt = JwtUtil.createJWT(String.valueOf(subject.hashCode()), JSON.toJSONString(subject));
            return new ResultUtil<>().setData(jwt, "登录成功");
        } else {
            return new ResultUtil<>().setErrorMsg(501, "用户组参数错误");
        }
    }

    @PostMapping("/creation")
    @ApiOperation(value = "用户注册", notes = "参数检查交给Android端完成")
    @ApiResponses({@ApiResponse(code = 200, message = "注册成功"),
            @ApiResponse(code = 500, message = "注册失败"),
            @ApiResponse(code = 501, message = "用户名已存在")})
    public Result<Object> register(@ApiParam(name = "username", value = "用户名", type = "String") @RequestParam String username,
                                   @ApiParam(name = "password", value = "密码", type = "String") @RequestParam String password,
                                   @ApiParam(name = "roleId", value = "用户组", type = "Integer") @RequestParam Integer roleId,
                                   @ApiParam(name = "email", value = "邮箱(其实是手机)", type = "String") @RequestParam String email,
                                   @ApiParam(name = "displayName", value = "显示名", type = "Stirng") @RequestParam String displayName) {
        User searchResult = this.userService.selectByUserName(username);
        if (searchResult == null) {
            User newUser = new User(username, password, displayName, roleId, CommonConstant.USER_STATUS_LOCK, email,
                    CommonConstant.USER_COMPETITION_ALL, new Date(System.currentTimeMillis()), username,
                    new Date(System.currentTimeMillis()), username);
            int insertResult = this.userService.insert(newUser);
            return insertResult == 0 ? new ResultUtil<>().setSuccessMsg("注册成功") : new ResultUtil<>().setErrorMsg("注册失败");
        } else {
            return new ResultUtil<>().setErrorMsg(501, "用户名已存在");
        }
    }
}