package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.mapper.UserMapper;
import top.spencercjh.crabscore.refactory.model.User;
import top.spencercjh.crabscore.refactory.model.vo.UserVo;
import top.spencercjh.crabscore.refactory.service.CompanyService;
import top.spencercjh.crabscore.refactory.service.UserService;

import java.util.stream.Collectors;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final CompanyService companyService;
    @Value("${crabScore.root}")
    private String rootDirectory;
    @Value("${crabScore.user}")
    private String userDirectory;

    public UserServiceImpl(CompanyService companyService) {
        this.companyService = companyService;
    }

    @NotNull
    @Override
    public IPage<UserVo> pageQuery(@Nullable Integer companyId, @Nullable Integer competitionId, @Nullable Integer roleId,
                                   @Nullable String userName, @Nullable String displayName,
                                   @NotNull Long page, @NotNull Long size) {
        final QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (companyId != null) {
            queryWrapper.eq(User.COL_COMPANY_ID, companyId);
        }
        if (competitionId != null) {
            queryWrapper.eq(User.COL_COMPETITION_ID, competitionId);
        }
        if (roleId != null) {
            queryWrapper.eq(User.COL_ROLE_ID, roleId);
        }
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like(User.COL_USER_NAME, userName + "%");
        }
        if (StringUtils.isNotBlank(displayName)) {
            queryWrapper.like(User.COL_DISPLAY_NAME, displayName + "%");
        }
        final Page<User> userPage = getBaseMapper().selectPage(new Page<>(page, size), queryWrapper);
        return new Page<UserVo>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal(), userPage.isSearchCount())
                .setRecords(userPage.getRecords()
                        .stream()
                        .map((User user) -> new UserVo(user, companyService.getById(user.getCompanyId())))
                        .collect(Collectors.toList()));
    }

    @Override
    public boolean commitAndUpdate(@NotNull User user, @Nullable MultipartFile image) {
        commitImage(user, image);
        // TODO update user
        return updateById(user);
    }

    @Override
    public boolean commitAndInsert(@NotNull User user, @Nullable MultipartFile image) {
        commitImage(user, image);
        // TODO create user
        return save(user);
    }

    private void commitImage(@NotNull User user, @Nullable MultipartFile image) {
        if (image != null) {
            user.setAvatarUrl(parsePathToUrl(saveFile(image, userDirectory), rootDirectory));
        }
    }
}
