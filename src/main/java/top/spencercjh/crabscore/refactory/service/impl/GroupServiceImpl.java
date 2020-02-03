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
import top.spencercjh.crabscore.refactory.mapper.GroupMapper;
import top.spencercjh.crabscore.refactory.mapper.ParticipantMapper;
import top.spencercjh.crabscore.refactory.model.Competition;
import top.spencercjh.crabscore.refactory.model.Group;
import top.spencercjh.crabscore.refactory.model.Participant;
import top.spencercjh.crabscore.refactory.service.GroupService;

import javax.annotation.Resource;
import java.util.List;

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
    @Resource
    private ParticipantMapper participantMapper;
    @Resource
    private GroupMapper groupMapper;
    @Resource
    private CompetitionMapper competitionMapper;

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

    private void setupAuthor(@NotNull Group group) {
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

    @Nullable
    @Override
    public Group getCurrent(@NotNull String username) {
        final Participant currentUser = participantMapper.selectOne(
                new QueryWrapper<Participant>().eq(Participant.COL_USERNAME, username));
        if (currentUser == null) {
            log.error("当前用户为空");
            return null;
        }
        final List<Competition> currentCompetitions = competitionMapper.selectList(
                new QueryWrapper<Competition>().eq(Competition.COL_STATUS, 1));
        if (currentCompetitions.isEmpty()) {
            log.error("当前大赛为空");
            return null;
        }
        final Competition currentCompetition = currentCompetitions.get(0);
        final Group currentGroup = groupMapper.selectOne(new QueryWrapper<Group>()
                .eq(Group.COL_COMPANY_ID, currentUser.getCompanyId())
                .eq(Group.COL_COMPETITION_ID, currentCompetition.getId()));
        if (currentGroup == null) {
            log.error("用户所绑定的参选单位的小组为空");
            return null;
        }
        return currentGroup;
    }

    private void commitImage(@NotNull Group group, @Nullable MultipartFile image) {
        if (image != null) {
            group.setAvatarUrl(parsePathToUrl(saveFile(image, groupDirectory), rootDirectory));
        }
    }
}
