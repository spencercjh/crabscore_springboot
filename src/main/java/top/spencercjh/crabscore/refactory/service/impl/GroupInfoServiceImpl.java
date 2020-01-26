package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.spencercjh.crabscore.refactory.mapper.GroupInfoMapper;
import top.spencercjh.crabscore.refactory.model.GroupInfo;
import top.spencercjh.crabscore.refactory.service.GroupInfoService;

@Service
public class GroupInfoServiceImpl extends ServiceImpl<GroupInfoMapper, GroupInfo> implements GroupInfoService {

}
