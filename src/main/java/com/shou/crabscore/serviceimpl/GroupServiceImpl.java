package com.shou.crabscore.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.shou.crabscore.dao.GroupMapper;
import com.shou.crabscore.entity.Group;
import com.shou.crabscore.entity.vo.GroupResult;
import com.shou.crabscore.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 小组接口实现
 *
 * @author spencercjh
 */
@Log4j2
@Service
@CacheConfig(cacheNames = "GroupServiceCache")
public class GroupServiceImpl implements GroupService {
    private final GroupMapper groupMapper;

    @Autowired
    public GroupServiceImpl(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Cacheable(key = "#competitionId")
    @Override
    public List<GroupResult> selectAllGroupOneCompetition(Integer competitionId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return groupMapper.selectAllGroupOneCompetition(competitionId);
    }

    @Cacheable(key = "#competitionId")
    @Override
    public List<GroupResult> selectAllGroupOneCompetitionOrderByQualityScore(Integer competitionId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return groupMapper.selectAllGroupOneCompetitionOrderByQualityScore(competitionId);
    }

    @Cacheable(key = "#competitionId")
    @Override
    public List<GroupResult> selectAllGroupOneCompetitionOrderByTasteScore(Integer competitionId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return groupMapper.selectAllGroupOneCompetitionOrderByTasteScore(competitionId);
    }

    @Cacheable(key = "#competitionId")

    @Override
    public List<GroupResult> selectAllGroupOneCompetitionOrderByFatnessScore(Integer competitionId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return groupMapper.selectAllGroupOneCompetitionOrderByFatnessScore(competitionId);
    }

    @Cacheable(key = "#competitionId")
    @Override
    public List<GroupResult> selectAllGroupOneCompetitionOneCompany(Integer competitionId, Integer companyId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return groupMapper.selectAllGroupOneCompetitionOneCompany(competitionId, companyId);
    }

    @CacheEvict(key = "#groupId")
    @Override
    public int deleteByPrimaryKey(Integer groupId) {
        return groupMapper.deleteByPrimaryKey(groupId);
    }

    @CachePut(key = "#record.groupId")
    @Override
    public int insert(Group record) {
        return groupMapper.insert(record);
    }

    @CachePut(key = "#record.groupId")
    @Override
    public int insertSelective(Group record) {
        return groupMapper.insertSelective(record);
    }

    @Cacheable(key = "#groupId")
    @Override
    public Group selectByPrimaryKey(Integer groupId) {
        return groupMapper.selectByPrimaryKey(groupId);
    }

    @CacheEvict(key = "record.groupId")
    @Override
    public int updateByPrimaryKeySelective(Group record) {
        return groupMapper.updateByPrimaryKeySelective(record);
    }

    @CacheEvict(key = "record.groupId")
    @Override
    public int updateByPrimaryKey(Group record) {
        return groupMapper.updateByPrimaryKey(record);
    }
}
