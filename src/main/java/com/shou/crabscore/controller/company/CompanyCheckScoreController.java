package com.shou.crabscore.controller.company;

import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.Company;
import com.shou.crabscore.entity.QualityScore;
import com.shou.crabscore.entity.TasteScore;
import com.shou.crabscore.entity.User;
import com.shou.crabscore.service.CompanyService;
import com.shou.crabscore.service.QualityScoreService;
import com.shou.crabscore.service.TasteScoreService;
import com.shou.crabscore.service.UserService;
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

    @Autowired
    public CompanyCheckScoreController(QualityScoreService qualityScoreService, TasteScoreService tasteScoreService, CompanyService companyService, UserService userService) {
        this.qualityScoreService = qualityScoreService;
        this.tasteScoreService = tasteScoreService;
        this.companyService = companyService;
        this.userService = userService;
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

    @GetMapping("/{companyId}")
    @ApiOperation("查询一个用户绑定的所有参选单位")
    @ApiResponses({@ApiResponse(code = 200, message = "查询绑定参选单位信息成功"),
            @ApiResponse(code = 500, message = "没有绑定的参选单位信息")})
    public Result<Object> oneUserAllCompany(
            @ApiParam(name = "companyId", value = "参选单位Id", type = "Integer")
            @PathVariable("companyId") Integer companyId,
            @RequestHeader("jwt") String jwt) {
        Company company = this.companyService.selectByPrimaryKey(companyId);
        return company == null ? new ResultUtil<>().setSuccessMsg(500, "没有绑定的参选单位信息") :
                new ResultUtil<>().setData(company, "查询绑定参选单位信息成功");
    }

    @PutMapping("/user")
    @ApiOperation("将参选单位信息与参选单位用户绑定")
    @ApiResponses({@ApiResponse(code = 200, message = "绑定成功"),
            @ApiResponse(code = 500, message = "绑定失败")})
    public Result<Object> bindUserCompany(@ApiParam(name = "userId", value = "用户Id", type = "Integer")
                                          @RequestParam Integer userId,
                                          @ApiParam(name = "companyId", value = "参选单位Id", type = "Integer")
                                          @RequestParam Integer companyId,
                                          @RequestHeader("jwt") String jwt) {
        User user = new User();
        user.setUserId(userId);
        user.setCompanyId(companyId);
        return this.userService.updateByPrimaryKeySelective(user) <= 0 ?
                new ResultUtil<>().setErrorMsg(500, "绑定失败") :
                new ResultUtil<>().setSuccessMsg("修改成功");
    }
}
