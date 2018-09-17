package com.shou.crabscore.serviceimpl;

import com.shou.crabscore.dao.TasteScoreMapper;
import com.shou.crabscore.entity.TasteScore;
import com.shou.crabscore.service.TasteScoreService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 口感分数接口实现
 *
 * @author spencercjh
 */
@Log4j2
@Service
public class TasteScoreServiceImpl implements TasteScoreService {
    private final TasteScoreMapper tasteScoreMapper;

    @Autowired
    public TasteScoreServiceImpl(TasteScoreMapper tasteScoreMapper) {
        this.tasteScoreMapper = tasteScoreMapper;
    }

    @Override
    public int deleteByPrimaryKey(Integer scoreId) {
        return tasteScoreMapper.deleteByPrimaryKey(scoreId);
    }

    @Override
    public int insert(TasteScore record) {
        return tasteScoreMapper.insert(record);
    }

    @Override
    public int insertSelective(TasteScore record) {
        return tasteScoreMapper.insertSelective(record);
    }

    @Override
    public TasteScore selectByPrimaryKey(Integer scoreId) {
        return tasteScoreMapper.selectByPrimaryKey(scoreId);
    }

    @Override
    public int updateByPrimaryKeySelective(TasteScore record) {
        return tasteScoreMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TasteScore record) {
        return tasteScoreMapper.updateByPrimaryKey(record);
    }
}
