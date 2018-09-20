package com.shou.crabscore.service;

import com.shou.crabscore.entity.User;

import java.util.List;

/**
 * 用户接口
 *
 * @author spencercjh
 */
public interface UserService {
    /**
     * 查找所有符合条件的用户
     *
     * @return 所有用户记录
     */
    List<User> selectAllUserSelective(Integer status);

    /**
     * 查找所有用户
     *
     * @return 所有用户记录
     */
    List<User> selectAllUser();

    /**
     * 通过用户名查找
     *
     * @param username 用户名
     * @return 用户记录
     */
    User selectByUserName(String username);

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