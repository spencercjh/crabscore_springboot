package com.shou.crabscore.dao;

import com.shou.crabscore.entity.User;
import org.springframework.stereotype.Repository;

/**
 * 用户数据层
 *
 * @author spencercjh
 */
@Repository
public interface UserMapper {
    /**
     * 通过主键删除
     *
     * @param userId 用户id
     * @return 删除记录条数
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer userId);

    /**
     * 通过记录插入
     *
     * @param record 用户记录
     * @return 插入记录条数
     * @mbggenerated
     */
    int insert(User record);

    /**
     * 选择性地通过记录插入
     *
     * @param record 用户记录
     * @return 插入记录条数
     * @mbggenerated
     */
    int insertSelective(User record);

    /**
     * 通过主键查找
     *
     * @param userId 用户id
     * @return 用户记录
     * @mbggenerated
     */
    User selectByPrimaryKey(Integer userId);

    /**
     * 通过主键选择性地更新
     *
     * @param record 用户记录
     * @return 更新记录条数
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * 通过主键更新
     *
     * @param record 用户记录
     * @return 更新记录条数
     * @mbggenerated
     */
    int updateByPrimaryKey(User record);
}