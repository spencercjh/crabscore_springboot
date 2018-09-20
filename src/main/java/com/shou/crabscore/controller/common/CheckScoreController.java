package com.shou.crabscore.controller.common;

import cn.hutool.core.util.NumberUtil;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.Group;
import com.shou.crabscore.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公共三大奖查分接口
 *
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "公共三大奖查分接口")
@RequestMapping("/api/common/score")
public class CheckScoreController {
    private final GroupService groupService;

    @Autowired
    public CheckScoreController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping(value = "/fatness/{competitionId}")
    @ApiOperation(value = "查询金蟹奖成绩", notes = "已在SQL中按照从大到小顺序排列")
    public Result<Object> fatnessPrize(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                       @PathVariable("competitionId") Integer competitionId) {
        if (NumberUtil.isBlankChar(competitionId)) {
            return new ResultUtil<>().setErrorMsg("参数为空");
        } else {
            List<Group> groupList = this.groupService.selectAllGroupOneCompetitionOrderByFatnessScore(competitionId);
            return groupList.size() == 0 ? (new ResultUtil<>().setErrorMsg("没有相关成绩")) : (new ResultUtil<>().setData(groupList));
        }
    }

    @GetMapping(value = "/quality/{competitionId}")
    @ApiOperation(value = "查询种质奖成绩", notes = "已在SQL中按照从大到小顺序排列")
    public Result<Object> qualityPrize(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                       @PathVariable("competitionId") Integer competitionId) {
        if (NumberUtil.isBlankChar(competitionId)) {
            return new ResultUtil<>().setErrorMsg("参数为空");
        } else {
            List<Group> groupList = this.groupService.selectAllGroupOneCompetitionOrderByQualityScore(competitionId);
            return groupList.size() == 0 ? (new ResultUtil<>().setErrorMsg("没有相关成绩")) : (new ResultUtil<>().setData(groupList));
        }
    }

    @GetMapping(value = "/taste/{competitionId}")
    @ApiOperation(value = "查询口感奖成绩", notes = "已在SQL中按照从大到小顺序排列")
    public Result<Object> tastePrize(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                     @PathVariable("competitionId") Integer competitionId) {
        if (NumberUtil.isBlankChar(competitionId)) {
            return new ResultUtil<>().setErrorMsg("参数为空");
        } else {
            List<Group> groupList = this.groupService.selectAllGroupOneCompetitionOrderByTasteScore(competitionId);
            return groupList.size() == 0 ? (new ResultUtil<>().setErrorMsg("没有相关成绩")) : (new ResultUtil<>().setData(groupList));
        }
    }
}
