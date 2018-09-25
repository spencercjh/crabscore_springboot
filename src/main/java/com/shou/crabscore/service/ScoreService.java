package com.shou.crabscore.service;

import com.shou.crabscore.common.vo.Result;
import org.apache.ibatis.annotations.Param;

/**
 * 肥满度，种质分，平常分计算接口
 *
 * @author spencercjh
 */
public interface ScoreService {
    /**
     * 计算并修改某一届大赛所有螃蟹的肥满度
     *
     * @param competitionId 大赛Id
     * @return 事务结果
     */
    public Result<Object> calculateFatness(@Param("competitionId") Integer competitionId) throws Exception;

    /**
     * 计算并修改某一届大赛所有组的肥满度评分
     *
     * @param competitionId 大赛Id
     * @return 事务结果
     */
    public Result<Object> calculateFatnessScore(@Param("competitionId") Integer competitionId);

    /**
     * 计算并修改某一届大赛所有组的种质评分
     *
     * @param competitionId 大赛Id
     * @return 事务结果
     */
    public Result<Object> calculateQualityScore(@Param("competitionId") Integer competitionId);

    /**
     * 计算并修改某一节大赛所有组的口感评分
     *
     * @param competitionId 大赛Id
     * @return 事务结果
     */
    public Result<Object> calculateTasteScore(@Param("competitionId") Integer competitionId);
}
