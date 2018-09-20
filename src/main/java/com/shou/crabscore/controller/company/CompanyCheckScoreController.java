package com.shou.crabscore.controller.company;

import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.entity.QualityScore;
import com.shou.crabscore.entity.TasteScore;
import com.shou.crabscore.service.GroupService;
import com.shou.crabscore.service.QualityScoreService;
import com.shou.crabscore.service.TasteScoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 参选单位查分接口
 *
 * @author spencercjh
 */
@Log4j2
@RestController
@Api(description = "参选单位查分接口")
@RequestMapping("/api/company/score")
public class CompanyCheckScoreController {
    private final QualityScoreService qualityScoreService;
    private final TasteScoreService tasteScoreService;

    @Autowired
    public CompanyCheckScoreController(QualityScoreService qualityScoreService, TasteScoreService tasteScoreService) {
        this.qualityScoreService = qualityScoreService;
        this.tasteScoreService = tasteScoreService;
    }

    @GetMapping("/quality/{competitionId}/{groupId}/{crabSex}")
    @ApiOperation("查询某一年的某一组的某一性别的所有螃蟹种质成绩")
    public Result<Object> allQualityScoreInfo(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                              @PathVariable("competitionId") Integer competitionId,
                                              @ApiParam(name = "groupId", value = "小组Id", type = "Integer")
                                              @PathVariable("groupId") Integer groupId,
                                              @ApiParam(name = "crabSex", value = "性别，1:雄 2：雌", type = "Integer")
                                              @PathVariable("crabSex") Integer crabSex) {
        List<QualityScore> qualityScoreList = this.qualityScoreService.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
        return qualityScoreList.size() == 0 ? new ResultUtil<>().setErrorMsg("没有相关分数信息") : new ResultUtil<>().setData(qualityScoreList);
    }

    @GetMapping("/taste/{competitionId}/{groupId}/{crabSex}")
    @ApiOperation("查询某一年的某一组的某一性别的所有螃蟹口感成绩")
    public Result<Object> allTasteScoreInfo(@ApiParam(name = "competitionId", value = "大赛Id", type = "Integer")
                                            @PathVariable("competitionId") Integer competitionId,
                                            @ApiParam(name = "groupId", value = "小组Id", type = "Integer")
                                            @PathVariable("groupId") Integer groupId,
                                            @ApiParam(name = "crabSex", value = "性别，1:雄 2：雌", type = "Integer")
                                            @PathVariable("crabSex") Integer crabSex) {
        List<TasteScore> tasteScoreList = this.tasteScoreService.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
        return tasteScoreList.size() == 0 ? new ResultUtil<>().setErrorMsg("没有相关信息") : new ResultUtil<>().setData(tasteScoreList);
    }
}
