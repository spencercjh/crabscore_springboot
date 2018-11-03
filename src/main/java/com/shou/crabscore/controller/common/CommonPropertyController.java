package com.shou.crabscore.controller.common;

import cn.hutool.core.util.CharUtil;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "公共用户组-大赛公共信息接口")
@RequestMapping("/api/common/property")
public class CommonPropertyController {

    private final CompetitionConfigService competitionConfigService;
    private final CompetitionService competitionService;

    @Autowired
    public CommonPropertyController(CompetitionConfigService competitionConfigService, CompetitionService competitionService) {
        this.competitionConfigService = competitionConfigService;
        this.competitionService = competitionService;
    }

    @GetMapping(value = "/competition/present")
    @ApiOperation("查询当前大赛Id")
    @ApiResponses({@ApiResponse(code = 200, message = "查询当前大赛Id成功"),
            @ApiResponse(code = 500, message = "查询当前大赛Id失败")})
    public Result<Object> presentCompetitionId() {
        CompetitionConfig competitionConfig = this.competitionConfigService.selectByPrimaryKey(1);
        return CharUtil.isBlankChar(competitionConfig.getCompetitionId()) ?
                new ResultUtil<>().setData(competitionConfig.getCompetitionId(), "查询当前大赛Id成功") :
                new ResultUtil<>().setErrorMsg("查询当前大赛Id失败");
    }

    @GetMapping(value = "/competition/{competitionId}")
    @ApiOperation("查询单个大赛信息")
    @ApiResponses({@ApiResponse(code = 200, message = "查询单个大赛信息成功"),
            @ApiResponse(code = 500, message = "查询单个大赛信息失败"),
            @ApiResponse(code = 501, message = "competitionId为空")})
    public Result<Object> singleCompetition(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                            @PathVariable("competitionId") Integer competitionId) {
        if (CharUtil.isBlankChar(competitionId)) {
            return new ResultUtil<>().setErrorMsg(501, "competitionId为空");
        } else {
            Competition competition = this.competitionService.selectByPrimaryKey(competitionId);
            return StrUtil.isNotBlank(competition.getCompetitionYear()) ? new ResultUtil<>().setData(competition, "查询单个大赛信息成功") :
                    new ResultUtil<>().setErrorMsg("查询单个大赛信息失败");
        }
    }
}
