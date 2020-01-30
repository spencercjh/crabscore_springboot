package top.spencercjh.crabscore.refactory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.model.ScoreQuality;

import java.io.Serializable;
import java.util.Date;

/**
 * The interface Score quality service.
 *
 * @author MyBatisCodeHelperPro
 */
public interface ScoreQualityService extends IService<ScoreQuality> {

    /**
     * Page query.
     *
     * @param groupId       the group id;
     * @param competitionId the competition id;
     * @param judgeId       the judge id;
     * @param crabId        the crab id;
     * @param beginTime     the begin time;
     * @param endTime       the end time;
     * @param page          the page;
     * @param size          the size;
     * @return the page;
     */
    @NotNull
    IPage<ScoreQuality> pageQuery(@Nullable Integer groupId,
                                  @Nullable Integer competitionId,
                                  @Nullable Integer judgeId,
                                  @Nullable Integer crabId,
                                  @Nullable Date beginTime,
                                  @Nullable Date endTime,
                                  @NotNull Long page,
                                  @NotNull Long size);

    /**
     * Delete score quality by crab id.
     *
     * @param crabId the crab id;
     */
    void deleteScoreQualityByCrabId(@NotNull Serializable crabId);

    /**
     * Save score quality by crab.
     *
     * @param crab the crab;
     */
    void asyncSaveScoreQualityByCrab(@NotNull Crab crab);
}
