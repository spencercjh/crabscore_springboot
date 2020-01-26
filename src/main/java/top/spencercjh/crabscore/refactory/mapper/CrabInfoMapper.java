package top.spencercjh.crabscore.refactory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.spencercjh.crabscore.refactory.model.CrabInfo;

@Mapper
public interface CrabInfoMapper extends BaseMapper<CrabInfo> {
}