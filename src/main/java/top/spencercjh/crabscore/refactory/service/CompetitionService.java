package top.spencercjh.crabscore.refactory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.model.Competition;

/**
 * @author MyBatisCodeHelperPro
 */
public interface CompetitionService extends IService<Competition>, BaseUploadFileService {
    /**
     * 分页查询
     *
     * @param year   年份名 右like;
     * @param status 禁用/启用;
     * @param page   页面;
     * @param size   大小;
     * @return 列表
     */
    @NotNull
    IPage<Competition> pageQuery(@Nullable String year, @Nullable Byte status, @NotNull Long page, Long size);

    /**
     * 更新资料
     *
     * @param competition 参选单位;
     * @param image       头像;
     * @return 更新后对象;
     */
    boolean commitAndUpdate(@NotNull Competition competition, @Nullable MultipartFile image);

    /**
     * 新增资料
     *
     * @param competition 参选单位;
     * @param image       头像;
     * @return 更新后对象;
     */
    boolean commitAndInsert(@NotNull Competition competition, @Nullable MultipartFile image);
}
