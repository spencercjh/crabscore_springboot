package com.shou.crabscore.controller.staff;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.Crab;
import com.shou.crabscore.service.CrabService;
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
@Api(description = "工作人员接口")
@RestController
@RequestMapping("/api/staff")
public class CrabAdminController {

    private final CrabService crabService;

    @Autowired
    public CrabAdminController(CrabService crabService) {
        this.crabService = crabService;
    }

    @DeleteMapping("/crab/{crabId}")
    @ApiOperation("删除螃蟹信息")
    @ApiResponses({@ApiResponse(code = 200, message = "删除螃蟹信息成功"),
            @ApiResponse(code = 500, message = "删除螃蟹信息失败"),
            @ApiResponse(code = 501, message = "crabId为空")})
    public Result<Object> deleteCrabInfo(@ApiParam(name = "crabId", value = "螃蟹Id", type = "Integer")
                                         @PathVariable("crabId") Integer crabId, @RequestHeader("jwt") String jwt) {
        if (CharUtil.isBlankChar(crabId)) {
            return new ResultUtil<>().setErrorMsg(501, "crabId为空");
        } else {
            int deleteResult = this.crabService.deleteByPrimaryKey(crabId);
            return (deleteResult <= 0) ? new ResultUtil<>().setErrorMsg("删除螃蟹信息失败") :
                    new ResultUtil<>().setSuccessMsg("删除螃蟹信息成功");
        }
    }

    @PostMapping("/crab")
    @ApiOperation("插入螃蟹信息")
    @ApiResponses({@ApiResponse(code = 200, message = "插入螃蟹信息成功"),
            @ApiResponse(code = 500, message = "插入螃蟹信息失败"),
            @ApiResponse(code = 501, message = "crabId为空")})
    public Result<Object> insertCrabInfo(@ApiParam(name = "crabInfo", value = "螃蟹信息Json", type = "String")
                                         @RequestBody Crab crab, @RequestHeader("jwt") String jwt) {
        if (CharUtil.isBlankChar(crab.getCrabId())) {
            return new ResultUtil<>().setErrorMsg(501, "crabId为空");
        } else {
            return this.crabService.insert(crab) <= 0 ? new ResultUtil<>().setErrorMsg("插入螃蟹信息失败") :
                    new ResultUtil<>().setSuccessMsg("插入螃蟹信息成功");
        }
    }

    @GetMapping("/crabs/{competitionId}/{groupId}/{crabSex}")
    @ApiOperation("查询某一年某一组某一性别的螃蟹信息")
    @ApiResponses({@ApiResponse(code = 200, message = "查询螃蟹信息成功"),
            @ApiResponse(code = 201, message = "crabList为空"),
            @ApiResponse(code = 501, message = "competitionId或groupId或crabSex为空")})
    public Result<Object> allCrab(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                  @PathVariable("competitionId") Integer competitionId,
                                  @ApiParam(name = "groupId", value = "小组Id", type = "Integer")
                                  @PathVariable("groupId") Integer groupId,
                                  @ApiParam(name = "crabSex", value = "性别，1:雄 2：雌", type = "Integer")
                                  @PathVariable("crabSex") Integer crabSex, @RequestHeader("jwt") String jwt) {
        if (CharUtil.isBlankChar(competitionId) || CharUtil.isBlankChar(groupId) || CharUtil.isBlankChar(crabSex)) {
            return new ResultUtil<>().setErrorMsg(501, "competitionId或groupId或crabSex为空");
        } else {
            List<Crab> crabList = this.crabService.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
            return crabList.size() == 0 ? new ResultUtil<>().setSuccessMsg(201, "crabList为空") :
                    new ResultUtil<>().setData(crabList, "查询螃蟹信息成功");
        }
    }

    @PutMapping("/crab")
    @ApiOperation(value = "更新螃蟹信息", notes = "这里应该在Android部分就把肥满度算好")
    @ApiResponses({@ApiResponse(code = 200, message = "更新螃蟹信息成功"),
            @ApiResponse(code = 500, message = "更新螃蟹信息失败"),
            @ApiResponse(code = 501, message = "crabId为空")})
    public Result<Object> updateCrabInfo(@ApiParam(name = "crabInfo", value = "螃蟹信息Json", type = "String")
                                         @RequestBody Crab crab, @RequestHeader("jwt") String jwt) {
        if (CharUtil.isBlankChar(crab.getCrabId())) {
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
                                      @PathVariable("label") String label, @RequestHeader("jwt") String jwt) {
        if (StrUtil.isNotBlank(label)) {
            return new ResultUtil<>().setErrorMsg(501, "label为空");
        } else {
            Crab crab = this.crabService.selectByLabel(label);
            return CharUtil.isBlankChar(crab.getCrabId()) ? new ResultUtil<>().setErrorMsg("查找螃蟹信息失败") :
                    new ResultUtil<>().setData(crab, "查找螃蟹信息成功");
        }
    }
}
