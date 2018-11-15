package com.shou.crabscore.controller.company;

import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.Company;
import com.shou.crabscore.entity.QualityScore;
import com.shou.crabscore.entity.TasteScore;
import com.shou.crabscore.entity.User;
import com.shou.crabscore.entity.vo.CrabScoreResult;
import com.shou.crabscore.entity.vo.GroupResult;
import com.shou.crabscore.service.*;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参选单位查分接口
 *
 * @author spencercjh
 */
@SuppressWarnings("unused")
@Log4j2
@RestController
@Api(description = "参选单位用户组-参选单位查分接口")
@RequestMapping("/api/company")
public class CompanyCheckScoreController {
    private final QualityScoreService qualityScoreService;
    private final TasteScoreService tasteScoreService;
    private final CompanyService companyService;
    private final UserService userService;
    private final GroupService groupService;
    private final CrabService crabService;

    @Autowired
    public CompanyCheckScoreController(QualityScoreService qualityScoreService, TasteScoreService tasteScoreService, CompanyService companyService, UserService userService, GroupService groupService, CrabService crabService) {
        this.qualityScoreService = qualityScoreService;
        this.tasteScoreService = tasteScoreService;
        this.companyService = companyService;
        this.userService = userService;
        this.groupService = groupService;
        this.crabService = crabService;
    }

    @GetMapping("/score/qualities/{competitionId}/{groupId}/{crabSex}/{pageNum}/{pageSize}")
    @ApiOperation("查询某一年的某一组的某一性别的所有螃蟹种质成绩")
    @ApiResponses({@ApiResponse(code = 200, message = "查询种质成绩信息成功"),
            @ApiResponse(code = 201, message = "没有种质成绩信息")})
    public Result<Object> allQualityScoreInfo(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                              @PathVariable("competitionId") Integer competitionId,
                                              @ApiParam(name = "groupId", value = "小组Id", type = "Integer")
                                              @PathVariable("groupId") Integer groupId,
                                              @ApiParam(name = "crabSex", value = "性别，1:雄 2：雌", type = "Integer")
                                              @PathVariable("crabSex") Integer crabSex,
                                              @RequestHeader("jwt") String jwt,
                                              @ApiParam(name = "pageNum", value = "页数", type = "Integer")
                                              @PathVariable("pageNum") Integer pageNum,
                                              @ApiParam(name = "pageSize", value = "页面大小", type = "Integer")
                                              @PathVariable("pageSize") Integer pageSize) {
        List<QualityScore> qualityScoreList = this.qualityScoreService.selectByCompetitionIdAndGroupIdAndCrabSex(
                competitionId, groupId, crabSex, pageNum, pageSize);
        return qualityScoreList.size() == 0 ? new ResultUtil<>().setSuccessMsg(201, "没有种质成绩信息") :
                new ResultUtil<>().setData(qualityScoreList, "查询种质成绩信息成功");
    }

    @GetMapping("/score/tastes/{competitionId}/{groupId}/{crabSex}/{pageNum}/{pageSize}")
    @ApiOperation("查询某一年的某一组的某一性别的所有螃蟹口感成绩")
    @ApiResponses({@ApiResponse(code = 200, message = "查询口感成绩信息成功"),
            @ApiResponse(code = 201, message = "没有口感成绩信息")})
    public Result<Object> allTasteScoreInfo(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                            @PathVariable("competitionId") Integer competitionId,
                                            @ApiParam(name = "groupId", value = "小组Id", type = "Integer")
                                            @PathVariable("groupId") Integer groupId,
                                            @ApiParam(name = "crabSex", value = "性别，1:雄 2：雌", type = "Integer")
                                            @PathVariable("crabSex") Integer crabSex,
                                            @RequestHeader("jwt") String jwt,
                                            @ApiParam(name = "pageNum", value = "页数", type = "Integer")
                                            @PathVariable("pageNum") Integer pageNum,
                                            @ApiParam(name = "pageSize", value = "页面大小", type = "Integer")
                                            @PathVariable("pageSize") Integer pageSize) {
        List<TasteScore> tasteScoreList = this.tasteScoreService.selectByCompetitionIdAndGroupIdAndCrabSex(
                competitionId, groupId, crabSex, pageNum, pageSize);
        return tasteScoreList.size() == 0 ? new ResultUtil<>().setSuccessMsg(201, "没有口感成绩信息") :
                new ResultUtil<>().setData(tasteScoreList, "查询口感成绩信息成功");
    }

    @GetMapping(value = "/companies")
    @ApiOperation("查询所有参选单位")
    @ApiResponses({@ApiResponse(code = 200, message = "查询所有参选单位成功"),
            @ApiResponse(code = 201, message = "companyList列表为空")})
    public Result<Object> allCompany(@RequestHeader("jwt") String jwt) {
        List<Company> companyList = this.companyService.selectAllCompany();
        if (companyList.size() == 0) {
            return new ResultUtil<>().setSuccessMsg(201, "companyList列表为空");
        } else {
            return new ResultUtil<>().setData(companyList, "查询所有参选单位成功");
        }
    }

    @PutMapping("/user")
    @ApiOperation("将参选单位信息与参选单位用户绑定")
    @ApiResponses({@ApiResponse(code = 200, message = "绑定成功"),
            @ApiResponse(code = 500, message = "绑定失败"),
            @ApiResponse(code = 501, message = "用户Id为空"),
            @ApiResponse(code = 502, message = "参选单位Id为空")})
    public Result<Object> userBindCompany(@ApiParam(name = "userId", value = "用户Id", type = "Integer")
                                          @RequestParam Integer userId,
                                          @ApiParam(name = "companyId", value = "参选单位Id", type = "Integer")
                                          @RequestParam Integer companyId,
                                          @RequestHeader("jwt") String jwt) {
        if (userId == null || userId == 0) {
            return new ResultUtil<>().setErrorMsg(501, "用户Id为空");
        } else if (companyId == null || companyId == 0) {
            return new ResultUtil<>().setErrorMsg(502, "参选单位Id为空");
        } else {
            User user = new User();
            user.setUserId(userId);
            user.setCompanyId(companyId);
            return this.userService.updateByPrimaryKeySelective(user) <= 0 ?
                    new ResultUtil<>().setErrorMsg(500, "绑定失败") :
                    new ResultUtil<>().setSuccessMsg("绑定成功");
        }

    }

    @GetMapping(value = "/groups/{competitionId}/{companyId}/{pageNum}/{pageSize}")
    @ApiOperation("查询在某一届大赛中某一参选单位的所有组")
    @ApiResponses({@ApiResponse(code = 200, message = "查询所有比赛组成功"),
            @ApiResponse(code = 500, message = "该参选单位没有比赛组"),
            @ApiResponse(code = 501, message = "competitionId为空"),
            @ApiResponse(code = 502, message = "companyId为空")})
    public Result<Object> oneCompanyAllGroup(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                             @PathVariable("competitionId") Integer competitionId,
                                             @ApiParam(name = "companyId", value = "参选单位Id", type = "Integer")
                                             @PathVariable("companyId") Integer companyId,
                                             @RequestHeader("jwt") String jwt,
                                             @ApiParam(name = "pageNum", value = "页数", type = "Integer")
                                             @PathVariable("pageNum") Integer pageNum,
                                             @ApiParam(name = "pageSize", value = "页面大小", type = "Integer")
                                             @PathVariable("pageSize") Integer pageSize) {
        if (competitionId == null || competitionId <= 0) {
            return new ResultUtil<>().setErrorMsg(501, "competitionId为空");
        } else if (companyId == null || companyId <= 0) {
            return new ResultUtil<>().setErrorMsg(502, "companyId为空");
        } else {
            List<GroupResult> groupList = this.groupService.selectAllGroupOneCompetitionOneCompany(competitionId, companyId,
                    pageNum, pageSize);
            if (groupList.size() == 0) {
                return new ResultUtil<>().setErrorMsg(500, "该参选单位没有比赛组");
            } else {
                return new ResultUtil<>().setData(groupList, "查询所有比赛组成功");
            }
        }
    }

    @GetMapping(value = "/{groupId}/crabs/{competitionId}/{pageNum}/{pageSize}")
    @ApiOperation("查找一个组的所有螃蟹和螃蟹对应的评分")
    @ApiResponses({@ApiResponse(code = 200, message = "查询所有螃蟹和评分成功"),
            @ApiResponse(code = 201, message = "评分为空，没有查询结果"),
            @ApiResponse(code = 501, message = "competitionId为空"),
            @ApiResponse(code = 502, message = "groupId为空")})
    public Result<Object> oneGroupAllCrabAndScore(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                                  @PathVariable("competitionId") Integer competitionId,
                                                  @ApiParam(name = "groupId", value = "小组Id", type = "Integer")
                                                  @PathVariable("groupId") Integer groupId,
                                                  @RequestHeader("jwt") String jwt,
                                                  @ApiParam(name = "pageNum", value = "页数", type = "Integer")
                                                  @PathVariable("pageNum") Integer pageNum,
                                                  @ApiParam(name = "pageSize", value = "页面大小", type = "Integer")
                                                  @PathVariable("pageSize") Integer pageSize) {
        if (competitionId == null || competitionId == 0) {
            return new ResultUtil<>().setErrorMsg(501, "competitionId为空");
        } else if (groupId == null || groupId == 0) {
            return new ResultUtil<>().setErrorMsg(502, "groupId为空");
        } else {
            List<CrabScoreResult> crabScoreResults = this.crabService.selectOneGroupAllCrabAndScore(competitionId, groupId, pageNum, pageSize);
            return crabScoreResults.size() > 0 ? new ResultUtil<>().setData(crabScoreResults, "查询所有螃蟹和评分成功") :
                    new ResultUtil<>().setSuccessMsg(201, "评分为空，没有查询结果");
        }
    }
}
