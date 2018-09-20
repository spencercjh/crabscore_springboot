package com.shou.crabscore.controller.admin;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.shou.crabscore.common.constant.CommonConstant;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.User;
import com.shou.crabscore.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户后台管理接口
 *
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "用户后台管理接口")
@RequestMapping("/api/admin/user")
public class UserAdminController {
    private final UserService userService;

    @Autowired
    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{userId}")
    @ApiOperation("查询单个用户")
    public Result<Object> singleUser(@ApiParam(name = "userId", value = "用户Id", type = "Integer")
                                         @PathVariable("userId") Integer userId) {
        if (NumberUtil.isBlankChar(userId)) {
            return new ResultUtil<>().setErrorMsg("主键为空");
        } else {
            User user = this.userService.selectByPrimaryKey(userId);
            return StrUtil.isNotBlank(user.getUserName()) ?
                    (new ResultUtil<>().setData(user)) : (new ResultUtil<>().setErrorMsg("查找失败"));
        }
    }

    @GetMapping(value = "/alluser")
    @ApiOperation("查询所有用户")
    public Result<Object> allUser() {
        List<User> userList = this.userService.selectAllUser();
        if (userList.size() == 0) {
            return new ResultUtil<>().setSuccessMsg("没有用户");
        } else {
            return new ResultUtil<>().setData(userList, "查询所有用户成功");
        }
    }

    @GetMapping(value = "/partuser/{status}")
    @ApiOperation("查询所有符合某一状态的用户")
    public Result<Object> partUser(@ApiParam(name = "status", value = "用户状态 1：可用 0：禁用", type = "Integer")
                                       @PathVariable("status") Integer status) {
        List<User> userList = this.userService.selectAllUserSelective(status);
        if (userList.size() == 0) {
            return new ResultUtil<>().setSuccessMsg("没有找到" +
                    (status.equals(CommonConstant.USER_STATUS_NORMAL) ? "可用" : "禁用") + "用户");
        } else {
            return new ResultUtil<>().setData(userList, "查询所有用户成功");
        }
    }

    @PutMapping(value = "/property")
    @ApiOperation("修改用户资料")
    @ApiImplicitParam(name = "user", value = "单个用户信息", dataType = "User")
    public Result<Object> updateUserProperty(@ApiParam("用户信息Json") @RequestBody User user) {
        if (NumberUtil.isBlankChar(user.getUserId())) {
            return new ResultUtil<>().setErrorMsg("主键为空");
        } else {
            int updateResult = this.userService.updateByPrimaryKeySelective(user);
            return (updateResult <= 0) ? new ResultUtil<>().setErrorMsg("修改失败") :
                    new ResultUtil<>().setSuccessMsg("修改成功");
        }
    }

    @DeleteMapping(value = "/{userId}")
    @ApiOperation("删除用户")
    public Result<Object> deleteUser(@ApiParam(name = "userId", value = "用户Id", type = "Integer")
                                         @PathVariable("userId") Integer userId) {
        if (NumberUtil.isBlankChar(userId)) {
            return new ResultUtil<>().setErrorMsg("主键为空");
        } else {
            int deleteResult = this.userService.deleteByPrimaryKey(userId);
            return (deleteResult <= 0) ? new ResultUtil<>().setErrorMsg("删除失败") :
                    new ResultUtil<>().setSuccessMsg("删除成功");
        }
    }
}
