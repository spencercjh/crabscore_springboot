package top.spencercjh.crabscore.refactory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.model.ScoreTaste;

import java.io.Serializable;
import java.util.Date;

/**
 * The interface Score taste service.
 *
 * @author MyBatisCodeHelperPro
 */
public interface ScoreTasteService extends IService<ScoreTaste> {

    /**
     * Page query page.
     *
     * @param groupId       the group id;
     * @param competitionId the competition id;
     * @param judgeUsername the judge id;
     * @param crabId        the crab id;
     * @param beginTime     the begin time;
     * @param endTime       the end time;
     * @param page          the page;
     * @param size          the size;
     * @return the page;
     */
    @NotNull
    IPage<ScoreTaste> pageQuery(@Nullable Integer groupId,
                                @Nullable Integer competitionId,
                                @Nullable String judgeUsername,
                                @Nullable Integer crabId,
                                @Nullable Date beginTime,
                                @Nullable Date endTime,
                                @NotNull Long page,
                                @NotNull Long size);

    /**
     * Delete score taste by crab id.
     *
     * @param crabId the crab id;
     */
    void deleteScoreTasteByCrabId(@NotNull Serializable crabId);

    /**
     * Save score taste by crab.
     *
     * @param crab the crab;
     */
    void saveScoreTasteByCrab(@NotNull Crab crab);
}
