package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.mapper.CrabMapper;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.model.enums.SexEnum;
import top.spencercjh.crabscore.refactory.service.CrabService;

import java.util.Date;

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

    @NotNull
    @Override
    public IPage<Crab> pageQuery(Integer competitionId, Integer groupId, SexEnum sex, Date beginTime,
                                 Date endTime, @NotNull Long page, @NotNull Long size) {
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
        return page(new Page<>(page, size), queryWrapper);
    }

    @Override
    public boolean commitAndUpdate(@NotNull Crab crab, MultipartFile image) {
        commitImage(crab, image);
        // TODO update user
        return updateById(crab);
    }

    @Override
    public boolean commitAndInsert(@NotNull Crab crab, MultipartFile image) {
        commitImage(crab, image);
        // TODO create user
        return save(crab);
    }

    private void commitImage(@NotNull Crab crab, MultipartFile image) {
        if (image != null) {
            String url = parsePathToUrl(saveFile(image, crabDirectory), rootDirectory);
            crab.setAvatarUrl(url);
        }
    }
}
