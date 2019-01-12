package com.shou.crabscore.controller.admin;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.shou.crabscore.common.constant.CommonConstant;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.service.CompanyService;
import com.shou.crabscore.service.GroupService;
import com.shou.crabscore.service.ScoreService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : spencercjh
 * @date : 2019/1/12 19:34
 */
@SuppressWarnings("unused")
@Log4j2
@Controller
@Api(description = "管理员用户组-大赛结果管理结果")
@RequestMapping("/api/admin/result")
public class ResultAdminController {
    private final ScoreService scoreService;
    private final CompanyService companyService;
    private final GroupService groupService;

    @Autowired
    public ResultAdminController(ScoreService scoreService, CompanyService companyService, GroupService groupService) {
        this.scoreService = scoreService;
        this.companyService = companyService;
        this.groupService = groupService;
    }

    @Value("${excel_url}")
    private String excelUrl;

    @PostMapping(value = "/calculate")
    @ApiOperation("计算大赛成绩")
    @ApiResponses({@ApiResponse(code = 200, message = "计算成功")})
    @ResponseBody
    public Result<Object> calculatePresentCompetitionScore(@ApiParam("大赛ID") @RequestBody Integer competitionId,
                                                           @RequestHeader("jwt") String jwt) {
        return scoreService.calculateAllFatnessScore(competitionId) &&
                scoreService.calculateQualityScore(competitionId) &&
                scoreService.calculateTasteScore(competitionId) ?
                new ResultUtil<>().setSuccessMsg("大赛成绩计算成功") : new ResultUtil<>().setErrorMsg("大赛成绩计算失败");
    }

    @PostMapping("/excel")
    @ApiOperation("生成大赛数据Excel文件")
    @ApiResponses({@ApiResponse(code = 200, message = "生成成功")})
    @ResponseBody
    public Result<Object> sendDataToPythonSever(@ApiParam("大赛ID") @RequestBody Integer competitionId,
                                                @RequestHeader("jwt") String jwt) {
        Map<String, Object> hashMap = new HashMap<>(4);
        hashMap.put("all_company", JSON.toJSONString(companyService.selectAllCompany()));
        hashMap.put("all_group", JSON.toJSONString(groupService.selectAllGroupOneCompetition(competitionId, 1, 10000)));
        HttpResponse httpResponse = HttpUtil.createPost(excelUrl).form(hashMap).execute();
        if (httpResponse.getStatus() == CommonConstant.SUCCESS) {
            return new ResultUtil<>().setSuccessMsg("生成成功");
        } else {
            return new ResultUtil<>().setErrorMsg("生成失败");
        }
    }
}
