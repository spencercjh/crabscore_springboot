package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.config.security.AuthUtils;
import top.spencercjh.crabscore.refactory.mapper.CompetitionMapper;
import top.spencercjh.crabscore.refactory.model.Competition;
import top.spencercjh.crabscore.refactory.service.CompetitionService;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CompetitionServiceImpl extends ServiceImpl<CompetitionMapper, Competition>
        implements CompetitionService {

    @Value("${crabScore.root}")
    private String rootDirectory;
    @Value("${crabScore.competition}")
    private String competitionDirectory;

    @NotNull
    @Override
    public IPage<Competition> pageQuery(@Nullable String year, @Nullable Byte status, @NotNull Long page, @NotNull Long size) {
        final QueryWrapper<Competition> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(year)) {
            queryWrapper.like(Competition.COL_COMPETITION_YEAR, year + "%");
        }
        if (status != null) {
            queryWrapper.eq(Competition.COL_STATUS, status);
        }
        return page(new Page<>(page, size), queryWrapper);
    }

    @Override
    public boolean commitAndUpdate(@NotNull Competition competition, @Nullable MultipartFile image) {
        commitImage(competition, image);
        setupAuthor(competition);
        return updateById(competition);
    }

    @Override
    public boolean commitAndInsert(@NotNull Competition competition, @Nullable MultipartFile image) {
        commitImage(competition, image);
        setupAuthor(competition);
        return save(competition);
    }

    private void setupAuthor(@NotNull Competition competition) {
        final Authentication authentication = AuthUtils.getAuthentication();
        if (authentication != null) {
            final String name = authentication.getName();
            competition.setCreateUser(StringUtils.isBlank(name) ? "ERROR" : name);
        }
    }

    private void commitImage(@NotNull Competition competition, @Nullable MultipartFile image) {
        if (image != null) {
            String url = parsePathToUrl(saveFile(image, competitionDirectory), rootDirectory);
            competition.setAvatarUrl(url);
        }
    }
}
