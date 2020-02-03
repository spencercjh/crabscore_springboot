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
import top.spencercjh.crabscore.refactory.mapper.GroupMapper;
import top.spencercjh.crabscore.refactory.model.Group;
import top.spencercjh.crabscore.refactory.service.GroupService;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {
    @Value("${crabScore.root}")
    private String rootDirectory;
    @Value("${crabScore.group}")
    private String groupDirectory;

    @NotNull
    @Override
    public IPage<Group> pageQuery(@Nullable Integer companyId, @Nullable Integer competitionId, @NotNull Long page, @NotNull Long size) {
        final QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
        if (companyId != null) {
            queryWrapper.eq(Group.COL_COMPANY_ID, companyId);
        }
        if (competitionId != null) {
            queryWrapper.eq(Group.COL_COMPETITION_ID, competitionId);
        }
        return page(new Page<>(page, size), queryWrapper);
    }

    @Override
    public boolean commitAndUpdate(@NotNull Group group, @Nullable MultipartFile image) {
        commitImage(group, image);
        setupAuthor(group);
        return updateById(group);
    }

    public void setupAuthor(@NotNull Group group) {
        final Authentication authentication = AuthUtils.getAuthentication();
        if (authentication != null) {
            final String name = authentication.getName();
            group.setUpdateUser(StringUtils.isBlank(name) ? "ERROR" : name);
        }
    }

    @Override
    public boolean commitAndInsert(@NotNull Group group, @Nullable MultipartFile image) {
        commitImage(group, image);
        setupAuthor(group);
        return save(group);
    }

    private void commitImage(@NotNull Group group, @Nullable MultipartFile image) {
        if (image != null) {
            group.setAvatarUrl(parsePathToUrl(saveFile(image, groupDirectory), rootDirectory));
        }
    }
}
