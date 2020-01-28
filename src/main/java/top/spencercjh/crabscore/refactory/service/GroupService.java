package top.spencercjh.crabscore.refactory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.model.Group;

/**
 * The interface Group service.
 *
 * @author MyBatisCodeHelperPro
 */
public interface GroupService extends IService<Group>, BaseUploadFileService {
    /**
     * 分页查询
     *
     * @param companyId     the company id;
     * @param competitionId the competition id;
     * @param page          the page;
     * @param size          the size;
     * @return page
     */
    @NotNull
    IPage<Group> pageQuery(@Nullable Integer companyId, @Nullable Integer competitionId, @NotNull Long page, @NotNull Long size);

    /**
     * Commit image and update group.
     *
     * @param group the group;
     * @param image the image;
     * @return the boolean;
     */
    boolean commitAndUpdate(@NotNull Group group, @Nullable MultipartFile image);

    /**
     * Commit image and insert group.
     *
     * @param group the group;
     * @param image the image;
     * @return the boolean;
     */
    boolean commitAndInsert(@NotNull Group group, @Nullable MultipartFile image);
}
