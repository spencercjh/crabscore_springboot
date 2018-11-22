package com.shou.crabscore.serviceimpl;

import com.shou.crabscore.dao.CompetitionMapper;
import com.shou.crabscore.entity.Competition;
import com.shou.crabscore.service.CompetitionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 大赛信息接口实现
 *
 * @author spencercjh
 */
@Log4j2
@Service
@CacheConfig(cacheNames = "CompetitionServiceCache")
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionMapper competitionMapper;

    @Autowired
    public CompetitionServiceImpl(CompetitionMapper competitionMapper) {
        this.competitionMapper = competitionMapper;
    }

    @Cacheable
    @Override
    public List<Competition> selectAllCompetition() {
        return competitionMapper.selectAllCompetition();
    }

    @CacheEvict(key = "#competitionId")
    @Override
    public int deleteByPrimaryKey(Integer competitionId) {
        return competitionMapper.deleteByPrimaryKey(competitionId);
    }

    @CachePut(key = "#record.competitionId")
    @Override
    public int insert(Competition record) {
        return competitionMapper.insert(record);
    }

    @CachePut(key = "#record.competitionId")
    @Override
    public int insertSelective(Competition record) {
        return competitionMapper.insertSelective(record);
    }

    @Cacheable(key = "#competitionId")
    @Override
    public Competition selectByPrimaryKey(Integer competitionId) {
        return competitionMapper.selectByPrimaryKey(competitionId);
    }

    @CacheEvict(key = "#record.competitionId")
    @Override
    public int updateByPrimaryKeySelective(Competition record) {
        return competitionMapper.updateByPrimaryKeySelective(record);
    }

    @CacheEvict(key = "#record.competitionId")
    @Override
    public int updateByPrimaryKey(Competition record) {
        return competitionMapper.updateByPrimaryKey(record);
    }
}
