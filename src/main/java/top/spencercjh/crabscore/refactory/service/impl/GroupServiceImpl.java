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
    public IPage<Group> pageQuery(Integer companyId, Integer competitionId, @NotNull Long page, @NotNull Long size) {
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
    public boolean commitAndUpdate(@NotNull Group group, MultipartFile image) {
        commitImage(group, image);
        // TODO update user
        return updateById(group);
    }

    @Override
    public boolean commitAndInsert(@NotNull Group group, MultipartFile image) {
        commitImage(group, image);
        // TODO create user
        return save(group);
    }

    private void commitImage(@NotNull Group group, MultipartFile image) {
        if (image != null) {
            group.setAvatarUrl(parsePathToUrl(saveFile(image, groupDirectory), rootDirectory));
        }
    }
}
