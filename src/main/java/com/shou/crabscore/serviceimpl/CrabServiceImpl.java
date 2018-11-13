package com.shou.crabscore.serviceimpl;

import com.github.pagehelper.PageHelper;
import com.shou.crabscore.dao.CrabMapper;
import com.shou.crabscore.entity.Crab;
import com.shou.crabscore.entity.vo.CrabResult;
import com.shou.crabscore.service.CrabService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 螃蟹接口实现
 *
 * @author spencercjh
 */
@Log4j2
@Service
public class CrabServiceImpl implements CrabService {

    private final CrabMapper crabMapper;

    @Autowired
    public CrabServiceImpl(CrabMapper crabMapper) {
        this.crabMapper = crabMapper;
    }

    @Override
    public Float sdFatnessByCompetitionIdAndGroupIdAndCrabSex(Integer competitionId, Integer groupId, Integer crabSex) {
        return crabMapper.sdFatnessByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
    }

    @Override
    public Float sdWeightByCompetitionIdAndGroupIdAndCrabSex(Integer competitionId, Integer groupId, Integer crabSex) {
        return crabMapper.sdWeightByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
    }

    @Override
    public Float averageFatnessByCompetitionIdAndGroupIdAndCrabSex(Integer competitionId, Integer groupId, Integer crabSex) {
        return crabMapper.averageFatnessByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
    }

    @Override
    public Float averageWeightByCompetitionIdAndGroupIdAndCrabSex(Integer competitionId, Integer groupId, Integer crabSex) {
        return crabMapper.averageWeightByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
    }

    @Override
    public int updateCrabFatness(Integer competitionId, Float varFatnessM, Float varFatnessF) {
        return crabMapper.updateCrabFatness(competitionId, varFatnessM, varFatnessF);
    }

    @Override
    public CrabResult selectByLabel(String label) {
        return crabMapper.selectByLabel(label);
    }

    @Override
    public List<Crab> selectByCompetitionIdAndGroupIdAndCrabSex(Integer competitionId, Integer groupId, Integer crabSex,
                                                                int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return crabMapper.selectByCompetitionIdAndGroupIdAndCrabSex(competitionId, groupId, crabSex);
    }

    @Override
    public int deleteByPrimaryKey(Integer crabId) {
        return crabMapper.deleteByPrimaryKey(crabId);
    }

    @Override
    public int insert(Crab record) {
        return crabMapper.insert(record);
    }

    @Override
    public int insertSelective(Crab record) {
        return crabMapper.insertSelective(record);
    }

    @Override
    public Crab selectByPrimaryKey(Integer crabId) {
        return crabMapper.selectByPrimaryKey(crabId);
    }

    @Override
    public int updateByPrimaryKeySelective(Crab record) {
        return crabMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Crab record) {
        return crabMapper.updateByPrimaryKey(record);
    }
}
