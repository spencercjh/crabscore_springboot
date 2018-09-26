package com.shou.crabscore.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.shou.crabscore.common.util.QiniuUtil;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.util.UsernameUtil;
import com.shou.crabscore.common.util.MessageUtil;
import com.shou.crabscore.common.vo.Result;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 测试接口
 *
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "测试接口")
public class TestController {
    private final MessageUtil messageUtil;
    private final QiniuUtil qiniuUtil;

    @Autowired
    public TestController(QiniuUtil qiniuUtil, MessageUtil messageUtil) {
        this.qiniuUtil = qiniuUtil;
        this.messageUtil = messageUtil;
    }

    @SuppressWarnings("unused")
    @PostMapping("/test")
    @ApiOperation("测试自己写的JWT校验业务")
    @ApiResponses({@ApiResponse(code = 200, message = "success"), @ApiResponse(code = 500, message = "beyond")})
    public Result<Object> test(@RequestHeader String jwt) {
        log.info("TEST CONTROLLER");
        return new ResultUtil<>().setSuccessMsg("success");
    }

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    @PutMapping(value = "/file")
    @ApiOperation(value = "文件上传")
    public Result<Object> upload(@RequestParam("file") MultipartFile file) {

        String imagePath;
        String fileName = qiniuUtil.renamePic(file.getOriginalFilename());
        try {
            FileInputStream inputStream = (FileInputStream) file.getInputStream();
            //上传七牛云服务器
            imagePath = qiniuUtil.qiniuInputStreamUpload(inputStream, fileName);
            if (StrUtil.isBlank(imagePath)) {
                return new ResultUtil<>().setErrorMsg("上传失败，请检查七牛云配置");
            }
            if (imagePath.contains("error")) {
                return new ResultUtil<>().setErrorMsg(imagePath);
            }
        } catch (Exception e) {
            log.error(e.toString());
            return new ResultUtil<>().setErrorMsg(e.toString());
        }

        return new ResultUtil<>().setData(imagePath);
    }

    @SuppressWarnings("Duplicates")
    @GetMapping("/code")
    @ApiOperation(value = "测试 请求发送验证码短信")
    @ApiResponses({@ApiResponse(code = 200, message = "验证码发送成功"),
            @ApiResponse(code = 500, message = "验证码发送失败"),
            @ApiResponse(code = 501, message = "手机号格式有误")})
    public Result<Object> sendCode(@ApiParam(name = "mobile", value = "手机号", type = "String") @RequestParam String mobile) {
        if (UsernameUtil.mobile(mobile)) {
            try {
                if (messageUtil.sendCode(mobile)) {
                    return new ResultUtil<>().setSuccessMsg("验证码发送成功");
                } else {
                    return new ResultUtil<>().setErrorMsg("验证码发送失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("短信发送请求错误");
                return new ResultUtil<>().setErrorMsg("验证码发送失败");
            }
        } else {
            return new ResultUtil<>().setErrorMsg(501, "手机号格式有误");
        }
    }

    @SuppressWarnings("Duplicates")
    @PostMapping("/code")
    @ApiOperation(value = "测试 请求校验验证码")
    @ApiResponses({@ApiResponse(code = 200, message = ""),})
    public Result<Object> verifyCode(@ApiParam(name = "mobile", value = "手机号", type = "String") @RequestParam String mobile,
                                     @ApiParam(name = "code", value = "验证码", type = "String") @RequestParam String code) {
        if (UsernameUtil.mobile(mobile) && NumberUtil.isNumber(code)) {
            try {
                if (messageUtil.verifyCode(mobile, code)) {
                    return new ResultUtil<>().setSuccessMsg("验证码校验成功");
                } else {
                    return new ResultUtil<>().setErrorMsg(502, "验证码无效");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new ResultUtil<>().setErrorMsg(500, "验证码校验失败");
            }
        } else {
            return new ResultUtil<>().setErrorMsg(501, "手机号或验证码格式有误");
        }
    }
}
