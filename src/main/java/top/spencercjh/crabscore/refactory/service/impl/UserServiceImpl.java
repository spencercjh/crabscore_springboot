package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.mapper.UserMapper;
import top.spencercjh.crabscore.refactory.model.User;
import top.spencercjh.crabscore.refactory.service.UserService;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
