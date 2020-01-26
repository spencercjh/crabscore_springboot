package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.spencercjh.crabscore.refactory.mapper.UserInfoMapper;
import top.spencercjh.crabscore.refactory.model.UserInfo;
import top.spencercjh.crabscore.refactory.service.UserInfoService;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
