package com.shou.crabscore.controller.common;

import cn.hutool.core.util.CharUtil;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.Group;
import com.shou.crabscore.service.GroupService;
import io.swagger.annotations.*;
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
@Api(description = "公共用户组-公共三大奖查分接口")
@RequestMapping("/api/common/score")
public class CommonCheckScoreController {
    private final GroupService groupService;

    @Autowired
    public CommonCheckScoreController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping(value = "/fatnesses/{competitionId}")
    @ApiOperation(value = "查询金蟹奖成绩", notes = "已在SQL中按照从大到小顺序排列")
    @ApiResponses({@ApiResponse(code = 200, message = "查找所有金蟹奖成绩成功"),
            @ApiResponse(code = 201, message = "没有金蟹奖成绩相关成绩"),
            @ApiResponse(code = 501, message = "competitionId为空")})
    public Result<Object> fatnessPrize(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                       @PathVariable("competitionId") Integer competitionId) {
        if (CharUtil.isBlankChar(competitionId)) {
            return new ResultUtil<>().setErrorMsg(501, "competitionId为空");
        } else {
            List<Group> groupList = this.groupService.selectAllGroupOneCompetitionOrderByFatnessScore(competitionId);
            return groupList.size() == 0 ? (new ResultUtil<>().setSuccessMsg(201, "没有金蟹奖成绩")) :
                    (new ResultUtil<>().setData(groupList, "查找所有金蟹奖成绩成功"));
        }
    }

    @GetMapping(value = "/qualities/{competitionId}")
    @ApiOperation(value = "查询种质奖成绩", notes = "已在SQL中按照从大到小顺序排列")
    @ApiResponses({@ApiResponse(code = 200, message = "查找所有种质奖成绩成功"),
            @ApiResponse(code = 201, message = "没有种质奖成绩"),
            @ApiResponse(code = 501, message = "competitionId为空")})
    public Result<Object> qualityPrize(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                       @PathVariable("competitionId") Integer competitionId) {
        if (CharUtil.isBlankChar(competitionId)) {
            return new ResultUtil<>().setErrorMsg(501, "competitionId为空");
        } else {
            List<Group> groupList = this.groupService.selectAllGroupOneCompetitionOrderByQualityScore(competitionId);
            return groupList.size() == 0 ? (new ResultUtil<>().setSuccessMsg(201, "没有种质奖成绩")) :
                    (new ResultUtil<>().setData(groupList, "查找所有种质奖成绩成功"));
        }
    }

    @GetMapping(value = "/tastes/{competitionId}")
    @ApiOperation(value = "查询口感奖成绩", notes = "已在SQL中按照从大到小顺序排列")
    @ApiResponses({@ApiResponse(code = 200, message = "查找所有口感奖成绩成功"),
            @ApiResponse(code = 201, message = "没有口感奖成绩"),
            @ApiResponse(code = 501, message = "competitionId为空")})
    public Result<Object> tastePrize(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                     @PathVariable("competitionId") Integer competitionId) {
        if (CharUtil.isBlankChar(competitionId)) {
            return new ResultUtil<>().setErrorMsg(501, "competitionId为空");
        } else {
            List<Group> groupList = this.groupService.selectAllGroupOneCompetitionOrderByTasteScore(competitionId);
            return groupList.size() == 0 ? (new ResultUtil<>().setSuccessMsg(201, "没有口感奖成绩")) :
                    (new ResultUtil<>().setData(groupList, "查找所有口感奖成绩成功"));
        }
    }
}
