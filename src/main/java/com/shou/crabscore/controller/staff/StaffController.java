package com.shou.crabscore.controller.staff;

import cn.hutool.core.util.StrUtil;
import com.shou.crabscore.common.constant.CommonConstant;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.Crab;
import com.shou.crabscore.entity.QualityScore;
import com.shou.crabscore.entity.TasteScore;
import com.shou.crabscore.entity.dto.CrabResult;
import com.shou.crabscore.entity.dto.GroupResult;
import com.shou.crabscore.service.CrabService;
import com.shou.crabscore.service.GroupService;
import com.shou.crabscore.service.QualityScoreService;
import com.shou.crabscore.service.TasteScoreService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工作人员接口
 *
 * @author spencercjh
 */
@SuppressWarnings("unused")
@Log4j2
@Api(description = "工作人员用户组-工作人员接口")
@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final CrabService crabService;
    private final GroupService groupService;
    private final TasteScoreService tasteScoreService;
    private final QualityScoreService qualityScoreService;

    @Autowired
    public StaffController(CrabService crabService, GroupService groupService, TasteScoreService tasteScoreService, QualityScoreService qualityScoreService) {
        this.crabService = crabService;
        this.groupService = groupService;
        this.tasteScoreService = tasteScoreService;
        this.qualityScoreService = qualityScoreService;
    }

    @DeleteMapping("/crab/{crabId}")
    @ApiOperation("删除螃蟹信息")
    @ApiResponses({@ApiResponse(code = 200, message = "删除螃蟹信息成功"),
            @ApiResponse(code = 500, message = "删除螃蟹信息失败"),
            @ApiResponse(code = 501, message = "crabId为空")})
    public Result<Object> deleteCrabInfo(@ApiParam(name = "crabId", value = "螃蟹Id", type = "Integer")
                                         @PathVariable("crabId") Integer crabId,
                                         @RequestHeader("jwt") String jwt) {
        if (crabId == null || crabId <= 0) {
            return new ResultUtil<>().setErrorMsg(501, "crabId为空");
        } else {
            int deleteResult = this.crabService.deleteByPrimaryKey(crabId);
            return (deleteResult <= 0) ? new ResultUtil<>().setErrorMsg("删除螃蟹信息失败") :
                    new ResultUtil<>().setSuccessMsg("删除螃蟹信息成功");
        }
    }

    @PostMapping(value = "/crab", consumes = "application/json")
    @ApiOperation("插入螃蟹信息")
    @ApiResponses({@ApiResponse(code = 200, message = "插入螃蟹信息成功"),
            @ApiResponse(code = 500, message = "插入螃蟹信息失败"),
            @ApiResponse(code = 501, message = "crabId为空")})
    public Result<Object> insertCrabInfo(@ApiParam(name = "crabInfo", value = "螃蟹信息Json", type = "String")
                                         @RequestBody Crab crab,
                                         @RequestHeader("jwt") String jwt) {
        int result = this.crabService.insertSelective(crab);
        return result <= 0 ? new ResultUtil<>().setErrorMsg("插入螃蟹信息失败") :
                new ResultUtil<>().setData(crab.getCrabId(), "插入螃蟹信息成功");

    }

    @GetMapping("/crabs/{competitionId}/{groupId}/{crabSex}/{pageNum}/{pageSize}")
    @ApiOperation("查询某一年某一组某一性别的螃蟹信息")
    @ApiResponses({@ApiResponse(code = 200, message = "查询螃蟹信息成功"),
            @ApiResponse(code = 201, message = "crabList为空"),
            @ApiResponse(code = 501, message = "competitionId为空"),
            @ApiResponse(code = 502, message = "groupId为空"),
            @ApiResponse(code = 503, message = "crabSex为空")})
    public Result<Object> allCrab(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
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
        if (competitionId == null || competitionId <= 0) {
            return new ResultUtil<>().setErrorMsg(501, "competitionId为空");
        } else if (groupId == null || groupId <= 0) {
            return new ResultUtil<>().setErrorMsg(502, "groupId为空");
        } else if (CommonConstant.CRAB_MALE != crabSex &&
                CommonConstant.CRAB_FEMALE != crabSex) {
            return new ResultUtil<>().setErrorMsg(503, "crabSex为空或非法");
        } else {
            List<Crab> crabList = this.crabService.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId,
                    crabSex, pageNum, pageSize);
            return crabList.size() == 0 ? new ResultUtil<>().setSuccessMsg(201, "crabList为空") :
                    new ResultUtil<>().setData(crabList, "查询螃蟹信息成功");
        }
    }

    @PutMapping(value = "/crab", consumes = "application/json")
    @ApiOperation(value = "更新螃蟹信息", notes = "这里应该在Android部分就把肥满度算好")
    @ApiResponses({@ApiResponse(code = 200, message = "更新螃蟹信息成功"),
            @ApiResponse(code = 500, message = "更新螃蟹信息失败"),
            @ApiResponse(code = 501, message = "crabId为空")})
    public Result<Object> updateCrabInfo(@ApiParam(name = "crabInfo", value = "螃蟹信息Json", type = "Crab")
                                         @RequestBody Crab crab,
                                         @RequestHeader("jwt") String jwt) {
        if (crab.getCrabId() == null || crab.getCrabId() <= 0) {
            return new ResultUtil<>().setErrorMsg(501, "crabId为空");
        } else {
            return this.crabService.updateByPrimaryKey(crab) <= 0 ? new ResultUtil<>().setErrorMsg("更新螃蟹信息失败") :
                    new ResultUtil<>().setSuccessMsg("更新螃蟹信息成功");
        }
    }

    @GetMapping("/crab/{label}")
    @ApiOperation("根据标签查找螃蟹信息")
    @ApiResponses({@ApiResponse(code = 200, message = "查找螃蟹信息成功"),
            @ApiResponse(code = 500, message = "查找螃蟹信息失败"),
            @ApiResponse(code = 501, message = "label为空")})
    public Result<Object> searchLabel(@ApiParam(name = "label", value = "标签", type = "String")
                                      @PathVariable("label") String label,
                                      @RequestHeader("jwt") String jwt) {
        if (!StrUtil.isNotBlank(label)) {
            return new ResultUtil<>().setErrorMsg(501, "label为空");
        } else {
            CrabResult crab = this.crabService.selectByLabel(label);
            return crab.getCrabId() <= 0 ? new ResultUtil<>().setErrorMsg("查找螃蟹信息失败") :
                    new ResultUtil<>().setData(crab, "查找螃蟹信息成功");
        }
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
            List<GroupResult> groupList = this.groupService.selectAllGroupOneCompetition(competitionId, pageNum, pageSize);
            if (groupList.size() == 0) {
                return new ResultUtil<>().setSuccessMsg(201, "没有比赛组");
            } else {
                return new ResultUtil<>().setData(groupList, "查询所有比赛组成功");
            }
        }
    }

    @PostMapping(value = "/crabs", consumes = "application/json")
    @ApiOperation("批量插入螃蟹信息和评分信息")
    @ApiResponses({@ApiResponse(code = 200, message = "批量插入螃蟹信息和评分信息成功"),
            @ApiResponse(code = 500, message = "批量插入螃蟹信息和评分信息失败"),
            @ApiResponse(code = 501, message = "crabList为空")})
    public Result<Object> insertCrabInfoList(@ApiParam(name = "crabList", value = "螃蟹信息对象List", type = "List")
                                             @RequestBody List<Crab> crabList,
                                             @RequestHeader("jwt") String jwt) {
        if (crabList == null || crabList.isEmpty()) {
            return new ResultUtil<>().setErrorMsg(501, "crabList为空");
        } else {
            int failCount = 0;
            for (Crab crab : crabList) {
                if (this.crabService.insertSelective(crab) <= 0) {
                    failCount++;
                } else {
                    if (!crabService.insertCrabAttachScoreInfo(crab)) {
                        failCount++;
                    }
                }
            }
            return failCount == 0 ? new ResultUtil<>().setSuccessMsg("批量插入螃蟹信息和评分信息成功") :
                    new ResultUtil<>().setData(failCount, "批量插入螃蟹信息失败", 500, false);
        }
    }

    @PostMapping(value = "/tastes", consumes = "application/json")
    @ApiOperation("批量插入口感得分信息")
    @ApiResponses({@ApiResponse(code = 200, message = "批量插入口感得分信息成功"),
            @ApiResponse(code = 500, message = "批量插入口感得分信息失败"),
            @ApiResponse(code = 501, message = "tasteScoreList为空")})
    public Result<Object> insertTasteScoreList(@ApiParam(name = "tasteScoreList", value = "口感得分信息对象List", type = "List")
                                               @RequestBody List<TasteScore> tasteScoreList,
                                               @RequestHeader("jwt") String jwt) {
        if (tasteScoreList == null || tasteScoreList.isEmpty()) {
            return new ResultUtil<>().setErrorMsg(501, "tasteScoreList为空");
        } else {
            int failCount = 0;
            for (TasteScore tasteScore : tasteScoreList) {
                if (this.tasteScoreService.insertSelective(tasteScore) <= 0) {
                    failCount++;
                }
            }
            return failCount == 0 ? new ResultUtil<>().setSuccessMsg("批量插入口感得分信息成功") :
                    new ResultUtil<>().setData(failCount, "批量插入口感得分信息失败", 500, false);
        }
    }

    @PostMapping(value = "/qualities", consumes = "application/json")
    @ApiOperation("批量插入种质得分信息")
    @ApiResponses({@ApiResponse(code = 200, message = "批量插入种质得分信息成功"),
            @ApiResponse(code = 500, message = "批量插入种质得分信息失败"),
            @ApiResponse(code = 501, message = "qualityScoreList为空")})
    public Result<Object> insertQualityScoreList(@ApiParam(name = "qualityScoreList", value = "种质得分信息对象List", type = "List")
                                                 @RequestBody List<QualityScore> qualityScoreList,
                                                 @RequestHeader("jwt") String jwt) {
        if (qualityScoreList == null || qualityScoreList.isEmpty()) {
            return new ResultUtil<>().setErrorMsg(501, "qualityScoreList为空");
        } else {
            int failCount = 0;
            for (QualityScore qualityScore : qualityScoreList) {
                if (this.qualityScoreService.insertSelective(qualityScore) <= 0) {
                    failCount++;
                }
            }
            return failCount == 0 ? new ResultUtil<>().setSuccessMsg("批量插入种质得分信息成功") :
                    new ResultUtil<>().setData(failCount, "批量插入种质得分信息失败", 500, false);
        }
    }
}
