package top.spencercjh.crabscore.refactory.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.multipart.MultipartFile;
import top.spencercjh.crabscore.refactory.model.Participant;
import top.spencercjh.crabscore.refactory.model.vo.ParticipantVo;

/**
 * @author MyBatisCodeHelperPro
 */
public interface ParticipantService extends IService<Participant>, BaseUploadFileService {
    @NotNull
    IPage<ParticipantVo> pageQuery(@Nullable Integer companyId, @Nullable Integer competitionId,
                                   @Nullable Integer roleId, @Nullable String userName, @Nullable String displayName,
                                   @NotNull Long page, @NotNull Long size);

    /**
     * Commit image and update User.
     *
     * @param participant the User;
     * @param image       the image;
     * @return the boolean;
     */
    boolean commitAndUpdate(@NotNull Participant participant, @Nullable MultipartFile image);

    /**
     * Commit image and insert User.
     *
     * @param participant the User;
     * @param image       the image;
     * @return the boolean;
     */
    boolean commitAndInsert(@NotNull Participant participant, @Nullable MultipartFile image);
}
