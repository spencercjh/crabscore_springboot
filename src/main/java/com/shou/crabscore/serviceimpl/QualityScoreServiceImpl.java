package com.shou.crabscore.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.shou.crabscore.dao.QualityScoreMapper;
import com.shou.crabscore.entity.QualityScore;
import com.shou.crabscore.service.QualityScoreService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 种质分数接口实现
 *
 * @author spencercjh
 */
@Log4j2
@Service
@CacheConfig(cacheNames = "QualityScoreServiceCache")
public class QualityScoreServiceImpl implements QualityScoreService {
    private final QualityScoreMapper qualityScoreMapper;

    @Autowired
    public QualityScoreServiceImpl(QualityScoreMapper qualityScoreMapper) {
        this.qualityScoreMapper = qualityScoreMapper;
    }

    @Cacheable(key = "#competitionId")
    @Override
    public List<QualityScore> selectByCompetitionIdAndGroupId(Integer competitionId, Integer groupId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return qualityScoreMapper.selectByCompetitionIdAndGroupId(competitionId, groupId);
    }

    @Cacheable(key = "#competitionId")
    @Override
    public List<QualityScore> selectByCompetitionIdAndGroupIdAndCrabSex(Integer competitionId, Integer groupId, Integer crabSex, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return qualityScoreMapper.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
    }

    @CacheEvict(key = "#scoreId")
    @Override
    public int deleteByPrimaryKey(Integer scoreId) {
        return qualityScoreMapper.deleteByPrimaryKey(scoreId);
    }

    @CachePut(key = "#record.scoreId")
    @Override
    public int insert(QualityScore record) {
        return qualityScoreMapper.insert(record);
    }

    @CachePut(key = "#record.scoreId")
    @Override
    public int insertSelective(QualityScore record) {
        return qualityScoreMapper.insertSelective(record);
    }

    @Cacheable(key = "#scoreId")
    @Override
    public QualityScore selectByPrimaryKey(Integer scoreId) {
        return qualityScoreMapper.selectByPrimaryKey(scoreId);
    }

    @CacheEvict(key = "#record.scoreId")
    @Override
    public int updateByPrimaryKeySelective(QualityScore record) {
        return qualityScoreMapper.updateByPrimaryKeySelective(record);
    }

    @CacheEvict(key = "#record.scoreId")
    @Override
    public int updateByPrimaryKey(QualityScore record) {
        return qualityScoreMapper.updateByPrimaryKey(record);
    }
}
