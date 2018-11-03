package com.shou.crabscore.controller.admin;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.shou.crabscore.common.constant.CommonConstant;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.User;
import com.shou.crabscore.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户后台管理接口
 *
 * @author spencercjh
 */
@SuppressWarnings("unused")
@Log4j2
@RestController
@Api(description = "管理员用户组-用户后台管理接口")
@RequestMapping("/api/admin/user")
public class UserAdminController {
    private final UserService userService;

    @Autowired
    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{userId}")
    @ApiOperation("查询单个用户")
    @ApiResponses({@ApiResponse(code = 200, message = "查询单个用户成功"),
            @ApiResponse(code = 500, message = "查询单个用户失败"),
            @ApiResponse(code = 501, message = "userId为空")})
    public Result<Object> singleUser(@ApiParam(name = "userId", value = "用户Id", type = "Integer")
                                     @PathVariable("userId") Integer userId,
                                     @RequestHeader("jwt") String jwt) {
        if (CharUtil.isBlankChar(userId)) {
            return new ResultUtil<>().setErrorMsg(501, "userId为空");
        } else {
            User user = this.userService.selectByPrimaryKey(userId);
            return StrUtil.isNotBlank(user.getUserName()) ?
                    (new ResultUtil<>().setData(user, "查询单个用户成功")) : (new ResultUtil<>().setErrorMsg("查询单个用户失败"));
        }
    }

    @GetMapping(value = "/users/{pageNum}/{pageSize}")
    @ApiOperation("查询所有用户，只返回状态为启用的用户")
    @ApiResponses({@ApiResponse(code = 200, message = "查询所有用户成功"),
            @ApiResponse(code = 201, message = "没有用户")})
    public Result<Object> allUser(@RequestHeader("jwt") String jwt,
                                  @ApiParam(name = "pageNum", value = "页数", type = "Integer")
                                  @PathVariable("pageNum") Integer pageNum,
                                  @ApiParam(name = "pageSize", value = "页面大小", type = "Integer")
                                  @PathVariable("pageSize") Integer pageSize) {
        List<User> userList = this.userService.selectAllUser(pageNum, pageSize);
        if (userList.size() == 0) {
            return new ResultUtil<>().setSuccessMsg(201, "没有用户");
        } else {
            return new ResultUtil<>().setData(userList, "查询所有用户成功");
        }
    }

    @GetMapping(value = "/users/{status}/{pageNum}/{pageSize}")
    @ApiOperation("查询所有符合某一状态的用户")
    @ApiResponses({@ApiResponse(code = 200, message = "查询所有用户成功"),
            @ApiResponse(code = 201, message = "没有找到可用/禁用用户")})
    public Result<Object> partUser(@ApiParam(name = "status", value = "用户状态 1：可用 0：禁用", type = "Integer")
                                   @PathVariable("status") Integer status,
                                   @RequestHeader("jwt") String jwt,
                                   @ApiParam(name = "pageNum", value = "页数", type = "Integer")
                                   @PathVariable("pageNum") Integer pageNum,
                                   @ApiParam(name = "pageSize", value = "页面大小", type = "Integer")
                                   @PathVariable("pageSize") Integer pageSize) {
        List<User> userList = this.userService.selectAllUserSelective(status, pageNum, pageSize);
        if (userList.size() == 0) {
            return new ResultUtil<>().setSuccessMsg(201, "没有找到" +
                    (status.equals(CommonConstant.USER_STATUS_NORMAL) ? "可用" : "禁用") + "用户");
        } else {
            return new ResultUtil<>().setData(userList, "查询所有用户成功");
        }
    }

    @PutMapping(value = "/property")
    @ApiOperation("修改用户资料")
    @ApiImplicitParam(name = "user", value = "单个用户信息", dataType = "User")
    @ApiResponses({@ApiResponse(code = 200, message = "修改用户资料成功"),
            @ApiResponse(code = 500, message = "修改用户资料失败"),
            @ApiResponse(code = 501, message = "UserId为空")})
    public Result<Object> updateUserProperty(@ApiParam("用户信息Json") @RequestBody User user,
                                             @RequestHeader("jwt") String jwt) {
        if (CharUtil.isBlankChar(user.getUserId())) {
            return new ResultUtil<>().setErrorMsg(501, "UserId为空");
        } else {
            int updateResult = this.userService.updateByPrimaryKeySelective(user);
            return (updateResult <= 0) ? new ResultUtil<>().setErrorMsg("修改用户资料失败") :
                    new ResultUtil<>().setSuccessMsg("修改用户资料成功");
        }
    }

    @DeleteMapping(value = "/{userId}")
    @ApiOperation("删除用户")
    @ApiResponses({@ApiResponse(code = 200, message = "删除用户成功"),
            @ApiResponse(code = 500, message = "删除用户失败"),
            @ApiResponse(code = 501, message = "userId为空")})
    public Result<Object> deleteUser(@ApiParam(name = "userId", value = "用户Id", type = "Integer")
                                     @PathVariable("userId") Integer userId, @RequestHeader("jwt") String jwt) {
        if (CharUtil.isBlankChar(userId)) {
            return new ResultUtil<>().setErrorMsg(501, "userId为空");
        } else {
            int deleteResult = this.userService.deleteByPrimaryKey(userId);
            return (deleteResult <= 0) ? new ResultUtil<>().setErrorMsg("删除用户失败") :
                    new ResultUtil<>().setSuccessMsg("删除用户成功");
        }
    }
}
