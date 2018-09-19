package com.shou.crabscore.dao;

import com.shou.crabscore.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 用户组数据层
 *
 * @author spencercjh
 */
@Repository
@Mapper
public interface RoleMapper {
    /**
     * 通过主键删除
     *
     * @param roleId 用户组id
     * @return 删除记录条数
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer roleId);

    /**
     * 通过记录插入
     *
     * @param record 用户组记录
     * @return 插入记录条数
     * @mbggenerated
     */
    int insert(Role record);

    /**
     * 选择性地通过记录插入
     *
     * @param record 用户组记录
     * @return 插入记录条数
     * @mbggenerated
     */
    int insertSelective(Role record);

    /**
     * 通过主键查找
     *
     * @param roleId 用户组id
     * @return 用户组记录
     * @mbggenerated
     */
    Role selectByPrimaryKey(Integer roleId);

    /**
     * 通过主键选择性地更新
     *
     * @param record 用户组记录
     * @return 更新记录条数
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Role record);

    /**
     * 通过主键更新
     *
     * @param record 用户组记录
     * @return 更新记录条数
     * @mbggenerated
     */
    int updateByPrimaryKey(Role record);
}