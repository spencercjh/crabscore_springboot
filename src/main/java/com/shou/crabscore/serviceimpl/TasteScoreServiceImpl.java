package com.shou.crabscore.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.shou.crabscore.dao.TasteScoreMapper;
import com.shou.crabscore.entity.TasteScore;
import com.shou.crabscore.service.TasteScoreService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 口感分数接口实现
 *
 * @author spencercjh
 */
@Log4j2
@Service
@CacheConfig(cacheNames = "TasteScoreServiceCache")
public class TasteScoreServiceImpl implements TasteScoreService {
    private final TasteScoreMapper tasteScoreMapper;

    @Autowired
    public TasteScoreServiceImpl(TasteScoreMapper tasteScoreMapper) {
        this.tasteScoreMapper = tasteScoreMapper;
    }

    @Cacheable(key = "#competitionId")
    @Override
    public List<TasteScore> selectByCompetitionIdAndGroupId(Integer competitionId, Integer groupId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return tasteScoreMapper.selectByCompetitionIdAndGroupId(competitionId, groupId);
    }

    @Cacheable(key = "#competitionId")
    @Override
    public List<TasteScore> selectByCompetitionIdAndGroupIdAndCrabSex(Integer competitionId, Integer groupId, Integer crabSex, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return tasteScoreMapper.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
    }

    @CacheEvict(key = "#scoreId")
    @Override
    public int deleteByPrimaryKey(Integer scoreId) {
        return tasteScoreMapper.deleteByPrimaryKey(scoreId);
    }

    @CachePut(key = "#record.scoreId")
    @Override
    public int insert(TasteScore record) {
        return tasteScoreMapper.insert(record);
    }

    @CachePut(key = "#record.scoreId")
    @Override
    public int insertSelective(TasteScore record) {
        return tasteScoreMapper.insertSelective(record);
    }

    @Cacheable(key = "#scoreId")
    @Override
    public TasteScore selectByPrimaryKey(Integer scoreId) {
        return tasteScoreMapper.selectByPrimaryKey(scoreId);
    }

    @CacheEvict(key = "#record.scoreId")
    @Override
    public int updateByPrimaryKeySelective(TasteScore record) {
        return tasteScoreMapper.updateByPrimaryKeySelective(record);
    }

    @CacheEvict(key = "#record.scoreId")
    @Override
    public int updateByPrimaryKey(TasteScore record) {
        return tasteScoreMapper.updateByPrimaryKey(record);
    }
}
