package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.spencercjh.crabscore.refactory.mapper.GroupMapper;
import top.spencercjh.crabscore.refactory.model.Group;
import top.spencercjh.crabscore.refactory.service.GroupService;

/**
 * @author MyBatisCodeHelperPro
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {

}
