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
import top.spencercjh.crabscore.refactory.mapper.CompanyMapper;
import top.spencercjh.crabscore.refactory.mapper.ParticipantMapper;
import top.spencercjh.crabscore.refactory.model.Company;
import top.spencercjh.crabscore.refactory.model.Participant;
import top.spencercjh.crabscore.refactory.service.CompanyService;

import javax.annotation.Resource;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {
    @Value("${crabScore.root}")
    private String rootDirectory;
    @Value("${crabScore.company}")
    private String companyInfoDirectory;
    @Resource
    private ParticipantMapper participantMapper;

    @NotNull
    @Override
    public IPage<Company> pageQuery(@Nullable String companyName, @Nullable Integer competitionId, @NotNull Long page, @NotNull Long size) {
        final QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(companyName)) {
            queryWrapper.like(Company.COL_COMPANY_NAME, companyName + "%");
        }
        if (competitionId != null) {
            queryWrapper.eq(Company.COL_COMPETITION_ID, competitionId);
        }
        return page(new Page<>(page, size), queryWrapper);
    }

    @Override
    public boolean commitAndUpdate(@NotNull Company companyInfo, @Nullable MultipartFile image) {
        commitImage(companyInfo, image);
        setupAuthor(companyInfo);
        return updateById(companyInfo);
    }

    @Override
    public boolean commitAndInsert(@NotNull Company companyInfo, @Nullable MultipartFile image) {
        commitImage(companyInfo, image);
        setupAuthor(companyInfo);
        return save(companyInfo);
    }

    @Nullable
    @Override
    public Company getOneByUsername(@NotNull String username) {
        final Participant participant = participantMapper.selectOne(new QueryWrapper<Participant>().eq(Participant.COL_USERNAME, username));
        if (participant == null) {
            return null;
        }
        return getById(participant.getCompanyId());
    }

    @Override
    public boolean updateByUsername(@NotNull String username, @NotNull Company toUpdate, @Nullable MultipartFile image) {
        final Participant participant = participantMapper.selectOne(new QueryWrapper<Participant>().eq(Participant.COL_USERNAME, username));
        if (participant == null) {
            return false;
        }
        return commitAndUpdate(toUpdate.setId(participant.getCompanyId()), image);
    }

    private void setupAuthor(@NotNull Company companyInfo) {
        final Authentication authentication = AuthUtils.getAuthentication();
        if (authentication != null) {
            final String name = authentication.getName();
            companyInfo.setCreateUser(StringUtils.isBlank(name) ? "ERROR" : name);
        }
    }

    private void commitImage(@NotNull Company companyInfo, @Nullable MultipartFile image) {
        if (image != null) {
            String url = parsePathToUrl(saveFile(image, companyInfoDirectory), rootDirectory);
            companyInfo.setAvatarUrl(url);
        }
    }
}