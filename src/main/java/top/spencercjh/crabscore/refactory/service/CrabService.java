package top.spencercjh.crabscore.refactory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.model.enums.SexEnum;

import java.util.Date;

/**
 * @author MyBatisCodeHelperPro
 */
public interface CrabService extends IService<Crab>, BaseUploadFileService {
    /**
     * 分页查询
     *
     * @param competitionId 大赛id;
     * @param groupId       小组id;
     * @param sex           雌雄;
     * @param beginTime     创建时间左区间;
     * @param endTime       创建时间右区间;
     * @param page          页面;
     * @param size          大小;
     * @return page result;
     */
    @NotNull
    IPage<Crab> pageQuery(@Nullable Integer competitionId, @Nullable Integer groupId, @Nullable SexEnum sex,
                          @Nullable Date beginTime, @Nullable Date endTime, @NotNull Long page, @NotNull Long size);

    /**
     * 更新资料
     *
     * @param crab  螃蟹;
     * @param image 头像;
     * @return 更新后对象;
     */
    boolean commitAndUpdate(@NotNull Crab crab, @Nullable MultipartFile image);

    /**
     * 新增资料
     *
     * @param crab  螃蟹;
     * @param image 头像;
     * @return 更新后对象;
     */
    boolean commitAndInsert(@NotNull Crab crab, @Nullable MultipartFile image);
}
