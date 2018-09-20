package com.shou.crabscore.controller.admin;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.Competition;
import com.shou.crabscore.entity.CompetitionConfig;
import com.shou.crabscore.service.CompetitionConfigService;
import com.shou.crabscore.service.CompetitionService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 大赛后台管理接口
 *
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "大赛后台管理接口")
@RequestMapping("/api/admin/competition")
public class CompetitionAdminController {
    private final CompetitionConfigService competitionConfigService;
    private final CompetitionService competitionService;

    @Autowired
    public CompetitionAdminController(CompetitionConfigService competitionConfigService, CompetitionService competitionService) {
        this.competitionConfigService = competitionConfigService;
        this.competitionService = competitionService;
    }

    @GetMapping(value = "/{competitionId}")
    @ApiOperation("查询单个大赛信息")
    @ApiResponses({@ApiResponse(code = 200, message = "查询单个大赛信息成功"),
            @ApiResponse(code = 500, message = "查询单个大赛信息失败"),
            @ApiResponse(code = 501, message = "competitionId为空")})
    public Result<Object> singleCompetition(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                            @PathVariable("competitionId") Integer competitionId) {
        if (NumberUtil.isBlankChar(competitionId)) {
            return new ResultUtil<>().setErrorMsg(501, "competitionId为空");
        } else {
            Competition competition = this.competitionService.selectByPrimaryKey(competitionId);
            return StrUtil.isNotBlank(competition.getCompetitionYear()) ? new ResultUtil<>().setData(competition, "查询单个大赛信息成功") :
                    new ResultUtil<>().setErrorMsg("查询单个大赛信息失败");
        }
    }

    @GetMapping(value = "/allcompetition")
    @ApiOperation("查询所有大赛")
    @ApiResponses({@ApiResponse(code = 200, message = "查询所有大赛成功"),
            @ApiResponse(code = 201, message = "没有大赛")})
    public Result<Object> allCompetition() {
        List<Competition> competitionList = this.competitionService.selectAllCompetition();
        if (competitionList.size() == 0) {
            return new ResultUtil<>().setSuccessMsg(201, "没有大赛");
        } else {
            return new ResultUtil<>().setData(competitionList, "查询所有大赛成功");
        }
    }

    @PutMapping(value = "/property")
    @ApiOperation("修改大赛资料")
    @ApiImplicitParam(name = "competition", value = "大赛信息", dataType = "Competition")
    @ApiResponses({@ApiResponse(code = 200, message = "修改大赛资料成功"),
            @ApiResponse(code = 500, message = "修改大赛资料失败"),
            @ApiResponse(code = 501, message = "CompetitionId为空")})
    public Result<Object> updateCompetitionProperty(@ApiParam("大赛信息Json") @RequestBody Competition competition) {
        if (NumberUtil.isBlankChar(competition.getCompetitionId())) {
            return new ResultUtil<>().setErrorMsg(501, "CompetitionId为空");
        } else {
            return this.competitionService.updateByPrimaryKeySelective(competition) == 0 ?
                    new ResultUtil<>().setErrorMsg("修改大赛资料失败") : new ResultUtil<>().setSuccessMsg(200, "修改大赛资料成功");
        }
    }

    @GetMapping(value = "/present")
    @ApiOperation("查询当前大赛Id")
    @ApiResponses({@ApiResponse(code = 200, message = "查询当前大赛Id成功"),
            @ApiResponse(code = 500, message = "查询当前大赛Id失败")})
    public Result<Object> presentCompetitionId() {
        CompetitionConfig competitionConfig = this.competitionConfigService.selectByPrimaryKey(1);
        return NumberUtil.isBlankChar(competitionConfig.getCompetitionId()) ?
                new ResultUtil<>().setData(competitionConfig.getCompetitionId(), "查询当前大赛Id成功") :
                new ResultUtil<>().setErrorMsg("查询当前大赛Id失败");
    }

    @PutMapping(value = "/present")
    @ApiOperation("修改当前大赛配置(大赛id)")
    @ApiImplicitParam(name = "competitionConfig", value = "大赛配置", dataType = "CompetitionConfig")
    @ApiResponses({@ApiResponse(code = 200, message = "修改当前大赛配置成功"),
            @ApiResponse(code = 500, message = "修改当前大赛配置失败"),
            @ApiResponse(code = 501, message = "CompetitionId为空")})
    public Result<Object> updatePresentCompetitionId(@ApiParam("大赛配置Json") @RequestBody CompetitionConfig competitionConfig) {
        if (NumberUtil.isBlankChar(competitionConfig.getCompetitionId()) || NumberUtil.isBlankChar(competitionConfig.getId())) {
            return new ResultUtil<>().setErrorMsg(501, "CompetitionId为空");
        } else {
            return this.competitionConfigService.updateByPrimaryKey(competitionConfig) == 0 ?
                    new ResultUtil<>().setErrorMsg("修改当前大赛配置失败") : new ResultUtil<>().setSuccessMsg("修改当前大赛配置成功");
        }
    }
}