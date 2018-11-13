package com.shou.crabscore.controller.common;

import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.Competition;
import com.shou.crabscore.entity.CompetitionConfig;
import com.shou.crabscore.entity.User;
import com.shou.crabscore.service.CompetitionConfigService;
import com.shou.crabscore.service.CompetitionService;
import com.shou.crabscore.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "公共用户组-大赛公共信息接口")
@RequestMapping("/api/common")
public class CommonPropertyController {

    private final CompetitionConfigService competitionConfigService;
    private final CompetitionService competitionService;
    private final UserService userService;

    @Autowired
    public CommonPropertyController(CompetitionConfigService competitionConfigService, CompetitionService competitionService, UserService userService) {
        this.competitionConfigService = competitionConfigService;
        this.competitionService = competitionService;
        this.userService = userService;
    }

    @GetMapping(value = "/property/competition/present")
    @ApiOperation("查询当前大赛的信息")
    @ApiResponses({@ApiResponse(code = 200, message = "查询当前大赛信息成功"),
            @ApiResponse(code = 500, message = "查询当前大赛信息失败")})
    public Result<Object> presentCompetitionId() {
        CompetitionConfig competitionConfig = this.competitionConfigService.selectByPrimaryKey(1);
        Competition competition = this.competitionService.selectByPrimaryKey(competitionConfig.getCompetitionId());
        return competition.getCompetitionId() > 0 ?
                new ResultUtil<>().setData(competition, "查询当前大赛信息成功") :
                new ResultUtil<>().setErrorMsg("查询当前大赛信息失败");
    }

    @PutMapping(value = "/person/property", consumes = "application/json")
    @ApiOperation("修改用户资料")
    @ApiImplicitParam(name = "user", value = "单个用户信息", dataType = "User")
    @ApiResponses({@ApiResponse(code = 200, message = "修改用户资料成功"),
            @ApiResponse(code = 500, message = "修改用户资料失败"),
            @ApiResponse(code = 501, message = "UserId为空")})
    public Result<Object> updateUserProperty(@ApiParam("用户信息Json") @RequestBody User user,
                                             @RequestHeader("jwt") String jwt) {
        if (user.getUserId() == null || user.getUserId() <= 0) {
            return new ResultUtil<>().setErrorMsg(501, "userId为空");
        } else {
            int updateResult = this.userService.updateByPrimaryKeySelective(user);
            return (updateResult <= 0) ? new ResultUtil<>().setErrorMsg("修改资料失败") :
                    new ResultUtil<>().setSuccessMsg("修改资料成功");
        }
    }
}
