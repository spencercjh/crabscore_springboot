package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.mapper.CompanyMapper;
import top.spencercjh.crabscore.refactory.model.Company;
import top.spencercjh.crabscore.refactory.service.CompanyService;

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

    @Override
    public IPage<Company> pageQuery(String companyName, Integer competitionId, Long page, Long size) {
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
    public boolean commitAndUpdate(Company companyInfo, MultipartFile image) {
        commitImage(companyInfo, image);
        // TODO update user
        return updateById(companyInfo);
    }

    @Override
    public boolean commitAndInsert(Company companyInfo, MultipartFile image) {
        commitImage(companyInfo, image);
        // TODO create user
        return save(companyInfo);
    }

    private void commitImage(Company companyInfo, MultipartFile image) {
        if (image != null) {
            String url = parsePathToUrl(saveFile(image, companyInfoDirectory), rootDirectory);
            companyInfo.setAvatarUrl(url);
        }
    }
}