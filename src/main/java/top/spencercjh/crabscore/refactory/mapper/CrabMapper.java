package top.spencercjh.crabscore.refactory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jetbrains.annotations.Nullable;
import top.spencercjh.crabscore.refactory.model.Crab;
import top.spencercjh.crabscore.refactory.model.enums.SexEnum;
import top.spencercjh.crabscore.refactory.model.vo.CrabVo;

import java.util.Date;

/**
 * The interface Crab mapper.
 *
 * @author MyBatisCodeHelperPro
 */
@Mapper
public interface CrabMapper extends BaseMapper<Crab> {
    /**
     * The execution process of associative query in XML is the same as that of checking multiple objects in Java.
     * <p>
     * Their execution process in SQL is the SAME
     *
     * @param page          the page;
     * @param competitionId the competition id;
     * @param groupId       the group id;
     * @param sex           the sex;
     * @param beginTime     the begin time;
     * @param endTime       the end time;
     * @return page;
     * @see top.spencercjh.crabscore.refactory.service.impl.CrabServiceImpl#pageQuery(Integer, Integer, SexEnum, Date, Date, Long, Long) top.spencercjh.crabscore.refactory.service.impl.CrabServiceImpl#pageQuery(Integer, Integer, SexEnum, Date, Date, Long, Long)
     */
    @Deprecated
    IPage<CrabVo> selectCrabVo(@Param("page") Page<CrabVo> page,
                               @Nullable @Param("competitionId") Integer competitionId,
                               @Nullable @Param("groupId") Integer groupId,
                               @Nullable @Param("sex") SexEnum sex,
                               @Nullable @Param("beginTime") Date beginTime,
                               @Nullable @Param("endTime") Date endTime);
}