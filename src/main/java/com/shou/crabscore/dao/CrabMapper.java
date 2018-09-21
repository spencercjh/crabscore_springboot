package com.shou.crabscore.dao;

import com.shou.crabscore.entity.Crab;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 螃蟹数据层
 *
 * @author spencercjh
 */
@Repository
@Mapper
public interface CrabMapper {
    /**
     * 根据唯一标签来查找螃蟹信息
     *
     * @param label 标签
     * @return 螃蟹信息记录
     */
    Crab selectByLabel(@Param("label") String label);

    /**
     * 根据大赛Id,小组Id和螃蟹性别来查询螃蟹信息
     *
     * @param competitionId 大赛Id
     * @param groupId       小组Id
     * @param crabSex       性别，1:雄 2：雌
     * @return 所有符合条件的螃蟹信息记录
     */
    List<Crab> selectByCompetitionIdAndGroupIdAndCrabSex(@Param("competitionId") Integer competitionId,
                                                         @Param("groupId") Integer groupId,
                                                         @Param("crabSex") Integer crabSex);

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