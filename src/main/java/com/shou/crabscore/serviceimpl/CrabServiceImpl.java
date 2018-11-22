package com.shou.crabscore.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.shou.crabscore.dao.CrabMapper;
import com.shou.crabscore.entity.Crab;
import com.shou.crabscore.entity.vo.CrabResult;
import com.shou.crabscore.entity.vo.CrabScoreResult;
import com.shou.crabscore.service.CrabService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 螃蟹接口实现
 *
 * @author spencercjh
 */
@Log4j2
@Service
@CacheConfig(cacheNames = "CrabServiceCache")
public class CrabServiceImpl implements CrabService {

    private final CrabMapper crabMapper;

    @Autowired
    public CrabServiceImpl(CrabMapper crabMapper) {
        this.crabMapper = crabMapper;
    }

    @Cacheable(key = "#groupId")
    @Override
    public List<CrabScoreResult> selectOneGroupAllCrabAndScore(Integer competitionId, Integer groupId,
                                                               int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return crabMapper.selectOneGroupAllCrabAndScore(competitionId, groupId);
    }

    @Cacheable(key = "#groupId")
    @Override
    public Float sdFatnessByCompetitionIdAndGroupIdAndCrabSex(Integer competitionId, Integer groupId, Integer crabSex) {
        return crabMapper.sdFatnessByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
    }

    @Cacheable(key = "#groupId")
    @Override
    public Float sdWeightByCompetitionIdAndGroupIdAndCrabSex(Integer competitionId, Integer groupId, Integer crabSex) {
        return crabMapper.sdWeightByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
    }

    @Cacheable(key = "#groupId")
    @Override
    public Float averageFatnessByCompetitionIdAndGroupIdAndCrabSex(Integer competitionId, Integer groupId, Integer crabSex) {
        return crabMapper.averageFatnessByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
    }

    @Cacheable(key = "#groupId")
    @Override
    public Float averageWeightByCompetitionIdAndGroupIdAndCrabSex(Integer competitionId, Integer groupId, Integer crabSex) {
        return crabMapper.averageWeightByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
    }

    @CacheEvict(key = "#competitionId")
    @Override
    public int updateCrabFatness(Integer competitionId, Float varFatnessM, Float varFatnessF) {
        return crabMapper.updateCrabFatness(competitionId, varFatnessM, varFatnessF);
    }

    @Cacheable(key = "#label")
    @Override
    public CrabResult selectByLabel(String label) {
        return crabMapper.selectByLabel(label);
    }

    @Cacheable(key = "#groupId")
    @Override
    public List<Crab> selectByCompetitionIdAndGroupIdAndCrabSex(Integer competitionId, Integer groupId, Integer crabSex,
                                                                int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return crabMapper.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
    }

    @CacheEvict(key = "#crabId")
    @Override
    public int deleteByPrimaryKey(Integer crabId) {
        return crabMapper.deleteByPrimaryKey(crabId);
    }

    @CachePut(key = "#record.crabId")
    @Override
    public int insert(Crab record) {
        return crabMapper.insert(record);
    }

    @CachePut(key = "#record.crabId")
    @Override
    public int insertSelective(Crab record) {
        return crabMapper.insertSelective(record);
    }

    @Cacheable(key = "#crabId")
    @Override
    public Crab selectByPrimaryKey(Integer crabId) {
        return crabMapper.selectByPrimaryKey(crabId);
    }

    @CacheEvict(key = "#record.crabId")
    @Override
    public int updateByPrimaryKeySelective(Crab record) {
        return crabMapper.updateByPrimaryKeySelective(record);
    }

    @CacheEvict(key = "#record.crabId")
    @Override
    public int updateByPrimaryKey(Crab record) {
        return crabMapper.updateByPrimaryKey(record);
    }
}
