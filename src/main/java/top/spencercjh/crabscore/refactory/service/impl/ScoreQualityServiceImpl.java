package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.mapper.ScoreQualityMapper;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.model.ScoreQuality;
import top.spencercjh.crabscore.refactory.service.ScoreQualityService;

import java.io.Serializable;
import java.util.Date;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class ScoreQualityServiceImpl extends ServiceImpl<ScoreQualityMapper, ScoreQuality> implements ScoreQualityService {

    @Override
    @NotNull
    public IPage<ScoreQuality> pageQuery(@Nullable Integer groupId,
                                         @Nullable Integer competitionId,
                                         @Nullable Integer judgeId,
                                         @Nullable Integer crabId,
                                         @Nullable Date beginTime,
                                         @Nullable Date endTime,
                                         @NotNull Long page,
                                         @NotNull Long size) {
        final QueryWrapper<ScoreQuality> queryWrapper = new QueryWrapper<>();
        if (groupId != null) {
            queryWrapper.eq(ScoreQuality.COL_GROUP_ID, groupId);
        }
        if (competitionId != null) {
            queryWrapper.eq(ScoreQuality.COL_COMPETITION_ID, competitionId);
        }
        if (judgeId != null) {
            queryWrapper.eq(ScoreQuality.COL_JUDGES_ID, judgeId);
        }
        if (crabId != null) {
            queryWrapper.eq(ScoreQuality.COL_CRAB_ID, crabId);
        }
        if (beginTime != null && endTime != null) {
            queryWrapper.between(Crab.COL_CREATE_DATE, beginTime, endTime);
        } else if (beginTime != null) {
            queryWrapper.ge(Crab.COL_CREATE_DATE, beginTime);
        } else if (endTime != null) {
            queryWrapper.le(Crab.COL_CREATE_DATE, endTime);
        }
        return page(new Page<>(page, size), queryWrapper);
    }

    @Override
    public boolean deleteScoreQualityByCrabId(@NotNull Serializable crabId) {
        return remove(new QueryWrapper<ScoreQuality>().eq(ScoreQuality.COL_CRAB_ID, crabId));
    }

    @Override
    public boolean saveScoreQualityByCrab(@NotNull Crab crab) {
        return save(new ScoreQuality()
                .setGroupId(crab.getGroupId())
                .setCrabId(crab.getId())
                .setCompetitionId(crab.getCompetitionId()));
    }
}
