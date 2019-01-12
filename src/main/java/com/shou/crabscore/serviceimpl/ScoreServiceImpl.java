package com.shou.crabscore.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.shou.crabscore.common.constant.CommonConstant;
import com.shou.crabscore.dao.*;
import com.shou.crabscore.entity.Competition;
import com.shou.crabscore.entity.CompetitionConfig;
import com.shou.crabscore.entity.Group;
import com.shou.crabscore.entity.dto.GroupResult;
import com.shou.crabscore.service.ScoreService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author spencercjh
 */
@Log4j2
@Service
public class ScoreServiceImpl implements ScoreService {
    private final CompanyMapper companyMapper;
    private final CompetitionMapper competitionMapper;
    private final CompetitionConfigMapper competitionConfigMapper;
    private final GroupMapper groupMapper;
    private final QualityScoreMapper qualityScoreMapper;
    private final TasteScoreMapper tasteScoreMapper;
    private final CrabMapper crabMapper;

    @Autowired
    public ScoreServiceImpl(CompanyMapper companyMapper, CompetitionMapper competitionMapper, CompetitionConfigMapper competitionConfigMapper,
                            GroupMapper groupMapper, QualityScoreMapper qualityScoreMapper, TasteScoreMapper tasteScoreMapper, CrabMapper crabMapper) {
        this.companyMapper = companyMapper;
        this.competitionMapper = competitionMapper;
        this.competitionConfigMapper = competitionConfigMapper;
        this.groupMapper = groupMapper;
        this.qualityScoreMapper = qualityScoreMapper;
        this.tasteScoreMapper = tasteScoreMapper;
        this.crabMapper = crabMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean calculateAllFatnessScore(Integer competitionId, String username) {
        CompetitionConfig presentCompetitionConfig = competitionConfigMapper.selectByPrimaryKey(1);
        Competition presentCompetition = competitionMapper.selectByPrimaryKey(presentCompetitionConfig.getCompetitionId());
        List<GroupResult> allGroups = groupMapper.selectAllGroupOneCompetition(competitionId);
        allGroups.stream().mapToInt(GroupResult::getGroupId).forEach(groupId -> {
            if (!(calculateFatnessScore(competitionId, groupId, CommonConstant.CRAB_MALE, presentCompetition, username) &&
                    calculateFatnessScore(competitionId, groupId, CommonConstant.CRAB_FEMALE, presentCompetition, username))) {
                throw new RuntimeException("肥满度分数计算失败,groupId: " + groupId + "引起的回滚");
            }
        });
        return true;
    }

    private boolean calculateFatnessScore(int competitionId, int groupId, int sex, Competition presentCompetition, String username) {
        if (sex != CommonConstant.CRAB_FEMALE && sex != CommonConstant.CRAB_MALE) {
            return false;
        }
        Float averageFatness = crabMapper.
                averageFatnessByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, sex);
        Float averageWeight = crabMapper.
                averageWeightByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, sex);
        Float sdFatness = crabMapper.
                sdFatnessByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, sex);
        Float sdWeight = crabMapper.
                sdWeightByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, sex);
        if (null == averageFatness || 0 == averageFatness ||
                null == averageWeight || 0 == averageWeight ||
                null == sdFatness || 0 == sdFatness ||
                null == sdWeight || 0 == sdWeight) {
            return false;
        }
        Group group = new Group();
        group.setGroupId(groupId);
        if (sex == CommonConstant.CRAB_MALE) {
            //fixme formula to calculate fatness
            float fatnessScore = averageFatness * presentCompetition.getVarFatnessM() + averageWeight * presentCompetition.getVarWeightM() +
                    sdFatness * presentCompetition.getVarMfatnessSd() + sdWeight * presentCompetition.getVarMweightSd();
            group.setFatnessScoreM(fatnessScore);
        } else {
            //fixme formula to calculate fatness
            float fatnessScore = averageFatness * presentCompetition.getVarFatnessF() + averageWeight * presentCompetition.getVarWeightF() +
                    sdFatness * presentCompetition.getVarFfatnessSd() + sdWeight * presentCompetition.getVarFweightSd();
            group.setFatnessScoreF(fatnessScore);
        }
        group.setUpdateDate(new Date(System.currentTimeMillis()));
        group.setUpdateUser(username);
        int updateResult = groupMapper.updateByPrimaryKeySelective(group);
        return updateResult > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean calculateAllQualityScore(Integer competitionId, String username) {
        List<GroupResult> allGroups = groupMapper.selectAllGroupOneCompetition(competitionId);
        allGroups.stream().mapToInt(GroupResult::getGroupId).forEach(groupId -> {
            if (!(calculateQualityScore(competitionId, groupId, CommonConstant.CRAB_MALE, username) &&
                    calculateQualityScore(competitionId, groupId, CommonConstant.CRAB_FEMALE, username))) {
                throw new RuntimeException("种质分数计算失败,groupId: " + groupId + "引起的回滚");
            }
            //fixme formula to calculate quality score
        });
        return true;
    }

    private boolean calculateQualityScore(int competitionId, int groupId, int sex, String username) {
        if (sex != CommonConstant.CRAB_MALE && sex != CommonConstant.CRAB_FEMALE) {
            return false;
        }
        Float average = qualityScoreMapper.averageQualityScoreByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, sex);
        Float sd = qualityScoreMapper.sdQualityScoreByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, sex);
        if (null == average || 0 == average ||
                null == sd || 0 == sd) {
            return false;
        }
        Group group = new Group();
        group.setGroupId(groupId);
        if (sex == CommonConstant.CRAB_MALE) {
            //fixme formula to calculate qualityScore
            float qualityScore = 0;
            group.setQualityScoreM(qualityScore);
        } else {
            //fixme formula to calculate qualityScore
            float qualityScore = 0;
            group.setQualityScoreF(qualityScore);
        }
        group.setUpdateDate(new Date(System.currentTimeMillis()));
        group.setUpdateUser(username);
        int updateResult = groupMapper.updateByPrimaryKeySelective(group);
        return updateResult > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean calculateAllTasteScore(Integer competitionId, String username) {
        List<GroupResult> allGroups = groupMapper.selectAllGroupOneCompetition(competitionId);
        allGroups.stream().mapToInt(GroupResult::getGroupId).forEach(groupId -> {
            if (!(calculateTasteScore(competitionId, groupId, CommonConstant.CRAB_MALE, username) && calculateTasteScore(competitionId, groupId,
                    CommonConstant.CRAB_FEMALE, username))) {
                throw new RuntimeException("口感分数计算失败,groupId: " + groupId + "引起的回滚");
            }
        });
        return true;
    }

    private boolean calculateTasteScore(int competitionId, int groupId, int sex, String username) {
        if (sex != CommonConstant.CRAB_MALE && sex != CommonConstant.CRAB_FEMALE) {
            return false;
        }
        Float average = tasteScoreMapper.averageTasteScoreByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, sex);
        Float sd = tasteScoreMapper.sdTasteScoreByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, sex);
        if (null == average || 0 == average ||
                null == sd || 0 == sd) {
            return false;
        }
        Group group = new Group();
        group.setGroupId(groupId);
        if (sex == CommonConstant.CRAB_MALE) {
            //fixme formula to calculate tasteScore
            float tasteScore = 0;
            group.setTasteScoreM(tasteScore);
        } else {
            //fixme formula to calculate tasteScore
            float tasteScore = 0;
            group.setTasteScoreF(tasteScore);
        }
        group.setUpdateDate(new Date(System.currentTimeMillis()));
        group.setUpdateUser(username);
        int updateResult = groupMapper.updateByPrimaryKeySelective(group);
        return updateResult > 0;
    }

    @Override
    public Map<String, Object> getExcelData(Integer competitionId) {
        Map<String, Object> parameter = new HashMap<>(16);
        parameter.put("all_company", JSON.toJSONString(companyMapper.selectAllCompany()));
        List<GroupResult> allGroupList = groupMapper.selectAllGroupOneCompetition(competitionId);
        List<Map<String, Object>> groupList = new ArrayList<>(64);
        allGroupList.forEach(group -> {
            Map<String, Object> groupCrabMap = new HashMap<>(16);
            groupCrabMap.put("info", JSON.toJSONString(group));
            groupCrabMap.put("male_crab", JSON.toJSONString(crabMapper.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, group.getGroupId(),
                    CommonConstant.CRAB_MALE)));
            groupCrabMap.put("female_crab", JSON.toJSONString(crabMapper.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, group.getGroupId(),
                    CommonConstant.CRAB_FEMALE)));
            groupCrabMap.put("male_quality", JSON.toJSONString(qualityScoreMapper.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, group.getGroupId(),
                    CommonConstant.CRAB_MALE)));
            groupCrabMap.put("female_quality", JSON.toJSONString(qualityScoreMapper.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, group.getGroupId(),
                    CommonConstant.CRAB_FEMALE)));
            groupCrabMap.put("male_taste", JSON.toJSONString(tasteScoreMapper.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, group.getGroupId(),
                    CommonConstant.CRAB_MALE)));
            groupCrabMap.put("female_taste", JSON.toJSONString(tasteScoreMapper.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, group.getGroupId(),
                    CommonConstant.CRAB_FEMALE)));
            groupList.add(groupCrabMap);
        });
        parameter.put("all_group", JSON.toJSONString(groupList));
        parameter.put("rank_fatness", JSON.toJSONString(groupMapper.selectAllGroupOneCompetitionOrderByFatnessScore(competitionId)));
        parameter.put("rank_quality", JSON.toJSONString(groupMapper.selectAllGroupOneCompetitionOrderByQualityScore(competitionId)));
        parameter.put("rank_taste", JSON.toJSONString(groupMapper.selectAllGroupOneCompetitionOrderByTasteScore(competitionId)));
        return parameter;
    }
}
