package com.shou.crabscore.controller.judge;

import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.Group;
import com.shou.crabscore.entity.QualityScore;
import com.shou.crabscore.entity.TasteScore;
import com.shou.crabscore.service.GroupService;
import com.shou.crabscore.service.QualityScoreService;
import com.shou.crabscore.service.TasteScoreService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评委评分接口
 *
 * @author spencercjh
 */
@SuppressWarnings("unused")
@Log4j2
@RestController
@Api(description = "评委用户组-评委评分接口")
@RequestMapping("/api/judge")
public class JudgeController {
    private final GroupService groupService;
    private final QualityScoreService qualityScoreService;
    private final TasteScoreService tasteScoreService;

    @Autowired
    public JudgeController(GroupService groupService, TasteScoreService tasteScoreService, QualityScoreService qualityScoreService) {
        this.groupService = groupService;
        this.tasteScoreService = tasteScoreService;
        this.qualityScoreService = qualityScoreService;
    }

    @GetMapping("/groups/{competitionId}/{pageNum}/{pageSize}")
    @ApiOperation("查看所有比赛组")
    @ApiResponses({@ApiResponse(code = 200, message = "查询所有比赛组成功"),
            @ApiResponse(code = 201, message = "groupList为空"),
            @ApiResponse(code = 501, message = "competitionId为空")})
    public Result<Object> allGroup(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                   @PathVariable("competitionId") Integer competitionId,
                                   @RequestHeader("jwt") String jwt,
                                   @ApiParam(name = "pageNum", value = "页数", type = "Integer")
                                   @PathVariable("pageNum") Integer pageNum,
                                   @ApiParam(name = "pageSize", value = "页面大小", type = "Integer")
                                   @PathVariable("pageSize") Integer pageSize) {
        if (competitionId == null || competitionId <= 0) {
            return new ResultUtil<>().setErrorMsg(501, "competitionId为空");
        } else {
            List<Group> groupList = this.groupService.selectAllGroupOneCompetition(competitionId, pageNum, pageSize);
            if (groupList.size() == 0) {
                return new ResultUtil<>().setSuccessMsg(201, "groupList为空");
            } else {
                return new ResultUtil<>().setData(groupList, "查询所有比赛组成功");
            }
        }
    }

    @PostMapping(value = "/quality", consumes = "application/json")
    @ApiOperation("插入种质成绩信息")
    @ApiResponses({@ApiResponse(code = 200, message = "插入种质成绩信息成功"),
            @ApiResponse(code = 500, message = "插入种质成绩信息失败"),
            @ApiResponse(code = 501, message = "ScoreId为空")})
    public Result<Object> insertQualityScoreInfo(@ApiParam(name = "qualityScoreInfo", value = "种质成绩信息Json", type = "String")
                                                 @RequestParam QualityScore qualityScore, @RequestHeader("jwt") String jwt) {
        if (qualityScore.getScoreId() == null || qualityScore.getScoreId() <= 0) {
            return new ResultUtil<>().setErrorMsg(501, "ScoreId为空");
        } else {
            return this.qualityScoreService.insert(qualityScore) <= 0 ?
                    new ResultUtil<>().setErrorMsg("插入种质成绩信息失败") : new ResultUtil<>().setSuccessMsg("插入种质成绩信息成功");
        }
    }

    @PostMapping(value = "/taste", consumes = "application/json")
    @ApiOperation("插入口感成绩信息")
    @ApiResponses({@ApiResponse(code = 200, message = "插入种质成绩信息成功"),
            @ApiResponse(code = 500, message = "插入种质成绩信息失败"),
            @ApiResponse(code = 501, message = "ScoreId为空")})
    public Result<Object> insertTasteScoreInfo(@ApiParam(name = "tasteScoreInfo", value = "口感成绩信息Json", type = "String")
                                               @RequestParam TasteScore tasteScore, @RequestHeader("jwt") String jwt) {
        if (tasteScore.getScoreId() == null || tasteScore.getScoreId() <= 0) {
            return new ResultUtil<>().setErrorMsg(501, "ScoreId为空");
        } else {
            return this.tasteScoreService.insert(tasteScore) <= 0 ?
                    new ResultUtil<>().setErrorMsg("插入口感成绩信息失败") : new ResultUtil<>().setSuccessMsg("插入口感成绩信息成功");
        }
    }

    @PutMapping(value = "/quality", consumes = "application/json")
    @ApiOperation("更新种质成绩信息")
    @ApiResponses({@ApiResponse(code = 200, message = "修改种质成绩信息成功"),
            @ApiResponse(code = 500, message = "修改种质成绩信息失败"),
            @ApiResponse(code = 501, message = "主键ScoreId为空")})
    public Result<Object> updateQualityScoreInfo(@ApiParam(name = "qualityScoreInfo", value = "种质成绩信息Json", type = "String")
                                                 @RequestParam QualityScore qualityScore, @RequestHeader("jwt") String jwt) {
        if (qualityScore.getScoreId() == null || qualityScore.getScoreId() <= 0) {
            return new ResultUtil<>().setErrorMsg(501, "主键ScoreId为空");
        } else {
            int updateResult = this.qualityScoreService.updateByPrimaryKey(qualityScore);
            return (updateResult <= 0) ? new ResultUtil<>().setErrorMsg("修改种质成绩信息失败") :
                    new ResultUtil<>().setSuccessMsg("修改种质成绩信息成功");
        }
    }

    @PutMapping(value = "/taste", consumes = "application/json")
    @ApiOperation("更新口感成绩信息")
    @ApiResponses({@ApiResponse(code = 200, message = "修改口感成绩信息成功"),
            @ApiResponse(code = 500, message = "修改口感成绩信息失败"),
            @ApiResponse(code = 501, message = "主键ScoreId为空")})
    public Result<Object> updateTasteScoreInfo(@ApiParam(name = "tasteScoreInfo", value = "口感成绩信息Json", type = "String")
                                               @RequestParam TasteScore tasteScore, @RequestHeader("jwt") String jwt) {
        if (tasteScore.getScoreId() == null || tasteScore.getScoreId() <= 0) {
            return new ResultUtil<>().setErrorMsg(501, "ScoreId为空");
        } else {
            return this.tasteScoreService.updateByPrimaryKey(tasteScore) <= 0 ?
                    new ResultUtil<>().setErrorMsg("修改口感成绩信息失败") : new ResultUtil<>().setSuccessMsg("修改口感成绩信息成功");
        }
    }
}
