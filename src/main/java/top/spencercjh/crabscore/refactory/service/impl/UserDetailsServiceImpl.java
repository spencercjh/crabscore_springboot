package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.spencercjh.crabscore.refactory.model.User;
import top.spencercjh.crabscore.refactory.model.dto.Account;
import top.spencercjh.crabscore.refactory.model.dto.RoleAuthority;
import top.spencercjh.crabscore.refactory.service.RoleService;
import top.spencercjh.crabscore.refactory.service.UserService;

import java.util.Collections;

/**
 * @author Spencer
 * @date 2020/2/3
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;
    private final RoleService roleService;

    public UserDetailsServiceImpl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userService.getOne(new QueryWrapper<User>().eq(User.COL_USER_NAME, username));
        if (user == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        return new Account(user.getUserName(), user.getPassword(), Collections.singleton(new RoleAuthority(roleService.getById(user.getRoleId()).getRoleName())));
    }
}