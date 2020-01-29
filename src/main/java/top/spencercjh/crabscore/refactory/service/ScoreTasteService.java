package top.spencercjh.crabscore.refactory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.spencercjh.crabscore.refactory.model.ScoreTaste;

import java.util.Date;

/**
 * @author MyBatisCodeHelperPro
 */
public interface ScoreTasteService extends IService<ScoreTaste> {

    @NotNull
    IPage<ScoreTaste> pageQuery(@Nullable Integer groupId,
                                @Nullable Integer competitionId,
                                @Nullable Integer judgeId,
                                @Nullable Integer crabId,
                                @Nullable Date beginTime,
                                @Nullable Date endTime,
                                @NotNull Long page,
                                @NotNull Long size);
}
