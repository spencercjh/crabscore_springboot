package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.mapper.CrabMapper;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.model.ScoreQuality;
import top.spencercjh.crabscore.refactory.model.ScoreTaste;
import top.spencercjh.crabscore.refactory.model.enums.SexEnum;
import top.spencercjh.crabscore.refactory.model.vo.CrabVo;
import top.spencercjh.crabscore.refactory.service.AsyncScoreService;
import top.spencercjh.crabscore.refactory.service.CrabService;
import top.spencercjh.crabscore.refactory.service.ScoreQualityService;
import top.spencercjh.crabscore.refactory.service.ScoreTasteService;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CrabServiceImpl extends ServiceImpl<CrabMapper, Crab> implements CrabService {

    private final ScoreQualityService scoreQualityService;
    private final ScoreTasteService scoreTasteService;
    private final AsyncScoreService asyncScoreService;
    @Value("${crabScore.root}")
    private String rootDirectory;
    @Value("${crabScore.crab}")
    private String crabDirectory;

    public CrabServiceImpl(ScoreQualityService scoreQualityService, ScoreTasteService scoreTasteService, AsyncScoreService asyncScoreService) {
        this.scoreQualityService = scoreQualityService;
        this.scoreTasteService = scoreTasteService;
        this.asyncScoreService = asyncScoreService;
    }

    @NotNull
    @Override
    public IPage<CrabVo> pageQuery(@Nullable Integer competitionId, @Nullable Integer groupId, @Nullable SexEnum sex,
                                   @Nullable Date beginTime, @Nullable Date endTime, @NotNull Long page, @NotNull Long size) {
        final QueryWrapper<Crab> queryWrapper = new QueryWrapper<>();
        if (competitionId != null) {
            queryWrapper.eq(Crab.COL_COMPETITION_ID, competitionId);
        }
        if (groupId != null) {
            queryWrapper.eq(Crab.COL_GROUP_ID, groupId);
        }
        if (sex != null) {
            queryWrapper.eq(Crab.COL_CRAB_SEX, sex);
        }
        if (beginTime != null && endTime != null) {
            queryWrapper.between(Crab.COL_CREATE_DATE, beginTime, endTime);
        } else if (beginTime != null) {
            queryWrapper.ge(Crab.COL_CREATE_DATE, beginTime);
        } else if (endTime != null) {
            queryWrapper.le(Crab.COL_CREATE_DATE, endTime);
        }
        final Page<Crab> crabPage = getBaseMapper().selectPage(new Page<>(page, size), queryWrapper);
        return new Page<CrabVo>(crabPage.getCurrent(), crabPage.getSize(), crabPage.getTotal(), crabPage.isSearchCount())
                .setRecords(crabPage.getRecords().stream().map((Crab crab) -> new CrabVo(crab,
                        scoreTasteService.getOne(new QueryWrapper<ScoreTaste>()
                                .eq(ScoreTaste.COL_CRAB_ID, crab.getId())),
                        scoreQualityService.getOne(new QueryWrapper<ScoreQuality>()
                                .eq(ScoreQuality.COL_CRAB_ID, crab.getId()))))
                        .filter(crabVo -> crabVo.getScoreQuality() != null && crabVo.getScoreTaste() != null)
                        .collect(Collectors.toList()));
    }

    @Override
    public boolean commitAndUpdate(@NotNull Crab crab, @Nullable MultipartFile image) {
        commitImage(crab, image);
        // TODO update user
        return updateById(crab);
    }

    @Override
    public boolean commitAndInsert(@NotNull Crab crab, @Nullable MultipartFile image) {
        commitImage(crab, image);
        return save(crab);
    }

    @Override
    public boolean save(Crab entity) {
        // TODO create user
        final boolean saveResult = super.save(entity);
        asyncScoreService.asyncSaveScoresByCrab(entity);
        return saveResult;
    }

    @Override
    public boolean saveCrabAndScoreBatch(List<Crab> toBatchInsert) {
        final boolean saveCrabResult = super.saveBatch(toBatchInsert);
        toBatchInsert.forEach(asyncScoreService::asyncSaveScoresByCrab);
        return saveCrabResult;
    }

    private void commitImage(@NotNull Crab crab, @Nullable MultipartFile image) {
        if (image != null) {
            String url = parsePathToUrl(saveFile(image, crabDirectory), rootDirectory);
            crab.setAvatarUrl(url);
        }
    }

    @Override
    public boolean removeById(Serializable id) {
        asyncScoreService.asyncDeleteScoresByCrab(id);
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        try {
            idList.forEach(id -> {
                if (!removeById(id)) {
                    throw new RuntimeException("删除Crab及其Scores失败,Id为" + id);
                }
            });
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e.getCause());
            return false;
        }
    }
}
