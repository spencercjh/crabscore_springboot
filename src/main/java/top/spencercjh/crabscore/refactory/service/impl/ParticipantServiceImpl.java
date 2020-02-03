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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.config.security.AuthUtils;
import top.spencercjh.crabscore.refactory.mapper.ParticipantMapper;
import top.spencercjh.crabscore.refactory.model.Participant;
import top.spencercjh.crabscore.refactory.model.vo.ParticipantVo;
import top.spencercjh.crabscore.refactory.service.CompanyService;
import top.spencercjh.crabscore.refactory.service.ParticipantService;

import java.util.stream.Collectors;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class ParticipantServiceImpl extends ServiceImpl<ParticipantMapper, Participant> implements ParticipantService {
    private final CompanyService companyService;
    @Value("${crabScore.root}")
    private String rootDirectory;
    @Value("${crabScore.user}")
    private String userDirectory;

    public ParticipantServiceImpl(CompanyService companyService) {
        this.companyService = companyService;
    }

    @NotNull
    @Override
    public IPage<ParticipantVo> pageQuery(@Nullable Integer companyId, @Nullable Integer competitionId, @Nullable Integer roleId,
                                          @Nullable String userName, @Nullable String displayName,
                                          @NotNull Long page, @NotNull Long size) {
        final QueryWrapper<Participant> queryWrapper = new QueryWrapper<>();
        if (companyId != null) {
            queryWrapper.eq(Participant.COL_COMPANY_ID, companyId);
        }
        if (competitionId != null) {
            queryWrapper.eq(Participant.COL_COMPETITION_ID, competitionId);
        }
        if (roleId != null) {
            queryWrapper.eq(Participant.COL_ROLE_ID, roleId);
        }
        if (StringUtils.isNotBlank(userName)) {
            queryWrapper.like(Participant.COL_USERNAME, userName + "%");
        }
        if (StringUtils.isNotBlank(displayName)) {
            queryWrapper.like(Participant.COL_DISPLAY_NAME, displayName + "%");
        }
        final Page<Participant> userPage = getBaseMapper().selectPage(new Page<>(page, size), queryWrapper);
        return new Page<ParticipantVo>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal(), userPage.isSearchCount())
                .setRecords(userPage.getRecords()
                        .stream()
                        .map((Participant participant) -> new ParticipantVo(participant, companyService.getById(participant.getCompanyId())))
                        .collect(Collectors.toList()));
    }

    @Override
    public boolean commitAndUpdate(@NotNull Participant participant, @Nullable MultipartFile image) {
        commitImage(participant, image);
        setupAuthor(participant);
        return updateById(participant);
    }

    @Override
    public boolean commitAndInsert(@NotNull Participant participant, @Nullable MultipartFile image) {
        commitImage(participant, image);
        setupAuthor(participant);
        return save(participant);
    }

    public void setupAuthor(@NotNull Participant participant) {
        final Authentication authentication = AuthUtils.getAuthentication();
        if (authentication != null) {
            final String name = authentication.getName();
            participant.setCreateUser(StringUtils.isBlank(name) ? "ERROR" : name);
        }
    }

    private void commitImage(@NotNull Participant participant, @Nullable MultipartFile image) {
        if (image != null) {
            participant.setAvatarUrl(parsePathToUrl(saveFile(image, userDirectory), rootDirectory));
        }
    }
}
