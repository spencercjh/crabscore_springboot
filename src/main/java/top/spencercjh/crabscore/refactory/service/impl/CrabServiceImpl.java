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
import top.spencercjh.crabscore.refactory.mapper.ScoreQualityMapper;
import top.spencercjh.crabscore.refactory.mapper.ScoreTasteMapper;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.model.ScoreQuality;
import top.spencercjh.crabscore.refactory.model.ScoreTaste;
import top.spencercjh.crabscore.refactory.model.enums.SexEnum;
import top.spencercjh.crabscore.refactory.model.vo.CrabVo;
import top.spencercjh.crabscore.refactory.service.CrabService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CrabServiceImpl extends ServiceImpl<CrabMapper, Crab> implements CrabService {

    @Value("${crabScore.root}")
    private String rootDirectory;
    @Value("${crabScore.crab}")
    private String crabDirectory;
    @Resource
    private ScoreQualityMapper scoreQualityMapper;
    @Resource
    private ScoreTasteMapper scoreTasteMapper;

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
                        scoreTasteMapper.selectOne(new QueryWrapper<ScoreTaste>().eq(ScoreTaste.COL_CRAB_ID, crab.getId())),
                        scoreQualityMapper.selectOne(new QueryWrapper<ScoreQuality>().eq(ScoreQuality.COL_CRAB_ID, crab.getId()))))
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
        // TODO create user
        return save(crab);
    }

    private void commitImage(@NotNull Crab crab, @Nullable MultipartFile image) {
        if (image != null) {
            String url = parsePathToUrl(saveFile(image, crabDirectory), rootDirectory);
            crab.setAvatarUrl(url);
        }
    }
}
