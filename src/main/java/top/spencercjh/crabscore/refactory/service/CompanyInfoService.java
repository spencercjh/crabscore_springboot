package top.spencercjh.crabscore.refactory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.model.CompanyInfo;

/**
 * @author MyBatisCodeHelperPro
 */
public interface CompanyInfoService extends IService<CompanyInfo> {

    /**
     * 分页查询
     *
     * @param companyName   参选单位名 右like;
     * @param competitionId 大赛id eq;
     * @param page          页面;
     * @param size          大小;
     * @return 列表
     */
    IPage<CompanyInfo> pageQuery(String companyName, Integer competitionId, Long page, Long size);

    /**
     * 更新资料
     *
     * @param companyInfo 参选单位
     * @param image       头像
     * @return 更新后对象
     */
    boolean commitAndUpdate(CompanyInfo companyInfo, MultipartFile image);

    /**
     * 新增资料
     *
     * @param companyInfo 参选单位
     * @param image       头像
     * @return 更新后对象
     */
    boolean commitAndInsert(CompanyInfo companyInfo, MultipartFile image);
}
