package com.shou.crabscore.serviceimpl;

import com.shou.crabscore.common.constant.CommonConstant;
import com.shou.crabscore.dao.*;
import com.shou.crabscore.entity.Competition;
import com.shou.crabscore.entity.CompetitionConfig;
import com.shou.crabscore.entity.dto.GroupResult;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean calculateAllFatnessScore(Integer competitionId) {
        CompetitionConfig presentCompetitionConfig = this.competitionConfigMapper.selectByPrimaryKey(1);
        Competition presentCompetition = this.competitionMapper.selectByPrimaryKey(presentCompetitionConfig.getCompetitionId());
        List<GroupResult> allGroups = this.groupMapper.selectAllGroupOneCompetition(competitionId);
        allGroups.stream().mapToInt(GroupResult::getGroupId).forEach(groupId -> {
            if (!(calculateFatnessScore(competitionId, groupId, CommonConstant.CRAB_MALE, presentCompetition) && calculateFatnessScore(competitionId, groupId,
                    CommonConstant.CRAB_FEMALE, presentCompetition))) {
                throw new RuntimeException("肥满度成绩失败,groupId: " + groupId);
            }
        });
        return true;
    }

    private boolean calculateFatnessScore(int competitionId, int groupId, int sex, Competition presentCompetition) {
        Float averageFatness = this.crabMapper.
                averageFatnessByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, sex);
        Float averageWeight = this.crabMapper.
                averageWeightByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, sex);
        Float sdFatness = this.crabMapper.
                sdFatnessByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, sex);
        Float sdWeight = this.crabMapper.
                sdWeightByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, sex);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean calculateQualityScore(Integer competitionId) {
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean calculateTasteScore(Integer competitionId) {
        return true;
    }
}
