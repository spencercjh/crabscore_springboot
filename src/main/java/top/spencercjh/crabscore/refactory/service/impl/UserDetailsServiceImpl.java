package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.spencercjh.crabscore.refactory.model.Participant;
import top.spencercjh.crabscore.refactory.model.dto.Account;
import top.spencercjh.crabscore.refactory.model.dto.RoleAuthority;
import top.spencercjh.crabscore.refactory.service.ParticipantService;
import top.spencercjh.crabscore.refactory.service.RoleService;

import java.util.Collections;

/**
 * @author Spencer
 * @date 2020/2/3
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ParticipantService participantService;
    private final RoleService roleService;

    public UserDetailsServiceImpl(ParticipantService participantService, RoleService roleService) {
        this.participantService = participantService;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Participant participant = participantService.getOne(new QueryWrapper<Participant>().eq(Participant.COL_USERNAME, username));
        if (participant == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        return new Account(participant.getUsername(), participant.getPassword(),
                Collections.singleton(new RoleAuthority(roleService.getById(participant.getRoleId()).getRoleName())));
    }
}