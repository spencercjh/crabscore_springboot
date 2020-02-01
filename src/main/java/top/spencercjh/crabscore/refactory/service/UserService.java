package top.spencercjh.crabscore.refactory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.model.User;
import top.spencercjh.crabscore.refactory.model.vo.UserVo;

/**
 * @author MyBatisCodeHelperPro
 */
public interface UserService extends IService<User>, BaseUploadFileService {
    @NotNull
    IPage<UserVo> pageQuery(@Nullable Integer companyId, @Nullable Integer competitionId,
                            @Nullable Integer roleId, @Nullable String userName, @Nullable String displayName,
                            @NotNull Long page, @NotNull Long size);

    /**
     * Commit image and update User.
     *
     * @param user  the User;
     * @param image the image;
     * @return the boolean;
     */
    boolean commitAndUpdate(@NotNull User user, @Nullable MultipartFile image);

    /**
     * Commit image and insert User.
     *
     * @param user  the User;
     * @param image the image;
     * @return the boolean;
     */
    boolean commitAndInsert(@NotNull User user, @Nullable MultipartFile image);
}
