package com.shou.crabscore.serviceimpl;

import com.shou.crabscore.dao.QualityScoreMapper;
import com.shou.crabscore.entity.QualityScore;
import com.shou.crabscore.service.QualityScoreService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 种质分数接口实现
 *
 * @author spencercjh
 */
@Log4j2
@Service
public class QualityScoreServiceImpl implements QualityScoreService {
    private final QualityScoreMapper qualityScoreMapper;

    @Autowired
    public QualityScoreServiceImpl(QualityScoreMapper qualityScoreMapper) {
        this.qualityScoreMapper = qualityScoreMapper;
    }

    @Override
    public List<QualityScore> selectByCompetitionIdAndGroupIdAndCrabSex(Integer competitionId, Integer groupId, Integer crabSex) {
        return qualityScoreMapper.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
    }

    @Override
    public int deleteByPrimaryKey(Integer scoreId) {
        return qualityScoreMapper.deleteByPrimaryKey(scoreId);
    }

    @Override
    public int insert(QualityScore record) {
        return qualityScoreMapper.insert(record);
    }

    @Override
    public int insertSelective(QualityScore record) {
        return qualityScoreMapper.insertSelective(record);
    }

    @Override
    public QualityScore selectByPrimaryKey(Integer scoreId) {
        return qualityScoreMapper.selectByPrimaryKey(scoreId);
    }

    @Override
    public int updateByPrimaryKeySelective(QualityScore record) {
        return qualityScoreMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(QualityScore record) {
        return qualityScoreMapper.updateByPrimaryKey(record);
    }
}
