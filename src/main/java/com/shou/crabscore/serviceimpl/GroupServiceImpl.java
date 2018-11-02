package com.shou.crabscore.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.shou.crabscore.dao.GroupMapper;
import com.shou.crabscore.entity.Group;
import com.shou.crabscore.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 小组接口实现
 *
 * @author spencercjh
 */
@Log4j2
@Service
public class GroupServiceImpl implements GroupService {
    private final GroupMapper groupMapper;

    @Autowired
    public GroupServiceImpl(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Override
    public List<Group> selectAllGroupOneCompetition(Integer competitionId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return groupMapper.selectAllGroupOneCompetition(competitionId);
    }

    @Override
    public List<Group> selectAllGroupOneCompetitionOrderByQualityScore(Integer competitionId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return groupMapper.selectAllGroupOneCompetitionOrderByQualityScore(competitionId);
    }

    @Override
    public List<Group> selectAllGroupOneCompetitionOrderByTasteScore(Integer competitionId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return groupMapper.selectAllGroupOneCompetitionOrderByTasteScore(competitionId);
    }

    @Override
    public List<Group> selectAllGroupOneCompetitionOrderByFatnessScore(Integer competitionId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return groupMapper.selectAllGroupOneCompetitionOrderByFatnessScore(competitionId);
    }

    @Override
    public List<Group> selectAllGroupOneCompetitionOneCompany(Integer competitionId, Integer companyId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return groupMapper.selectAllGroupOneCompetitionOneCompany(competitionId, companyId);
    }

    @Override
    public int deleteByPrimaryKey(Integer groupId) {
        return groupMapper.deleteByPrimaryKey(groupId);
    }

    @Override
    public int insert(Group record) {
        return groupMapper.insert(record);
    }

    @Override
    public int insertSelective(Group record) {
        return groupMapper.insertSelective(record);
    }

    @Override
    public Group selectByPrimaryKey(Integer groupId) {
        return groupMapper.selectByPrimaryKey(groupId);
    }

    @Override
    public int updateByPrimaryKeySelective(Group record) {
        return groupMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Group record) {
        return groupMapper.updateByPrimaryKey(record);
    }
}
