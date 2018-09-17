package com.shou.crabscore.service;

import com.shou.crabscore.entity.Crab;

/**
 * 螃蟹接口
 *
 * @author spencercjh
 */
public interface CrabService {
    /**
     * 通过主键删除
     *
     * @param crabId 螃蟹id
     * @return 删除记录条数
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer crabId);

    /**
     * 通过记录插入
     *
     * @param record 螃蟹记录
     * @return 插入记录条数
     * @mbggenerated
     */
    int insert(Crab record);

    /**
     * 选择性地通过记录插入
     *
     * @param record 螃蟹记录
     * @return 插入记录条数
     * @mbggenerated
     */
    int insertSelective(Crab record);

    /**
     * 通过主键查找
     *
     * @param crabId 螃蟹id
     * @return 螃蟹记录
     * @mbggenerated
     */
    Crab selectByPrimaryKey(Integer crabId);

    /**
     * 通过主键选择性地更新
     *
     * @param record 螃蟹记录
     * @return 更新记录条数
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Crab record);

    /**
     * 通过主键更新
     *
     * @param record 螃蟹记录
     * @return 更新记录条数
     * @mbggenerated
     */
    int updateByPrimaryKey(Crab record);
}
