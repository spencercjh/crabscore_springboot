package top.spencercjh.crabscore.refactory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.spencercjh.crabscore.refactory.mapper.CompetitionInfoMapper;
import top.spencercjh.crabscore.refactory.model.CompetitionInfo;
import top.spencercjh.crabscore.refactory.service.CompetitionInfoService;

@Service
public class CompetitionInfoServiceImpl extends ServiceImpl<CompetitionInfoMapper, CompetitionInfo> implements CompetitionInfoService {

}
