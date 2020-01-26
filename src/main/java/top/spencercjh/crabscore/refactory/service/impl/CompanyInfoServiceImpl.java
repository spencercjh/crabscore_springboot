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
import top.spencercjh.crabscore.refactory.mapper.CompanyInfoMapper;
import top.spencercjh.crabscore.refactory.model.CompanyInfo;
import top.spencercjh.crabscore.refactory.service.BaseUploadFileService;
import top.spencercjh.crabscore.refactory.service.CompanyInfoService;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CompanyInfoServiceImpl extends ServiceImpl<CompanyInfoMapper, CompanyInfo> implements
        CompanyInfoService, BaseUploadFileService {
    @Value("${crabScore.root}")
    private String rootDirectory;
    @Value("${crabScore.companyInfo}")
    private String companyInfoDirectory;

    @Override
    public IPage<CompanyInfo> pageQuery(String companyName, Integer competitionId, Long page, Long size) {
        final QueryWrapper<CompanyInfo> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(companyName)) {
            queryWrapper.like(CompanyInfo.COL_COMPANY_NAME, companyName + "%");
        }
        if (competitionId != null) {
            queryWrapper.eq(CompanyInfo.COL_COMPETITION_ID, competitionId);
        }
        return page(new Page<>(page, size), queryWrapper);
    }

    @Override
    public boolean commitAndUpdate(CompanyInfo companyInfo, MultipartFile image) {
        commitImage(companyInfo, image);
        // TODO update user
        return updateById(companyInfo);
    }

    @Override
    public boolean commitAndInsert(CompanyInfo companyInfo, MultipartFile image) {
        commitImage(companyInfo, image);
        // TODO create user
        return save(companyInfo);
    }

    private void commitImage(CompanyInfo companyInfo, MultipartFile image) {
        if (image != null) {
            String url = parsePathToUrl(saveFile(image, companyInfoDirectory), rootDirectory);
            companyInfo.setAvatarUrl(url);
        }
    }
}