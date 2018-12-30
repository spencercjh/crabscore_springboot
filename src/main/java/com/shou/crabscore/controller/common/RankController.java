package com.shou.crabscore.controller.common;

import com.github.pagehelper.PageInfo;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.dto.RankResult;
import com.shou.crabscore.service.GroupService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公共三大奖查分接口
 *
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "公共用户组-公共三大奖查分接口")
@RequestMapping("/api/common/score")
public class RankController {
    private final GroupService groupService;

    @Autowired
    public RankController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping(value = "/fatness/{competitionId}/{pageNum}/{pageSize}")
    @ApiOperation(value = "查询金蟹奖成绩", notes = "已在SQL中按照从大到小顺序排列")
    @ApiResponses({@ApiResponse(code = 200, message = "查找所有金蟹奖成绩成功"),
            @ApiResponse(code = 201, message = "没有金蟹奖成绩相关成绩"),
            @ApiResponse(code = 501, message = "competitionId为空")})
    public Result<Object> fatnessPrize(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                       @PathVariable("competitionId") Integer competitionId,
                                       @ApiParam(name = "pageNum", value = "页数", type = "Integer")
                                       @PathVariable("pageNum") Integer pageNum,
                                       @ApiParam(name = "pageSize", value = "页面大小", type = "Integer")
                                       @PathVariable("pageSize") Integer pageSize) {
        if (competitionId == null || competitionId <= 0) {
            return new ResultUtil<>().setErrorMsg(501, "competitionId为空");
        } else {
            PageInfo<RankResult> pageInfo = groupService.selectAllGroupOneCompetitionOrderByFatnessScore(competitionId,
                    pageNum, pageSize);
            return pageInfo.getList().isEmpty() ? (new ResultUtil<>().setSuccessMsg(201, "没有金蟹奖成绩")) :
                    (new ResultUtil<>().setData(pageInfo, "查找所有金蟹奖成绩成功"));
        }
    }

    @GetMapping(value = "/qualities/{competitionId}/{pageNum}/{pageSize}")
    @ApiOperation(value = "查询种质奖成绩", notes = "已在SQL中按照从大到小顺序排列")
    @ApiResponses({@ApiResponse(code = 200, message = "查找所有种质奖成绩成功"),
            @ApiResponse(code = 201, message = "没有种质奖成绩"),
            @ApiResponse(code = 501, message = "competitionId为空")})
    public Result<Object> qualityPrize(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                       @PathVariable("competitionId") Integer competitionId,
                                       @ApiParam(name = "pageNum", value = "页数", type = "Integer")
                                       @PathVariable("pageNum") Integer pageNum,
                                       @ApiParam(name = "pageSize", value = "页面大小", type = "Integer")
                                       @PathVariable("pageSize") Integer pageSize) {
        if (competitionId == null || competitionId <= 0) {
            return new ResultUtil<>().setErrorMsg(501, "competitionId为空");
        } else {
            PageInfo<RankResult> pageInfo = groupService.selectAllGroupOneCompetitionOrderByQualityScore(competitionId,
                    pageNum, pageSize);
            return pageInfo.getList().isEmpty() ? (new ResultUtil<>().setSuccessMsg(201, "没有种质奖成绩")) :
                    (new ResultUtil<>().setData(pageInfo, "查找所有种质奖成绩成功"));
        }
    }

    @GetMapping(value = "/tastes/{competitionId}/{pageNum}/{pageSize}")
    @ApiOperation(value = "查询口感奖成绩", notes = "已在SQL中按照从大到小顺序排列")
    @ApiResponses({@ApiResponse(code = 200, message = "查找所有口感奖成绩成功"),
            @ApiResponse(code = 201, message = "没有口感奖成绩"),
            @ApiResponse(code = 501, message = "competitionId为空")})
    public Result<Object> tastePrize(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                     @PathVariable("competitionId") Integer competitionId,
                                     @ApiParam(name = "pageNum", value = "页数", type = "Integer")
                                     @PathVariable("pageNum") Integer pageNum,
                                     @ApiParam(name = "pageSize", value = "页面大小", type = "Integer")
                                     @PathVariable("pageSize") Integer pageSize) {
        if (competitionId == null || competitionId <= 0) {
            return new ResultUtil<>().setErrorMsg(501, "competitionId为空");
        } else {
            PageInfo<RankResult> pageInfo = groupService.selectAllGroupOneCompetitionOrderByTasteScore(competitionId,
                    pageNum, pageSize);
            return pageInfo.getList().isEmpty() ? (new ResultUtil<>().setSuccessMsg(201, "没有口感奖成绩")) :
                    (new ResultUtil<>().setData(pageInfo, "查找所有口感奖成绩成功"));
        }
    }
}
