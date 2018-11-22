package com.shou.crabscore.serviceimpl;

import com.shou.crabscore.dao.CompetitionConfigMapper;
import com.shou.crabscore.entity.CompetitionConfig;
import com.shou.crabscore.service.CompetitionConfigService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 大赛配置接口实现
 *
 * @author spencercjh
 */
@Log4j2
@Service
@CacheConfig(cacheNames = "CompetitionConfigServiceCache")
public class CompetitionConfigServiceImpl implements CompetitionConfigService {

    private final CompetitionConfigMapper competitionConfigMapper;

    @Autowired
    public CompetitionConfigServiceImpl(CompetitionConfigMapper competitionConfigMapper) {
        this.competitionConfigMapper = competitionConfigMapper;
    }

    @CacheEvict(key = "#id")
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return competitionConfigMapper.deleteByPrimaryKey(id);
    }

    @CachePut(key = "#record.id")
    @Override
    public int insert(CompetitionConfig record) {
        return competitionConfigMapper.insert(record);
    }

    @CachePut(key = "#record.id")
    @Override
    public int insertSelective(CompetitionConfig record) {
        return competitionConfigMapper.insertSelective(record);
    }

    @Cacheable(key = "#id")
    @Override
    public CompetitionConfig selectByPrimaryKey(Integer id) {
        return competitionConfigMapper.selectByPrimaryKey(id);
    }

    @CacheEvict(key = "#record.id")
    @Override
    public int updateByPrimaryKeySelective(CompetitionConfig record) {
        return competitionConfigMapper.updateByPrimaryKeySelective(record);
    }

    @CacheEvict(key = "#record.id")
    @Override
    public int updateByPrimaryKey(CompetitionConfig record) {
        return competitionConfigMapper.updateByPrimaryKey(record);
    }
}
