package com.shou.crabscore.dao;

import com.shou.crabscore.entity.Group;
import org.springframework.stereotype.Repository;

/**
 * 小组数据层
 *
 * @author spencercjh
 */
@Repository
public interface GroupMapper {
    /**
     * 通过主键删除
     *
     * @param groupId 小组id
     * @return 删除记录条数
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer groupId);

    /**
     * 通过记录插入
     *
     * @param record 小组记录
     * @return 插入记录条数
     * @mbggenerated
     */
    int insert(Group record);

    /**
     * 选择性地通过记录插入
     *
     * @param record 小组记录
     * @return 插入记录条数
     * @mbggenerated
     */
    int insertSelective(Group record);

    /**
     * 通过主键查找
     *
     * @param groupId 小组id
     * @return 小组记录
     * @mbggenerated
     */
    Group selectByPrimaryKey(Integer groupId);

    /**
     * 通过主键选择性地更新
     *
     * @param record 小组记录
     * @return 更新记录条数
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Group record);

    /**
     * 通过主键更新
     *
     * @param record 小组记录
     * @return 更新记录条数
     * @mbggenerated
     */
    int updateByPrimaryKey(Group record);
}