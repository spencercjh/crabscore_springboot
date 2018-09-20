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
    public Result<Object> singleCompetition(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                                @PathVariable("competitionId") Integer competitionId) {
        if (NumberUtil.isBlankChar(competitionId)) {
            return new ResultUtil<>().setErrorMsg("主键为空");
        } else {
            Competition competition = this.competitionService.selectByPrimaryKey(competitionId);
            return StrUtil.isNotBlank(competition.getCompetitionYear()) ? (new ResultUtil<>().setData(competition)) :
                    (new ResultUtil<>().setErrorMsg("查找失败"));
        }
    }

    @GetMapping(value = "/allcompetition")
    @ApiOperation("查询所有大赛")
    public Result<Object> allCompetition() {
        List<Competition> competitionList = this.competitionService.selectAllCompetition();
        if (competitionList.size() == 0) {
            return new ResultUtil<>().setSuccessMsg("没有大赛");
        } else {
            return new ResultUtil<>().setData(competitionList, "查询所有大赛成功");
        }
    }

    @PostMapping(value = "/property")
    @ApiOperation("修改大赛资料")
    @ApiImplicitParam(name = "competition", value = "大赛信息", dataType = "Competition")
    public Result<Object> updateCompetitionProperty(@ApiParam("大赛信息Json") @RequestBody Competition competition) {
        if (NumberUtil.isBlankChar(competition.getCompetitionId())) {
            return new ResultUtil<>().setErrorMsg("主键为空");
        } else {
            return (this.competitionService.updateByPrimaryKeySelective(competition) == 0) ?
                    (new ResultUtil<>().setErrorMsg("修改失败")) : (new ResultUtil<>().setSuccessMsg("修改成功"));
        }
    }

    @GetMapping(value = "/present")
    @ApiOperation("查询当前大赛Id")
    public Result<Object> presentCompetitionId() {
        return new ResultUtil<>().setData(this.competitionConfigService.selectByPrimaryKey(1));
    }

    @PostMapping(value = "/present")
    @ApiOperation("修改当前大赛配置(大赛id)")
    @ApiImplicitParam(name = "competitionConfig", value = "大赛配置", dataType = "CompetitionConfig")
    public Result<Object> updatePresentCompetitionId(@ApiParam("大赛配置Json") @RequestBody CompetitionConfig competitionConfig) {
        if (NumberUtil.isBlankChar(competitionConfig.getCompetitionId()) || NumberUtil.isBlankChar(competitionConfig.getId())) {
            return new ResultUtil<>().setErrorMsg("主键为空");
        } else {
            return (this.competitionConfigService.updateByPrimaryKey(competitionConfig) == 0) ?
                    (new ResultUtil<>().setErrorMsg("修改失败")) : (new ResultUtil<>().setSuccessMsg("修改成功"));
        }
    }
}
