package com.shou.crabscore.service;

import org.apache.ibatis.annotations.Param;

/**
 * 肥满度，种质分，平常分计算接口
 *
 * @author spencercjh
 */
public interface ScoreService {

    /**
     * 计算并修改某一届大赛所有组的肥满度评分
     *
     * @param competitionId 大赛Id
     * @return 事务结果
     */
    boolean calculateAllFatnessScore(@Param("competitionId") Integer competitionId);

    /**
     * 计算并修改某一届大赛所有组的种质评分
     *
     * @param competitionId 大赛Id
     * @return 事务结果
     */
    boolean calculateQualityScore(@Param("competitionId") Integer competitionId);

    /**
     * 计算并修改某一节大赛所有组的口感评分
     *
     * @param competitionId 大赛Id
     * @return 事务结果
     */
    boolean calculateTasteScore(@Param("competitionId") Integer competitionId);
}
