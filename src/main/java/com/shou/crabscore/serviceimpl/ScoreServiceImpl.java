package com.shou.crabscore.serviceimpl;

import com.shou.crabscore.common.util.ResultUtil;
import com.shou.crabscore.common.vo.Result;
import com.shou.crabscore.dao.*;
import com.shou.crabscore.entity.Competition;
import com.shou.crabscore.entity.CompetitionConfig;
import com.shou.crabscore.entity.Group;
import com.shou.crabscore.service.ScoreService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author spencercjh
 */
@Log4j2
@Service
@Transactional(rollbackFor = Exception.class)
public class ScoreServiceImpl implements ScoreService {
    private final CompetitionMapper competitionMapper;
    private final CompetitionConfigMapper competitionConfigMapper;
    private final GroupMapper groupMapper;
    private final QualityScoreMapper qualityScoreMapper;
    private final TasteScoreMapper tasteScoreMapper;
    private final CrabMapper crabMapper;

    @Autowired
    public ScoreServiceImpl(CompetitionMapper competitionMapper, CompetitionConfigMapper competitionConfigMapper,
                            GroupMapper groupMapper, QualityScoreMapper qualityScoreMapper, TasteScoreMapper tasteScoreMapper, CrabMapper crabMapper) {
        this.competitionMapper = competitionMapper;
        this.competitionConfigMapper = competitionConfigMapper;
        this.groupMapper = groupMapper;
        this.qualityScoreMapper = qualityScoreMapper;
        this.tasteScoreMapper = tasteScoreMapper;
        this.crabMapper = crabMapper;
    }

    /**
     * 默认认为所有的螃蟹的肥满度在插入的时候就已经计算好了，这个事务不应该被使用了
     *
     * @param competitionId 大赛Id
     * @return 更新记录条数
     * @throws Exception 数据库异常
     */
    @Override
    @Deprecated
    public Result<Object> calculateFatness(Integer competitionId) throws Exception {
        CompetitionConfig presentCompetitionConfig = this.competitionConfigMapper.selectByPrimaryKey(1);
        Competition presentCompetition = this.competitionMapper.selectByPrimaryKey(presentCompetitionConfig.getCompetitionId());
        int updateResult = this.crabMapper.updateCrabFatness(presentCompetition.getCompetitionId(),
                presentCompetition.getVarFatnessM(), presentCompetition.getVarFatnessF());
        if (updateResult <= 0) {
            throw new Exception("批量修改螃蟹肥满度事务失败");
        } else {
            return new ResultUtil<>().setSuccessMsg("批量修改螃蟹肥满度事务成功");
        }
    }

    @Override
    public Result<Object> calculateFatnessScore(Integer competitionId) {
        CompetitionConfig presentCompetitionConfig = this.competitionConfigMapper.selectByPrimaryKey(1);
        Competition presentCompetition = this.competitionMapper.selectByPrimaryKey(presentCompetitionConfig.getCompetitionId());
        List<Group> allGroups = this.groupMapper.selectAllGroupOneCompetition(competitionId);
        for (Group group : allGroups) {
            int groupId = group.getGroupId();
            Float averageMaleFatness = this.crabMapper.
                    averageFatnessByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, 1);
            Float averageFemaleFatness = this.crabMapper.
                    averageFatnessByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, 2);
            Float averageMaleWeight = this.crabMapper.
                    averageWeightByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, 1);
            Float averageFemaleWeight = this.crabMapper.
                    averageWeightByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, 2);
            Float sdMaleFatness = this.crabMapper.
                    sdFatnessByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, 1);
            Float sdFemaleFatness = this.crabMapper.
                    sdFatnessByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, 2);
            Float sdMaleWeight = this.crabMapper.
                    sdWeightByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, 1);
            Float sdFemaleWeight = this.crabMapper.
                    sdWeightByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, 2);
        }
        return new ResultUtil<>().setSuccessMsg("批量修改肥满度得分事务成功");
    }

    @Override
    public Result<Object> calculateQualityScore(Integer competitionId) {
        return new ResultUtil<>().setSuccessMsg("批量修改种质得分事务成功");
    }

    @Override
    public Result<Object> calculateTasteScore(Integer competitionId) {
        return new ResultUtil<>().setSuccessMsg("批量修改口感得分事务成功");
    }
}
