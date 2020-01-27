package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.mapper.RoleMapper;
import top.spencercjh.crabscore.refactory.model.Role;
import top.spencercjh.crabscore.refactory.service.RoleService;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
