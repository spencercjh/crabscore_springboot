package top.spencercjh.crabscore.refactory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.spencercjh.crabscore.refactory.model.UserInfo;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}