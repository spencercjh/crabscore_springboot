package com.shou.crabscore.controller.admin;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.Company;
import com.shou.crabscore.entity.Group;
import com.shou.crabscore.service.CompanyService;
import com.shou.crabscore.service.GroupService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参选单位后台管理接口
 *
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "参选单位后台管理接口")
@RequestMapping("/api/admin/company")
public class CompanyAdminController {
    private final CompanyService companyService;
    private final GroupService groupService;

    @Autowired
    public CompanyAdminController(CompanyService companyService, GroupService groupService) {
        this.companyService = companyService;
        this.groupService = groupService;
    }

    @GetMapping(value = "/{companyId}")
    @ApiOperation("查询单个参选单位")
    @ApiResponses({@ApiResponse(code = 200, message = "查找单个参选单位成功"),
            @ApiResponse(code = 501, message = "companyId为空"),
            @ApiResponse(code = 502, message = "查找单个参选单位失败")})
    public Result<Object> singleCompany(@ApiParam(name = "companyId", value = "参选单位Id", type = "Integer")
                                        @PathVariable("companyId") Integer companyId) {
        if (NumberUtil.isBlankChar(companyId)) {
            return new ResultUtil<>().setErrorMsg(501, "companyId为空");
        } else {
            Company company = this.companyService.selectByPrimaryKey(companyId);
            return StrUtil.isNotBlank(company.getCompanyName()) ?
                    (new ResultUtil<>().setData(company, "查找单个参选单位成功")) :
                    (new ResultUtil<>().setErrorMsg(502, "查找单个参选单位失败"));
        }
    }

    @GetMapping(value = "/allcompany")
    @ApiOperation("查询所有参选单位")
    @ApiResponses({@ApiResponse(code = 200, message = "查询所有参选单位成功"),
            @ApiResponse(code = 201, message = "companyList列表为空")})
    public Result<Object> allCompany() {
        List<Company> companyList = this.companyService.selectAllCompany();
        if (companyList.size() == 0) {
            return new ResultUtil<>().setSuccessMsg(201, "companyList列表为空");
        } else {
            return new ResultUtil<>().setData(companyList, "查询所有参选单位成功");
        }
    }

    @GetMapping(value = "/group/{competitionId}/{companyId}")
    @ApiOperation("查询在某一届大赛中某一参选单位的所有组")
    @ApiResponses({@ApiResponse(code = 200, message = "查询所有比赛组成功"),
            @ApiResponse(code = 200, message = "查询所有比赛组成功"),
            @ApiResponse(code = 201, message = "groupList为空"),
            @ApiResponse(code = 501, message = "competitionId或者companyId为空")})
    public Result<Object> allGroup(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                   @PathVariable("competitionId") Integer competitionId,
                                   @ApiParam(name = "companyId", value = "参选单位Id", type = "Integer")
                                   @PathVariable("companyId") Integer companyId) {
        if (NumberUtil.isBlankChar(competitionId) || NumberUtil.isBlankChar(companyId)) {
            return new ResultUtil<>().setErrorMsg(501, "competitionId或者companyId为空");
        } else {
            List<Group> groupList = this.groupService.selectAllGroupOneCompetitionOneCompany(competitionId, companyId);
            if (groupList.size() == 0) {
                return new ResultUtil<>().setSuccessMsg(201, "groupList为空");
            } else {
                return new ResultUtil<>().setData(groupList, "查询所有比赛组成功");
            }
        }
    }

    @PutMapping(value = "/property")
    @ApiOperation("修改参选单位资料")
    @ApiImplicitParam(name = "company", value = "单个参选单位信息", dataType = "Company")
    @ApiResponses({@ApiResponse(code = 200, message = "修改参选单位资料成功"),
            @ApiResponse(code = 500, message = "修改参选单位资料失败"),
            @ApiResponse(code = 501, message = "主键CompanyId为空")})
    public Result<Object> updateCompanyProperty(@ApiParam("参选单位信息Json") @RequestBody Company company) {
        if (NumberUtil.isBlankChar(company.getCompanyId())) {
            return new ResultUtil<>().setErrorMsg(501, "主键CompanyId为空");
        } else {
            int updateResult = this.companyService.updateByPrimaryKeySelective(company);
            return (updateResult <= 0) ? new ResultUtil<>().setErrorMsg("修改参选单位资料失败") :
                    new ResultUtil<>().setSuccessMsg("修改参选单位资料成功");
        }
    }

    @PostMapping(value = "/creation")
    @ApiOperation("创建参选单位")
    @ApiImplicitParam(name = "company", value = "单个参选单位信息", dataType = "Company")
    @ApiResponses({@ApiResponse(code = 200, message = "创建参选单位成功"),
            @ApiResponse(code = 500, message = "创建参选单位失败"),
            @ApiResponse(code = 501, message = "主键CompanyId为空为空")})
    public Result<Object> insertCompanyProperty(@ApiParam("参选单位信息Json") @RequestBody Company company) {
        if (NumberUtil.isBlankChar(company.getCompanyId())) {
            return new ResultUtil<>().setErrorMsg(501, "主键CompanyId为空为空");
        } else {
            int updateResult = this.companyService.insert(company);
            return (updateResult <= 0) ? new ResultUtil<>().setErrorMsg("创建参选单位失败") :
                    new ResultUtil<>().setSuccessMsg("创建参选单位成功");
        }
    }

    @DeleteMapping(value = "/{companyId}")
    @ApiOperation("删除参选单位")
    @ApiResponses({@ApiResponse(code = 200, message = "删除参选单位成功"),
            @ApiResponse(code = 500, message = "删除参选单位失败"),
            @ApiResponse(code = 501, message = "companyId为空")})
    public Result<Object> deleteCompany(@ApiParam(name = "companyId", value = "参选单位Id", type = "Integer")
                                        @PathVariable("companyId") Integer companyId) {
        if (NumberUtil.isBlankChar(companyId)) {
            return new ResultUtil<>().setErrorMsg("companyId为空");
        } else {
            int deleteResult = this.companyService.deleteByPrimaryKey(companyId);
            return (deleteResult <= 0) ? new ResultUtil<>().setErrorMsg("删除参选单位失败") :
                    new ResultUtil<>().setSuccessMsg("删除参选单位成功");
        }
    }
}